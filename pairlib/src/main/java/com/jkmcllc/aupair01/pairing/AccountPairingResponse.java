package com.jkmcllc.aupair01.pairing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.structure.Account;

public interface AccountPairingResponse {
    
    /**
     * The account associated with this response.
     * 
     * @return the account associated with this response
     */
    Account getAccount();
    
    /**
     * The best, lowest-requirement result of option strategy pairing, grouped by option root.
     * 
     * @return the strategies, by option root.
     */
    Map<String, List<Strategy>> getStrategies();
    
    /**
     * The name of the strategy group list which produced the best pairing outcome. This would
     * typically be useful to confirm that adding a new list produced a more optimal outcome.
     * 
     * @return
     */
    Map<String, String> getStrategyGroupByRoot();
    
    /**
     * Calculates the total maintenance requirement, across all strategies for all
     * option roots in the account, for <i>positions</i>.  This would typically be used
     * as an input to the maintenance excess calculation, such as for maintenance calls.
     * <p>
     * This calculation does not include the requirement for non-option positions
     * (i.e. does not include the requirement for marginable securities).
     * 
     * @param includeOrders true if orders should be included for the output (like for buying power calculations)
     * @return the total option maintenance requirement for positions
     */
    BigDecimal getTotalMaintenanceRequirement(boolean includeOrders);
    
    /**
     * Calculates the total initial requirement, across all strategies for all
     * option roots in the account, for <i>positions</i>.  This would typically be used
     * as an input to the buying power calculation.
     * <p>
     * This calculation does not include the requirement for non-option positions
     * (i.e. does not include the requirement for marginable securities).
     * 
     * @param includeOrders true if orders should be included for the output (like for buying power calculations)
     * @return the total option initial requirement for positions
     */
    BigDecimal getTotalInitialRequirement(boolean includeOrders);
    
    /**
     * @deprecated use {@link getTotalMaintenanceRequirement}
     */
    BigDecimal getTotalMaintenanceMargin();
    
    /**
     * @deprecated use {@link getTotalInitialRequirement}
     */
    BigDecimal getTotalInitialMargin();
    
    /**
     * If the request included the flag requestAllStrategyLists, then this returns a Map
     * grouped by option root, with a value of another Map having the strategy pairing
     * result for all strategy lists in the strategy group requested for this account.
     * <p>
     * This output would typically be used for testing and debugging, to compare the outcome of all
     * lists in the strategy group, and ensure that the expected list was selected as the 
     * most optimal pairing outcome. It would also be used to test adding a new list, to ensure
     * that it produced a better outcome than existing lists. 
     * 
     * @see PairingRequest#isRequestAllStrategyLists()
     * @return the result of all strategy list testing
     */
    Map<String, Map<String, List<Strategy>>> getAllStrategyListResults();
    
    /**
     * Returns a Map where the key is the option root symbol, and the value is
     * the result of testing orders and positions in the option root for the
     * worst case order execution.
     * <p>
     * This output would typically be used for explaining order reserves from buying power. 
     * <p>
     * If the worst-case outcome is that no orders are executed, then the outcome of pairing the
     * positions is returned as the worst case outcome. 
     * <p>
     * If no orders exist for the option root, then the outcome of pairing the
     * positions is returned as the worst case outcome. 
     * 
     * @return the Map of worst case order execution outcomes
     * @see WorstCaseOrderOutcome
     */
    Map<String, WorstCaseOrderOutcome> getWorstCaseOrderOutcomes();
    
    
    /**
     * The change in the initial option requirement, including the change due
     * to the worst-case order outcome.
     * 
     * <p>
     * The order cost is added as-is, therefore if the order cost includes
     * the change in requirement for marginable securities, then this method
     * will return the total change to apply to an initial excess calculation.
     * <p>
     * Typically this method would be called, with includeOrderCost equal to true,
     * to find the total change to apply to buying power, for open orders.
     * It would also be used to show "open order reserves" for buying power.
     * 
     * @param includeOrderCost true, to include the order cost passed in the request
     * @return the change in initial requirement
     */
    BigDecimal getInitialValueChange(boolean includeOrderCost);
    
    /**
     * The change in the maintenance option requirement, including the change due
     * to the worst-case order outcome.
     * 
     * <p>
     * The order cost is added as-is, therefore if the order cost includes
     * the change in requirement for marginable securities, then this method
     * will return the total change to apply to a maintenance excess calculation.
     * <p>
     * Typically this method would be called, with includeOrderCost equal to true,
     * to find the total change to apply to buying power, for open orders.
     * It would also be used to show "open order reserves" for buying power.
     * 
     * @param includeOrderCost true, to include the order cost passed in the request
     * @return the change in initial requirement
     */
    BigDecimal getMaintenanceValueChange(boolean includeOrderCost);
    
    
    /**
     * 
     * @deprecated use {@link getMaintenanceRequirement}
     */
    public static BigDecimal getMaintenanceMargin(List<Strategy> strategies) {
        return getMaintenanceRequirement(strategies);
    }
    
    /**
     * 
     * @deprecated use {@link getInitialRequirement}
     */
    public static BigDecimal getInitialMargin(List<Strategy> strategies) {
        return getInitialRequirement(strategies);
    }
    
    /**
     * Helper method for summarizing the option maintenance requirement 
     * 
     * @param strategies for the summarization
     * @return the total option maintenance requirement for the given strategies
     */
    public static BigDecimal getMaintenanceRequirement(List<Strategy> strategies) {
        BigDecimal totalMaintMargin = strategies.parallelStream().map(s1 -> s1.getMaintenanceRequirement()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
    
    /**
     * Helper method for summarizing the option initial requirement 
     * 
     * @param strategies for the summarization
     * @return the total option initial requirement for the given strategies
     */
    public static BigDecimal getInitialRequirement(List<Strategy> strategies) {
        BigDecimal totalMaintMargin = strategies.parallelStream().map(s1 -> s1.getInitialRequirement()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b) );
        return totalMaintMargin;
    }
}
