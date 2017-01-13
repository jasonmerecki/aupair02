package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Collection;

import com.jkmcllc.aupair01.exception.PairingException;
import com.jkmcllc.aupair01.store.Constants;

abstract class AbstractLeg implements Leg {
    static final String STOCK = "STOCK";
    static final String STOCKOPTION = "STOCKOPTION";
    static final String DELIVERABLE = "DELIVERABLE";
    static enum OpenClose {OPEN, CLOSE};
    
    protected final String symbol;
    protected final String description;
    protected Integer resetQty;
    protected final Integer positionResetQty;
    protected final BigDecimal price;
    
    protected Integer qty;
    protected BigDecimal legValue;
    
    // used only if this is an order
    OpenClose openClose;
    
    protected AbstractLeg(String symbol, String description, Integer qty, Integer positionResetQty, BigDecimal price) {
        this.symbol = symbol;
        this.description = description;
        this.positionResetQty = positionResetQty;
        this.resetQty = this.qty = qty;
        this.price = price;
        this.legValue = null;
    }
    
    protected String basicLegInfo() {
        StringBuilder basicInfo = new StringBuilder();
        if (symbol != null) {
            basicInfo.append("symbol: \"").append(symbol).append("\"");
        }
        if (description != null && Constants.EMPTY_STRING.equals(description) == false) {
            basicInfo.append(", ").append("description: \"").append(description).append("\"");;
        }
        if (qty != null) {
            basicInfo.append(", ").append("qty: ").append(qty);
        }
        if (price != null) {
            basicInfo.append(", ").append("price: ").append(price);
        }
        if (openClose != null) {
            basicInfo.append(", ").append("openClose: ").append(openClose);
        }
        return basicInfo.toString();
    }
    
    protected Leg reduceBy(Integer used) {
        int startSign = Integer.signum(qty);
        if (startSign == -1) {
            qty = qty + used;
        } else if (startSign == 1) {
            qty = qty - used;
        } else {
            throw new PairingException("Error: using leg with zero remaining quantity, used=" + used + ", remainQty=" + qty + ", leg=" + this.toString());
        }
        int endSign = Integer.signum(qty);
        if (endSign != 0 && endSign != startSign) {
            throw new PairingException("Error: sign crossed for leg, used=" + used + ", remainQty=" + qty + ", leg=" + this.toString());
        }
        return newLegWith(used * startSign);
    }
    
    protected void restoreBy(Integer used) {
        int startSign = Integer.signum(resetQty);
        if (startSign == -1) {
            qty = qty - used;
        } else if (startSign == 1) {
            qty = qty + used;
        } else {
            throw new PairingException("Error: restoring leg with resetQty of zero, used=" + used + ", remainQty=" + qty + ", leg=" + this.toString());
        }
        int endSign = Integer.signum(qty);
        if (endSign != startSign) {
            throw new PairingException("Error: restoring a leg crossed tbe sign for leg, used=" + used + ", remainQty=" + qty + ", leg=" + this.toString());
        }
    }
    
    protected void resetQty(boolean hardReset) {
        this.qty = hardReset ? this.positionResetQty : this.resetQty;
        this.resetQty = this.qty;
    }
    
    void modifyQty(Integer deltaQty) {
        this.qty += deltaQty;
        this.resetQty += deltaQty;
    }
    
    protected Integer getRemainQty() {
        return qty;
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
