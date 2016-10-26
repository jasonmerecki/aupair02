package com.jkmcllc.aupair01.pairing.impl;

import java.util.Collection;

public interface Leg {
    Integer getQty();
    String getSymbol();
    String getDescription();
    String getType();
    Collection<? extends Leg> getMultiLegs();
}
