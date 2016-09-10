package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class ShortPut extends AbstractOptionLeg {
    ShortPut(String symbol, String description, Integer qty, BigDecimal price, OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, description, qty, price, optionConfig, optionRoot);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        ShortPut t = new ShortPut(this.symbol, this.description, used, this.price, this.optionConfig, this.optionRoot);
        return t;
    }
}
