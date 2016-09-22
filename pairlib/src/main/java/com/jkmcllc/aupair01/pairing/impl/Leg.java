package com.jkmcllc.aupair01.pairing.impl;

import java.util.Collection;

public interface Leg {
    Integer getQty();
    String getSymbol();
    String getDescription();
    Collection<? extends Leg> getMultiLegs();
    String getType();
}
