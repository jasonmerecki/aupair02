package com.jkmcllc.aupair01.structure.impl;

import com.jkmcllc.aupair01.structure.Deliverables;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.UnderlyerType;

class OptionRootImpl implements OptionRoot {
    private final String optionRootSymbol;
    private final ExerciseStyle exerciseStyle;
    private final UnderlyerType underlyerType;
    private final Deliverables deliverables;
    OptionRootImpl(String optionRootSymbol, ExerciseStyle exerciseStyle, UnderlyerType underlyerType,
            Deliverables deliverables) {
        this.optionRootSymbol = optionRootSymbol;
        this.exerciseStyle = exerciseStyle;
        this.underlyerType = underlyerType;
        this.deliverables = deliverables;
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
}
