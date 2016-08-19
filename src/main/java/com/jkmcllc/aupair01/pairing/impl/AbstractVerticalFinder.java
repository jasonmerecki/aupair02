package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public abstract class AbstractVerticalFinder extends AbstractFinder {

    private static final Logger logger = LoggerFactory.getLogger(AbstractVerticalFinder.class);
    protected final List<Strategy> foundStrategies = new ArrayList<>();

    public AbstractVerticalFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
    }

    protected void testLegs(Leg[] legs, String expression, String strategyName) {
        List<Leg> legList = Arrays.asList(legs);
        if (logger.isTraceEnabled()) {
            logger.trace("testLegs, legs=" + Arrays.asList(legs));
        }
        JexlContext context = TacoCat.buildStandardContext(legList, this.pairingInfo.accountInfo);
        JexlExpression e = TacoCat.getJexlEngine().createExpression( "legs.get(0).optionConfig.strikePrice.compareTo(legs.get(1).optionConfig.strikePrice) <= 0" ); 
        Boolean valid = (Boolean) e.evaluate(context);
        if (valid) {
            Integer strategyQty = findAndReduceMaxQty(legs);
            List<Leg> strategyLegs = new ArrayList<>(2);
            LongCall sourceLongCall = (LongCall) legs[0];
            ShortCall sourceShortCall = (ShortCall) legs[1];
            sourceLongCall.reduceBy(strategyQty);
            sourceShortCall.reduceBy(strategyQty);
            Leg longCall = new LongCall(sourceLongCall.symbol, strategyQty, sourceLongCall.optionConfig);
            strategyLegs.add(longCall);
            Leg shortCall = new ShortCall(sourceShortCall.symbol, strategyQty, sourceShortCall.optionConfig);
            strategyLegs.add(shortCall);
            Strategy callVerticalLong = new AbstractStrategy(Strategy.CALL_VERTICAL_LONG, strategyLegs, strategyQty, pairingInfo.accountInfo, "zero");
            foundStrategies.add(callVerticalLong);
        }
    }

    protected List<? extends Strategy> getFoundStrategies() {
        return foundStrategies;
    }

}