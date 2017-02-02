package com.jkmcllc.aupair01.structure.impl;

import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.OptionRoot;

class PairingRequestImpl implements PairingRequest {
    protected final List<Account> accounts;
    protected final Map<String, OptionRoot> optionRoots;
    protected final boolean requestAllStrategyLists ;
    
    PairingRequestImpl (List<Account> accounts, Map<String, OptionRoot> optionRoots, boolean requestAllStrategyLists) {
        this.accounts = accounts;
        this.optionRoots = optionRoots;
        this.requestAllStrategyLists = requestAllStrategyLists;
    }
    
    @Override
    public List<Account> getAccounts() {
        return accounts;
    }
    @Override
    public Map<String, OptionRoot> getOptionRoots() {
        return optionRoots;
    }
    @Override
    public boolean isRequestAllStrategyLists() {
        return this.requestAllStrategyLists;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PairingRequest: {accounts:");
        builder.append(accounts);
        builder.append(", optionRoots:");
        builder.append(optionRoots);
        builder.append("}");
        return builder.toString();
    }
}
