package com.mereckiconsulting.aupair01.structure;

import java.math.BigDecimal;

public class DeliverableImpl implements Deliverable {
    private final String symbol;
    private final BigDecimal qty;
    private final DeliverableType deliverableType;
    
    public DeliverableImpl(String symbol, BigDecimal qty, DeliverableType type) {
        this.symbol = symbol;
        this.qty = qty;
        this.deliverableType = type;
    }
    
    public String getSymbol() {
        return symbol;
    }
    public BigDecimal getQty() {
        return qty;
    }
    public DeliverableType getDeliverableType() {
        return deliverableType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeliverableImpl: {symbol:");
        builder.append(symbol);
        builder.append(", qty:");
        builder.append(qty);
        builder.append(", deliverableType:");
        builder.append(deliverableType);
        builder.append("}");
        return builder.toString();
    }
}
