package com.jkmcllc.aupair01.pairing.impl;

public class StrategyLeg extends AbstractLeg {
    public StrategyLeg(String symbol, Integer qty) {
        super(symbol, qty);
    }
    public Integer getQty() {
        return this.qty;
    }
}
