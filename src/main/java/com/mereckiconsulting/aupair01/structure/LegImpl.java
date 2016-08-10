package com.mereckiconsulting.aupair01.structure;

public class LegImpl implements Leg {
    private final String symbol;
    private final Integer qty;
    private final OptionConfig optionConfig;
    
    public LegImpl(String symbol, Integer qty, OptionConfig optionConfig) {
        this.symbol = symbol;
        this.qty = qty;
        this.optionConfig = optionConfig;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LegImpl: {symbol:");
        builder.append(symbol);
        builder.append(", qty:");
        builder.append(qty);
        builder.append(", optionConfig:");
        builder.append(optionConfig);
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
