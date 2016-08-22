package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;

abstract class OptionLeg extends AbstractLeg {
    protected final OptionConfig optionConfig;
    protected final OptionRoot optionRoot;
    
    protected OptionLeg(String symbol, Integer qty, OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, qty);
        this.optionConfig = optionConfig;
        this.optionRoot = optionRoot;
    }

    public OptionConfig getOptionConfig() {
        return optionConfig;
    }
    
    public OptionRoot getOptionRoot() {
        return optionRoot;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("optionLeg: {");
        builder.append(super.basicLegInfo()).append(", ");
        builder.append(optionConfig);
        builder.append("}");
        return builder.toString();
    }
    
    
}
