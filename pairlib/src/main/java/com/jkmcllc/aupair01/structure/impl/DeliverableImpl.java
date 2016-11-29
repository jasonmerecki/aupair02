package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.pairing.impl.GlobalConfigType;
import com.jkmcllc.aupair01.pairing.impl.StrategyConfigs;
import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.DeliverableType;

class DeliverableImpl implements Deliverable {
    private final String symbol;
    private final BigDecimal qty;
    private final BigDecimal price;
    private final BigDecimal maintenancePct;
    private final DeliverableType deliverableType;
    
    DeliverableImpl(String symbol, BigDecimal qty, BigDecimal price, BigDecimal maintenancePct, DeliverableType type) {
        this.symbol = symbol;
        this.qty = qty;
        this.price = price;
        if (maintenancePct == null) {
            maintenancePct = StrategyConfigs.getInstance().getGlobalConfig(GlobalConfigType.MAINTENANCE_PCT);
        }
        this.maintenancePct = maintenancePct;
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
    public BigDecimal getMaintenancePct() {
        return maintenancePct;
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
        builder.append(", maintenancePct:");
        builder.append(maintenancePct);
        builder.append(", deliverableType:");
        builder.append(deliverableType);
        builder.append("}");
        return builder.toString();
    }
}
