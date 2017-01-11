package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;

interface OrderPairingResult {

    boolean isWorstCaseOutcome();

    List<AbstractLeg> getOrderLegs();

    String getOrderId();

    String getOrderDescription();

    BigDecimal getEquityInitialMargin();

    BigDecimal getEquityMaintMargin();

    BigDecimal getInitialMargin();

    BigDecimal getMaintenanceMargin();

    BigDecimal getOptionInitialMargin();

    BigDecimal getOptionMaintenanceMargin();

}