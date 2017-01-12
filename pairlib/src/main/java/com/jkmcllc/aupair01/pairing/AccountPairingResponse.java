package com.jkmcllc.aupair01.pairing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public interface AccountPairingResponse {
    Map<String, List<Strategy>> getStrategies();
    Map<String, String> getStrategyGroupByRoot();
    BigDecimal getTotalMaintenanceMargin();
    BigDecimal getTotalInitialMargin();
    Map<String, Map<String, List<Strategy>>> getAllStrategyListResults();
    Map<String, WorstCaseOrderOutcome> getWorstCaseOrderOutcomes();
    
    public static BigDecimal getMaintenanceMargin(List<Strategy> strategies) {
        BigDecimal totalMaintMargin = strategies.parallelStream().map(s1 -> s1.getMaintenanceMargin()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
    
    public static BigDecimal getInitialMargin(List<Strategy> strategies) {
        BigDecimal totalMaintMargin = strategies.parallelStream().map(s1 -> s1.getInitialMargin()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
}
