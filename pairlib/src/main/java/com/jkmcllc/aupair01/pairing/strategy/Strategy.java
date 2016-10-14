package com.jkmcllc.aupair01.pairing.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.jkmcllc.aupair01.pairing.impl.Leg;

public interface Strategy {
    String getStrategyName();
    List<? extends Leg> getLegs();
    BigDecimal getMaintenanceMargin();
    BigDecimal getInitialMargin();
    Integer getQuantity();
    boolean isProhibitedStrategy();
}
