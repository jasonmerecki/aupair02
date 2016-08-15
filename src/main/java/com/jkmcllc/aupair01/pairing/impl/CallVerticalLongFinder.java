package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.jkmcllc.aupair01.pairing.strategy.CallVerticalLong;

class CallVerticalLongFinder {

    private final List<CallVerticalLong> foundStrategies = new ArrayList<>();
    private CallVerticalLongFinder() {};
    
    static CallVerticalLongFinder newInstance() {
        return new CallVerticalLongFinder();
    }
    List<CallVerticalLong> findIn(PairingInfo pairingInfo) {
        
        List<List<? extends Leg>> recursiveLists = getRecursiveLists(pairingInfo);
        Leg[] legs = new Leg[recursiveLists.size()];
        recurseList(recursiveLists, 0, legs);
        return null;
    }
    
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longCalls);
        recursiveLists.add(pairingInfo.shortCalls);
        return recursiveLists;
    }
    
    protected void recurseList(List<List<? extends Leg>> recursiveLists, int listIndex, Leg[] legs) {
        System.out.println("recurseList called with listIndex " + listIndex + " and legs=" + Arrays.asList(legs));
        Iterator<? extends Leg> iter = recursiveLists.get(listIndex).iterator();
        int nextIndex = listIndex + 1;
        while (iter.hasNext()) {
            Leg nextLeg = iter.next();
            legs[listIndex] = nextLeg;
            if (nextIndex == recursiveLists.size()) {
                testLegs(legs);
            }  else {
                recurseList(recursiveLists, nextIndex, legs);
            }
            
        }
    }
    
    protected void testLegs(Leg[] legs) {
        System.out.println("testLegs called with legs=" + Arrays.asList(legs));
    }
    
}
