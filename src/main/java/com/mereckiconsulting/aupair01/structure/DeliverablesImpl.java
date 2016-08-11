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

}
