package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface OrderLeg extends Position {
    
    class OrderLegBuilder extends AbstractPositionBuilder {
        private OrderLegBuilder() {
            super();
        };
        public OrderLegBuilder setOrderLegPrice(String price) {
            try {
                this.price = new BigDecimal(price);
            } catch (Exception e) {
                throw new BuilderException("Invalid OrderLeg price: " + price);
            }
            return this;
        }
        @Override
        public OrderLegBuilder setSymbol(String symbol) {
            super.setSymbol(symbol);
            return this;
        }
        @Override
        public OrderLegBuilder setDescription(String description) {
            super.setDescription(description);
            return this;
        }
        @Override
        public OrderLegBuilder setQty(Integer qty) {
            super.setQty(qty);
            return this;
        }
        @Override
        public OrderLegBuilder setOptionRoot(String optionRoot) {
            super.setOptionRoot(optionRoot);
            return this;
        }
        @Override
        public OrderLegBuilder setOptionType(OptionType optionType) {
            super.setOptionType(optionType);
            return this;
        }
        @Override
        public OrderLegBuilder setOptionStrike(String strike) {
            super.setOptionStrike(strike);
            return this;
        }
        @Override
        public OrderLegBuilder setOptionExpiry(String expiry) {
            super.setOptionExpiry(expiry);
            return this;
        }
        public OrderLeg build() {
            super.validate("OrderLeg");
            if (optionConfigBuilder != null) {
                optionConfig = optionConfigBuilder.build();
            }
            OrderLeg orderLeg = StructureImplFactory.buildOrderLeg(symbol, description, qty, price, optionConfig);
            symbol = null;
            qty = null;
            optionConfig = null;
            description = null;
            price = null;
            optionConfigBuilder = null;
            return orderLeg;
        }
    }
    
    static OrderLegBuilder newBuilder() {
        return new OrderLegBuilder(); 
    }
    
}