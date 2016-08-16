package com.jkmcllc.aupair01.pairing.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.store.Constants;

abstract class AbstractFinder {
    
    protected final PairingInfo pairingInfo;
    protected static final JexlEngine jexlEngine = (new JexlBuilder()).cache(512).strict(true).silent(false).create();
    
    protected AbstractFinder(PairingInfo pairingInfo) {
        this.pairingInfo = pairingInfo;
    }
    
    List<? extends Strategy> find() {
        List<List<? extends Leg>> recursiveLists = getRecursiveLists(pairingInfo);
        Leg[] legs = new Leg[recursiveLists.size()];
        recurseList(recursiveLists, 0, legs);
        return getFoundStrategies();
    }
    
    protected void recurseList(List<List<? extends Leg>> recursiveLists, int listIndex, Leg[] legs) {
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
    
    protected JexlContext buildStandardContext(Leg[] legs) {
        JexlContext context = new MapContext();
        context.set("legs", legs);
        context.set("accountInfo", pairingInfo.accountInfo);
        return context;
    }
    
    protected Integer findAndReduceMaxQty(Leg[] legs) {
        Integer maxQty = null;
        for (int i = 0;  i < legs.length; i++) {
            AbstractLeg leg = (AbstractLeg) legs[i];
            Integer avail = Math.abs(leg.remainQty);
            if (maxQty == null || maxQty > avail) {
                maxQty = avail;
            }
        }
        maxQty = (maxQty != null) ? maxQty : Constants.ZERO;
        if (maxQty > 0) {
            for (int i = 0;  i < legs.length; i++) {
                AbstractLeg leg = (AbstractLeg) legs[i];
                leg.reduceBy(maxQty);
            }
        }
        return maxQty;
    }
    
    protected abstract List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo);
    
    protected abstract void testLegs(Leg[] legs);
    
    protected abstract List<? extends Strategy> getFoundStrategies();
    
}
