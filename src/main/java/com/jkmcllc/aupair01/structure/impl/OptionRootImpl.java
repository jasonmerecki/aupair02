package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;

import com.jkmcllc.aupair01.structure.Deliverables;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.UnderlyerType;

class OptionRootImpl implements OptionRoot {
    private final String optionRootSymbol;
    private final ExerciseStyle exerciseStyle;
    private final UnderlyerType underlyerType;
    private final Deliverables deliverables;
    private final BigDecimal multiplier;
    OptionRootImpl(String optionRootSymbol, ExerciseStyle exerciseStyle, UnderlyerType underlyerType,
            BigDecimal multiplier, Deliverables deliverables) {
        this.optionRootSymbol = optionRootSymbol;
        this.exerciseStyle = exerciseStyle;
        this.underlyerType = underlyerType;
        this.deliverables = deliverables;
        this.multiplier = multiplier;
    }
    @Override
    public String getOptionRootSymbol() {
        return optionRootSymbol;
    }
    @Override
    public ExerciseStyle getExerciseStyle() {
        return exerciseStyle;
    }
    @Override
    public UnderlyerType getUnderlyerType() {
        return underlyerType;
    }
    @Override
    public Deliverables getDeliverables() {
        return deliverables;
    }
    @Override
    public BigDecimal getMultiplier() {
        return multiplier;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OptionRoot: {optionRootSymbol:");
        builder.append(optionRootSymbol);
        builder.append(", exerciseStyle:");
        builder.append(exerciseStyle);
        builder.append(", underlyerType:");
        builder.append(underlyerType);
        builder.append(", deliverables:");
        builder.append(deliverables);
        builder.append("}");
        return builder.toString();
    }
    @Override
    public int hashCode() {
        return optionRootSymbol.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OptionRootImpl == false) {
            return false;
        }
        return optionRootSymbol.equals( ((OptionRootImpl) obj).optionRootSymbol );
    }
}
