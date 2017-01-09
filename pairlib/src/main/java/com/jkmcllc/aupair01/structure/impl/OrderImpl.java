package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.util.List;

import com.jkmcllc.aupair01.structure.Order;
import com.jkmcllc.aupair01.structure.OrderLeg;

public class OrderImpl implements Order {
    private final List<OrderLeg> orderLegs;
    private final String orderId;
    private final String orderDescription;
    private final BigDecimal equityMaintenanceMargin;
    private final BigDecimal equityInitialMargin;
    
    
    OrderImpl(String orderId, String orderDescription, List<OrderLeg> orderLegs, BigDecimal equityMaintenanceMargin, BigDecimal equityInitialMargin) {
        this.orderId = orderId;
        this.orderDescription = orderDescription;
        this.orderLegs = orderLegs;
        this.equityMaintenanceMargin = equityMaintenanceMargin;
        this.equityInitialMargin = equityInitialMargin;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Order: {");
        builder.append("orderId: ");
        builder.append(orderId);
        builder.append(", orderDescription: '");
        builder.append(orderDescription);
        builder.append("', equityMaintenanceMargin: ");
        builder.append(equityMaintenanceMargin);
        builder.append(", equityInitialMargin: ");
        builder.append(equityInitialMargin);
        builder.append(", OrderLegs: ");
        builder.append(orderLegs);
        builder.append("}");
        return builder.toString();
    }
    
    @Override
    public List<OrderLeg> getOrderLegs() {
        return orderLegs;
    }

    @Override
    public BigDecimal getEquityMaintenanceMargin() {
        return equityMaintenanceMargin;
    }

    @Override
    public BigDecimal getEquityInitialMargin() {
        return equityInitialMargin;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public String getOrderDescription() {
        return orderDescription;
    }

}
