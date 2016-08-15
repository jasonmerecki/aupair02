package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class AbstractFinder {
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longCalls);
        recursiveLists.add(pairingInfo.shortCalls);
        return recursiveLists;
    }
    
    protected void recurseList(List<List<? extends Leg>> recursiveLists, int listIndex, Leg[] legs) {
        // System.out.println("recurseList called with listIndex " + listIndex + " and legs=" + Arrays.asList(legs));
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
    
    protected abstract void testLegs(Leg[] legs);
    
}
