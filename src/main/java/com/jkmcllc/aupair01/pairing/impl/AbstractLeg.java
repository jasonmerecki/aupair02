package com.jkmcllc.aupair01.pairing.impl;

class AbstractLeg implements Leg {
    protected final String symbol;
    protected final Integer qty;
    protected Integer remainQty;
    protected AbstractLeg(String symbol, Integer qty) {
        this.symbol = symbol;
        this.qty = qty;
    }
    protected String basicLegInfo() {
        return "symbol: \"" + symbol + "\", qty: " + qty;
    }
}
