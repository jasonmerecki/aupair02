package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Collection;

import com.jkmcllc.aupair01.exception.PairingException;

abstract class AbstractLeg implements Leg {
    static final String STOCK = "STOCK";
    static final String STOCKOPTION = "STOCKOPTION";
    static final String DELIVERABLE = "DELIVERABLE";
    
    protected final String symbol;
    protected final String description;
    protected final Integer origQty;
    protected final BigDecimal price;
    
    protected Integer remainQty;
    protected BigDecimal legValue;
    protected BigDecimal bigDecimalQty;
    protected Integer qty;
    
    protected AbstractLeg(String symbol, String description, Integer qty, BigDecimal price) {
        this.symbol = symbol;
        this.description = description;
        this.remainQty = this.origQty = this.qty = qty;
        this.price = price;
        this.bigDecimalQty = new BigDecimal(qty);
        this.legValue = null;
    }
    protected String basicLegInfo() {
        return "symbol: \"" + symbol + "\", description: \"" + description + "\", qty: " + remainQty + ", price: " + price;
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
            throw new PairingException("Error: sign crossed for leg, used=" + used + ", remainQty=" + remainQty + ", leg=" + this.toString());
        }
        this.qty = this.remainQty;
        this.bigDecimalQty = new BigDecimal(this.qty);
        return newLegWith(used * startSign);
    }
    
    protected void resetQty() {
        this.remainQty = this.origQty;
        this.qty = this.origQty;
        this.bigDecimalQty = new BigDecimal(this.qty);
    }
    
    protected Integer getRemainQty() {
        return remainQty;
    }
    
    protected abstract Leg newLegWith(Integer used);
    public abstract String getType();
    public abstract BigDecimal getLegValue();
    
    @Override
    public Integer getQty() {
        return qty;
    }
    @Override
    public String getSymbol() {
        return symbol;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public Collection<? extends Leg> getMultiLegs() {
        return null;
    }
    
}
