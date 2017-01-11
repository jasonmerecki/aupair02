package com.jkmcllc.aupair01.structure.impl;

import java.util.List;

import com.jkmcllc.aupair01.structure.Order;
import com.jkmcllc.aupair01.structure.OrderLeg;

public class OrderImpl implements Order {
    private final List<OrderLeg> orderLegs;
    private final String orderId;
    private final String orderDescription;
    
    OrderImpl(String orderId, String orderDescription, List<OrderLeg> orderLegs) {
        this.orderId = orderId;
        this.orderDescription = orderDescription;
        this.orderLegs = orderLegs;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Order: {");
        builder.append("orderId: ");
        builder.append(orderId);
        builder.append(", orderDescription: '");
        builder.append(orderDescription);
        builder.append("', OrderLegs: ");
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

}
