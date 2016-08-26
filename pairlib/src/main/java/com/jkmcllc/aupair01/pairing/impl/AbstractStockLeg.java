package com.jkmcllc.aupair01.pairing.impl;

abstract class AbstractStockLeg extends AbstractLeg {
    protected AbstractStockLeg(String symbol, String description, Integer qty) {
        super(symbol, description, qty);
    }
}
