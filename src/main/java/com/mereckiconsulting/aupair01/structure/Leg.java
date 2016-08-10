package com.mereckiconsulting.aupair01.structure;

import java.util.ArrayList;
import java.util.List;

import com.mereckiconsulting.aupair01.exception.BuilderException;

public interface Leg {
    String getSymbol();
    Integer getQty();
    OptionConfig getOptionConfig();
    
    class LegBuilder {
        private LegBuilder() {};
        private String symbol;
        private Integer qty;
        private OptionConfig optionConfig;
        public LegBuilder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public LegBuilder setQty(Integer qty) {
            this.qty = qty;
            return this;
        }
        public LegBuilder setOptionConfig(OptionConfig optionConfig) {
            this.optionConfig = optionConfig;
            return this;
        }
        public Leg build() {
            if (symbol == null || qty == null || optionConfig == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Leg, missing data: ");
                if (symbol == null) {
                    missing.add("symbol");
                }
                if (qty == null) {
                    missing.add("qty");
                }
                if (optionConfig == null) {
                    missing.add("optionConfig");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            Leg leg = new LegImpl(symbol, qty, optionConfig);
            return leg;
        }
    }
    static LegBuilder newBuilder() {
        return new LegBuilder();
    }

}