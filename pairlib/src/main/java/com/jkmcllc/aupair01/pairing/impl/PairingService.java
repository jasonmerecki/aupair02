package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public class PairingService {
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
                // TODO: the config may throw an exception, should catch and throw meaningful one
                pairingServiceInstance.strategyConfigs = StrategyConfigs.getInstance();
            }
        }
        return pairingServiceInstance;
    }
    
    public PairingResponse service(PairingRequest pairingRequest) {
        OptionRootStore optionRootStore = OptionRootStore.getInstance();
        optionRootStore.addRoots(pairingRequest.getOptionRoots());
        
        AccountPairingConsumer consumer = new AccountPairingConsumer(optionRootStore, pairingRequest.isRequestAllStrategyLists());
        // ConcurrentMap<String, AccountPairingResponse> resultMap = pairingRequest.getAccounts().parallelStream().collect(ConcurrentHashMap::new, consumer, ConcurrentHashMap::addAll);
        
        pairingRequest.getAccounts().parallelStream().forEach(consumer);
        ConcurrentMap<String, AccountPairingResponse> resultMap = consumer.resultMap;
        PairingResponse response = StructureImplFactory.buildPairingResponse(resultMap);
        return response;
    }
    
    // private class AccountPairingConsumer implements BiConsumer<Map<String, AccountPairingResponse>, Account> {
    private class AccountPairingConsumer implements Consumer<Account> {
        private final OptionRootStore optionRootStore;
        private final boolean isRequestAllStrategies;
        private final ConcurrentMap<String, AccountPairingResponse> resultMap = new ConcurrentHashMap<>();
        private AccountPairingConsumer(OptionRootStore optionRootStore, boolean isRequestAllStrategies) {
            this.optionRootStore = optionRootStore;
            this.isRequestAllStrategies = isRequestAllStrategies;
        }
        @Override
        public void accept(Account account) {
            accept(resultMap, account);
        }
        // @Override
        public void accept(Map<String, AccountPairingResponse> resultMap, Account account) {
            // initialize some values, including the PairingInfo
            Map<String,List<Strategy>> optionRootResults = new HashMap<>();
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
                
                LeastMarginOutcome outcome = findLeastMarginOutcome(strategyGroupListsList, pairingInfo, 
                        strategyListResults, leastMarginConfig);
                if (allStrategyListResultMap != null && strategyListResults != null) {
                    allStrategyListResultMap.put(optionRoot, strategyListResults);
                }
                strategyGroupByRoot.put(optionRoot, outcome.strategyGroupListName);
                optionRootResults.put(optionRoot, outcome.leastMarginStrategyList);
                
                // now that the leastMarginPairingOutcome is found, the optional orders can be considered
            }

            AccountPairingResponse accountPairingResponse = StructureImplFactory.buildAccountPairingResponse(optionRootResults, strategyGroupByRoot, allStrategyListResultMap);
            resultMap.put(account.getAccountId(), accountPairingResponse);

        }
        
        private LeastMarginOutcome findLeastMarginOutcome(List<StrategyGroupLists> strategyGroupListsList, PairingInfo pairingInfo,
                Map<String, List<Strategy>> strategyListResults, String leastMarginConfig) {
            List<Strategy> leastMarginStrategyList = null;
            String strategyGroupListName = null;
            BigDecimal leastMargin = null;
            for (StrategyGroupLists strategyGroupLists : strategyGroupListsList) {
                String testStrategyGroupListName = strategyGroupLists.getStrategyListName();
                List<StrategyMeta> strategyMetas = strategyGroupLists.getStrategyMetas();
                List<Strategy> testFound = new ArrayList<>();
                for (StrategyMeta strategyMeta : strategyMetas) {
                    List<? extends Strategy> foundForMeta = StrategyFinder.newInstance(pairingInfo, strategyConfigs, strategyMeta).find() ;
                    testFound.addAll(foundForMeta);
                }
                BigDecimal testMargin = BigDecimal.ZERO;

                if (StrategyConfigs.MAINTENANCE.equals(leastMarginConfig)) {
                    testMargin = AccountPairingResponse.getMaintenanceMargin(testFound);
                } else if (StrategyConfigs.INITIAL.equals(leastMarginConfig)) {
                    testMargin = AccountPairingResponse.getInitialMargin(testFound);
                }
                if (leastMargin == null
                        || leastMargin.compareTo(testMargin) > 0) {
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
        }
        
    }
    
}
