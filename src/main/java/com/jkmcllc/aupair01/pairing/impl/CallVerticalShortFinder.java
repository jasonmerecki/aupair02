package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class CallVerticalShortFinder extends AbstractVerticalFinder {
    private CallVerticalShortFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
    };
    protected static CallVerticalShortFinder newInstance(PairingInfo pairingInfo) {
        return new CallVerticalShortFinder(pairingInfo);
    }
    
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longCalls);
        recursiveLists.add(pairingInfo.shortCalls);
        return recursiveLists;
    }
    protected void testLegs(Leg[] legs) {
        testLegs(legs, "legs.get(0).optionConfig.strikePrice.compareTo(legs.get(1).optionConfig.strikePrice) > 0", Strategy.CALL_VERTICAL_SHORT); 
    }
    
}
