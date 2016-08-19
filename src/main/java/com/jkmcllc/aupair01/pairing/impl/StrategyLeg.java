package com.jkmcllc.aupair01.pairing.impl;

abstract public class StrategyLeg extends AbstractLeg {
    public StrategyLeg(String symbol, Integer qty) {
        super(symbol, qty);
    }
    public Integer getQty() {
        return this.qty;
    }
}
