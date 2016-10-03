package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;

import com.jkmcllc.aupair01.exception.ConfigurationException;

class StrategyMeta implements Cloneable {
    final String strategyName;
    final String[] legs;
    final Integer[] legsRatio;
    final String sort;
    final List<StrategyMeta> childStrategies;
    final JexlExpression childStrategiesLegs;
    final List<String> childStrategiesString;
    final List<JexlExpression> strategyPatterns = new ArrayList<>();
    final List<String> strategyPatternStrings = new ArrayList<>();
    final List<JexlExpression> maintenanceMarginPatterns = new ArrayList<>();
    final List<String> maintenanceMarginPatternStrings = new ArrayList<>();
    final List<JexlExpression> initialMarginPatterns = new ArrayList<>();
    final List<String> initialMarginPatternStrings = new ArrayList<>();
    final List<JexlExpression> marginDebugPatterns = new ArrayList<>();
    final List<String> marginDebugPatternStrings = new ArrayList<>();
    
    private final JexlExpression TRUE = TacoCat.getJexlEngine().createExpression("true");
    private final JexlExpression ZERO = TacoCat.getJexlEngine().createExpression("zero");
    
    private StrategyMeta(String strategyName, String[] legs, Integer[] legsRatio, String sort, List<StrategyMeta> childStrategies,
            JexlExpression childStrategiesLegs, List<String> childStrategiesString, List<JexlExpression> strategyPatterns, List<String> strategyPatternStrings,
            List<JexlExpression> maintenanceMarginPatterns, List<String> maintenanceMarginPatternStrings,
            List<JexlExpression> initialMarginPatterns, List<String> initialMarginPatternStrings, List<JexlExpression> marginDebugPatterns, List<String> marginDebugPatternStrings) {
        this.strategyName = strategyName;
        this.legs = legs;
        this.legsRatio = legsRatio;
        this.sort = sort;
        this.childStrategies = childStrategies;
        this.childStrategiesLegs = childStrategiesLegs;
        this.childStrategiesString = childStrategiesString;
        this.strategyPatterns.addAll(strategyPatterns);
        this.strategyPatternStrings.addAll(strategyPatternStrings);
        this.maintenanceMarginPatterns.addAll(maintenanceMarginPatterns);
        this.maintenanceMarginPatternStrings.addAll(maintenanceMarginPatternStrings);
        this.initialMarginPatterns.addAll(initialMarginPatterns);
        this.initialMarginPatternStrings.addAll(initialMarginPatternStrings);
        this.marginDebugPatterns.addAll(marginDebugPatterns);
        this.marginDebugPatternStrings.addAll(marginDebugPatternStrings);
    }
    
    StrategyMeta (String sortString) {
        // TODO: throw exception if this is not a valid sort
        this.strategyName = sortString.trim();
        this.legs = null;
        this.legsRatio = null;
        this.sort = sortString;
        this.childStrategies = null;
        this.childStrategiesLegs = null;
        this.childStrategiesString = null;
        this.strategyPatterns.add(TRUE);
        this.maintenanceMarginPatterns.add(ZERO);
    }
    
    StrategyMeta(String strategyName, String legs, String legsRatioString, String childStrategiesString, String childStrategiesLegsString) {
        this.strategyName = strategyName;
        String[] legsTemp = legs.split(",");
        for (int i = 0; i < legsTemp.length; i++) {
            String legsString = legsTemp[i].trim();
            if (StrategyConfigs.ALL_LEG_LIST_NAMES.contains(legsString) == false) {
                throw new ConfigurationException("Configuration for legs is invalid, strategyName=" + strategyName + " and legs=" + legsString);
            }
            legsTemp[i] = legsString;
        }
        this.legs = legsTemp;
        String[] legsRatioTemp = legsRatioString.split(",");
        Integer[] legsRatioIntegers = new Integer[legsRatioTemp.length];
        for (int i = 0; i < legsRatioTemp.length; i++) {
            try {
                legsRatioIntegers[i] = Integer.parseInt(legsRatioTemp[i].trim());
            } catch (Exception e) {
                throw new ConfigurationException("Configuration for legs ratio is invalid, strategyName=" + strategyName + " and ratio=" + legsRatioTemp[i].trim());
            }
        }
        this.legsRatio = legsRatioIntegers;
        this.sort = null;
        if (childStrategiesString != null) {
            this.childStrategiesString = new ArrayList<>();
            String[] childStrategyTemp = childStrategiesString.split(",");
            for (String c : childStrategyTemp) {
                this.childStrategiesString.add(c.trim());
            }
            this.childStrategies = new ArrayList<>();
            this.childStrategiesLegs = TacoCat.getJexlEngine().createExpression(childStrategiesLegsString);
        } else {
            this.childStrategiesString = null;
            this.childStrategies = null;
            this.childStrategiesLegs = null;
        }
    }
    StrategyMeta addStrategyPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            strategyPatterns.add(p);
            strategyPatternStrings.add(pattern);
        }
        return this;
    }
    StrategyMeta addMarginPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            maintenanceMarginPatterns.add(p);
            maintenanceMarginPatternStrings.add(pattern);
        }
        return this;
    }
    StrategyMeta addMarginDebugPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            marginDebugPatterns.add(p);
            marginDebugPatternStrings.add(pattern);
        }
        return this;
    }
    
    
    StrategyMeta copy(String strategyName) {
        // must be a deep copy 
        StrategyMeta copy = new StrategyMeta(strategyName, legs, legsRatio, sort, childStrategies, 
                childStrategiesLegs, childStrategiesString, strategyPatterns, strategyPatternStrings, 
                maintenanceMarginPatterns, maintenanceMarginPatternStrings, 
                initialMarginPatterns, initialMarginPatternStrings, marginDebugPatterns, marginDebugPatternStrings);
        return copy;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StrategyMeta: {strategyName: ");
        builder.append(strategyName);
        builder.append(", strategyPatternStrings: ");
        builder.append(strategyPatternStrings);
        builder.append(", maintenanceMarginPatternStrings: ");
        builder.append(maintenanceMarginPatternStrings);
        builder.append(", initialMarginPatternStrings: ");
        builder.append(initialMarginPatternStrings);
        if (childStrategies != null) {
            builder.append(", childStrategies: ");
            builder.append(childStrategies);
        }
        builder.append("}");
        return builder.toString();
    }
}
