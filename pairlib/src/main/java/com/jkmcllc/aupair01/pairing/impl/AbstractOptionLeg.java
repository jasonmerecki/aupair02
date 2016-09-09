package com.jkmcllc.aupair01.pairing.impl;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;

abstract class AbstractOptionLeg extends AbstractLeg {
    protected final OptionConfig optionConfig;
    protected final OptionRoot optionRoot;
    
    protected AbstractOptionLeg(String symbol, String description, Integer qty, OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, description, qty);
        this.optionConfig = optionConfig;
        this.optionRoot = optionRoot;
    }

    public OptionConfig getOptionConfig() {
        return optionConfig;
    }
    
    public OptionRoot getOptionRoot() {
        return optionRoot;
    }
    
    String getType() {
        return AbstractLeg.STOCKOPTION;
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
