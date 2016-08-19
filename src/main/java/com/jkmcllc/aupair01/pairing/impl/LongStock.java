package com.jkmcllc.aupair01.pairing.impl;

public class LongStock extends AbstractLeg {
    protected LongStock(String symbol, Integer qty) {
        super(symbol, qty);
    }

    @Override
    protected Leg newLegWith(Integer used) {
        LongStock t = new LongStock(this.symbol, used);
        return t;
    }
}
