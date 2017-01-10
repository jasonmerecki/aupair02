package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface OrderLeg extends Position {
    
    class OrderLegBuilder extends AbstractPositionBuilder {
        private OrderLegBuilder() {
            super();
        };
        public OrderLegBuilder setOrderLegEquityMaintenanceMargin(String equityMaintenanceMargin) {
            try {
                this.equityMaintenanceMargin = new BigDecimal(equityMaintenanceMargin);
            } catch (Exception e) {
                throw new BuilderException("Invalid OrderLeg equityMaintenanceMargin: " + equityMaintenanceMargin);
            }
            return this;
        }
        public OrderLegBuilder setOrderLegEquityInitialMargin(String equityInitialMargin) {
            try {
                this.equityInitialMargin = new BigDecimal(equityInitialMargin);
            } catch (Exception e) {
                throw new BuilderException("Invalid OrderLeg equityInitialMargin: " + equityInitialMargin);
            }
            return this;
        }
        public AbstractPositionBuilder setOrderLegPrice(String price) {
            try {
                this.price = new BigDecimal(price);
            } catch (Exception e) {
                throw new BuilderException("Invalid OrderLeg price: " + price);
            }
            return this;
        }
        public OrderLeg build() {
            super.validate("OrderLeg");
            if (equityMaintenanceMargin == null || equityInitialMargin == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build OrderLeg, missing data: ");
                if (equityMaintenanceMargin == null) {
                    missing.add("equityMaintenanceMargin");
                }
                if (equityInitialMargin == null) {
                    missing.add("equityInitialMargin");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            return StructureImplFactory.buildOrderLeg(symbol, description, qty, price, equityMaintenanceMargin, equityInitialMargin, optionConfig);
        }
    }
    
    static OrderLegBuilder newBuilder() {
        return new OrderLegBuilder(); 
    }
    
}