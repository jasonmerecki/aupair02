package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.store.Constants;

abstract class AbstractFinder {
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractFinder.class);
    protected final PairingInfo pairingInfo;
    protected final List<Strategy> foundStrategies = new ArrayList<>();
    
    protected AbstractFinder(PairingInfo pairingInfo) {
        this.pairingInfo = pairingInfo;
    }
    
    List<? extends Strategy> find() {
        List<List<? extends Leg>> recursiveLists = getRecursiveLists(pairingInfo);
        Leg[] legs = new Leg[recursiveLists.size()];
        recurseList(recursiveLists, 0, legs);
        return getFoundStrategies();
    }
    
    protected void recurseList(List<List<? extends Leg>> recursiveLists, int recursiveListIndex, Leg[] legs) {
        List<? extends Leg> nextList = recursiveLists.get(recursiveListIndex);
        int nextRecursiveListIndex = recursiveListIndex + 1;
        for (int i = 0; i < nextList.size(); i++) {
            Leg nextLeg = nextList.get(i);
            legs[recursiveListIndex] = nextLeg;
            if (nextRecursiveListIndex == recursiveLists.size()) {
                testLegs(legs);
            }  else {
                recurseList(recursiveLists, nextRecursiveListIndex, legs);
            }
        }
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
    
    protected void testLegs(Leg[] legs, String strategyName, List<JexlExpression> strategyPatterns, JexlExpression marginExpression) {
        List<Leg> legList = Arrays.asList(legs);
        if (logger.isTraceEnabled()) {
            logger.trace("testLegs, legs=" + Arrays.asList(legs));
        }
        JexlContext context = TacoCat.buildStandardContext(legList, this.pairingInfo.accountInfo);
        Boolean valid = false;
        for (JexlExpression strategyPattern : strategyPatterns) {
            valid = (Boolean) strategyPattern.evaluate(context);
            if (!valid) {
                break;
            }
        }
        if (valid) {
            Integer strategyQty = findAndReduceMaxQty(legs);
            if (strategyQty > 0) {
                List<Leg> strategyLegs = new ArrayList<>(legList.size());
                for (int i = 0; i < legList.size(); i++) {
                    AbstractLeg sourceLeg1 = (AbstractLeg) legs[i];
                    Leg newLeg1 = sourceLeg1.reduceBy(strategyQty);
                    strategyLegs.add(newLeg1);
                }
                Strategy strategy = new AbstractStrategy(strategyName, strategyLegs, strategyQty, pairingInfo.accountInfo, marginExpression);
                foundStrategies.add(strategy);
            }
        }
    }
    
    protected List<? extends Strategy> getFoundStrategies() {
        return foundStrategies;
    }
    
}
