package com.jkmcllc.aupair01.pairing.impl;

public class ShortStock extends AbstractLeg {
    protected ShortStock(String symbol, Integer qty) {
        super(symbol, qty);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        ShortStock t = new ShortStock(this.symbol, used);
        return t;
    }
}
