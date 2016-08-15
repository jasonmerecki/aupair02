package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jkmcllc.aupair01.pairing.strategy.CallVerticalLong;

class CallVerticalLongFinder extends AbstractFinder {

    private final List<CallVerticalLong> foundStrategies = new ArrayList<>();
    private CallVerticalLongFinder() {};
    
    static CallVerticalLongFinder newInstance() {
        return new CallVerticalLongFinder();
    }
    List<CallVerticalLong> findIn(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = getRecursiveLists(pairingInfo);
        Leg[] legs = new Leg[recursiveLists.size()];
        recurseList(recursiveLists, 0, legs);
        return foundStrategies;
    }

    protected void testLegs(Leg[] legs) {
        System.out.println("testLegs called with legs=" + Arrays.asList(legs));
    }
    
}
