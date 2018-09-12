package com.jkmcllc.aupair01.structure.impl;

import com.jkmcllc.aupair01.structure.Position;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.store.Constants;
import com.jkmcllc.aupair01.structure.CorePosition;
import com.jkmcllc.aupair01.structure.OptionConfig;

class PositionImpl implements Position {
    private final String symbol;
    private final String description;
    private final Integer qty;
    private final BigDecimal price;
    private final OptionConfig optionConfig;
    
    PositionImpl(String symbol, String description, Integer qty, BigDecimal price, 
            OptionConfig optionConfig) {
        this.symbol = symbol;
        this.description = (description == null) ? Constants.EMPTY_STRING : description;
        this.qty = qty;
        this.price = price;
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
    public CorePositionType getCorePositionType() {
        return CorePosition.CorePositionType.POSITION;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((qty == null) ? 0 : qty.hashCode());
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
    		boolean matches = matches(obj);
    		if (!matches) {
    			return false;
    		}
        PositionImpl other = (PositionImpl) obj;
        if (qty == null) {
            if (other.qty != null)
                return false;
        } else if (!qty.equals(other.qty))
            return false;
        return true;
    }
    
    @Override
    public boolean matches(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PositionImpl other = (PositionImpl) obj;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        if (optionConfig != null && other.optionConfig != null &&
                !optionConfig.equals(other.optionConfig))
            return false;
        if (optionConfig != null && other.optionConfig == null)
    			return false;
        if (optionConfig == null && other.optionConfig != null)
    			return false;
        return true;
    }

}
