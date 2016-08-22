package com.jkmcllc.aupair01.pairing.impl;

import org.apache.commons.jexl3.JexlExpression;

class DefaultMargins {
    static JexlExpression shortVerticalMargin = TacoCat.getJexlEngine().createExpression("legs.get(0).optionConfig.strikePrice.subtract(legs.get(1).optionConfig.strikePrice).abs().multiply(legs.get(1).optionRoot.multiplier).multiply(legs.get(1).qty)");
    static JexlExpression longVerticalMargin = TacoCat.getJexlEngine().createExpression("zero");
}
