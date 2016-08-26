package com.jkmcllc.aupair01.pairing.impl;

public class LongStock extends AbstractStockLeg {
    protected LongStock(String symbol, String description, Integer qty) {
        super(symbol, description, qty);
    }

    @Override
    protected Leg newLegWith(Integer used) {
        LongStock t = new LongStock(this.symbol, this.description, used);
        return t;
    }
}
