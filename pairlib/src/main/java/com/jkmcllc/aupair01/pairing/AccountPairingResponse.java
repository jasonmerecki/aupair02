package com.jkmcllc.aupair01.pairing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public interface AccountPairingResponse {
    Map<String, List<Strategy>> getStrategies();
    BigDecimal getTotalMaintenanceMargin();
    BigDecimal getTotalInitialMargin();
    static BigDecimal getMaintenanceMargin(List<Strategy> strategies) {
        BigDecimal totalMaintMargin = strategies.parallelStream().map(s1 -> s1.getMaintenanceMargin()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
    
    static BigDecimal getInitialMargin(List<Strategy> strategies) {
        BigDecimal totalMaintMargin = strategies.parallelStream().map(s1 -> s1.getInitialMargin()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
}
