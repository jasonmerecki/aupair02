package com.jkmcllc.aupair01.structure;

import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.store.Constants;
import com.jkmcllc.aupair01.structure.OptionConfig.OptionConfigBuilder;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface Position {
    String getSymbol();
    String getDescription();
    Integer getQty();
    OptionConfig getOptionConfig();
    
    class PositionBuilder {
        private PositionBuilder() {};
        private String symbol;
        private String description;
        private Integer qty;
        private OptionConfig optionConfig;
        private OptionConfigBuilder optionConfigBuilder;
        public PositionBuilder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public PositionBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
        public PositionBuilder setQty(Integer qty) {
            if (qty == null || Constants.ZERO.compareTo(qty) == 0) {
                throw new BuilderException("Invalid qty: " + qty);
            }
            this.qty = qty;
            return this;
        }
        public PositionBuilder setOptionRoot(String optionRoot) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setOptionRoot(optionRoot);
            return this;
        }
        public PositionBuilder setOptionType(OptionType optionType) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setOptionType(optionType);
            return this;
        }
        public PositionBuilder setOptionStrike(String strike) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setStrike(strike);
            return this;
        }
        public PositionBuilder setOptionExpiry(String expiry) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setExpiry(expiry);
            return this;
        }
        public Position build() {
            if (symbol == null || qty == null /* || optionConfig == null */) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Position, missing data: ");
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
            Position position = StructureImplFactory.buildPosition(symbol, description, qty, optionConfig);
            symbol = null;
            qty = null;
            optionConfig = null;
            description = null;
            return position;
        }
    }
    static PositionBuilder newBuilder() {
        return new PositionBuilder();
    }

}