package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.Deliverables.DeliverablesBuilder;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface OptionRoot {
    String getOptionRootSymbol();
    ExerciseStyle getExerciseStyle();
    UnderlyerType getUnderlyerType();
    Deliverables getDeliverables();
    BigDecimal getMultiplier();
    BigDecimal getNakedDeliverablePct();
    BigDecimal getNakedCashPct();

    class OptionRootBuilder {
        private String optionRootSymbol;
        private ExerciseStyle exerciseStyle;
        private UnderlyerType underlyerType;
        private Deliverables deliverables;
        private BigDecimal multiplier;
        private BigDecimal nakedCashPct;
        private BigDecimal nakedDeliverablePct;
        private final DeliverablesBuilder deliverablesBuilder = Deliverables.newBuilder();
        private OptionRootBuilder() {}
        public OptionRootBuilder setOptionRootSymbol(String optionRootSymbol) {
            this.optionRootSymbol = optionRootSymbol;
            return this;
        }
        public OptionRootBuilder setNakedDeliverablePct(String pctString) {
            try {
                BigDecimal nakedDeliverablePct = new BigDecimal(pctString);
                this.nakedDeliverablePct = nakedDeliverablePct;
            } catch (Exception e) {
                throw new BuilderException("Unable to parse naked deliverable percent: " + pctString);
            }
            return this;
        }
        public OptionRootBuilder setNakedCashPct(String pctString) {
            try {
                BigDecimal nakedCashPct = new BigDecimal(pctString);
                this.nakedCashPct = nakedCashPct;
            } catch (Exception e) {
                throw new BuilderException("Unable to parse naked cash percent: " + pctString);
            }
            return this;
        }
        public OptionRootBuilder setExerciseStyle(ExerciseStyle exerciseStyle) {
            this.exerciseStyle = exerciseStyle;
            return this;
        }
        public OptionRootBuilder setUnderlyerType(UnderlyerType underlyerType) {
            this.underlyerType = underlyerType;
            return this;
        }
        public OptionRootBuilder setMultiplier(String mulitplierString) {
            try {
                BigDecimal multiplier = new BigDecimal(mulitplierString);
                this.multiplier = multiplier;
            } catch (Exception e) {
                throw new BuilderException("Unable to parse option root multiplier: " + mulitplierString);
            }
            return this;
        }
        public OptionRootBuilder setDeliverableSymbol(String symbol) {
            deliverablesBuilder.setDeliverableSymbol(symbol);
            return this;
        }
        public OptionRootBuilder setDeliverableQty(String qty) {
            deliverablesBuilder.setDeliverableQty(qty);
            return this;
        }
        public OptionRootBuilder setDeliverablePrice(String price) {
            deliverablesBuilder.setDeliverablePrice(price);
            return this;
        }
        public OptionRootBuilder setDeliverableType(DeliverableType deliverableType) {
            deliverablesBuilder.setDeliverableType(deliverableType);
            return this;
        }
        public OptionRootBuilder addDeliverable() {
            deliverablesBuilder.add();
            return this;
        }
        public OptionRoot build() {
            if (optionRootSymbol == null || exerciseStyle == null || underlyerType == null || multiplier == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build OptionRoot, missing data: ");
                if (optionRootSymbol == null) {
                    missing.add("optionRootSymbol");
                }
                if (exerciseStyle == null) {
                    missing.add("exerciseStyle");
                }
                if (underlyerType == null) {
                    missing.add("underlyerType");
                }
                if (deliverables == null) {
                    missing.add("deliverables");
                }
                if (multiplier == null) {
                    missing.add("multiplier");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            this.deliverables = deliverablesBuilder.build();
            OptionRoot optionRoot = StructureImplFactory.buildOptionRoot(optionRootSymbol, exerciseStyle, underlyerType, multiplier, deliverables, nakedDeliverablePct, nakedCashPct);
            optionRootSymbol = null;
            exerciseStyle = null;
            underlyerType = null;
            return optionRoot;
        }
    }
    public static OptionRootBuilder newBuilder() {
        return new OptionRootBuilder();
    }
}