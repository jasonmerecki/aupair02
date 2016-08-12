package com.mereckiconsulting.aupair01.structure;

import java.util.ArrayList;
import java.util.List;

import com.mereckiconsulting.aupair01.exception.BuilderException;
import com.mereckiconsulting.aupair01.structure.OptionConfig.OptionConfigBuilder;
import com.mereckiconsulting.aupair01.structure.impl.StructureImplFactory;

public interface Leg {
    String getSymbol();
    Integer getQty();
    OptionConfig getOptionConfig();
    
    class LegBuilder {
        private LegBuilder() {};
        private String symbol;
        private Integer qty;
        private OptionConfig optionConfig;
        private OptionConfigBuilder optionConfigBuilder;
        public LegBuilder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public LegBuilder setQty(Integer qty) {
            this.qty = qty;
            return this;
        }
        public LegBuilder setOptionRoot(String optionRoot) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setOptionRoot(optionRoot);
            return this;
        }
        public LegBuilder setOptionType(OptionType optionType) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setOptionType(optionType);
            return this;
        }
        public LegBuilder setOptionStrike(String strike) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setStrike(strike);
            return this;
        }
        public LegBuilder setOptionExpiry(String expiry) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setExpiry(expiry);
            return this;
        }
        public Leg build() {
            if (symbol == null || qty == null /* || optionConfig == null */) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Leg, missing data: ");
                if (symbol == null) {
                    missing.add("symbol");
                }
                if (qty == null) {
                    missing.add("qty");
                }
                /*
                if (optionConfig == null) {
                    missing.add("optionConfig");
                }
                */
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            if (optionConfigBuilder != null) {
                optionConfig = optionConfigBuilder.build();
            }
            Leg leg = StructureImplFactory.buildLeg(symbol, qty, optionConfig);
            symbol = null;
            qty = null;
            optionConfig = null;
            return leg;
        }
    }
    static LegBuilder newBuilder() {
        return new LegBuilder();
    }

}