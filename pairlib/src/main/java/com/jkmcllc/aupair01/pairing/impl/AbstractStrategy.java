package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.pairing.impl.TacoCat.NakedOptionLegWrapper;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.structure.OptionType;

class AbstractStrategy implements Strategy {
    private static final Logger logger = LoggerFactory.getLogger(AbstractStrategy.class);
    private final String strategyName;
    private final boolean prohibitedStrategy;
    private final List<? extends Leg> legs;
    BigDecimal maintenanceMargin = BigDecimal.ZERO;
    BigDecimal initialMargin = BigDecimal.ZERO;
    BigDecimal pureNakedCallMargin = BigDecimal.ZERO;
    BigDecimal pureNakedPutMargin = BigDecimal.ZERO;
    BigDecimal pureNakedMargin = BigDecimal.ZERO;
    String marginDebug = null;
    private final Integer quantity;
    
    AbstractStrategy(String strategyName, boolean prohibitedStrategy, List<Leg> legs, Integer quantity, AccountInfo accountInfo, PairingInfo pairingInfo,
            List<JexlExpression> maintenanceMarginExpressions, List<JexlExpression> initialMarginExpressions, 
            List<JexlExpression> marginDebugExpressions) {
        this.strategyName = strategyName;
        this.prohibitedStrategy = prohibitedStrategy;
        this.legs = legs;
        this.quantity = quantity;

        JexlContext context = TacoCat.buildMarginContext(legs, accountInfo, pairingInfo, this);
        for (Leg leg : legs) { 
            if (leg instanceof ShortCall || leg instanceof ShortPut) {
                OptionType optionType = null;
                AbstractOptionLeg optionLeg = null;
                if (leg instanceof ShortCall) {
                    optionType = OptionType.C;
                    optionLeg = (ShortCall) leg;
                } else if (leg instanceof ShortPut) {
                    optionType = OptionType.P;
                    optionLeg = (ShortPut) leg;
                }

                List<JexlExpression> shortMarginExpressions = StrategyConfigs.getInstance().nakedMarginMap.get(optionType);
                if (shortMarginExpressions == null || shortMarginExpressions.isEmpty()) {
                    logger.warn("No naked margin expression found for type: " + optionType);
                    return;
                }
                NakedOptionLegWrapper mcHammer = (NakedOptionLegWrapper) context.get(TacoCat.NAKED_LEG);
                mcHammer.leg = optionLeg;
                for (JexlExpression expression : shortMarginExpressions) {
                    BigDecimal nakedResult = (BigDecimal) expression.evaluate(context);
                    switch (optionType) {
                    case C:
                        pureNakedCallMargin = nakedResult;
                        break;
                    case P:
                        pureNakedPutMargin = nakedResult;
                        break;
                    }
                }
            } 
        }

        pureNakedMargin = pureNakedCallMargin.add(pureNakedPutMargin);
        
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
    public BigDecimal getPureNakedCallMargin() {
        return pureNakedCallMargin;
    }
    
    @Override
    public BigDecimal getPureNakedPutMargin() {
        return pureNakedPutMargin;
    }
    
    @Override
    public BigDecimal getPureNakedMargin() {
        return pureNakedMargin;
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
        builder.append(", pureNakedMargin: ");
        builder.append(pureNakedMargin);
        builder.append(", legs: ");
        builder.append(legs);
        builder.append("}");
        return builder.toString();
    }

}
