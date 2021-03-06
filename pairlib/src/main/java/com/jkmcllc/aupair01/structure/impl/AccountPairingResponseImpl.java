package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.OrderPairingResult;
import com.jkmcllc.aupair01.pairing.WorstCaseOrderOutcome;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.structure.Account;

class AccountPairingResponseImpl implements AccountPairingResponse {
    private final Account account;
    private final Map<String, List<Strategy>> resultMap ;
    private final Map<String, String> strategyGroupByRoot;
    private final Map<String, Map<String, List<Strategy>>> allStrategyListResultMap ;
    private final Map<String, WorstCaseOrderOutcome> worstCaseOrderOutcomes;
    
    AccountPairingResponseImpl(Account account, Map<String, List<Strategy>> resultMap, Map<String, String> strategyGroupByRoot,
            Map<String, Map<String, List<Strategy>>> allStrategyListResultMap, Map<String, WorstCaseOrderOutcome> worstCaseOrderOutcomes) {
        this.account = account;
        if (resultMap == null) resultMap = Collections.emptyMap();
        this.resultMap = resultMap;
        this.strategyGroupByRoot = (strategyGroupByRoot != null) ? strategyGroupByRoot : Collections.emptyMap();
        this.allStrategyListResultMap = allStrategyListResultMap;
        this.worstCaseOrderOutcomes = worstCaseOrderOutcomes;
    };
    
    @Override
    public Account getAccount() {
        return account;
    }
    
    @Override
    public Map<String, List<Strategy>> getStrategies() {
        return resultMap;
    }
    
    @Override
    public Map<String, String> getStrategyGroupByRoot() {
        return strategyGroupByRoot;
    }

    @Override
    public BigDecimal getTotalMaintenanceMargin() {
        return getTotalMaintenanceRequirement(false);
    }
    
