package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.store.Constants;

abstract class AbstractFinder {
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractFinder.class);
    private static final Comparator<Leg> ASC_STRIKE = (Leg o1, Leg o2)-> {
        AbstractLeg o1leg = (AbstractLeg) o1, o2leg = (AbstractLeg) o2;
        if (AbstractLeg.STOCK.equals(o1) && AbstractLeg.STOCK.equals(o2)) {
            // huh? 
            return o1leg.getSymbol().compareTo(o2leg.getSymbol());
        } else if (AbstractLeg.STOCK.equals(o1)) {
            return 1;
        } else if (AbstractLeg.STOCK.equals(o2)) {
            return -1;
        }
        AbstractOptionLeg o1option = (AbstractOptionLeg) o1leg, o2option = (AbstractOptionLeg) o2leg;
        return o1option.getOptionConfig().getStrikePrice().compareTo(o2option.getOptionConfig().getStrikePrice());
    };
    
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
        legLoop:
        for (int i = 0; i < nextList.size(); i++) {
            Leg nextLeg = nextList.get(i);
            for (int j = 0; j < recursiveListIndex; j++) {
                if (legs[j] == nextLeg) {
                    continue legLoop;
                }
            }
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
        Integer[] legsRatio = getLegsRatio();
        for (int i = 0;  i < legs.length; i++) {
            AbstractLeg leg = (AbstractLeg) legs[i];
            Integer testQty = leg.remainQty / legsRatio[i];
            Integer avail = Math.abs(testQty);
            if (maxQty == null || maxQty > avail) {
                maxQty = avail;
            }
        }
        maxQty = (maxQty != null) ? maxQty : Constants.ZERO;
        return maxQty;
    }
    
    protected abstract List<List<? extends Leg>> getRecursiveLists(PairingInfo pairingInfo);
    protected abstract void testLegs(Leg[] legs);
    protected abstract Integer[] getLegsRatio();
    
    protected void testLegs(Leg[] legs, String strategyName, List<JexlExpression> strategyPatterns, List<JexlExpression> marginExpressions) {
        List<Leg> legList = Arrays.asList(legs);
        if (logger.isTraceEnabled()) {
            logger.trace("testLegs, legs=" + Arrays.asList(legs));
        }
        JexlContext context = TacoCat.buildPairingContext(legList, this.pairingInfo.accountInfo);
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
                Integer[] legsRatio = getLegsRatio();
                List<Leg> strategyLegs = new ArrayList<>(legList.size());
                for (int i = 0; i < legList.size(); i++) {
                    AbstractLeg sourceLeg1 = (AbstractLeg) legs[i];
                    Integer testQty = strategyQty * legsRatio[i];
                    Leg newLeg1 = sourceLeg1.reduceBy(testQty);
                    strategyLegs.add(newLeg1);
                }
                Collections.sort(strategyLegs, ASC_STRIKE);
                Strategy strategy = new AbstractStrategy(strategyName, strategyLegs, strategyQty, pairingInfo.accountInfo, marginExpressions);
                foundStrategies.add(strategy);
            }
        }
    }
    
    protected List<? extends Strategy> getFoundStrategies() {
        return foundStrategies;
    }
    
}
