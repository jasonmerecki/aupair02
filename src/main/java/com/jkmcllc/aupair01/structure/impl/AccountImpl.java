package com.jkmcllc.aupair01.structure.impl;

import java.util.List;

import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.Position;

class AccountImpl implements Account {
    private final List<Position> positions;
    private final String accountId;
    
    AccountImpl(String accountId, List<Position> legs) {
        this.accountId = accountId;
        this.positions = legs;
    }

    @Override
    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("account ");
        builder.append("{accountId: ");
        builder.append(accountId);
        builder.append(", positions: ");
        builder.append(positions);
        builder.append("}");
        return builder.toString();
    }
}
