package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;

public class LongCall extends OptionLeg implements Leg {
    LongCall(String symbol, Integer qty, OptionConfig optionConfig) {
        super(symbol, qty, optionConfig);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        LongCall t = new LongCall(this.symbol, used, this.optionConfig);
        return t;
    }
}
