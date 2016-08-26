package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

abstract class AbstractLeg implements Leg {
    protected final String symbol;
    protected final Integer qty;
    protected Integer remainQty;
    protected final BigDecimal bigDecimalQty;
    protected AbstractLeg(String symbol, Integer qty) {
        this.symbol = symbol;
        this.remainQty = this.qty = qty;
        this.bigDecimalQty = new BigDecimal(qty);
    }
    protected String basicLegInfo() {
        return "symbol: \"" + symbol + "\", qty: " + qty;
    }
    protected Leg reduceBy(Integer used) {
        int startSign = Integer.signum(remainQty);
        if (startSign == -1) {
            remainQty = remainQty + used;
        } else if (startSign == 1) {
            remainQty = remainQty - used;
        } else {
            // throw exception here, cannot use when zero remains
        }
        int endSign = Integer.signum(remainQty);
        if (endSign != 0 && endSign != startSign) {
            // throw exception here, cannot cross zero
        }
        return newLegWith(used);
    }
    protected void resetQty() {
        remainQty = qty;
    }
    protected Integer getRemainQty() {
        return remainQty;
    }
    protected abstract Leg newLegWith(Integer used);
    public BigDecimal getQty() {
        return bigDecimalQty;
    }
}