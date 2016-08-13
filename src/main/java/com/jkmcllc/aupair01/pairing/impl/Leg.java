package com.jkmcllc.aupair01.pairing.impl;

class Leg {
    protected final String symbol;
    protected final Integer qty;
    protected Integer remainQty;
    protected Leg(String symbol, Integer qty) {
        this.symbol = symbol;
        this.qty = qty;
    }
}
