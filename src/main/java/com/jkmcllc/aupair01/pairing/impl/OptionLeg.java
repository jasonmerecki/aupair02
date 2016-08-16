package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;

class OptionLeg extends AbstractLeg {
    protected final OptionConfig optionConfig;
    
    protected OptionLeg(String symbol, Integer qty, OptionConfig optionConfig) {
        super(symbol, qty);
        this.optionConfig = optionConfig;
    }

    public OptionConfig getOptionConfig() {
        return optionConfig;
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
