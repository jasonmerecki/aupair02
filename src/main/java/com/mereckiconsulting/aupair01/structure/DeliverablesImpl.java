package com.mereckiconsulting.aupair01.structure;

import java.util.List;

public class DeliverablesImpl implements Deliverables {
    private final List<Deliverable> deliverableList;
    public DeliverablesImpl(List<Deliverable> deliverableList) {
        this.deliverableList = deliverableList;
    }
    @Override
    public List<Deliverable> getDeliverableList() {
        return deliverableList;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeliverablesImpl: {deliverableList:");
        builder.append(deliverableList);
        builder.append("}");
        return builder.toString();
    }

}
