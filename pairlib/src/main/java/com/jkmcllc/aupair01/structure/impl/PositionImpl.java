package com.jkmcllc.aupair01.structure.impl;

import com.jkmcllc.aupair01.structure.Position;
import com.jkmcllc.aupair01.structure.OptionConfig;

class PositionImpl implements Position {
    private final String symbol;
    private final Integer qty;
    private final OptionConfig optionConfig;
    
    PositionImpl(String symbol, Integer qty, OptionConfig optionConfig) {
        this.symbol = symbol;
        this.qty = qty;
        this.optionConfig = optionConfig;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Posotion: {symbol:");
        builder.append(symbol);
        builder.append(", qty:");
        builder.append(qty);
        if (optionConfig != null) {
            builder.append(", optionConfig:");
            builder.append(optionConfig);
        }
        builder.append("}");
        return builder.toString();
    }
    @Override
    public String getSymbol() {
        return symbol;
    }
    @Override
    public Integer getQty() {
        return qty;
    }
    @Override
    public OptionConfig getOptionConfig() {
        return optionConfig;
    }

}