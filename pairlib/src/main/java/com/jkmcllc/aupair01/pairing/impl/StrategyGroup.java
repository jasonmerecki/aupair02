package com.jkmcllc.aupair01.pairing.impl;

import java.util.List;

class StrategyGroup {
    final String groupName;
    String testLeastMargin;
    final List<StrategyGroupLists> strategyGroupLists;
    StrategyGroup(String groupName, List<StrategyGroupLists> strategyGroupLists) {
        this.groupName = groupName;
        this.strategyGroupLists = strategyGroupLists;
    }
}
