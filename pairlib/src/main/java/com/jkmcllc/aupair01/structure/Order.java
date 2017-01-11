package com.jkmcllc.aupair01.structure;

import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.store.Constants;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface Order {
    List<OrderLeg> getOrderLegs();
    String getOrderId();
    String getOrderDescription();
    
    class OrderBuilder {
        private OrderBuilder() {};
        private List<OrderLeg> orderLegs = new ArrayList<>();
        private String orderId;
        private String orderDescription = Constants.EMPTY_STRING;;
        
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

        public Order build() {
            if (orderLegs.isEmpty()) {
                throw new BuilderException("Invalid order, has no legs");
            }
            String hasDupe = CorePosition.findDuplicate(orderLegs);
            if (hasDupe != null) {
                throw new BuilderException("Invalid order, has duplicate leg information: " + hasDupe);
            }
            if (orderId == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Order, missing data: ");
                if (orderId == null) {
                    missing.add("orderId");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            Order order = StructureImplFactory.buildOrder(orderId, orderDescription, orderLegs);
            orderLegs = new ArrayList<>();
            orderId = null;
            orderDescription = Constants.EMPTY_STRING;
            return order;
        }
    }
    static OrderBuilder newBuilder() {
        return new OrderBuilder();
    }
}