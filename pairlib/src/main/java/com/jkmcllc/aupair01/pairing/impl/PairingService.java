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
        for (Account account : pairingRequest.getAccounts()) {
            Map<String,List<Strategy>> optionRootResults = new HashMap<>();
            Map<String, PairingInfo> pairingInfos = PairingInfo.from(account, optionRootStore);
            for (Map.Entry<String, PairingInfo> entry : pairingInfos.entrySet()) {
                // for each option root that has pairing info, loop through strategies
                List<Strategy> found = new ArrayList<>();
                String strategyGroupName = account.getStrategyGroupName();
                // TODO: validation, group config must be non null
                List<List<StrategyMeta>> strategyMetasList = strategyConfigs.getStrategyGroup(strategyGroupName);
                PairingInfo pairingInfo = entry.getValue();
                String optionRoot = entry.getKey();
                BigDecimal leastMargin = null;
                for (List<StrategyMeta> strategyMetas : strategyMetasList) {
                    List<Strategy> testFound = new ArrayList<>();
                    for (StrategyMeta strategyMeta : strategyMetas) {
                        List<? extends Strategy> foundForMeta = StrategyFinder.newInstance(pairingInfo, strategyMeta).find() ;
                        testFound.addAll(foundForMeta);
                    }
                    // TODO: configure for maintenance or initial margin
                    BigDecimal testMargin = AccountPairingResponse.getMaintenanceMargin(found);
                    if (leastMargin == null
                            || leastMargin.compareTo(testMargin) > 0) {
                        leastMargin = testMargin;
                        found = testFound;
                    } 
                    pairingInfo.reset();
                }
                optionRootResults.put(optionRoot, found);
            }
            AccountPairingResponse accountPairingResponse = StructureImplFactory.buildAccountPairingResponse(optionRootResults);
            resultMap.put(account.getAccountId(), accountPairingResponse);
        }
        PairingResponse response = StructureImplFactory.buildPairingResponse(resultMap);
        return response;
    }
    
}
