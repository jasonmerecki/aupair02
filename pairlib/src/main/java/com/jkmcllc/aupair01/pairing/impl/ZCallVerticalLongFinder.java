package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class ZCallVerticalLongFinder extends ZAbstractVerticalFinder {
    private final JexlExpression strategyPattern;
    private ZCallVerticalLongFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
        strategyPattern = TacoCat.getJexlEngine().createExpression( "legs.get(0).optionConfig.strikePrice.compareTo(legs.get(1).optionConfig.strikePrice) <= 0"  
                + equalTwoDatesExpressionFrag );
    };
    protected static ZCallVerticalLongFinder newInstance(PairingInfo pairingInfo) {
        return new ZCallVerticalLongFinder(pairingInfo);
    }
    
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longCalls);
        recursiveLists.add(pairingInfo.shortCalls);
        return recursiveLists;
    }
    protected void testLegs(Leg[] legs) {
        testLegs(legs, Strategy.CALL_VERTICAL_LONG, Collections.singletonList(strategyPattern), DefaultMargins.longVerticalMargin); 
    }
    @Override
    protected Integer[] getLegsRatio() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
