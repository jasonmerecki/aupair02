package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface OptionConfig {
    String getOptionRoot();
    OptionType getOptionType();
    String getStrike();
    String getExpiry();
    static final String OPTION_EXPIRATION_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    class OptionConfigBuilder {
        private String optionRootSymbol;
        private OptionType optionType;
        private String strike;
        private String expiry;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OPTION_EXPIRATION_DATE_FORMAT);
        private final ThreadLocal<SimpleDateFormat> localSDF = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                SimpleDateFormat formatter = new SimpleDateFormat(OPTION_EXPIRATION_DATE_FORMAT);
                return formatter;
            }
            
        };
        private OptionConfigBuilder() {};
        public OptionConfigBuilder setOptionRoot(String optionRoot) {
            this.optionRootSymbol = optionRoot;
            return this;
        }
        public OptionConfigBuilder setOptionType(OptionType optionType) {
            this.optionType = optionType;
            return this;
        }
        public OptionConfigBuilder setStrike(String strike) {
            this.strike = strike;
            return this;
        }
        public OptionConfigBuilder setExpiry(String expiry) {
            this.expiry = expiry;
            return this;
        }
        public OptionConfig build() {
            if (optionRootSymbol == null || optionType == null || strike == null || expiry == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build OptionConfig, missing data: ");
                if (optionRootSymbol == null) {
                    missing.add("optionRootSymbol");
                }
                if (optionType == null) {
                    missing.add("optionType");
                }
                if (strike == null) {
                    missing.add("strike");
                }
                if (expiry == null) {
                    missing.add("expiry");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            BigDecimal strikePrice;
            try {
                strikePrice = new BigDecimal(strike);
            } catch (Exception e) {
                throw new BuilderException("Cannot parse strike to numerical value: " + strike);
            }
            LocalDateTime expiryTimeLocal;
            Date expiryTimeDate;
            try {
                expiryTimeLocal = LocalDateTime.parse(expiry, formatter);
                expiryTimeDate = localSDF.get().parse(expiry);
            } catch (Exception e) {
                throw new BuilderException("Cannot parse expiry to date/time value: " + expiry);
            }
            OptionConfig optionConfig = StructureImplFactory.buildOptionConfig(optionRootSymbol, optionType, strike, strikePrice, expiry, expiryTimeLocal, expiryTimeDate);
            optionRootSymbol = null;
            optionType = null;
            strike = null;
            expiry = null;
            return optionConfig;
        }
    }
    static OptionConfigBuilder newBuilder() {
        return new OptionConfigBuilder();
    }
}