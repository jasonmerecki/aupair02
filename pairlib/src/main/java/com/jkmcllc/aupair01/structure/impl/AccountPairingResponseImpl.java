package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class AccountPairingResponseImpl implements AccountPairingResponse {
    private final Map<String, List<Strategy>> resultMap ;
    
    AccountPairingResponseImpl(Map<String, List<Strategy>> resultMap) {
        if (resultMap == null) resultMap = Collections.emptyMap();
        this.resultMap = resultMap;
    };
    
    @Override
    public Map<String, List<Strategy>> getStrategies() {
        return resultMap;
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountPairingResponseImpl: {resultMap:");
        builder.append(resultMap);
        builder.append("}");
        return builder.toString();
    }
    
    

}
