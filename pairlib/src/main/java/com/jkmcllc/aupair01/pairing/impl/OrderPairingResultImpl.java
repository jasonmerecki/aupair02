package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class OrderPairingResultImpl implements Comparable<OrderPairingResultImpl>, OrderPairingResult {
    private final List<AbstractLeg> orderLegs = new CopyOnWriteArrayList<>();
    private final String orderId;
    private final String orderDescription;
    
    boolean worstCaseOutcome;
    private boolean allSellOptions = true;
    private int sellToOpenOptionLegs;
    private int sellToCloseOptionLegs;
    private int sellOptionLegs;
    private int buyOptionLegs;
    private int sellStockLegs;
    private int buyStockLegs;
    private BigDecimal equityInitialMargin = BigDecimal.ZERO;
    private BigDecimal equityMaintMargin = BigDecimal.ZERO;
    BigDecimal totalInitialMargin = BigDecimal.ZERO;
    BigDecimal totalMaintenanceMargin = BigDecimal.ZERO;
    private BigDecimal optionInitialMargin = BigDecimal.ZERO;
    private BigDecimal optionMaintenanceMargin = BigDecimal.ZERO;
    
    protected OrderPairingResultImpl(String orderId, String orderDescription) {
        this.orderId = orderId;
        this.orderDescription = orderDescription;
    }
    
    public void addOrderLegs(List<AbstractLeg> orderLegs) {
        this.orderLegs.addAll(orderLegs);
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
            equityMaintMargin = equityMaintMargin.add(leg.getEquityMaintenanceMargin());
            equityInitialMargin = equityInitialMargin.add(leg.getEquityInitialMargin());
        });
    }
    
    @Override
    public int compareTo(OrderPairingResultImpl that) {
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
        if (this.equityInitialMargin.compareTo(that.equityInitialMargin) != 0) {
            return this.equityInitialMargin.compareTo(that.equityInitialMargin);
        }
        // orderID (to remove all other ambiguity) 
        return this.orderId.compareTo(that.orderId);
    }
    
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#isWorstCaseOutcome()
     */
    @Override
    public boolean isWorstCaseOutcome() {
        return worstCaseOutcome;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getOrderLegs()
     */
    @Override
    public List<? extends Leg> getOrderLegs() {
        return orderLegs;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getOrderId()
     */
    @Override
    public String getOrderId() {
        return orderId;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getOrderDescription()
     */
    @Override
    public String getOrderDescription() {
        return orderDescription;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getEquityInitialMargin()
     */
    @Override
    public BigDecimal getEquityInitialMargin() {
        return equityInitialMargin;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getEquityMaintMargin()
     */
    @Override
    public BigDecimal getEquityMaintMargin() {
        return equityMaintMargin;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getTotalInitialMargin()
     */
    @Override
    public BigDecimal getInitialMargin() {
        return totalInitialMargin;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getTotalMaintenanceMargin()
     */
    @Override
    public BigDecimal getMaintenanceMargin() {
        return totalMaintenanceMargin;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getOptionInitialMargin()
     */
    @Override
    public BigDecimal getOptionInitialMargin() {
        return optionInitialMargin;
    }
    /* (non-Javadoc)
     * @see com.jkmcllc.aupair01.pairing.impl.OrderPairingResult#getOptionMaintenanceMargin()
     */
    @Override
    public BigDecimal getOptionMaintenanceMargin() {
        return optionMaintenanceMargin;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderPairingResultImpl == false) {
            return false;
        }
        OrderPairingResultImpl that = (OrderPairingResultImpl) obj;
        return that.orderId.equals(this.orderId);
    }

    @Override
    public int hashCode() {
        return this.orderId.hashCode();
    }

    public int getOptionLegsCount() {
        return sellOptionLegs + buyOptionLegs;
    }
    
    public int getStockLegsCount() {
        return sellStockLegs + buyStockLegs;
    }
    
    public int getSellLegsCount() {
        return sellStockLegs + sellOptionLegs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderPairingResultImpl: {");
        builder.append("orderId: ");
        builder.append(orderId);
        builder.append(", orderDescription: '");
        builder.append(orderDescription);
        builder.append("'");
        builder.append(", orderLegs: ");
        builder.append(orderLegs);
        builder.append("}");
        return builder.toString();
    }

}
