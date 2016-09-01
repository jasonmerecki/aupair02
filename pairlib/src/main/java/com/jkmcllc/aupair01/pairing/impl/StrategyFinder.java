package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.List;

class StrategyFinder extends AbstractFinder {

    protected final StrategyMeta strategyMeta;
    
    private StrategyFinder(PairingInfo pairingInfo, StrategyMeta strategyMeta) {
        super(pairingInfo);
        this.strategyMeta = strategyMeta;
    }
    
    public static StrategyFinder newInstance(PairingInfo pairingInfo, StrategyMeta strategyMeta) {
        return new StrategyFinder(pairingInfo, strategyMeta);
    }
    
    @Override
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        int legsLength = strategyMeta.legs.length;
        List<List<? extends Leg>> legList = new ArrayList<>(legsLength);
        for (int i = 0; i < legsLength; i++) {
            String legsType = strategyMeta.legs[i];
            List<? extends Leg> legs = pairingInfo.getLegsByType(legsType);
            legList.add(legs);
        }
        return legList;
    }

    @Override
    protected void testLegs(Leg[] legs) {
        testLegs(legs, strategyMeta.strategyName, strategyMeta.strategyPatterns, strategyMeta.marginPatterns.get(0) ); 
    }

}
