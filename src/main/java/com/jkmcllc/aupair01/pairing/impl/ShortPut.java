package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;

public class ShortPut extends OptionLeg {
    ShortPut(String symbol, Integer qty, OptionConfig optionConfig) {
        super(symbol, qty, optionConfig);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        ShortPut t = new ShortPut(this.symbol, used, this.optionConfig);
        return t;
    }
}
