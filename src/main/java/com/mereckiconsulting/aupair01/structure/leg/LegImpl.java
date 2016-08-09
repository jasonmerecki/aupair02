package com.mereckiconsulting.aupair01.structure.leg;

public class LegImpl implements Leg {
    private String symbol;
    private Integer qty;
    private OptionConfig optionConfig;
    
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public Integer getQty() {
        return qty;
    }
    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public OptionConfig getOptionConfig() {
        return optionConfig;
    }
    public void setOptionConfig(OptionConfig optionConfig) {
        this.optionConfig = optionConfig;
    }
}
