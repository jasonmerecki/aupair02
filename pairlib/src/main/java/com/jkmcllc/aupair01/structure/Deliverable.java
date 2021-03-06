package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;

public interface Deliverable {
    String getSymbol();
    BigDecimal getQty();
    BigDecimal getPrice();
    DeliverableType getDeliverableType();
    BigDecimal getMaintenancePct();
}