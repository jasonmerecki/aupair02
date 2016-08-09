package com.mereckiconsulting.aupair01.structure.leg;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OptionConfigImpl implements OptionConfig {
    private String optionRoot;
    private OptionType optionType;
    private BigDecimal strike;
    private LocalDateTime expiry;
}
