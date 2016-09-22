package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class AbstractStrategy implements Strategy {

    final String strategyName;
    final List<? extends Leg> legs;
    BigDecimal margin = BigDecimal.ZERO;
    String marginDebug = null;
    final Integer quantity;
    
    AbstractStrategy(String strategyName, List<Leg> legs, Integer quantity, AccountInfo accountInfo, 
            List<JexlExpression> marginExpressions, List<JexlExpression> marginDebugExpressions) {
        this.strategyName = strategyName;
        this.legs = legs;
        this.quantity = quantity;
        JexlContext context = TacoCat.buildMarginContext(legs, accountInfo, this);
        for (JexlExpression marginExpression : marginExpressions) {
            this.margin = (BigDecimal) marginExpression.evaluate(context);
        }
        if (marginDebugExpressions != null) {
            StringBuilder sb = new StringBuilder("{\"");
            Iterator<JexlExpression> iter = marginDebugExpressions.iterator();
            while (iter.hasNext()) {
                JexlExpression marginDebugExpression = iter.next();
                sb.append(marginDebugExpression.evaluate(context).toString());
                if (iter.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("\"}");
            if (sb.length() > 0) {
                marginDebug = sb.toString();
            }
        }
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
    public Integer getQuantity() {
        return quantity;
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
        if (marginDebug != null) {
            builder.append(", marginDebug: ");
            builder.append(marginDebug);
        }
        builder.append(", legs: ");
        builder.append(legs);
        builder.append("}");
        return builder.toString();
    }

}
