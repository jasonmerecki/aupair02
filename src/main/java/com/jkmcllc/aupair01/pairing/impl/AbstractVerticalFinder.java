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
    protected JexlExpression strategyPattern;
    protected final List<Strategy> foundStrategies = new ArrayList<>();

    public AbstractVerticalFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
    }

    protected void testLegs(Leg[] legs, String strategyName) {
        List<Leg> legList = Arrays.asList(legs);
        if (logger.isTraceEnabled()) {
            logger.trace("testLegs, legs=" + Arrays.asList(legs));
        }
        JexlContext context = TacoCat.buildStandardContext(legList, this.pairingInfo.accountInfo);
        Boolean valid = (Boolean) strategyPattern.evaluate(context);
        if (valid) {
            Integer strategyQty = findAndReduceMaxQty(legs);
            if (strategyQty > 0) {
                List<Leg> strategyLegs = new ArrayList<>(2);
                AbstractLeg sourceLeg1 = (AbstractLeg) legs[0];
                AbstractLeg sourceLeg2 = (AbstractLeg) legs[1];
                Leg newLeg1 = sourceLeg1.reduceBy(strategyQty);
                Leg newLeg2 = sourceLeg2.reduceBy(strategyQty);
                strategyLegs.add(newLeg1);
                strategyLegs.add(newLeg2);
                Strategy callVerticalLong = new AbstractStrategy(strategyName, strategyLegs, strategyQty, pairingInfo.accountInfo, "zero");
                foundStrategies.add(callVerticalLong);
            }
        }
    }

    protected List<? extends Strategy> getFoundStrategies() {
        return foundStrategies;
    }

}