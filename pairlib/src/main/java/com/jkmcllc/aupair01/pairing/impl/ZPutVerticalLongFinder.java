package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class ZPutVerticalLongFinder extends ZAbstractVerticalFinder {
    private final JexlExpression strategyPattern;
    private ZPutVerticalLongFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
        strategyPattern = TacoCat.getJexlEngine().createExpression( "legs.get(0).optionConfig.strikePrice.compareTo(legs.get(1).optionConfig.strikePrice) >= 0 " 
                + equalTwoDatesExpressionFrag );
    };
    protected static ZPutVerticalLongFinder newInstance(PairingInfo pairingInfo) {
        return new ZPutVerticalLongFinder(pairingInfo);
    }
    
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longPuts);
        recursiveLists.add(pairingInfo.shortPuts);
        return recursiveLists;
    }
    protected void testLegs(Leg[] legs) {
        testLegs(legs, Strategy.PUT_VERTICAL_LONG, Collections.singletonList(strategyPattern), Collections.singletonList( DefaultMargins.longVerticalMargin) ); 
    }
    @Override
    protected Integer[] getLegsRatio() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
