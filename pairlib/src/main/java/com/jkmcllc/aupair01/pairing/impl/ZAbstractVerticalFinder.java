package com.jkmcllc.aupair01.pairing.impl;

public abstract class ZAbstractVerticalFinder extends AbstractFinder {

    protected final String equalTwoDatesExpressionFrag = " && legs.get(0).optionConfig.expiryDateExpression.compareTo(legs.get(1).optionConfig.expiryDateExpression) == 0 ";

    public ZAbstractVerticalFinder(PairingInfo pairingInfo) {
        super(pairingInfo);
    }

}