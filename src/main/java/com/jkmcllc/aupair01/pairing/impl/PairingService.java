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
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public class PairingService {
    private static final Logger logger = LoggerFactory.getLogger(PairingService.class);
    private static PairingService pairingServiceImpl;
    private PairingService() {};
    public static PairingService getInstance() {
        if (pairingServiceImpl == null) {
            synchronized (PairingService.class) {
                if (pairingServiceImpl == null) {
                    pairingServiceImpl = new PairingService();
                }
            }
        }
        return pairingServiceImpl;
    }
    
    public PairingResponse service(PairingRequest pairingRequest) {
        Map<String, List<Strategy>> resultMap = new HashMap<>();
        OptionRootStore optionRootStore = OptionRootStore.getInstance();
        for (OptionRoot optionRoot : pairingRequest.getOptionRoots()) {
            optionRootStore.addRoot(optionRoot);
        }
        for (Account account : pairingRequest.getAccounts()) {
            List<Strategy> found = new ArrayList<>();
            Map<String, PairingInfo> pairingInfos = PairingInfo.from(account, optionRootStore);
            for (Map.Entry<String, PairingInfo> entry : pairingInfos.entrySet()) {
                // TODO: build collections of finders in strategy prioritization order, reset info and sort after each collection
                List<? extends Strategy> callVertLongs = CallVerticalLongFinder.newInstance(entry.getValue()).find();
                List<? extends Strategy> callVertShorts = CallVerticalShortFinder.newInstance(entry.getValue()).find();
                List<? extends Strategy> putVertLongs = PutVerticalLongFinder.newInstance(entry.getValue()).find();
                List<? extends Strategy> putVertShorts = PutVerticalShortFinder.newInstance(entry.getValue()).find();
                
                logger.info("Found for account id '" + account.getAccountId() + "' and option root symbol: " + entry.getValue().optionRootSymbol 
                        + "\ncallVerticalLongs=" + callVertLongs
                        + "\ncallVerticalShorts=" + callVertShorts
                        + "\nputVerticalLongs=" + putVertLongs
                        + "\nputVerticalShorts=" + putVertShorts
                        );
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
}
