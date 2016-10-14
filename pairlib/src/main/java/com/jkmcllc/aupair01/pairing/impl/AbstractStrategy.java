package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

class AbstractStrategy implements Strategy {

    final String strategyName;
    final boolean prohibitedStrategy;
    final List<? extends Leg> legs;
    BigDecimal maintenanceMargin = BigDecimal.ZERO;
    BigDecimal initialMargin = BigDecimal.ZERO;
    String marginDebug = null;
    final Integer quantity;
    
    AbstractStrategy(String strategyName, boolean prohibitedStrategy, List<Leg> legs, Integer quantity, AccountInfo accountInfo, PairingInfo pairingInfo,
            List<JexlExpression> maintenanceMarginExpressions, List<JexlExpression> initialMarginExpressions, 
            List<JexlExpression> marginDebugExpressions) {
        this.strategyName = strategyName;
        this.prohibitedStrategy = prohibitedStrategy;
        this.legs = legs;
        this.quantity = quantity;
        JexlContext context = TacoCat.buildMarginContext(legs, accountInfo, pairingInfo, this);
        for (JexlExpression marginExpression : maintenanceMarginExpressions) {
            this.maintenanceMargin = (BigDecimal) marginExpression.evaluate(context);
        }
        if (initialMarginExpressions == null || initialMarginExpressions.isEmpty()) {
            initialMarginExpressions = maintenanceMarginExpressions;
        } 
        for (JexlExpression marginExpression : initialMarginExpressions) {
            this.initialMargin = (BigDecimal) marginExpression.evaluate(context);
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
            if (sb.length() > 4) {
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
    public BigDecimal getMaintenanceMargin() {
        return maintenanceMargin;
    }
    
    @Override
    public BigDecimal getInitialMargin() {
        return initialMargin;
    }
    
    @Override
    public boolean isProhibitedStrategy() {
        return prohibitedStrategy;
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
        if (prohibitedStrategy) {
            builder.append(" (prohibited)");
        }
        builder.append(", quantity: ");
        builder.append(quantity);
        builder.append(", maintenanceMargin: ");
        builder.append(maintenanceMargin);
        if (marginDebug != null) {
            builder.append(", marginDebug: ");
            builder.append(marginDebug);
        }
        builder.append(", initialMargin: ");
        builder.append(initialMargin);
        builder.append(", legs: ");
        builder.append(legs);
        builder.append("}");
        return builder.toString();
    }

}
