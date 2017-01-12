package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

interface OrderPairingResult {

    boolean isWorstCaseOutcome();

    List<? extends Leg> getOrderLegs();

    String getOrderId();

    String getOrderDescription();

    BigDecimal getInitialMargin();

    BigDecimal getMaintenanceMargin();

    BigDecimal getOrderInitialCost();

    BigDecimal getOrderMaintenanceCost();
    
    public static BigDecimal getOrderMaintenanceCost(Collection<? extends OrderPairingResult> orderPairingResults) {
        BigDecimal totalMaintMargin = orderPairingResults.parallelStream().map(s1 -> s1.getOrderMaintenanceCost()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
    
    public static BigDecimal getOrderInitialCost(Collection<? extends OrderPairingResult> orderPairingResults) {
        BigDecimal totalMaintMargin = orderPairingResults.parallelStream().map(s1 -> s1.getOrderInitialCost()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }

}