package com.mereckiconsulting.aupair01.structure;

public class OptionRootImpl {
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
}
