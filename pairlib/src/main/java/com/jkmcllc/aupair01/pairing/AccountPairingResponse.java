package com.jkmcllc.aupair01.pairing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public interface AccountPairingResponse {
    Map<String, List<Strategy>> getStrategies();
    BigDecimal getTotalMaintenanceMargin();
    BigDecimal getTotalInitialMargin();
}
