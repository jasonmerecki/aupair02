package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.pairing.strategy.CallVerticalLong;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class CallVerticalLongFinder extends AbstractFinder {
    private static final Logger logger = LoggerFactory.getLogger(CallVerticalLongFinder.class);
    private final List<CallVerticalLong> foundStrategies = new ArrayList<>();
    private CallVerticalLongFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
    };
    
    static CallVerticalLongFinder newInstance(PairingInfo pairingInfo) {
        return new CallVerticalLongFinder(pairingInfo);
    }
    
    protected List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo) {
        List<List<? extends Leg>> recursiveLists = new ArrayList<>(2);
        recursiveLists.add(pairingInfo.longCalls);
        recursiveLists.add(pairingInfo.shortCalls);
        return recursiveLists;
    }

    protected void testLegs(Leg[] legs) {
        logger.info("testLegs called with legs=" + Arrays.asList(legs));
        JexlContext context = buildStandardContext(legs);
        JexlExpression e = jexlEngine.createExpression( "legs.0.optionConfig.strikePrice.compareTo(legs.1.optionConfig.strikePrice) < 0" ); // "legs.0.optionConfig.strikePrice"
        Boolean valid = (Boolean) e.evaluate(context);
        if (valid) {
            Integer strategyQty = findAndReduceMaxQty(legs);
        }
    }
    
    protected List<? extends Strategy> getFoundStrategies() {
        return foundStrategies;
    }
    
}
