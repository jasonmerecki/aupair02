package com.mereckiconsulting.aupair01.structure;

public class OptionRootImpl implements OptionRoot {
    private final String optionRootSymbol;
    private final ExerciseStyle exerciseStyle;
    private final UnderlyerType underlyerType;
    private final Deliverables deliverables;
    public OptionRootImpl(String optionRootSymbol, ExerciseStyle exerciseStyle, UnderlyerType underlyerType,
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
        builder.append("OptionRootImpl: {optionRootSymbol:");
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
