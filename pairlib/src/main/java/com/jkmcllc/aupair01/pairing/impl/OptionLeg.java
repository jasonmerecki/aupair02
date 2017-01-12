package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;

public class OptionLeg extends AbstractOptionLeg {
    OptionLeg(String symbol, String description, Integer qty, Integer positionResetQty, BigDecimal price, 
            OptionType optionType, OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, description, qty, positionResetQty, price, optionType, optionConfig, optionRoot);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        OptionLeg t = new OptionLeg(this.symbol, this.description, used, this.positionResetQty, this.price, 
                this.optionType, this.optionConfig, this.optionRoot);
        return t;
    }
}
