package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.store.Constants;
import com.jkmcllc.aupair01.structure.OptionConfig.OptionConfigBuilder;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface Position extends CorePosition {
    abstract class AbstractPositionBuilder {
        protected AbstractPositionBuilder() {};
        protected String symbol;
        protected String description;
        protected Integer qty;
        protected BigDecimal price;
        protected OptionConfig optionConfig;
        protected OptionConfigBuilder optionConfigBuilder;
        public AbstractPositionBuilder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public AbstractPositionBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
        public AbstractPositionBuilder setQty(Integer qty) {
            if (qty == null || Constants.ZERO.compareTo(qty) == 0) {
                throw new BuilderException("Invalid qty: " + qty);
            }
            this.qty = qty;
            return this;
        }
        public AbstractPositionBuilder setOptionRoot(String optionRoot) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setOptionRoot(optionRoot);
            return this;
        }
        public AbstractPositionBuilder setOptionType(OptionType optionType) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setOptionType(optionType);
            return this;
        }
        public AbstractPositionBuilder setOptionStrike(String strike) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setStrike(strike);
            return this;
        }
        public AbstractPositionBuilder setOptionExpiry(String expiry) {
            if (optionConfigBuilder == null) optionConfigBuilder = OptionConfig.newBuilder();
            optionConfigBuilder.setExpiry(expiry);
            return this;
        }
        
        protected boolean validate(String type) {
            if (symbol == null || qty == null || price == null /* || optionConfig == null */) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build " + type + ", missing data: ");
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
    
    class PositionBuilder extends AbstractPositionBuilder {
    		@Override
        public PositionBuilder setSymbol(String symbol) {
            super.setSymbol(symbol);
            return this;
        }
        @Override
        public PositionBuilder setDescription(String description) {
            super.setDescription(description);
            return this;
        }
        @Override
        public PositionBuilder setQty(Integer qty) {
            super.setQty(qty);
            return this;
        }
        @Override
        public PositionBuilder setOptionRoot(String optionRoot) {
            super.setOptionRoot(optionRoot);
            return this;
        }
        @Override
        public PositionBuilder setOptionType(OptionType optionType) {
            super.setOptionType(optionType);
            return this;
        }
        @Override
        public PositionBuilder setOptionStrike(String strike) {
            super.setOptionStrike(strike);
            return this;
        }
        @Override
        public PositionBuilder setOptionExpiry(String expiry) {
            super.setOptionExpiry(expiry);
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
        public Position build() {
            validate("Position");
            if (optionConfigBuilder != null) {
                optionConfig = optionConfigBuilder.build();
            }
            Position position = StructureImplFactory.buildPosition(symbol, description, qty, price, optionConfig);
            symbol = null;
            qty = null;
            optionConfig = null;
            description = null;
            price = null;
            optionConfigBuilder = null;
            return position;
        }
    }
    static PositionBuilder newBuilder() {
        return new PositionBuilder();
    }

}