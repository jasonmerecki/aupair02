package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class AbstractStrategy implements Strategy {

    final String strategyName;
    final List<? extends Leg> legs;
    BigDecimal margin = BigDecimal.ZERO;
    final Integer quantity;
    
    AbstractStrategy(String strategyName, List<Leg> legs, Integer quantity, AccountInfo accountInfo, JexlExpression marginExpression) {
        this.strategyName = strategyName;
        this.legs = legs;
        this.quantity = quantity;
        JexlContext context = TacoCat.buildStandardContext(legs, accountInfo);
        BigDecimal margin = (BigDecimal) marginExpression.evaluate(context);
        this.margin = margin;
    }
    
    @Override
    public String getStrategyName() {
        return this.strategyName;
    }

    @Override
    public List<? extends Leg> getLegs() {
        return this.legs;
    }

    @Override
    public BigDecimal getMargin() {
        return margin;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Strategy: {strategyName: ");
        builder.append(strategyName);
        builder.append(", quantity: ");
        builder.append(quantity);
        builder.append(", margin: ");
        builder.append(margin);
        builder.append(", legs: ");
        builder.append(legs);
        builder.append("}");
        return builder.toString();
    }

}
