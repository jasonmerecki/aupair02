package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;

abstract class AbstractOptionLeg extends AbstractLeg {
    protected final OptionConfig optionConfig;
    protected final OptionRoot optionRoot;
    protected BigDecimal grossItmAmount;
    protected BigDecimal weightedItmCompliment;
    
    protected AbstractOptionLeg(String symbol, String description, Integer qty, BigDecimal price, 
            OptionConfig optionConfig, OptionRoot optionRoot) {
        super(symbol, description, qty, price);
        this.optionConfig = optionConfig;
        this.optionRoot = optionRoot;
    }

    public OptionConfig getOptionConfig() {
        return optionConfig;
    }
    
    public OptionRoot getOptionRoot() {
        return optionRoot;
    }
    
    BigDecimal grossItmAmount() {
        if (this.grossItmAmount == null) {
            BigDecimal value = BigDecimal.ZERO;
            BigDecimal deliverablesValue = this.optionRoot.getDeliverables().getDeliverablesValue();
            BigDecimal strikeValue = this.getStrikeValue();
            if (this.getOptionConfig().getOptionType() == OptionType.C) {
                value = deliverablesValue.subtract(strikeValue, MathContext.DECIMAL32);
            } else if (this.getOptionConfig().getOptionType() == OptionType.P) {
                value = strikeValue.subtract(deliverablesValue, MathContext.DECIMAL32);
            }
            this.grossItmAmount = value;
        }
        return this.grossItmAmount;
    }
    
    public BigDecimal getOtmAmount() {
        return BigDecimal.ZERO.min(grossItmAmount());
    }
    
    public BigDecimal getItmAmount() {
        return BigDecimal.ZERO.max(grossItmAmount());
    }
    
    public BigDecimal getWeightedItmCompliment() {
        if (this.weightedItmCompliment == null) {
            BigDecimal weightedItmCompliment = BigDecimal.ZERO;
            BigDecimal itmAmount = getItmAmount();
            BigDecimal deliverablesValue = this.optionRoot.getDeliverables().getDeliverablesValue();
            if (itmAmount.signum() > 0) {
                for (Deliverable d : this.optionRoot.getDeliverables().getDeliverableList()) {
                    if (DeliverableType.S.equals(d.getDeliverableType()) 
                            && d.getPrice() != null && d.getQty() != null) {
                        BigDecimal maintenanceCompliment = BigDecimal.ONE.subtract(d.getMaintenancePct());
                        BigDecimal value = d.getPrice().multiply(d.getQty());
                        BigDecimal pctOfDeliv = value.divide(deliverablesValue, RoundingMode.HALF_UP);
                        BigDecimal partOfItm = itmAmount.multiply(pctOfDeliv);
                        weightedItmCompliment = weightedItmCompliment.add(partOfItm.multiply(maintenanceCompliment));
                    } else if (DeliverableType.C.equals(d.getDeliverableType())
                            && d.getPrice() != null) {
                        // do nothing for cash deliverables; they do not have margin applied therefore they do not have adjustments
                    }
                }
            }
            this.weightedItmCompliment = weightedItmCompliment;
        }
        return this.weightedItmCompliment;
    }
    
    public BigDecimal getDeliverablesValue() {
        return optionRoot.getDeliverables().getDeliverablesValue();
    }
    
    public BigDecimal getCashDeliverableValue() {
        return optionRoot.getDeliverables().getCashDeliverableValue();
    }
    
    public BigDecimal getStrikePrice() {
        return optionConfig.getStrikePrice();
    }
    
    public BigDecimal getMultiplier() {
        return optionRoot.getMultiplier();
    }
    
    @Override
    public String getType() {
        return AbstractLeg.STOCKOPTION;
    }
    
    @Override
    public BigDecimal getLegValue() {
        if (this.legValue == null) {
            BigDecimal value = BigDecimal.ZERO;
            if (this.bigDecimalQty != null && this.price != null) {
                value = this.bigDecimalQty.multiply(this.price);
            }
            if (this.optionRoot != null) {
                value = value.multiply(this.optionRoot.getMultiplier());
            }
            this.legValue = value;
        }
        return this.legValue;
    }
    
    public BigDecimal getStrikeValue() {
        BigDecimal strikeRaw = this.optionConfig.getStrikePrice();
        return strikeRaw.multiply(this.optionRoot.getMultiplier());
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("optionLeg: {");
        builder.append(super.basicLegInfo()).append(", ");
        builder.append(optionConfig);
        builder.append("}");
        return builder.toString();
    }
    
    
}
