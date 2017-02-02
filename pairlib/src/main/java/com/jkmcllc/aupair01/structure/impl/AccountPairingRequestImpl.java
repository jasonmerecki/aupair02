package com.jkmcllc.aupair01.structure.impl;

import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.AccountPairingRequest;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class AccountPairingRequestImpl extends PairingRequestImpl implements AccountPairingRequest {
    AccountPairingRequestImpl (List<Account> accounts, Map<String, OptionRoot> optionRoots, boolean requestAllStrategyLists) {
        super(accounts, optionRoots, requestAllStrategyLists);
    }

    @Override
    public Account getAccount() {
        if (accounts.size() == 1) {
            return accounts.get(0);
        }
        return null;
    }
}
