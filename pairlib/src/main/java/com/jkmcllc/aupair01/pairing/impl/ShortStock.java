package com.jkmcllc.aupair01.pairing.impl;

public class ShortStock extends AbstractStockLeg {
    protected ShortStock(String symbol, String description, Integer qty) {
        super(symbol, description, qty);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        ShortStock t = new ShortStock(this.symbol, this.description, used);
        return t;
    }
}
