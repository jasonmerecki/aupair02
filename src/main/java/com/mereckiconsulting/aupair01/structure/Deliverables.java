package com.mereckiconsulting.aupair01.structure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mereckiconsulting.aupair01.exception.BuilderException;
import com.mereckiconsulting.aupair01.structure.impl.StructureImplFactory;

public interface Deliverables {
    List<Deliverable> getDeliverableList();
    
    public class DeliverablesBuilder {
        private String symbol;
        private BigDecimal qty;
        private DeliverableType deliverableType;
        private List<Deliverable> deliverableList = new ArrayList<>();
        private DeliverablesBuilder() {}
        public DeliverablesBuilder setDeliverableSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public DeliverablesBuilder setDeliverableQty(String qty) {
            try {
                this.qty = new BigDecimal(qty);
            } catch (Exception e) {
                throw new BuilderException("Invalid deliverable quantity: " + qty);
            }
            return this;
        }
        public DeliverablesBuilder setDeliverableType(DeliverableType deliverableType) {
            this.deliverableType = deliverableType;
            return this;
        }
        public DeliverablesBuilder add() {
            if (symbol == null || qty == null || deliverableType == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Deliverable, missing data: ");
                if (symbol == null) {
                    missing.add("symbol");
                }
                if (qty == null) {
                    missing.add("qty");
                }
                if (deliverableType == null) {
                    missing.add("deliverableType");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            Deliverable deliverable = StructureImplFactory.buildDeliverable(symbol, qty, deliverableType);
            deliverableList.add(deliverable);
            symbol = null; qty = null; deliverableType = null;
            return this;
        }
        public Deliverables build() {
            if (deliverableList.isEmpty()) {
                throw new BuilderException("Cannot build Deliverables, no Deliverable added");
            }
            Deliverables deliverables = StructureImplFactory.buildDeliverables(deliverableList);
            deliverableList = new ArrayList<>();
            return deliverables;
        }
    }
    public static DeliverablesBuilder newBuilder() {
        return new DeliverablesBuilder();
    }
}
