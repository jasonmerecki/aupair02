package com.jkmcllc.aupair01.pairing;

import java.util.Collection;
import java.util.List;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

/**
 * The WorstCaseOrderOutcome provides the details for the results of testing open
 * orders passed in the pairing request, with the worst outcome for each option root,
 * for a single account.
 * <p>
 * The results may be rolled-up to the account level to calculate the open order
 * reserves to buying power, or the helper methods on the {@link AccountPairingResponse}
 * may be used.
 * 
 * @author Jason Merecki
 *
 */
public interface WorstCaseOrderOutcome {
    /**
     * The option root which applies to this worst-case order outcome.
     * 
     * @return the option root string
     */
    String getOptionRoot();
    
    /**
     * The result of option strategy pairing for the worst-case outcome. 
     * 
     * <p> 
     * Note that the strategy pairing outcome includes those orders which have
     * {@link OrderPairingResult#isWorstCaseOutcome()} set to true.
     * 
     * @return the option pairing strategies resulting from the worst-case outcome
     */
    List<Strategy> getStrategies();
    
    /**
     * Returns the orders which were passed in the request, but with additional
     * information, such as the impact of the order on the initial and maintenance
     * requirement. See {@link OrderPairingResult} for more details.
     * 
     * @see OrderPairingResult
     * @return all orders passed in the request, both worst-case and not wost-case
     */
    Collection<? extends OrderPairingResult> getOrders();
}
