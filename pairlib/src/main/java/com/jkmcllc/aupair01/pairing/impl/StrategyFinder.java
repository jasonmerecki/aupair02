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

class StrategyFinder {
    
    private static final Logger logger = LoggerFactory.getLogger(StrategyFinder.class);
    
    private static final Comparator<Leg> ASC_STRIKE = (Leg o1, Leg o2)-> {
        AbstractLeg o1leg = (AbstractLeg) o1, o2leg = (AbstractLeg) o2;
        if (AbstractLeg.STOCK.equals(o1.getType()) && AbstractLeg.STOCK.equals(o2.getType())) {
            // huh? 
            return o1leg.getSymbol().compareTo(o2leg.getSymbol());
        } else if (AbstractLeg.DELIVERABLE.equals(o1.getType()) && AbstractLeg.DELIVERABLE.equals(o2.getType())) {
            return o1leg.getSymbol().compareTo(o2leg.getSymbol());
        } else if (AbstractLeg.STOCK.equals(o1.getType())
                && AbstractLeg.DELIVERABLE.equals(o2.getType())) {
            return 1;
        } else if (AbstractLeg.STOCK.equals(o2.getType())
                && AbstractLeg.DELIVERABLE.equals(o1.getType())) {
            return -1;
        } else if (AbstractLeg.STOCK.equals(o2.getType())) {
            return -1;
        } else if (AbstractLeg.STOCK.equals(o1.getType())) {
            return 1;
        } else if (AbstractLeg.DELIVERABLE.equals(o2.getType())) {
            return -1;
        } else if (AbstractLeg.DELIVERABLE.equals(o1.getType())) {
            return 1;
        }
        AbstractOptionLeg o1option = (AbstractOptionLeg) o1leg, o2option = (AbstractOptionLeg) o2leg;
        return o1option.getOptionConfig().getStrikePrice().compareTo(o2option.getOptionConfig().getStrikePrice());
    };
    
    protected final PairingInfo pairingInfo;
    protected final StrategyMeta strategyMeta;
    protected final List<Strategy> foundStrategies = new ArrayList<>();
    
    public static StrategyFinder newInstance(PairingInfo pairingInfo, StrategyMeta strategyMeta) {
        return new StrategyFinder(pairingInfo, strategyMeta);
    }
    
    private StrategyFinder(PairingInfo pairingInfo, StrategyMeta strategyMeta) {
        this.pairingInfo = pairingInfo;
        this.strategyMeta = strategyMeta;
    }
    
    List<? extends Strategy> find() {
        if (strategyMeta.sort != null) {
            pairingInfo.sortBy(strategyMeta.sort);
            return Collections.emptyList();
        }
        List<List<? extends Leg>> recursiveLists = getRecursiveLists(pairingInfo);
        for (List<? extends Leg> recursiveList : recursiveLists) {
            if (recursiveList.size() == 0) {
                return getFoundStrategies();
            }
        }
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
            AbstractLeg abLeg = (AbstractLeg) nextLeg;
            if (abLeg.remainQty == 0) {
                continue;
            }
            legs[recursiveListIndex] = nextLeg;
            if (nextRecursiveListIndex == recursiveLists.size()) {
                testLegs(legs);
            }  else {
                recurseList(recursiveLists, nextRecursiveListIndex, legs);
            }
        }
    }
    
    private Integer findMaxQty(Leg[] legs) {
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

    protected Integer[] getLegsRatio() {
        return strategyMeta.legsRatio;
    }
    
    protected void testLegs(Leg[] legs) {
        String strategyName = strategyMeta.strategyName;
        List<JexlExpression> strategyPatterns = strategyMeta.strategyPatterns;

        List<Leg> legList = Arrays.asList(legs);
        if (logger.isTraceEnabled()) {
            logger.trace("testLegs, legs=" + Arrays.asList(legs));
        }
        JexlContext context = TacoCat.buildPairingContext(legList, this.pairingInfo.accountInfo, this.pairingInfo);
        Boolean valid = false;
        for (JexlExpression strategyPattern : strategyPatterns) {
            valid = (Boolean) strategyPattern.evaluate(context);
            if (!valid) {
                break;
            }
        }

        if (valid) {
            if (strategyMeta.childStrategies == null) {
                Integer strategyQty = findMaxQty(legs);
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
                    List<JexlExpression> maintenanceMarginExpressions = strategyMeta.maintenanceMarginPatterns;
                    List<JexlExpression> initialMarginExpressions = strategyMeta.initialMarginPatterns;
                    Strategy strategy = new AbstractStrategy(strategyName, strategyLegs, strategyQty, pairingInfo.accountInfo, 
                            pairingInfo, maintenanceMarginExpressions, initialMarginExpressions, strategyMeta.marginDebugPatterns);
                    foundStrategies.add(strategy);
                }
            } else {
                // delegate to child strategies
                Object[] legObjectArray =  (Object[]) strategyMeta.childStrategiesLegs.evaluate(context);
                for (int i = 0; i < strategyMeta.childStrategies.size(); i++) {
                    Leg[] childLegs = (Leg[]) legObjectArray[i];
                    StrategyMeta childStrategy = strategyMeta.childStrategies.get(i);
                    StrategyFinder childFinder = StrategyFinder.newInstance(pairingInfo, childStrategy);
                    childFinder.testLegs(childLegs);
                    foundStrategies.addAll(childFinder.getFoundStrategies());
                }
            }
        }
    }
    
    protected List<? extends Strategy> getFoundStrategies() {
        return foundStrategies;
    }
    
}
