package com.jkmcllc.aupair01.pairing.impl;

import java.util.Collection;
import java.util.List;

import com.jkmcllc.aupair01.pairing.OrderPairingResult;
import com.jkmcllc.aupair01.pairing.WorstCaseOrderOutcome;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class WorstCaseOrderOutcomeImpl implements WorstCaseOrderOutcome {

    private final String optionRoot;
    private final List<Strategy> strategies;
    private final Collection<? extends OrderPairingResult> orders;
    
    WorstCaseOrderOutcomeImpl(String optionRoot, List<Strategy> strategies, Collection<? extends OrderPairingResult> orders) {
        this.optionRoot = optionRoot;
        this.strategies = strategies;
        this.orders = orders;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WorstCaseOrderOutcomeImpl: {");
        builder.append("optionRoot: ");
        builder.append(optionRoot);
        builder.append(", strategies: ");
        builder.append(strategies);
        builder.append("}");
        return builder.toString();
    }
    @Override
    public String getOptionRoot() {
        return optionRoot;
    }
    
    @Override
    public List<Strategy> getStrategies() {
        return strategies;
    }

    @Override
    public Collection<? extends OrderPairingResult> getOrders() {
        return orders;
    }

}
