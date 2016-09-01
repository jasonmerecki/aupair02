package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;

import com.jkmcllc.aupair01.pairing.impl.DefaultMargins;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class ZPutVerticalShortFinder extends ZAbstractVerticalFinder {
    private final JexlExpression strategyPattern;
    private ZPutVerticalShortFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
        strategyPattern = TacoCat.getJexlEngine().createExpression( "legs.get(0).optionConfig.strikePrice.compareTo(legs.get(1).optionConfig.strikePrice) < 0"  
                + equalTwoDatesExpressionFrag );
    };
    protected static ZPutVerticalShortFinder newInstance(PairingInfo pairingInfo) {
        return new ZPutVerticalShortFinder(pairingInfo);
    }
    
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longPuts);
        recursiveLists.add(pairingInfo.shortPuts);
        return recursiveLists;
    }
    protected void testLegs(Leg[] legs) {
        testLegs(legs, Strategy.PUT_VERTICAL_SHORT, Collections.singletonList(strategyPattern), DefaultMargins.shortVerticalMargin); 
    }
    
}
