package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

public class StockLeg extends AbstractStockLeg {
    protected StockLeg(String symbol, String description, Integer qty, BigDecimal price) {
        super(symbol, description, qty, price);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        StockLeg t = new StockLeg(this.symbol, this.description, used, this.price);
        return t;
    }
}
