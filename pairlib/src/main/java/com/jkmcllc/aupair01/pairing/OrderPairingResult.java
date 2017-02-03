package com.jkmcllc.aupair01.pairing;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.jkmcllc.aupair01.pairing.impl.Leg;

/**
 * Returns the result of an order passed in a request, with useful information
 * about the order after it has been tested for the option strategy pairing
 * outcome. 
 * 
 * @author Jason Merecki
 *
 */
public interface OrderPairingResult {

    boolean isWorstCaseOutcome();

    List<? extends Leg> getOrderLegs();

    String getOrderId();

    String getOrderDescription();

    /**
     * @deprecated use {@link getInitialRequirement}
     */
    BigDecimal getInitialMargin();

    /**
     * @deprecated use {@link getMaintenanceRequirement}
     */
    BigDecimal getMaintenanceMargin();
    
    /**
     * The initial requirement change only for this order, 
     * as a result of pairing this order with current positions
     * into the optimal strategy outcome. This is useful for
     * populating an order confirmation screen, to show the user
     * what the impact will be to option requirement as a result
     * of executing only this order.
     * <p>
     * This calculation does not include the requirement for non-option positions
     * (i.e. does not include the requirement for marginable securities).
     * 
     * @return the initial requirement for this order only
     */
    BigDecimal getInitialRequirement();

    /**
     * The maintenance requirement change only for this order, 
     * as a result of pairing this order with current positions
     * into the optimal strategy outcome. This is useful for
     * populating an order confirmation screen, to show the user
     * what the impact will be to option requirement as a result
     * of executing only this order.
     * <p>
     * This calculation does not include the requirement for non-option positions
     * (i.e. does not include the requirement for marginable securities).
     * 
     * @return the initial requirement for this order only
     */
    BigDecimal getMaintenanceRequirement();

    /**
     * Echo's the initial cost which was passed into the request. This should
     * be the cost impact to buying power for <b>marginable</b> orders. 
     * 
     * @see Order#getOrderInitialCost()
     * @see OrderBuilder#setOrderInitialCost(String orderInitialCost)
     * @return the initial cost passed into the request
     */
    BigDecimal getOrderInitialCost();

    /**
     * Echo's the maintenance cost which was passed into the request. This should
     * be the cost impact to buying power for <b>marginable</b> orders. 
     * 
     * @see Order#getOrderMaintenanceCost()
     * @see OrderBuilder#setOrderMaintenanceCost(String orderMaintenanceCost)
     * @return the maintenance cost passed into the request
     */
    BigDecimal getOrderMaintenanceCost();
    
    /**
     * Returns the change in initial value for this order, only if
     * this order executes on its own, <i>without considering the impact of
     * any other orders.</i> This value could be used to show on an order
     * preview screen, to show the user how their buying power will change
     * if this order fills.  It might be useful to include an explanation for
     * the user that this amount assumes no other current orders will fill, because
     * other orders could change the outcome of the order in the preview screen.
     * 
     * @return the change in initial value for this order
     */
    BigDecimal getInitialChange();
    
    /**
     * Returns the change in maintenance value for this order, only if
     * this order executes on its own, <i>without considering the impact of
     * any other orders.</i> This value could be used to show on an order
     * preview screen, to show the user how their maintenance excess will change
     * if this order fills.  It might be useful to include an explanation for
     * the user that this amount assumes no other current orders will fill, because
     * other orders could change the outcome of the order in the preview screen.
     * 
     * @return the change in maintenance value for this order
     */
    BigDecimal getMaintenanceChange();
    
    public static BigDecimal getOrderMaintenanceCost(Collection<? extends OrderPairingResult> orderPairingResults) {
        BigDecimal totalMaintMargin = orderPairingResults.parallelStream().map(s1 -> s1.getOrderMaintenanceCost()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
    
    public static BigDecimal getOrderInitialCost(Collection<? extends OrderPairingResult> orderPairingResults) {
        BigDecimal totalMaintMargin = orderPairingResults.parallelStream().map(s1 -> s1.getOrderInitialCost()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }

}