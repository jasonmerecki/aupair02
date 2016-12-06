package com.jkmcllc.aupair01.pairing.impl;

import java.util.List;

class StrategyGroupLists {
    private final String strategyListName;
    private final List<StrategyMeta> strategyMetas;
    
    StrategyGroupLists(String strategyListName, List<StrategyMeta> strategyMetas) {
        this.strategyListName = strategyListName;
        this.strategyMetas = strategyMetas;
    }
    
    String getStrategyListName() {
        return this.strategyListName;
    }
    
    List<StrategyMeta> getStrategyMetas() {
        return strategyMetas;
    }

}
