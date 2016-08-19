package com.jkmcllc.aupair01.pairing.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.jkmcllc.aupair01.pairing.impl.Leg;

public interface Strategy {
    public static final String CALL_VERTICAL_LONG = "CallVerticalLong";
    public static final String CALL_VERTICAL_SHORT = "CallVerticalShort";
    public static final String PUT_VERTICAL_LONG = "PutVerticalLong";
    public static final String PUT_VERTICAL_SHORT = "PutVerticalShort";
    
    public String getStrategyName();
    public List<? extends Leg> getLegs();
    public BigDecimal getMargin();
}
