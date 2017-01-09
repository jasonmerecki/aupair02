package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;
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
    BigDecimal getPrice();
    OptionConfig getOptionConfig();
    BigDecimal getEquityMaintenanceMargin();
    BigDecimal getEquityInitialMargin();
    
    class PositionBuilder {
        protected PositionBuilder() {};
        protected String symbol;
        protected String description;
        protected Integer qty;
        protected BigDecimal price;
        protected BigDecimal equityMaintenanceMargin;
        protected BigDecimal equityInitialMargin;
        protected OptionConfig optionConfig;
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
        public PositionBuilder setPositionEquityMaintenanceMargin(String equityMaintenanceMargin) {
            try {
                this.equityMaintenanceMargin = new BigDecimal(equityMaintenanceMargin);
            } catch (Exception e) {
                throw new BuilderException("Invalid position equityMaintenanceMargin: " + equityMaintenanceMargin);
            }
            return this;
        }
        public PositionBuilder setPositionEquityInitialMargin(String equityInitialMargin) {
            try {
                this.equityInitialMargin = new BigDecimal(equityInitialMargin);
            } catch (Exception e) {
                throw new BuilderException("Invalid position equityInitialMargin: " + equityInitialMargin);
            }
            return this;
        }
        public PositionBuilder setPositionPrice(String price) {
            try {
                this.price = new BigDecimal(price);
            } catch (Exception e) {
                throw new BuilderException("Invalid position price: " + price);
            }
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
            validate();
            if (optionConfigBuilder != null) {
                optionConfig = optionConfigBuilder.build();
            }
            Position position = StructureImplFactory.buildPosition(symbol, description, qty, price, equityMaintenanceMargin, equityInitialMargin, optionConfig);
            symbol = null;
            qty = null;
            optionConfig = null;
            description = null;
            price = null;
            optionConfigBuilder = null;
            return position;
        }
        protected boolean validate() {
            if (symbol == null || qty == null || price == null /* || optionConfig == null */) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Position, missing data: ");
                if (symbol == null) {
                    missing.add("symbol");
                }
                if (qty == null) {
                    missing.add("qty");
                }
                if (price == null) {
                    missing.add("price");
                }
                /*
                if (optionConfig == null) {
                    missing.add("optionConfig");
                }
                */
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            return true;
        }
    }
    static PositionBuilder newBuilder() {
        return new PositionBuilder();
    }

}