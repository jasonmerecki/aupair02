package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionType;

class OptionConfigImpl implements OptionConfig {
    private final String optionRootSymbol;
    private final OptionType optionType;
    private final String strike;
    private final String expiry;
    private final BigDecimal strikePrice;
    private final LocalDateTime expiryTime;
    
    OptionConfigImpl(String optionRoot, OptionType optionType, 
            String strike, BigDecimal strikePrice, String expiry, LocalDateTime expiryTime) {
        this.optionRootSymbol = optionRoot;
        this.optionType = optionType;
        this.strike = strike;
        this.expiry = expiry;
        this.strikePrice = strikePrice;
        this.expiryTime = expiryTime;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OptionConfig: {optionRoot:");
        builder.append(optionRootSymbol);
        builder.append(", optionType:");
        builder.append(optionType);
        builder.append(", strike:");
        builder.append(strike);
        builder.append(", expiry:");
        builder.append(expiry);
        builder.append("}");
        return builder.toString();
    }
    @Override
    public String getOptionRoot() {
        return optionRootSymbol;
    }
    @Override
    public OptionType getOptionType() {
        return optionType;
    }
    @Override
    public String getStrike() {
        return strike;
    }
    @Override
    public String getExpiry() {
        return expiry;
    }

    public String getOptionRootSymbol() {
        return optionRootSymbol;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

}
