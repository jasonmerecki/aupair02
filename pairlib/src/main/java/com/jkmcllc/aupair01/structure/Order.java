package com.jkmcllc.aupair01.structure;

import java.util.Collection;

public interface Order {
    Collection<OrderLeg> getOrderLegs();
    
    class OrderBuilder {
    }
    static OrderBuilder newBuilder() {
        return new OrderBuilder();
    }
}