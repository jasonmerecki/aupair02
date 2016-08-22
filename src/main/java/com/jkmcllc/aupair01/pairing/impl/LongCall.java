package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class LongCall extends OptionLeg implements Leg {
    LongCall(String symbol, Integer qty, OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, qty, optionConfig, optionRoot);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        LongCall t = new LongCall(this.symbol, used, this.optionConfig, this.optionRoot);
        return t;
    }
}
