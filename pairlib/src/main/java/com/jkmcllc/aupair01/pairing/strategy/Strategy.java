package com.jkmcllc.aupair01.pairing.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.jkmcllc.aupair01.pairing.impl.Leg;

public interface Strategy {
    public String getStrategyName();
    public List<? extends Leg> getLegs();
    public BigDecimal getMargin();
    public Integer getQuantity();
}
