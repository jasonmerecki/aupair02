package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.jkmcllc.aupair01.structure.Order;
import com.jkmcllc.aupair01.structure.OrderLeg;

public class OrderImpl implements Order {
    private final List<OrderLeg> orderLegs;
    private final String orderId;
    private final String orderDescription;
    private final BigDecimal orderMaintenanceCost;
    private final BigDecimal orderInitialCost;
    
    OrderImpl(String orderId, String orderDescription, 
            BigDecimal orderMaintenanceCost, BigDecimal orderInitialCost, List<OrderLeg> orderLegs) {
        this.orderId = orderId;
        this.orderDescription = orderDescription;
        this.orderLegs = Collections.unmodifiableList(orderLegs);
        this.orderMaintenanceCost = orderMaintenanceCost;
        this.orderInitialCost = orderInitialCost;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Order: {");
        builder.append("orderId: ");
        builder.append(orderId);
        builder.append(", orderDescription: '");
        builder.append(orderDescription);
        builder.append("'");
        builder.append(", orderInitialCost: ");
        builder.append(orderInitialCost);
        builder.append(", orderMaintenanceCost: ");
        builder.append(orderMaintenanceCost);
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
    public String getOrderId() {
        return orderId;
    }

    @Override
    public String getOrderDescription() {
        return orderDescription;
    }

    @Override
    public BigDecimal getOrderMaintenanceCost() {
        return orderMaintenanceCost;
    }

    @Override
    public BigDecimal getOrderInitialCost() {
        return orderInitialCost;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
        result = prime * result + ((orderLegs == null) ? 0 : orderLegs.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderImpl other = (OrderImpl) obj;
        if (orderId == null) {
            if (other.orderId != null)
                return false;
        } else if (!orderId.equals(other.orderId))
            return false;
        if (orderLegs == null) {
            if (other.orderLegs != null)
                return false;
        } else if (!orderLegs.equals(other.orderLegs))
            return false;
        return true;
    }

}
