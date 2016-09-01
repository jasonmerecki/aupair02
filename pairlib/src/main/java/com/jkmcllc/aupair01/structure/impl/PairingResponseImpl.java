package com.jkmcllc.aupair01.structure.impl;

import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class PairingResponseImpl implements PairingResponse {

    private final Map<String, Map<String, List<Strategy>>> resultMap ;
    
    PairingResponseImpl(Map<String, Map<String, List<Strategy>>> resultMap) {
        this.resultMap = resultMap;
    };
    
    @Override
    public Map<String, Map<String,List<Strategy>>> getResultsByAccount() {
        return resultMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PairingResponseImpl {resultMap: ");
        builder.append(resultMap);
        builder.append("}");
        return builder.toString();
    }
    
    

}
