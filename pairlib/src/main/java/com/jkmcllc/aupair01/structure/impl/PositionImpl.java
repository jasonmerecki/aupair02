package com.jkmcllc.aupair01.structure.impl;

import com.jkmcllc.aupair01.structure.Position;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.store.Constants;
import com.jkmcllc.aupair01.structure.OptionConfig;

class PositionImpl implements Position {
    private final String symbol;
    private final String description;
    private final Integer qty;
    private final BigDecimal price;
    private final BigDecimal equityMaintenanceMargin;
    private final BigDecimal equityInitialMargin;
    private final OptionConfig optionConfig;
    
    PositionImpl(String symbol, String description, Integer qty, BigDecimal price, 
            BigDecimal equityMaintenanceMargin, BigDecimal equityInitialMargin, OptionConfig optionConfig) {
        this.symbol = symbol;
        this.description = (description == null) ? Constants.EMPTY_STRING : description;
        this.qty = qty;
        this.price = price;
        this.equityMaintenanceMargin = equityMaintenanceMargin != null ? equityMaintenanceMargin : BigDecimal.ZERO;
        this.equityInitialMargin = equityInitialMargin != null ? equityInitialMargin : BigDecimal.ZERO;
        this.optionConfig = optionConfig;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Position: {");
        builder.append(buildCorePositionString());
        builder.append("}");
        return builder.toString();
    }
    protected StringBuilder buildCorePositionString() {
        StringBuilder builder = new StringBuilder();
        builder.append("symbol:");
        builder.append(symbol);
        if (description != null && Constants.EMPTY_STRING.equals(description) == false) {
            builder.append(", description: \"");
            builder.append(description).append("\"");
        }
        builder.append(", qty: ");
        builder.append(qty);
        builder.append(", price: ");
        builder.append(price);
        if (optionConfig != null) {
            builder.append(", ").append(optionConfig);
        }
        return builder;
    }
    @Override
    public String getSymbol() {
        return symbol;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public Integer getQty() {
        return qty;
    }
    @Override
    public BigDecimal getPrice() {
        return price;
    }
    @Override
    public OptionConfig getOptionConfig() {
        return optionConfig;
    }
    @Override
    public BigDecimal getEquityMaintenanceMargin() {
        return equityMaintenanceMargin;
    }
    @Override
    public BigDecimal getEquityInitialMargin() {
        return equityInitialMargin;
    }

}
