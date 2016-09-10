package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

abstract class StrategyLeg extends AbstractLeg {
    StrategyLeg(String symbol, String description, Integer qty, BigDecimal price) {
        super(symbol, description, qty, price);
    }
}
