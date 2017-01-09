package com.jkmcllc.aupair01.structure;

import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface OrderLeg extends Position {
    
    class OrderLegBuilder extends PositionBuilder {
        private OrderLegBuilder() {
            super();
        };
        public OrderLeg build() {
            super.validate();
            return StructureImplFactory.buildOrderLeg(symbol, description, qty, price, optionConfig);
        }
    }
    
    static OrderLegBuilder newBuilder() {
        return new OrderLegBuilder(); 
    }
    
}