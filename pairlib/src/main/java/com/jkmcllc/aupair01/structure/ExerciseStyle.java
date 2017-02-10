package com.jkmcllc.aupair01.structure;

public enum ExerciseStyle {
    A (StyleType.A), E (StyleType.E), AMERICAN (StyleType.A), EUROPEAN (StyleType.E);
    public enum StyleType {A, E}
    private ExerciseStyle(StyleType styleType) {
        this.styleType = styleType;
    }
    private final StyleType styleType;
    public StyleType getStyleType() { return this.styleType; }
}
