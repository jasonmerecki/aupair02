package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

public class LongStock extends AbstractStockLeg {
    protected LongStock(String symbol, String description, Integer qty, BigDecimal price) {
        super(symbol, description, qty, price);
    }

    @Override
    protected Leg newLegWith(Integer used) {
        LongStock t = new LongStock(this.symbol, this.description, used, this.price);
        return t;
    }
}
