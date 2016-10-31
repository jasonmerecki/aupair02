package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, AccountPairingResponse> resultMap = new HashMap<>();
        OptionRootStore optionRootStore = OptionRootStore.getInstance();
        optionRootStore.addRoots(pairingRequest.getOptionRoots());
        Map<String, Map<String, List<Strategy>>> allStrategyListResultMap = null;
        if (pairingRequest.isRequestAllStrategyLists()) {
            allStrategyListResultMap = new HashMap<>();
        }
        for (Account account : pairingRequest.getAccounts()) {
            Map<String,List<Strategy>> optionRootResults = new HashMap<>();
            Map<String, PairingInfo> pairingInfos = PairingInfo.from(account, optionRootStore);
            Map<String, String> strategyGroupByRoot = new HashMap<>();
            for (Map.Entry<String, PairingInfo> entry : pairingInfos.entrySet()) {
                Map<String, List<Strategy>> strategyListResults = null;
                if (pairingRequest.isRequestAllStrategyLists()) {
                    strategyListResults = new HashMap<>();
                }
                // for each option root that has pairing info, loop through strategies
                List<Strategy> found = new ArrayList<>();
                String strategyGroupListName = null;
                String testStrategyGroupName = account.getStrategyGroupName();
                // TODO: validation, group config must be non null
                List<StrategyGroupLists> strategyGroupListsList = strategyConfigs.getStrategyGroup(testStrategyGroupName);
                PairingInfo pairingInfo = entry.getValue();
                String optionRoot = entry.getKey();
                BigDecimal leastMargin = null;
                for (StrategyGroupLists strategyGroupLists : strategyGroupListsList) {
                    String testStrategyGroupListName = strategyGroupLists.getStrategyListName();
                    List<StrategyMeta> strategyMetas = strategyGroupLists.getStrategyMetas();
                    List<Strategy> testFound = new ArrayList<>();
                    for (StrategyMeta strategyMeta : strategyMetas) {
                        List<? extends Strategy> foundForMeta = StrategyFinder.newInstance(pairingInfo, strategyConfigs, strategyMeta).find() ;
                        testFound.addAll(foundForMeta);
                    }
                    // TODO: configure for maintenance or initial margin
                    BigDecimal testMargin = BigDecimal.ZERO;
                    String leastMarginConfig = strategyConfigs.getGlobalConfig(StrategyConfigs.TEST_LEAST_MARGIN);
                    if (StrategyConfigs.MAINTENANCE.equals(leastMarginConfig)) {
                        testMargin = AccountPairingResponse.getMaintenanceMargin(testFound);
                    } else if (StrategyConfigs.INITIAL.equals(leastMarginConfig)) {
                        testMargin = AccountPairingResponse.getInitialMargin(testFound);
                    }
                    if (leastMargin == null
                            || leastMargin.compareTo(testMargin) > 0) {
                        leastMargin = testMargin;
                        found = testFound;
                        strategyGroupListName = testStrategyGroupListName;
                    } 
                    if (strategyListResults != null) {
                        strategyListResults.put(testStrategyGroupListName, testFound);
                    }
                    pairingInfo.reset();
                }
                if (allStrategyListResultMap != null && strategyListResults != null) {
                    allStrategyListResultMap.put(optionRoot, strategyListResults);
                }
                strategyGroupByRoot.put(optionRoot, strategyGroupListName);
                optionRootResults.put(optionRoot, found);
            }
            AccountPairingResponse accountPairingResponse = StructureImplFactory.buildAccountPairingResponse(optionRootResults, strategyGroupByRoot, allStrategyListResultMap);
            resultMap.put(account.getAccountId(), accountPairingResponse);
            if (allStrategyListResultMap != null) {
                allStrategyListResultMap = new HashMap<>();
            }
        }
        PairingResponse response = StructureImplFactory.buildPairingResponse(resultMap);
        return response;
    }
    
}
