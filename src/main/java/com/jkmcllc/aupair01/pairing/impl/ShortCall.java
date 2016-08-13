package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;

class ShortCall extends OptionLeg {
    ShortCall(String symbol, Integer qty, OptionConfig optionConfig) {
        super(symbol, qty, optionConfig);
    }
}
