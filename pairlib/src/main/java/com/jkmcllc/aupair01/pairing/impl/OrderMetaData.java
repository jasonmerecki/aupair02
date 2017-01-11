package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;

class OrderMetaData implements Comparable<OrderMetaData> {
    final List<AbstractLeg> orderLegs;
    private final String orderId;
    private final String orderDescription;
    
    private boolean worstCaseOutcome;
    private boolean allSellOptions = true;
    private int sellToOpenOptionLegs;
    private int sellToCloseOptionLegs;
    private int sellOptionLegs;
    private int buyOptionLegs;
    private int sellStockLegs;
    private int buyStockLegs;
    private BigDecimal equityInitialCost = BigDecimal.ZERO;
    private BigDecimal equityMaintCost = BigDecimal.ZERO;
    
    protected OrderMetaData(String orderId, String orderDescription, List<AbstractLeg> orderLegs) {
        this.orderLegs = orderLegs;
        this.orderId = orderId;
        this.orderDescription = orderDescription;
        orderLegs.forEach( leg -> {
            String legType = leg.getType();
            int signum = Integer.signum(leg.getQty());
            if (AbstractLeg.STOCKOPTION.equals(legType)) {
                if (signum > 0) {
                    buyOptionLegs++;
                    allSellOptions = false;
                } else {
                    sellOptionLegs++;
                    if (AbstractLeg.OpenClose.OPEN.equals(leg.openClose)) {
                        sellToOpenOptionLegs++;
                    } else if (AbstractLeg.OpenClose.CLOSE.equals(leg.openClose)) {
                        sellToCloseOptionLegs++;
                    }
                }
            } else if (AbstractLeg.STOCKOPTION.equals(legType)) {
                if (signum > 0) {
                    buyStockLegs++;
                    allSellOptions = false;
                } else {
                    sellStockLegs++;
                    allSellOptions = false;
                }
            }
            equityMaintCost = equityMaintCost.add(leg.getEquityMaintenanceMargin());
            equityInitialCost = equityInitialCost.add(leg.getEquityInitialMargin());
        });
    }
    @Override
    public int compareTo(OrderMetaData that) {
        // If order is all short options, then count of sell legs
        if (this.allSellOptions && that.allSellOptions
                && this.sellOptionLegs != that.sellOptionLegs) {
            return this.sellOptionLegs - that.sellOptionLegs;
        }
        // sell to open option count
        if (this.sellToOpenOptionLegs != that.sellToOpenOptionLegs) {
            return this.sellToOpenOptionLegs - that.sellToOpenOptionLegs;
        }
        // sell option count
        if (this.sellOptionLegs != that.sellOptionLegs) {
            return this.sellOptionLegs - that.sellOptionLegs;
        }
        // sell stock count
        if (this.sellStockLegs != that.sellStockLegs) {
            return this.sellStockLegs - that.sellStockLegs;
        }
        // buy stock count
        if (this.buyStockLegs != that.buyStockLegs) {
            return this.buyStockLegs - that.buyStockLegs;
        }
        // buy option count
        if (this.buyOptionLegs != that.buyOptionLegs) {
            return this.buyOptionLegs - that.buyOptionLegs;
        }
        // cost of order, proceeds or debit
        if (this.equityInitialCost.compareTo(that.equityInitialCost) != 0) {
            return this.equityInitialCost.compareTo(that.equityInitialCost);
        }
        // orderID (to remove all other ambiguity) 
        return this.orderId.compareTo(that.orderId);
    }
    
    public boolean isWorstCaseOutcome() {
        return worstCaseOutcome;
    }

    void setWorstCaseOutcome(boolean worstCaseOutcome) {
        this.worstCaseOutcome = worstCaseOutcome;
    }
}
