package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

abstract class AbstractLeg implements Leg {
    static final String STOCK = "STOCK";
    static final String STOCKOPTION = "STOCKOPTION";
    
    protected final String symbol;
    protected final String description;
    protected final Integer qty;
    protected Integer remainQty;
    protected final BigDecimal bigDecimalQty;
    protected final BigDecimal price;
    
    protected AbstractLeg(String symbol, String description, Integer qty, BigDecimal price) {
        this.symbol = symbol;
        this.description = description;
        this.remainQty = this.qty = qty;
        this.price = price;
        this.bigDecimalQty = new BigDecimal(qty);
    }
    protected String basicLegInfo() {
        return "symbol: \"" + symbol + "\", description: \"" + description + "\", qty: " + qty + ", price: " + price;
    }
    protected Leg reduceBy(Integer used) {
        int startSign = Integer.signum(remainQty);
        if (startSign == -1) {
            remainQty = remainQty + used;
        } else if (startSign == 1) {
            remainQty = remainQty - used;
        } else {
            // TODO: throw exception here, cannot use when zero remains
        }
        int endSign = Integer.signum(remainQty);
        if (endSign != 0 && endSign != startSign) {
            // TODO: throw exception here, cannot cross zero
        }
        return newLegWith(used * startSign);
    }
    
    protected void resetQty() {
        remainQty = qty;
    }
    
    protected Integer getRemainQty() {
        return remainQty;
    }
    
    protected abstract Leg newLegWith(Integer used);
    abstract String getType();
    
    public BigDecimal getQty() {
        return bigDecimalQty;
    }
    String getSymbol() {
        return symbol;
    }
    
}
