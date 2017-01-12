package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.structure.CorePosition;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OrderLeg;

public class OrderLegImpl extends PositionImpl implements OrderLeg  {

    OrderLegImpl (String symbol, String description, Integer qty, BigDecimal price, 
            OptionConfig optionConfig) {
        super(symbol, description, qty, price, optionConfig);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderLeg: {");
        builder.append(buildCorePositionString());
        builder.append("}");
        return builder.toString();
    }
    @Override
    public CorePositionType getCorePositionType() {
        return CorePosition.CorePositionType.ORDERLEG;
    }
    
}
