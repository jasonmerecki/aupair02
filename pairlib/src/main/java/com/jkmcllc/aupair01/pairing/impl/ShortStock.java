package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

public class ShortStock extends AbstractStockLeg {
    protected ShortStock(String symbol, String description, Integer qty, BigDecimal price) {
        super(symbol, description, qty, price);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        ShortStock t = new ShortStock(this.symbol, this.description, used, this.price);
        return t;
    }
}
