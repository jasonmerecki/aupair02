package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionType;

public class OptionConfigImpl implements OptionConfig {
    private final String optionRootSymbol;
    private final OptionType optionType;
    private final String strike;
    private final String expiry;
    private final BigDecimal strikePrice;
    private final LocalDateTime expiryTimeLocal;
    private final Date expiryDate;
    
    OptionConfigImpl(String optionRoot, OptionType optionType, 
            String strike, BigDecimal strikePrice, String expiry, LocalDateTime expiryTime, Date expiryDate) {
        this.optionRootSymbol = optionRoot;
        this.optionType = optionType;
        this.strike = strike;
        this.expiry = expiry;
        this.strikePrice = strikePrice;
        this.expiryTimeLocal = expiryTime;
        this.expiryDate = expiryDate;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("optionConfig: {optionRoot: ");
        builder.append(optionRootSymbol);
        builder.append(", optionType: ");
        builder.append(optionType);
        builder.append(", strike: ");
        builder.append(strike);
        builder.append(", expiry: \"");
        builder.append(expiry);
        builder.append("\"}");
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
    public String getExpiryString() {
        return expiry;
    }

    public String getOptionRootSymbol() {
        return optionRootSymbol;
    }
    @Override
    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public LocalDateTime getExpiryTimeLocal() {
        return expiryTimeLocal;
    }
    
    public Date getExpiryDateDate() {
        return expiryDate;
    }
    
    @SuppressWarnings("unchecked")
    public LocalDateTime getExpiryDate() {
        return expiryTimeLocal;
    }

}
