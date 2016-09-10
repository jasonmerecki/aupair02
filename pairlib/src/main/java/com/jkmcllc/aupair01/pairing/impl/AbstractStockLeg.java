package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

abstract class AbstractStockLeg extends AbstractLeg {
    String getType() {
        return AbstractLeg.STOCK;
    }
    protected AbstractStockLeg(String symbol, String description, Integer qty, BigDecimal price) {
        super(symbol, description, qty, price);
    }
}
