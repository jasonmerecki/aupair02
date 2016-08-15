package com.jkmcllc.aupair01.pairing.impl;

import java.util.List;

import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.CallVerticalLong;
import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class PairingService {
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
        for (OptionRoot optionRoot : pairingRequest.getOptionRoots()) {
            OptionRootStore.addRoot(optionRoot);
        }
        for (Account account : pairingRequest.getAccounts()) {
            PairingInfo pairingInfo = PairingInfo.from(account);
            List<CallVerticalLong> callVertLongs = CallVerticalLongFinder.newInstance().findIn(pairingInfo);
        }
        return null;
    }
}
