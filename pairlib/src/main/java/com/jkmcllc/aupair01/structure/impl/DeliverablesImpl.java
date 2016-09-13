package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.util.List;

import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.Deliverables;

class DeliverablesImpl implements Deliverables {
    private final List<Deliverable> deliverableList;
    private BigDecimal deliverablesValue = null;
    DeliverablesImpl(List<Deliverable> deliverableList) {
        this.deliverableList = deliverableList;
    }
    @Override
    public BigDecimal getDeliverablesValue() {
        if (this.deliverablesValue == null) {
            BigDecimal value = BigDecimal.ZERO;
            for (Deliverable d : deliverableList) {
                if (d.getPrice() != null && d.getQty() != null) {
                    value = value.add(d.getPrice().multiply(d.getQty()));
                }
            }
            this.deliverablesValue = value;
        }
        return this.deliverablesValue;
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
