package com.jkmcllc.aupair01.structure;

public interface OrderLeg extends Position {
    
    class OrderLegBuilder extends PositionBuilder {
        private OrderLegBuilder() {
            super();
        };
    }
    
    static OrderLegBuilder newBuilder() {
        return new OrderLegBuilder(); 
    }
}