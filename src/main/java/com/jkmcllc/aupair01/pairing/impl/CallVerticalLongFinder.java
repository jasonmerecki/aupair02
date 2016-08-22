package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class CallVerticalLongFinder extends AbstractVerticalFinder {
    private CallVerticalLongFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
        strategyPattern = TacoCat.getJexlEngine().createExpression( "legs.get(0).optionConfig.strikePrice.compareTo(legs.get(1).optionConfig.strikePrice) <= 0" );
    };
    protected static CallVerticalLongFinder newInstance(PairingInfo pairingInfo) {
        return new CallVerticalLongFinder(pairingInfo);
    }
    
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longCalls);
        recursiveLists.add(pairingInfo.shortCalls);
        return recursiveLists;
    }
    protected void testLegs(Leg[] legs) {
        testLegs(legs, Strategy.CALL_VERTICAL_LONG, DefaultMargins.longVerticalMargin); 
    }
    
}
