package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

abstract class AbstractStockLeg extends AbstractLeg {
    @Override
    public BigDecimal getLegValue() {
        if (this.legValue == null) {
            BigDecimal value = BigDecimal.ZERO;
            if (this.qty != null && this.price != null) {
                value = this.price.multiply(new BigDecimal(this.qty));
            }
            this.legValue = value;
        }
        return this.legValue;
    }
    @Override
    public String getType() {
        return AbstractLeg.STOCK;
    }
    protected AbstractStockLeg(String symbol, String description, Integer qty, Integer positionResetQty, BigDecimal price) {
        super(symbol, description, qty, positionResetQty, price);
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("stockLeg: {");
        builder.append(super.basicLegInfo());
        builder.append("}");
        return builder.toString();
    }
}
