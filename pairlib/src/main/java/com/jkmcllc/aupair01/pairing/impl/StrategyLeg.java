package com.jkmcllc.aupair01.pairing.impl;

abstract class StrategyLeg extends AbstractLeg {
    StrategyLeg(String symbol, String description, Integer qty) {
        super(symbol, description, qty);
    }
}
