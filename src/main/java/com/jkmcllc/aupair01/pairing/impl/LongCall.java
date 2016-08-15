package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;

class LongCall extends OptionLeg implements Leg {
    LongCall(String symbol, Integer qty, OptionConfig optionConfig) {
        super(symbol, qty, optionConfig);
    }
}
