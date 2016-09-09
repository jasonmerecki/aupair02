package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Map<String, Map<String,List<Strategy>>> resultMap = new HashMap<>();
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
                for (List<StrategyMeta> strategyMetas : strategyMetasList) {
                    for (StrategyMeta strategyMeta : strategyMetas) {
                        pairingInfo.sort1();
                        List<? extends Strategy> foundForMeta = StrategyFinder.newInstance(pairingInfo, strategyMeta).find() ;
                        found.addAll(foundForMeta);
                    }
                    // TODO: compare with previously found and keep this one if lower or equal margin
                }
                optionRootResults.put(optionRoot, found);
            }
            resultMap.put(account.getAccountId(), optionRootResults);
        }
        PairingResponse response = StructureImplFactory.buildPairingResponse(resultMap);
        return response;
    }
    
}
