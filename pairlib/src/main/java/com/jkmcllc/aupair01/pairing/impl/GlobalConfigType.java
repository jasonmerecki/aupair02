package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

public class GlobalConfigType<T> {
    private String typeName;
    private GlobalConfigType(String typeName) {
        this.typeName = typeName;
    }
    public String getTypeName() {return this.typeName;}
    public static GlobalConfigType<String> DEFAULT_STRATEGY_GROUP = new GlobalConfigType<>(StrategyConfigs.DEFAULT_STRATEGY_GROUP);
    public static GlobalConfigType<String> TEST_LEAST_MARGIN = new GlobalConfigType<>(StrategyConfigs.TEST_LEAST_MARGIN);
    public static GlobalConfigType<BigDecimal> NAKED_DELIVERABLE_PCT = new GlobalConfigType<>(StrategyConfigs.NAKED_DELIVERABLE_PCT);
    public static GlobalConfigType<BigDecimal> NAKED_CASH_PCT = new GlobalConfigType<>(StrategyConfigs.NAKED_CASH_PCT);
}
