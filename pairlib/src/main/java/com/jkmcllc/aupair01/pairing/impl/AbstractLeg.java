package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Collection;

import com.jkmcllc.aupair01.exception.PairingException;
import com.jkmcllc.aupair01.store.Constants;

abstract class AbstractLeg implements Leg {
    static final String STOCK = "STOCK";
    static final String STOCKOPTION = "STOCKOPTION";
    static final String DELIVERABLE = "DELIVERABLE";
    
    protected final String symbol;
    protected final String description;
    protected final Integer resetQty;
    protected final BigDecimal price;
    
    protected Integer remainQty;
    protected BigDecimal legValue;
    protected BigDecimal bigDecimalQty;
    protected Integer qty;
    
    protected AbstractLeg(String symbol, String description, Integer qty, BigDecimal price) {
        this.symbol = symbol;
        this.description = description;
        this.remainQty = this.resetQty = this.qty = qty;
        this.price = price;
        this.bigDecimalQty = new BigDecimal(qty);
        this.legValue = null;
    }
    protected String basicLegInfo() {
        StringBuilder basicInfo = new StringBuilder();
        if (symbol != null) {
            basicInfo.append("symbol: \"").append(symbol).append("\", ");
        }
        if (description != null && Constants.EMPTY_STRING.equals(description) == false) {
            basicInfo.append("description: \"").append(description).append("\", ");;
        }
        if (qty != null) {
            basicInfo.append("qty: ").append(qty).append(", ");
        }
        if (price != null) {
            basicInfo.append("price: ").append(price);
        }
        return basicInfo.toString();
    }
    protected Leg reduceBy(Integer used) {
        int startSign = Integer.signum(remainQty);
        if (startSign == -1) {
            remainQty = remainQty + used;
        } else if (startSign == 1) {
            remainQty = remainQty - used;
        } else {
            throw new PairingException("Error: using leg with zero remaining quantity, used=" + used + ", remainQty=" + remainQty + ", leg=" + this.toString());
        }
        int endSign = Integer.signum(remainQty);
        if (endSign != 0 && endSign != startSign) {
            throw new PairingException("Error: sign crossed for leg, used=" + used + ", remainQty=" + remainQty + ", leg=" + this.toString());
        }
        this.qty = this.remainQty;
        this.bigDecimalQty = new BigDecimal(this.qty);
        return newLegWith(used * startSign);
    }
    
    protected void restoreBy(Integer used) {
        int startSign = Integer.signum(resetQty);
        if (startSign == -1) {
            remainQty = remainQty - used;
        } else if (startSign == 1) {
            remainQty = remainQty + used;
        } else {
            throw new PairingException("Error: restoring leg with resetQty of zero, used=" + used + ", remainQty=" + remainQty + ", leg=" + this.toString());
        }
        int endSign = Integer.signum(remainQty);
        if (endSign != startSign) {
            throw new PairingException("Error: restoring a leg crossed tbe sign for leg, used=" + used + ", remainQty=" + remainQty + ", leg=" + this.toString());
        }
        this.qty = this.remainQty;
        this.bigDecimalQty = new BigDecimal(this.qty);
    }
    
    protected void resetQty() {
        this.remainQty = this.resetQty;
        this.qty = this.resetQty;
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
