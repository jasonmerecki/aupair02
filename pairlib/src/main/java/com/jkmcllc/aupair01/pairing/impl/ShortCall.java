package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class ShortCall extends AbstractOptionLeg {
    ShortCall(String symbol, String description, Integer qty, OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, description, qty, optionConfig, optionRoot);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        ShortCall t = new ShortCall(this.symbol, this.description, used, this.optionConfig, this.optionRoot);
        return t;
    }
}
