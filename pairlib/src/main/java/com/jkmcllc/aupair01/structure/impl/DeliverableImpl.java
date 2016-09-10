package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.DeliverableType;

class DeliverableImpl implements Deliverable {
    private final String symbol;
    private final BigDecimal qty;
    private final BigDecimal price;
    private final DeliverableType deliverableType;
    
    DeliverableImpl(String symbol, BigDecimal qty, BigDecimal price, DeliverableType type) {
        this.symbol = symbol;
        this.qty = qty;
        this.price = price;
        this.deliverableType = type;
    }
    
    public String getSymbol() {
        return symbol;
    }
    public BigDecimal getQty() {
        return qty;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public DeliverableType getDeliverableType() {
        return deliverableType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Deliverable: {symbol:");
        builder.append(symbol);
        builder.append(", qty:");
        builder.append(qty);
        builder.append(", price:");
        builder.append(price);
        builder.append(", deliverableType:");
        builder.append(deliverableType);
        builder.append("}");
        return builder.toString();
    }
}
