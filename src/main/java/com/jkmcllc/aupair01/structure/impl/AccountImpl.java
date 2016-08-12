package com.jkmcllc.aupair01.structure.impl;

import java.util.List;

import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.Leg;

class AccountImpl implements Account {
    private final List<Leg> legs;
    private final String accountId;
    
    AccountImpl(String accountId, List<Leg> legs) {
        this.accountId = accountId;
        this.legs = legs;
    }

    @Override
    public List<Leg> getLegs() {
        return legs;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountImpl ");
        builder.append("{accountId:");
        builder.append(accountId);
        builder.append(", legs:");
        builder.append(legs);
        builder.append("}");
        return builder.toString();
    }
}
