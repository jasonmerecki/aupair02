package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.exception.ConfigurationException;
import com.jkmcllc.aupair01.exception.PairingException;
import com.jkmcllc.aupair01.pairing.AccountPairingRequest;
import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.OrderPairingResult;
import com.jkmcllc.aupair01.pairing.PairingProcessor;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.WorstCaseOrderOutcome;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public class PairingService implements PairingProcessor {
    public static Perfwatch PERFWATCH = new Perfwatch();
    private static final Logger logger = LoggerFactory.getLogger(PairingService.class);
    private static PairingService pairingServiceInstance;
    
    private StrategyConfigs strategyConfigs;
    
    private PairingService() {};
    
    public static PairingService getInstance() {
        if (pairingServiceInstance == null) {
            synchronized (PairingService.class) {
                if (pairingServiceInstance == null) {
                    pairingServiceInstance = new PairingService();
                }
                try {
                    pairingServiceInstance.strategyConfigs = StrategyConfigs.getInstance();
                } catch (Throwable t) {
                    StringBuilder error = new StringBuilder("Could not initialize PairingService, please check that the ");
                    error.append("configuration files exist and are correctly formatted ");
                    error.append("(i.e. '-Dpairconfig=/opt/pair/pairconfig.ini,/opt/pair/apexdefault.ini') ");
                    String fileNames = StrategyConfigs.getFileConfig();
                    error.append("the passed value is: '").append(fileNames).append("'");
                    throw new ConfigurationException(error.toString(), t);
                }
            }
        }
        return pairingServiceInstance;
    }
    

    @Override
    public PairingResponse processRequest(PairingRequest request) {
        return service(request);
    }

    @Override
    public AccountPairingResponse processAccountRequest(AccountPairingRequest request) {
        Map<String, AccountPairingResponse> resultMap = serviceInternal(request);
        // expects only one result
        if (resultMap.size() != 1) {
            throw new PairingException("More than one account in request, cannot return single account response, reqeust: " + request);
        }
        return resultMap.entrySet().stream().findFirst().get().getValue();
    }
    
    public PairingResponse service(PairingRequest pairingRequest) {
        Map<String, AccountPairingResponse> resultMap = serviceInternal(pairingRequest);
        PairingResponse response = StructureImplFactory.buildPairingResponse(resultMap);
        return response;
    }
    
    private Map<String, AccountPairingResponse> serviceInternal(PairingRequest pairingRequest) {
        OptionRootStore optionRootStore = OptionRootStore.getInstance();
        optionRootStore.addRoots(pairingRequest.getOptionRoots());
        
        boolean isRequestAllStrategyLists = pairingRequest.isRequestAllStrategyLists();

        ConcurrentMap<String, AccountPairingResponse> resultMap = pairingRequest.getAccounts().parallelStream().collect(Collectors.toConcurrentMap(
                account -> account.getAccountId(), 
                account -> { return pairAccount(account, optionRootStore, isRequestAllStrategyLists); }
                ));
        
        return resultMap;
    }
    

    private AccountPairingResponse pairAccount(Account account, OptionRootStore optionRootStore,
            boolean isRequestAllStrategies) {
        if (PairingService.PERFWATCH != null) {
            PairingService.PERFWATCH.reset();
            PairingService.PERFWATCH.start("account");
        }
        // initialize some values, including the PairingInfo
        Map<String,List<Strategy>> optionRootResults = new HashMap<>();
        Map<String, WorstCaseOrderOutcome> worstCaseOutcomes = new HashMap<>();
        Map<String, PairingInfo> pairingInfos = PairingInfo.from(account, optionRootStore);
        Map<String, String> strategyGroupByRoot = new HashMap<>();
        Map<String, Map<String, List<Strategy>>> allStrategyListResultMap = null;
        
        // find the strategy group name from the account, and also find the leastMarginConfig 
        // from the selected strategy group, or from the global
        String testStrategyGroupName = account.getStrategyGroupName();
        StrategyGroup strategyGroup = strategyConfigs.getStrategyGroup(testStrategyGroupName);
        String leastMarginConfig = strategyGroup.testLeastMargin;
        if (leastMarginConfig == null) {
            leastMarginConfig = strategyConfigs.getGlobalConfig(GlobalConfigType.TEST_LEAST_MARGIN);
        }
        
        // get the strategy group lists from the strategy group selected
        List<StrategyGroupLists> strategyGroupListsList = strategyGroup.strategyGroupLists;
        
        if (isRequestAllStrategies) {
            allStrategyListResultMap = new HashMap<>();
        }
        for (Map.Entry<String, PairingInfo> entry : pairingInfos.entrySet()) {
            Map<String, List<Strategy>> strategyListResults = null;
            if (isRequestAllStrategies) {
                strategyListResults = new HashMap<>();
            }
            // for each option root that has pairing info, loop through strategies in strategyGroupListsList
            PairingInfo pairingInfo = entry.getValue();
            String optionRoot = entry.getKey();
            
            StrategyFinder strategyFinder = StrategyFinder.newInstance(pairingInfo, strategyConfigs);
            
            LeastMarginOutcome positionOutcome = findLeastMarginOutcome(strategyGroupListsList, pairingInfo, 
                    strategyFinder, strategyListResults, leastMarginConfig);
            if (allStrategyListResultMap != null && strategyListResults != null) {
                allStrategyListResultMap.put(optionRoot, strategyListResults);
            }
            strategyGroupByRoot.put(optionRoot, positionOutcome.strategyGroupListName);
            optionRootResults.put(optionRoot, positionOutcome.leastMarginStrategyList);
            
            // now that the leastMarginPairingOutcome is found, the optional orders can be considered
            LeastMarginOutcome worstPairingOutcome = findWorstOrderOutcome(strategyGroupListsList, strategyFinder, pairingInfo, 
                    positionOutcome, leastMarginConfig);
            if (worstPairingOutcome == null /* && pairingInfo.orderPairings != null && !pairingInfo.orderPairings.isEmpty() */) {
                // the worst outcome is that no orders fill (they must all be BP releasing)
                worstPairingOutcome = positionOutcome;
            }
            if (worstPairingOutcome != null) {
                WorstCaseOrderOutcomeImpl worstOutcome = new WorstCaseOrderOutcomeImpl(optionRoot, worstPairingOutcome.leastMarginStrategyList, pairingInfo.orderPairings);
                worstCaseOutcomes.put(optionRoot, worstOutcome);
            }
            
        }
        
        if (PairingService.PERFWATCH != null) {
            PairingService.PERFWATCH.stop("account");
            logger.info("PERFWATCH : " + PERFWATCH.toString());
        }

        AccountPairingResponse accountPairingResponse = StructureImplFactory.buildAccountPairingResponse(account, optionRootResults, strategyGroupByRoot, allStrategyListResultMap, worstCaseOutcomes);
        return accountPairingResponse;

    }
    
    private LeastMarginOutcome findLeastMarginOutcome(List<StrategyGroupLists> strategyGroupListsList, PairingInfo pairingInfo,
            StrategyFinder strategyFinder, Map<String, List<Strategy>> strategyListResults, String leastMarginConfig) {
        List<Strategy> leastMarginStrategyList = null;
        String strategyGroupListName = null;
        BigDecimal leastMargin = null;
        for (StrategyGroupLists strategyGroupLists : strategyGroupListsList) {
            String testStrategyGroupListName = strategyGroupLists.getStrategyListName();
            List<StrategyMeta> strategyMetas = strategyGroupLists.getStrategyMetas();
            List<Strategy> testFound = new ArrayList<>();
            for (StrategyMeta strategyMeta : strategyMetas) {
                List<? extends Strategy> foundForMeta = strategyFinder.resetWithMeta(strategyMeta).find() ;
                testFound.addAll(foundForMeta);
            }
            BigDecimal testMargin = findMarginOutcome(testFound, leastMarginConfig);
            int compare = leastMargin != null ? leastMargin.compareTo(testMargin) : 0;
            if (leastMargin == null
                    || compare > 0
                    || (compare == 0 && leastMarginStrategyList.size() > testFound.size())) {
                leastMargin = testMargin;
                leastMarginStrategyList = testFound;
                strategyGroupListName = testStrategyGroupListName;
            } 
            if (strategyListResults != null) {
                strategyListResults.put(testStrategyGroupListName, testFound);
            }
            pairingInfo.reset(false);
        }
        LeastMarginOutcome outcome = new LeastMarginOutcome(leastMarginStrategyList, strategyGroupListName);
        return outcome;
    }
    
    private class LeastMarginOutcome {
        private final List<Strategy> leastMarginStrategyList;
        private final String strategyGroupListName;
        private LeastMarginOutcome(List<Strategy> leastMarginStrategyList, String strategyGroupListName) {
            this.leastMarginStrategyList = leastMarginStrategyList;
            this.strategyGroupListName = strategyGroupListName;
        }
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("LeastMarginOutcome: {");
            builder.append("leastMarginStrategyList: ");
            builder.append(leastMarginStrategyList);   
            builder.append("}");
            return builder.toString();
        }
    }
    
    private LeastMarginOutcome findWorstOrderOutcome(List<StrategyGroupLists> strategyGroupListsList, 
            StrategyFinder strategyFinder, PairingInfo pairingInfo, LeastMarginOutcome positionOutcome,
            String leastMarginConfig) {
        LeastMarginOutcome worstOutcome = null;
        BigDecimal worstPositionMargin = findMarginOutcome(positionOutcome.leastMarginStrategyList, leastMarginConfig);
        BigDecimal posMaintOutcome = AccountPairingResponse.getMaintenanceRequirement(positionOutcome.leastMarginStrategyList);
        BigDecimal posInitOutcome = AccountPairingResponse.getInitialRequirement(positionOutcome.leastMarginStrategyList);
        if (pairingInfo.orderPairings != null && !pairingInfo.orderPairings.isEmpty()) {
            Set<OrderPairingResultImpl> worstOrders = new HashSet<>(), shortOrders = new HashSet<>(), nextOrders = new HashSet<>();
            for (OrderPairingResultImpl orderPairResult : pairingInfo.orderPairings) {
                nextOrders.add(orderPairResult);
                nextOrders.addAll(worstOrders);
                nextOrders.forEach(opr -> pairingInfo.applyOrderLegs(opr));
                LeastMarginOutcome nextOutcome = findLeastMarginOutcome(strategyGroupListsList, pairingInfo, 
                        strategyFinder, null, leastMarginConfig);
                BigDecimal nextOutcomeMargin = findMarginOutcome(nextOutcome.leastMarginStrategyList, leastMarginConfig);
                nextOutcomeMargin = nextOutcomeMargin.add(findOrderCost (nextOrders, leastMarginConfig));
                if (nextOutcomeMargin.compareTo(worstPositionMargin) > 0) {
                    orderPairResult.worstCaseOutcome = true;
                    worstOrders.add(orderPairResult);
                    worstOutcome = nextOutcome;
                }
                nextOrders.clear();
                pairingInfo.reset(true);
                
                if (orderPairResult.getSellLegsCount() != 0) {
                    shortOrders.add(orderPairResult);
                    LeastMarginOutcome shortOutcome = null;
                    if (!orderPairResult.isWorstCaseOutcome()) {
                        shortOrders.forEach(opr -> pairingInfo.applyOrderLegs(opr));
                        // here is the special short leg pairing
                        shortOutcome = findLeastMarginOutcome(strategyGroupListsList, pairingInfo, 
                                strategyFinder, null, leastMarginConfig);
                        nextOutcomeMargin = findMarginOutcome(shortOutcome.leastMarginStrategyList, leastMarginConfig);
                        nextOutcomeMargin = nextOutcomeMargin.add(findOrderCost (shortOrders, leastMarginConfig));
                        pairingInfo.reset(true);
                    }
                    if (nextOutcomeMargin.compareTo(worstPositionMargin) > 0 && shortOutcome != null) {
                        // now we clear out all of the current worst orders, the shorts are now the worst
                        worstOrders.forEach(o -> o.worstCaseOutcome = false);
                        worstOrders.clear();
                        worstOrders.addAll(shortOrders);
                        worstOrders.forEach(o -> o.worstCaseOutcome = true);
                        worstOutcome = shortOutcome;
                    }
                }
                
                // now the single order outcome to determine the 'cost' of one
                pairingInfo.applyOrderLegs(orderPairResult);
                LeastMarginOutcome oneOutcome = findLeastMarginOutcome(strategyGroupListsList, pairingInfo, 
                        strategyFinder, null, leastMarginConfig);
                BigDecimal oneMaintOutcome = AccountPairingResponse.getMaintenanceRequirement(oneOutcome.leastMarginStrategyList);
                BigDecimal oneInitOutcome = AccountPairingResponse.getInitialRequirement(oneOutcome.leastMarginStrategyList);
                orderPairResult.totalMaintenanceMargin = oneMaintOutcome.subtract(posMaintOutcome);
                orderPairResult.totalInitialMargin = oneInitOutcome.subtract(posInitOutcome);
                pairingInfo.reset(true);
            }
        }
        return worstOutcome;
    }
    
    private BigDecimal findMarginOutcome(List<Strategy> strategies, String leastMarginConfig) {
        BigDecimal marginOutcome = BigDecimal.ZERO;
        if (StrategyConfigs.MAINTENANCE.equals(leastMarginConfig)) {
            marginOutcome = AccountPairingResponse.getMaintenanceRequirement(strategies);
        } else if (StrategyConfigs.INITIAL.equals(leastMarginConfig)) {
            marginOutcome = AccountPairingResponse.getInitialRequirement(strategies);
        }
        return marginOutcome;
    }
    
    private BigDecimal findOrderCost(Set<? extends OrderPairingResult> orderPairResults, String leastMarginConfig) {
        BigDecimal marginOutcome = BigDecimal.ZERO;
        if (StrategyConfigs.MAINTENANCE.equals(leastMarginConfig)) {
            marginOutcome = OrderPairingResult.getOrderMaintenanceCost(orderPairResults);
        } else if (StrategyConfigs.INITIAL.equals(leastMarginConfig)) {
            marginOutcome = OrderPairingResult.getOrderInitialCost(orderPairResults);
        }
        return marginOutcome;
    }
    
}
