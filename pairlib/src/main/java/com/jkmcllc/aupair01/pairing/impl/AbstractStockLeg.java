package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

abstract class AbstractStockLeg extends AbstractLeg {
    @Override
    public BigDecimal getLegValue() {
        if (this.legValue == null) {
            BigDecimal value = BigDecimal.ZERO;
            if (this.bigDecimalQty != null && this.price != null) {
                value = this.bigDecimalQty.multiply(this.price);
            }
            this.legValue = value;
        }
        return this.legValue;
    }
    @Override
    String getType() {
        return AbstractLeg.STOCK;
    }
    protected AbstractStockLeg(String symbol, String description, Integer qty, BigDecimal price) {
        super(symbol, description, qty, price);
    }
}
