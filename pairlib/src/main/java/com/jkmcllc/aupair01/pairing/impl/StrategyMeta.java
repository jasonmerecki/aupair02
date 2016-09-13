package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;

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
    final List<JexlExpression> marginPatterns = new ArrayList<>();
    final List<String> marginPatternStrings = new ArrayList<>();
    
    private final JexlExpression TRUE = TacoCat.getJexlEngine().createExpression("true");
    private final JexlExpression ZERO = TacoCat.getJexlEngine().createExpression("zero");
    
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
        this.marginPatterns.add(ZERO);
    }
    
    StrategyMeta(String strategyName, String legs, String legsRatioString, String childStrategiesString, String childStrategiesLegsString) {
        this.strategyName = strategyName;
        String[] legsTemp = legs.split(",");
        for (int i = 0; i < legsTemp.length; i++) {
            legsTemp[i] = legsTemp[i].trim();
        }
        this.legs = legsTemp;
        String[] legsRatioTemp = legsRatioString.split(",");
        Integer[] legsRatioIntegers = new Integer[legsRatioTemp.length];
        for (int i = 0; i < legsRatioTemp.length; i++) {
            legsRatioIntegers[i] = Integer.parseInt(legsRatioTemp[i].trim());
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
            marginPatterns.add(p);
            marginPatternStrings.add(pattern);
        }
        return this;
    }
    
    
    @Override
    protected StrategyMeta clone() {
        StrategyMeta clone = null;
        try {
            clone = (StrategyMeta) super.clone();
        } catch (CloneNotSupportedException e) {
            // will never happen
            e.printStackTrace();
        }
        return clone;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StrategyMeta: {strategyName: ");
        builder.append(strategyName);
        builder.append(", strategyPatternStrings: ");
        builder.append(strategyPatternStrings);
        builder.append(", marginPatternStrings: ");
        builder.append(marginPatternStrings);
        builder.append(", childStrategies: ");
        builder.append(childStrategies);
        builder.append("}");
        return builder.toString();
    }
}
