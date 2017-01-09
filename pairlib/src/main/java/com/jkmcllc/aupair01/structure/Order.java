package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.store.Constants;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface Order {
    List<OrderLeg> getOrderLegs();
    String getOrderId();
    String getOrderDescription();
    BigDecimal getEquityMaintenanceMargin();
    BigDecimal getEquityInitialMargin();
    
    class OrderBuilder {
        private OrderBuilder() {};
        private List<OrderLeg> orderLegs = new ArrayList<>();
        private String orderId;
        private String orderDescription = Constants.EMPTY_STRING;;
        private BigDecimal equityMaintenanceMargin;
        private BigDecimal equityInitialMargin;
        
        public OrderBuilder addOrderLeg(OrderLeg orderLeg) {
            orderLegs.add(orderLeg);
            return this;
        }
        public OrderBuilder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }
        public OrderBuilder setOrderDescription(String orderDescription) {
            this.orderDescription = orderDescription;
            return this;
        }
        public OrderBuilder setOrderEquityMaintenanceMargin(String equityMaintenanceMargin) {
            try {
                this.equityMaintenanceMargin = new BigDecimal(equityMaintenanceMargin);
            } catch (Exception e) {
                throw new BuilderException("Invalid order equityMaintenanceMargin: " + equityMaintenanceMargin);
            }
            return this;
        }
        public OrderBuilder setOrderEquityInitialMargin(String equityInitialMargin) {
            try {
                this.equityInitialMargin = new BigDecimal(equityInitialMargin);
            } catch (Exception e) {
                throw new BuilderException("Invalid order equityInitialMargin: " + equityInitialMargin);
            }
            return this;
        }
        public Order build() {
            if (orderLegs.isEmpty()) {
                throw new BuilderException("Invalid order, has no legs");
            }
            if (orderId == null || equityMaintenanceMargin == null || equityInitialMargin == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Order, missing data: ");
                if (equityMaintenanceMargin == null) {
                    missing.add("equityMaintenanceMargin");
                }
                if (equityInitialMargin == null) {
                    missing.add("equityInitialMargin");
                }
                if (orderId == null) {
                    missing.add("orderId");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            Order order = StructureImplFactory.buildOrder(orderId, orderDescription, orderLegs, equityMaintenanceMargin, equityInitialMargin);
            orderLegs = new ArrayList<>();
            orderId = null;
            equityMaintenanceMargin = null;
            equityInitialMargin = null;
            orderDescription = Constants.EMPTY_STRING;
            return order;
        }
    }
    static OrderBuilder newBuilder() {
        return new OrderBuilder();
    }
}