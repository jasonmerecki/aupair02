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
    final List<JexlExpression> strikesPatterns = new ArrayList<>();
    final List<String> strikesPatternStrings = new ArrayList<>();
    final List<JexlExpression> widthPatterns = new ArrayList<>();
    final List<String> widthPatternStrings = new ArrayList<>();
    final List<JexlExpression> expirationPatterns = new ArrayList<>();
    final List<String> expirationPatternStrings = new ArrayList<>();
    final List<JexlExpression> exercisePatterns = new ArrayList<>();
    final List<String> exercisePatternStrings = new ArrayList<>();
    final List<JexlExpression> otherPatterns = new ArrayList<>();
    final List<String> otherPatternStrings = new ArrayList<>();
    final List<JexlExpression> maintenanceMarginPatterns = new ArrayList<>();
    final List<String> maintenanceMarginPatternStrings = new ArrayList<>();
    final List<JexlExpression> initialMarginPatterns = new ArrayList<>();
    final List<String> initialMarginPatternStrings = new ArrayList<>();
    final List<JexlExpression> marginDebugPatterns = new ArrayList<>();
    final List<String> marginDebugPatternStrings = new ArrayList<>();
    
    private StrategyMeta(String strategyName, String[] legs, Integer[] legsRatio, String sort, List<StrategyMeta> childStrategies,
            JexlExpression childStrategiesLegs, List<String> childStrategiesString, 
            List<JexlExpression> strikesPatterns, List<String> strikesPatternStrings,
            List<JexlExpression> widthPatterns, List<String> widthPatternStrings,
            List<JexlExpression> expirationPatterns, List<String> expirationPatternStrings,
            List<JexlExpression> otherPatterns, List<String> otherPatternStrings,
            List<JexlExpression> maintenanceMarginPatterns, List<String> maintenanceMarginPatternStrings,
            List<JexlExpression> initialMarginPatterns, List<String> initialMarginPatternStrings, List<JexlExpression> marginDebugPatterns, List<String> marginDebugPatternStrings) {
        this.strategyName = strategyName;
        this.legs = legs;
        this.legsRatio = legsRatio;
        this.sort = sort;
        this.childStrategies = childStrategies;
        this.childStrategiesLegs = childStrategiesLegs;
        this.childStrategiesString = childStrategiesString;
        this.strikesPatterns.addAll(strikesPatterns);
        this.strikesPatternStrings.addAll(strikesPatternStrings);
        this.widthPatterns.addAll(widthPatterns);
        this.widthPatternStrings.addAll(widthPatternStrings);
        this.expirationPatterns.addAll(expirationPatterns);
        this.expirationPatternStrings.addAll(expirationPatternStrings);
        this.otherPatterns.addAll(otherPatterns);
        this.otherPatternStrings.addAll(otherPatternStrings);
        this.otherPatterns.addAll(otherPatterns);
        this.otherPatternStrings.addAll(otherPatternStrings);
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
    StrategyMeta addStrikesPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            strikesPatterns.add(p);
            strikesPatternStrings.add(pattern);
        }
        return this;
    }
    StrategyMeta addWidthPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            widthPatterns.add(p);
            widthPatternStrings.add(pattern);
        }
        return this;
    }
    StrategyMeta addExpirationPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            expirationPatterns.add(p);
            expirationPatternStrings.add(pattern);
        }
        return this;
    }
    StrategyMeta addExercisePattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            exercisePatterns.add(p);
            exercisePatternStrings.add(pattern);
        }
        return this;
    }
    StrategyMeta addOtherPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            otherPatterns.add(p);
            otherPatternStrings.add(pattern);
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
    StrategyMeta addInitialMarginPattern(String pattern) {
        if (pattern != null) {
            JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
            initialMarginPatterns.add(p);
            initialMarginPatternStrings.add(pattern);
        }
        return this;
    }
    
    
    StrategyMeta copy(String strategyName) {
        // must be a deep copy 
        StrategyMeta copy = new StrategyMeta(strategyName, legs, legsRatio, sort, childStrategies, 
                childStrategiesLegs, childStrategiesString, 
                strikesPatterns, strikesPatternStrings, 
                widthPatterns, widthPatternStrings, 
                expirationPatterns, expirationPatternStrings, 
                otherPatterns, otherPatternStrings, 
                maintenanceMarginPatterns, maintenanceMarginPatternStrings, 
                initialMarginPatterns, initialMarginPatternStrings, marginDebugPatterns, marginDebugPatternStrings);
        return copy;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StrategyMeta: {strategyName: ");
        builder.append(strategyName);
        builder.append(", strikesPatternStrings: ");
        builder.append(strikesPatternStrings);
        builder.append(", widthPatternStrings: ");
        builder.append(widthPatternStrings);
        builder.append(", expirationPatternStrings: ");
        builder.append(expirationPatternStrings);
        builder.append(", exercisePatternStrings: ");
        builder.append(exercisePatternStrings);
        builder.append(", otherPatternStrings: ");
        builder.append(otherPatternStrings);
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
