package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.Deliverables;

class DeliverablesImpl implements Deliverables {
    private final List<Deliverable> deliverableList;
    private final List<Deliverable> stockDeliverables;
    private BigDecimal deliverablesValue = null;
    DeliverablesImpl(List<Deliverable> deliverableList) {
        this.deliverableList = deliverableList;
        this.stockDeliverables = deliverableList.stream()
                .filter(d -> DeliverableType.S.equals(d.getDeliverableType()))
                .collect(Collectors.toList());
    }
    @Override
    public BigDecimal getDeliverablesValue() {
        if (this.deliverablesValue == null) {
            BigDecimal value = BigDecimal.ZERO;
            for (Deliverable d : deliverableList) {
                if (DeliverableType.S.equals(d.getDeliverableType()) 
                        && d.getPrice() != null && d.getQty() != null) {
                    value = value.add(d.getPrice().multiply(d.getQty()));
                } else if (DeliverableType.C.equals(d.getDeliverableType())
                        && d.getPrice() != null) {
                    value = value.add(d.getPrice());
                }
            }
            this.deliverablesValue = value;
        }
        return this.deliverablesValue;
    }
    @Override
    public List<Deliverable> getStockDeliverableList() {
        return stockDeliverables;
    }
    @Override
    public List<Deliverable> getDeliverableList() {
        return deliverableList;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // builder.append("Deliverables: {deliverableList:");
        builder.append(deliverableList);
        // builder.append("}");
        return builder.toString();
    }

}
