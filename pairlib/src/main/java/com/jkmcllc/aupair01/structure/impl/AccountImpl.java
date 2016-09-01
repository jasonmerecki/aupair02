package com.jkmcllc.aupair01.structure.impl;

import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.impl.StrategyConfigs;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.Position;

class AccountImpl implements Account {
    private final List<Position> positions;
    private final String accountId;
    private final Map<String, String> customProperties;
    private final String strategyGroupName;
    
    AccountImpl(String accountId, List<Position> legs, String strategyGroupName, Map<String, String> customProperties) {
        this.accountId = accountId;
        this.positions = legs;
        this.customProperties = customProperties;
        if (strategyGroupName == null) {
            strategyGroupName = StrategyConfigs.CORE;
        }
        this.strategyGroupName = strategyGroupName;
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
    public Map<String, String> getCustomProperties() {
        return customProperties;
    }

    @Override
    public String getStrategyGroupName() {
        return strategyGroupName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("account ");
        builder.append("{accountId: ");
        builder.append(accountId);
        builder.append(", strategyGroupName: ");
        builder.append(strategyGroupName);
        builder.append(", positions: ");
        builder.append(positions);
        builder.append(", customProperties: ");
        builder.append(customProperties);
        builder.append("}");
        return builder.toString();
    }


}
