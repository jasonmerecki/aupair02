package com.mereckiconsulting.aupair01.structure.impl;

import java.util.List;

import com.mereckiconsulting.aupair01.structure.Deliverable;
import com.mereckiconsulting.aupair01.structure.Deliverables;

class DeliverablesImpl implements Deliverables {
    private final List<Deliverable> deliverableList;
    DeliverablesImpl(List<Deliverable> deliverableList) {
        this.deliverableList = deliverableList;
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
