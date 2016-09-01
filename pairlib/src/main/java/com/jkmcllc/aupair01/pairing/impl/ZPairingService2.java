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

public class ZPairingService2 {
    private static final Logger logger = LoggerFactory.getLogger(ZPairingService2.class);
    private static ZPairingService2 pairingServiceInstance;
    
    private StrategyConfigs strategyConfigs;
    
    private ZPairingService2() {};
    
    public static ZPairingService2 getInstance() {
        if (pairingServiceInstance == null) {
            synchronized (ZPairingService2.class) {
                if (pairingServiceInstance == null) {
                    pairingServiceInstance = new ZPairingService2();
                }
                pairingServiceInstance.strategyConfigs = StrategyConfigs.getInstance();
            }
        }
        return pairingServiceInstance;
    }
    
    /*
    public PairingResponse service(PairingRequest pairingRequest) {
        Map<String, List<Strategy>> resultMap = new HashMap<>();
        OptionRootStore optionRootStore = OptionRootStore.getInstance();
        optionRootStore.addRoots(pairingRequest.getOptionRoots());
        for (Account account : pairingRequest.getAccounts()) {
            List<Strategy> found = new ArrayList<>();
            Map<String, PairingInfo> pairingInfos = PairingInfo.from(account, optionRootStore);
            for (Map.Entry<String, PairingInfo> entry : pairingInfos.entrySet()) {
                String strategyGroupName = account.getStrategyGroupName();
                // TODO: validation, group config must be non null
                List<List<StrategyMeta>> strategyMetasList = strategyConfigs.getStrategyGroup(strategyGroupName);
                PairingInfo pairingInfo = entry.getValue();
                for (List<StrategyMeta> strategyMetas : strategyMetasList) {
                    for (StrategyMeta strategyMeta : strategyMetas) {
                        List<? extends Strategy> foundForMeta = StrategyFinder.newInstance(pairingInfo, strategyMeta).find() ;
                        found.addAll(foundForMeta);
                    }
                }
               
            }
            resultMap.put(account.getAccountId(), found);
            
        }
        PairingResponse response = StructureImplFactory.buildPairingResponse(resultMap);
        return response;
    }
    
    public PairingResponse serviceold(PairingRequest pairingRequest) {
        Map<String, List<Strategy>> resultMap = new HashMap<>();
        OptionRootStore optionRootStore = OptionRootStore.getInstance();
        optionRootStore.addRoots(pairingRequest.getOptionRoots());
        for (Account account : pairingRequest.getAccounts()) {
            List<Strategy> found = new ArrayList<>();
            Map<String, PairingInfo> pairingInfos = PairingInfo.from(account, optionRootStore);
            for (Map.Entry<String, PairingInfo> entry : pairingInfos.entrySet()) {
                // TODO: build collections of finders in strategy prioritization order, reset info and sort after each collection
                List<? extends Strategy> callVertLongs = ZCallVerticalLongFinder.newInstance(entry.getValue()).find();
                List<? extends Strategy> callVertShorts = ZCallVerticalShortFinder.newInstance(entry.getValue()).find();
                List<? extends Strategy> putVertLongs = ZPutVerticalLongFinder.newInstance(entry.getValue()).find();
                List<? extends Strategy> putVertShorts = ZPutVerticalShortFinder.newInstance(entry.getValue()).find();
                if (logger.isDebugEnabled()) {
                    logger.debug("Found for account id '" + account.getAccountId() + "' and option root symbol: " + entry.getValue().optionRootSymbol 
                            + "\ncallVerticalLongs=" + callVertLongs
                            + "\ncallVerticalShorts=" + callVertShorts
                            + "\nputVerticalLongs=" + putVertLongs
                            + "\nputVerticalShorts=" + putVertShorts
                            );
                }
                found.addAll(callVertLongs);
                found.addAll(callVertShorts);
                found.addAll(putVertLongs);
                found.addAll(putVertShorts);
            }
            resultMap.put(account.getAccountId(), found);
            
        }
        PairingResponse response = StructureImplFactory.buildPairingResponse(resultMap);
        return response;
        
    }
    */
}
