package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class ShortPut extends OptionLeg {
    ShortPut(String symbol, Integer qty, OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, qty, optionConfig, optionRoot);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        ShortPut t = new ShortPut(this.symbol, used, this.optionConfig, this.optionRoot);
        return t;
    }
}
