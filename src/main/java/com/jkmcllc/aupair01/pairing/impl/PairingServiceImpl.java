package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.PairingService;
import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class PairingServiceImpl implements PairingService {
    private static PairingServiceImpl pairingServiceImpl;
    private PairingServiceImpl() {};
    public static PairingService getInstance() {
        if (pairingServiceImpl == null) {
            synchronized (PairingServiceImpl.class) {
                if (pairingServiceImpl == null) {
                    pairingServiceImpl = new PairingServiceImpl();
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
        }
        return null;
    }
}
