package com.jkmcllc.aupair01.pairing.impl;

import java.util.Collection;

public interface Leg {
    static final String STOCK = "STOCK";
    static final String STOCKOPTION = "STOCKOPTION";
    static final String DELIVERABLE = "DELIVERABLE";
    Integer getQty();
    String getSymbol();
    String getDescription();
    String getType();
    Collection<? extends Leg> getMultiLegs();
}
