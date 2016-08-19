package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;

public class LongPut extends OptionLeg {
    LongPut(String symbol, Integer qty, OptionConfig optionConfig) {
        super(symbol, qty, optionConfig);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        LongPut t = new LongPut(this.symbol, used, this.optionConfig);
        return t;
    }
}