    @Override
    public BigDecimal getTotalMaintenanceRequirement(boolean includeOrders) {
        BigDecimal totalMaintMargin = BigDecimal.ZERO;
        if (!includeOrders) {
            totalMaintMargin = resultMap.values().stream().map((e) -> {
                BigDecimal totalRootMargin = AccountPairingResponse.getMaintenanceRequirement(e);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        } else {
            totalMaintMargin = worstCaseOrderOutcomes.values().stream().map( (e) -> {
                List<Strategy> worstOrderStrategies = e.getStrategies();
                BigDecimal totalRootMargin = AccountPairingResponse.getMaintenanceRequirement(worstOrderStrategies);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        }
        return totalMaintMargin;
    }

    @Override
    public BigDecimal getTotalInitialMargin() {
        return getTotalInitialRequirement(false);
    }
    
    @Override
    public BigDecimal getTotalInitialRequirement(boolean includeOrders) {
        BigDecimal totalMaintMargin = BigDecimal.ZERO;
        if (!includeOrders) {
            totalMaintMargin = resultMap.values().stream().map((e) -> {
                BigDecimal totalRootMargin = AccountPairingResponse.getInitialRequirement(e);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        } else {
            totalMaintMargin = worstCaseOrderOutcomes.values().stream().map((e) -> {
                List<Strategy> worstOrderStrategies = e.getStrategies();
                BigDecimal totalRootMargin = AccountPairingResponse.getInitialRequirement(worstOrderStrategies);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        }
        return totalMaintMargin;
    }
    
    private BigDecimal getTotalInitialNonOptionPriceRequirement(boolean includeOrders) {
        BigDecimal totalMaintMargin = BigDecimal.ZERO;
        if (!includeOrders) {
            totalMaintMargin = resultMap.values().stream().map((e) -> {
                BigDecimal totalRootMargin = AccountPairingResponse.getNonOptionPriceInitialRequirement(e);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        } else {
            totalMaintMargin = worstCaseOrderOutcomes.values().stream().map((e) -> {
                List<Strategy> worstOrderStrategies = e.getStrategies();
                BigDecimal totalRootMargin = AccountPairingResponse.getNonOptionPriceInitialRequirement(worstOrderStrategies);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        }
        return totalMaintMargin;
    }
    
    private BigDecimal getTotalMaintenanceNonOptionPriceRequirement(boolean includeOrders) {
        BigDecimal totalMaintMargin = BigDecimal.ZERO;
        if (!includeOrders) {
            totalMaintMargin = resultMap.values().stream().map((e) -> {
                BigDecimal totalRootMargin = AccountPairingResponse.getNonOptionPriceMaintenanceRequirement(e);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        } else {
            totalMaintMargin = worstCaseOrderOutcomes.values().stream().map((e) -> {
                List<Strategy> worstOrderStrategies = e.getStrategies();
                BigDecimal totalRootMargin = AccountPairingResponse.getNonOptionPriceMaintenanceRequirement(worstOrderStrategies);
                return totalRootMargin;
            }).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        }
        return totalMaintMargin;
    }
    
    @Override
    public Map<String, Map<String, List<Strategy>>> getAllStrategyListResults() {
        return this.allStrategyListResultMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountPairingResponseImpl: {resultMap:");
        builder.append(resultMap);
        builder.append(", strategyGroupByRoot: ");
        builder.append(strategyGroupByRoot);
        if (worstCaseOrderOutcomes != null) {
            builder.append(", worstCaseOrderOutcomes: ");
            builder.append(worstCaseOrderOutcomes);
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Map<String, WorstCaseOrderOutcome> getWorstCaseOrderOutcomes() {
        return this.worstCaseOrderOutcomes;
    }

    @Override
    public BigDecimal getInitialChange(boolean includeOrderCost) {
    	BigDecimal change = BigDecimal.ZERO;
    	if (includeOrderCost) {
	        BigDecimal positionReq = getTotalInitialRequirement(false);
	        BigDecimal orderReq = getTotalInitialRequirement(true);
	        change = positionReq.subtract(orderReq);
            List<OrderPairingResult> worstSelectedOrders = worstCaseOrderOutcomes.values().stream()
                    .map(out -> out.getOrders()).flatMap(orders -> orders.stream())
                    .filter(o -> o.isWorstCaseOutcome()).collect(Collectors.toList());
            BigDecimal costInitialWorstOrders = OrderPairingResult.getOrderInitialCost(worstSelectedOrders);
            change = change.subtract(costInitialWorstOrders);	        
    	} else  {
    		/* do not subtract the order cost because limit price on short unpaired
    		 * may not equal the market price and that throws off the math */
	        BigDecimal positionReq = getTotalInitialNonOptionPriceRequirement(false);
	        BigDecimal orderReq = getTotalInitialNonOptionPriceRequirement(true);
	        change = positionReq.subtract(orderReq);
        }
        return change;
    }

    @Override
    public BigDecimal getMaintenanceChange(boolean includeOrderCost) {
    	BigDecimal change = BigDecimal.ZERO;
    	if (includeOrderCost) {
	        BigDecimal positionReq = getTotalMaintenanceRequirement(false);
	        BigDecimal orderReq = getTotalMaintenanceRequirement(true);
	        change = positionReq.subtract(orderReq);
            List<OrderPairingResult> worstSelectedOrders = worstCaseOrderOutcomes.values().stream()
                    .map(out -> out.getOrders()).flatMap(orders -> orders.stream())
                    .filter(o -> o.isWorstCaseOutcome()).collect(Collectors.toList());
            BigDecimal costMaintnanceWorstOrders = OrderPairingResult.getOrderMaintenanceCost(worstSelectedOrders);
            change = change.subtract(costMaintnanceWorstOrders);
    	} else {
            /* do not subtract the order cost because limit price on short unpaired
    		 * may not equal the market price and that throws off the math */
	        BigDecimal positionReq = getTotalMaintenanceNonOptionPriceRequirement(false);
	        BigDecimal orderReq = getTotalMaintenanceNonOptionPriceRequirement(true);
	        change = positionReq.subtract(orderReq);
        }
        return change;
    }
 

}
