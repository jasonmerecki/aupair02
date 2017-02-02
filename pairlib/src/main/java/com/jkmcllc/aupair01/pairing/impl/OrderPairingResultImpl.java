package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jkmcllc.aupair01.pairing.OrderPairingResult;

class OrderPairingResultImpl implements Comparable<OrderPairingResultImpl>, OrderPairingResult {
    private final List<AbstractLeg> orderLegs = new CopyOnWriteArrayList<>();
    private final String orderId;
    private final String orderDescription;
    private final BigDecimal orderMaintenanceCost;
    private final BigDecimal orderInitialCost;
    
    boolean worstCaseOutcome;
    private boolean allSellOptions = true;
    private int sellToOpenOptionLegs;
    private int sellToCloseOptionLegs;
    private int sellOptionLegs;
    private int buyOptionLegs;
    private int sellStockLegs;
    private int buyStockLegs;
    BigDecimal totalInitialMargin = BigDecimal.ZERO;
    BigDecimal totalMaintenanceMargin = BigDecimal.ZERO;

    
    protected OrderPairingResultImpl(String orderId, String orderDescription,
            BigDecimal orderMaintenanceCost, BigDecimal orderInitialCost) {
        this.orderId = orderId;
        this.orderDescription = orderDescription;
        this.orderMaintenanceCost = orderMaintenanceCost;
        this.orderInitialCost = orderInitialCost;
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
            } else if (AbstractLeg.STOCK.equals(legType)) {
                if (signum > 0) {
                    buyStockLegs++;
                    allSellOptions = false;
                } else {
                    sellStockLegs++;
                    allSellOptions = false;
                }
            }
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
        if (this.orderMaintenanceCost.compareTo(that.orderMaintenanceCost) != 0) {
            return this.orderMaintenanceCost.compareTo(that.orderMaintenanceCost);
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
    
    @Override
    public BigDecimal getInitialMargin() {
        return totalInitialMargin;
    }
    
    @Override
    public BigDecimal getMaintenanceMargin() {
        return totalMaintenanceMargin;
    }
    
    @Override
    public BigDecimal getInitialRequirement() {
        return totalInitialMargin;
    }
    
    @Override
    public BigDecimal getMaintenanceRequirement() {
        return totalMaintenanceMargin;
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
        builder.append(", orderMaintenanceCost: ");
        builder.append(orderMaintenanceCost);        
        builder.append(", orderInitialCost: ");
        builder.append(orderInitialCost);
        builder.append(", totalMaintenanceMargin: ");
        builder.append(totalMaintenanceMargin);   
        builder.append(", totalInitialMargin: ");
        builder.append(totalInitialMargin);
        builder.append(", worstCaseOutcome: ");
        builder.append(worstCaseOutcome);
        builder.append(", orderLegs: ");
        builder.append(orderLegs);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public BigDecimal getOrderInitialCost() {
        return orderInitialCost;
    }

    @Override
    public BigDecimal getOrderMaintenanceCost() {
        return orderMaintenanceCost;
    }

}
