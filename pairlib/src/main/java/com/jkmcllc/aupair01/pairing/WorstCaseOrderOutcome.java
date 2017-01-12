package com.jkmcllc.aupair01.pairing;

import java.util.Collection;
import java.util.List;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public interface WorstCaseOrderOutcome {
    String getOptionRoot();
    List<Strategy> getStrategies();
    Collection<? extends OrderPairingResult> getOrders();
}
