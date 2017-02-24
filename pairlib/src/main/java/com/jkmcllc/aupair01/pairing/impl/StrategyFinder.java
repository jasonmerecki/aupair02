package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.store.Constants;

class StrategyFinder {
    
    private static final Logger logger = LoggerFactory.getLogger(StrategyFinder.class);
    private final StrategyConfigs strategyConfigs;
    
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
    
    private final PairingInfo pairingInfo;
    private StrategyMeta strategyMeta;
    private final List<Strategy> foundStrategies = new ArrayList<>();
    
    static StrategyFinder newInstance(PairingInfo pairingInfo, StrategyConfigs strategyConfigs) {
        return new StrategyFinder(pairingInfo, strategyConfigs);
    }
    
    private StrategyFinder(PairingInfo pairingInfo, StrategyConfigs strategyConfigs) {
        this.pairingInfo = pairingInfo;
        this.strategyConfigs = strategyConfigs;
    }
    
    StrategyFinder resetWithMeta(StrategyMeta strategyMeta) {
        this.strategyMeta = strategyMeta;
        this.foundStrategies.clear();
        return this;
    }
    
    List<? extends Strategy> find() {
        if (strategyMeta.sort != null) {
            pairingInfo.sortBy(strategyMeta.sort);
            return Collections.emptyList();
        }
        List<List<? extends Leg>> recursiveLists = getRecursiveLists(pairingInfo);
        if (recursiveLists == null) {
            return getFoundStrategies();
        }
        pairingInfo.clearAndSizeContextLegs(recursiveLists.size());
        recurseList(recursiveLists, 0);
        /* remove the legs that went to zero (no longer necessary)
        for (List<? extends Leg> recursiveList : recursiveLists) {
            for (int j = recursiveList.size() - 1; j >= 0; j--) {
                AbstractLeg abLeg = (AbstractLeg) recursiveList.get(j);
                if (abLeg.qty == 0) {
                    recursiveList.remove(j);
                }
            }
        }
        */
        return getFoundStrategies();
    }
    
    protected void recurseList(List<List<? extends Leg>> recursiveLists, int recursiveListIndex) {
        List<? extends Leg> nextList = recursiveLists.get(recursiveListIndex);
        int nextRecursiveListIndex = recursiveListIndex + 1;
        legLoop:
        for (int i = 0; i < nextList.size(); i++) {
            Leg nextLeg = nextList.get(i);
            for (int j = 0; j < recursiveListIndex; j++) {
                if (pairingInfo.contextLegs.get(j) == nextLeg) {
                    continue legLoop;
                }
            }
            AbstractLeg abLeg = (AbstractLeg) nextLeg;
            if (abLeg.qty == 0) {
                continue;
            }
            pairingInfo.contextLegs.set(recursiveListIndex, nextLeg);
            if (nextRecursiveListIndex == recursiveLists.size()) {
                if (PairingService.PERFWATCH != null) {
                    PairingService.PERFWATCH.start("testlegs");
                }
                testLegs();
                if (PairingService.PERFWATCH != null) {
                    PairingService.PERFWATCH.stop("testlegs");
                }
            }  else {
                recurseList(recursiveLists, nextRecursiveListIndex);
            }
            
            // with legs tested, find out of any previous legs are zero, no need to go on
            for (int j = 0; j < recursiveListIndex; j++) {
                AbstractLeg priorLeg = (AbstractLeg) pairingInfo.contextLegs.get(j);
                if (priorLeg.qty == 0) {
                    break legLoop;
                }
            }
        }
    }
    
    private Integer findMaxQty(List<Leg> legs) {
        Integer maxQty = null;
        Integer[] legsRatio = getLegsRatio();
        for (int i = 0;  i < legs.size(); i++) {
            AbstractLeg leg = (AbstractLeg) legs.get(i);
            Integer testQty = leg.qty / legsRatio[i];
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
            if (legs.size() == 0) {
                return null;
            }
            legList.add(legs);
        }
        return legList;
    }

    protected Integer[] getLegsRatio() {
        return strategyMeta.legsRatio;
    }
    
