package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OrderLeg;

public class OrderLegImpl extends PositionImpl implements OrderLeg  {

    OrderLegImpl (String symbol, String description, Integer qty, BigDecimal price, 
            BigDecimal equityMaintenanceMargin, BigDecimal equityInitialMargin, OptionConfig optionConfig) {
        super(symbol, description, qty, price, equityMaintenanceMargin, equityInitialMargin, optionConfig);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderLeg: {");
        builder.append(buildCorePositionString());
        builder.append("}");
        return builder.toString();
    }
    
}
