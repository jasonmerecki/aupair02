package com.jkmcllc.aupair01.pairing.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.jkmcllc.aupair01.pairing.impl.Leg;

public interface Strategy {
    String getStrategyName();
    List<? extends Leg> getLegs();
    BigDecimal getMaintenanceRequirement();
    BigDecimal getInitialRequirement();
    BigDecimal getNonOptionPriceMaintenanceRequirement();
    BigDecimal getNonOptionPriceInitialRequirement();
    /**
     * @deprecated use {@link getMaintenanceRequirement}
     */
    BigDecimal getMaintenanceMargin();
    /**
     * @deprecated use {@link getInitialRequirement}
     */
    BigDecimal getInitialMargin();
    BigDecimal getPureNakedCallMargin();
    BigDecimal getPureNakedPutMargin();
    BigDecimal getPureNakedMargin();
    BigDecimal getPureNakedLastResult();
    BigDecimal getNonOptionPriceCallMargin();
    BigDecimal getNonOptionPricePutMargin();
    Integer getQuantity();
    boolean isProhibitedStrategy();
}