    protected void testLegs() {
        List<Leg> legList = pairingInfo.contextLegs;
        if (logger.isTraceEnabled()) {
            logger.trace("testLegs, legs=" + Arrays.asList(legList));
        }
        
        Boolean valid = true;
        
        List<JexlExpression> widthPatterns = strategyMeta.widthPatterns;
        for (JexlExpression pattern : widthPatterns) {
            valid = (Boolean) pattern.evaluate(pairingInfo.legContext);
            if (!valid) {
                return;
            }
        }
        
        List<JexlExpression> strikesPatterns = strategyMeta.strikesPatterns;
        for (JexlExpression pattern : strikesPatterns) {
            valid = (Boolean) pattern.evaluate(pairingInfo.legContext);
            if (!valid) {
                return;
            }
        }
        
        List<JexlExpression> expirationPatterns = strategyMeta.expirationPatterns;
        for (JexlExpression pattern : expirationPatterns) {
            valid = (Boolean) pattern.evaluate(pairingInfo.legContext);
            if (!valid) {
                return;
            }
        }
        
        List<JexlExpression> exercisePatterns = strategyMeta.exercisePatterns;
        for (JexlExpression pattern : exercisePatterns) {
            valid = (Boolean) pattern.evaluate(pairingInfo.legContext);
            if (!valid) {
                return;
            }
        }
        
        List<JexlExpression> otherPatterns = strategyMeta.otherPatterns;
        for (JexlExpression pattern : otherPatterns) {
            valid = (Boolean) pattern.evaluate(pairingInfo.legContext);
            if (!valid) {
                return;
            }
        }

        if (strategyMeta.childStrategies == null) {
            Integer strategyQty = findMaxQty(pairingInfo.contextLegs);
            if (strategyQty > 0) {
                Integer[] legsRatio = getLegsRatio();
                List<Leg> strategyLegs = new ArrayList<>(legList.size());
                for (int i = 0; i < legList.size(); i++) {
                    AbstractLeg sourceLeg1 = (AbstractLeg) pairingInfo.contextLegs.get(i);
                    Integer testQty = strategyQty * legsRatio[i];
                    Leg newLeg1 = sourceLeg1.reduceBy(testQty);
                    strategyLegs.add(newLeg1);
                }
                
                Collections.sort(strategyLegs, ASC_STRIKE);
                Strategy strategy = new AbstractStrategy(strategyMeta, strategyLegs, strategyQty, pairingInfo.accountInfo, 
                        pairingInfo);
                
                
                // if the strategy margin isn't lower than the equivalent naked margin, then restore the legs
                // and do not add the strategy
                if (strategyMeta.allowLowerNaked) {
                    String leastMarginConfig = strategyConfigs.getGlobalConfig(GlobalConfigType.TEST_LEAST_MARGIN);
                    BigDecimal testMargin = BigDecimal.ZERO;
                    if (StrategyConfigs.MAINTENANCE.equals(leastMarginConfig)) {
                        testMargin = strategy.getMaintenanceRequirement();
                    } else if (StrategyConfigs.INITIAL.equals(leastMarginConfig)) {
                        testMargin = strategy.getInitialRequirement();
                    }
                    BigDecimal pureNakedMargin = strategy.getPureNakedMargin();
                    if (pureNakedMargin.compareTo(testMargin) >= 0) {
                        foundStrategies.add(strategy);
                    } else {
                        for (int i = 0; i < legList.size(); i++) {
                            AbstractLeg sourceLeg1 = (AbstractLeg) pairingInfo.contextLegs.get(i);
                            Integer testQty = strategyQty * legsRatio[i];
                            sourceLeg1.restoreBy(testQty);
                        }
                    }
                } else {
                    foundStrategies.add(strategy);
                }
                
            }
        } else {
            // delegate to child strategies
            Object[] legObjectArray =  (Object[]) strategyMeta.childStrategiesLegs.evaluate(pairingInfo.legContext);
            for (int i = 0; i < strategyMeta.childStrategies.size(); i++) {
                Leg[] childLegs = (Leg[]) legObjectArray[i];
                StrategyMeta childStrategy = strategyMeta.childStrategies.get(i);
                StrategyFinder childFinder = StrategyFinder.newInstance(pairingInfo, strategyConfigs);
                childFinder.resetWithMeta(childStrategy);
                pairingInfo.contextLegs.clear();
                Arrays.stream(childLegs).forEach(l -> pairingInfo.contextLegs.add(l));
                childFinder.testLegs();
                foundStrategies.addAll(childFinder.getFoundStrategies());
            }
        }
    }
    
    protected List<? extends Strategy> getFoundStrategies() {
        return foundStrategies;
    }
    
}
