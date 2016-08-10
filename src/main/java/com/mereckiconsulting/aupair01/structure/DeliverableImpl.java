package com.mereckiconsulting.aupair01.structure;

import java.math.BigDecimal;

public class DeliverableImpl implements Deliverable {
    String symbol;
    BigDecimal qty;
    DeliverableType type;
    
    public String getSymbol() {
        return symbol;
    }
    public BigDecimal getQty() {
        return qty;
    }
    public DeliverableType getType() {
        return type;
    }
}
