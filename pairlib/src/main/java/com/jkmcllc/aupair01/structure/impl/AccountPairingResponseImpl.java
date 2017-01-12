package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.WorstCaseOrderOutcome;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class AccountPairingResponseImpl implements AccountPairingResponse {
    private final Map<String, List<Strategy>> resultMap ;
    private final Map<String, String> strategyGroupByRoot;
    private final Map<String, Map<String, List<Strategy>>> allStrategyListResultMap ;
    private final Map<String, WorstCaseOrderOutcome> worstCaseOrderOutcomes;
    
    AccountPairingResponseImpl(Map<String, List<Strategy>> resultMap, Map<String, String> strategyGroupByRoot,
            Map<String, Map<String, List<Strategy>>> allStrategyListResultMap, Map<String, WorstCaseOrderOutcome> worstCaseOrderOutcomes) {
        if (resultMap == null) resultMap = Collections.emptyMap();
        this.resultMap = resultMap;
        this.strategyGroupByRoot = (strategyGroupByRoot != null) ? strategyGroupByRoot : Collections.emptyMap();
        this.allStrategyListResultMap = allStrategyListResultMap;
        this.worstCaseOrderOutcomes = worstCaseOrderOutcomes;
    };
    
    @Override
    public Map<String, List<Strategy>> getStrategies() {
        return resultMap;
    }
    
    @Override
    public Map<String, String> getStrategyGroupByRoot() {
        return strategyGroupByRoot;
    }

    @Override
    public BigDecimal getTotalMaintenanceMargin() {
        BigDecimal totalMaintMargin = resultMap.values().stream().map((e) -> {
            BigDecimal totalRootMargin = AccountPairingResponse.getMaintenanceMargin(e);
            return totalRootMargin;
        }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        return totalMaintMargin;
    }
    
    @Override
    public BigDecimal getTotalInitialMargin() {
        BigDecimal totalMaintMargin = resultMap.values().stream().map((e) -> {
            BigDecimal totalRootMargin = AccountPairingResponse.getInitialMargin(e);
            return totalRootMargin;
        }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        return totalMaintMargin;
    }
    
    @Override
    public Map<String, Map<String, List<Strategy>>> getAllStrategyListResults() {
        return this.allStrategyListResultMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountPairingResponseImpl: {resultMap:");
        builder.append(resultMap);
        builder.append(", strategyGroupByRoot: ");
        builder.append(strategyGroupByRoot);
        if (worstCaseOrderOutcomes != null) {
            builder.append(", worstCaseOrderOutcomes: ");
            builder.append(worstCaseOrderOutcomes);
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Map<String, WorstCaseOrderOutcome> getWorstCaseOrderOutcomes() {
        return this.worstCaseOrderOutcomes;
    }
    
    

}
