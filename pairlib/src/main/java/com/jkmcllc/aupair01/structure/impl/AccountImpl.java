package com.jkmcllc.aupair01.structure.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.impl.GlobalConfigType;
import com.jkmcllc.aupair01.pairing.impl.StrategyConfigs;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.Order;
import com.jkmcllc.aupair01.structure.Position;

class AccountImpl implements Account {
    private List<Position> positions;
    private List<Order> orders;
    private final String accountId;
    private final Map<String, String> customProperties;
    private final String strategyGroupName;
    
    AccountImpl(String accountId, List<Position> legs, List<Order> orders, String strategyGroupName, Map<String, String> customProperties) {
        this.accountId = accountId;
        this.positions = Collections.unmodifiableList(legs);
        this.orders = orders != null ? Collections.unmodifiableList(orders) : Collections.emptyList();
        this.customProperties = Collections.unmodifiableMap(customProperties);
        if (strategyGroupName == null) {
            strategyGroupName = StrategyConfigs.getInstance().getGlobalConfig(GlobalConfigType.DEFAULT_STRATEGY_GROUP);
        }
        this.strategyGroupName = strategyGroupName;
    }

    @Override
    public List<Position> getPositions() {
        return positions;
    }
    
    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }
    
    @Override
    public Map<String, String> getCustomProperties() {
        return customProperties;
    }

    @Override
    public String getStrategyGroupName() {
        return strategyGroupName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("account ");
        builder.append("{accountId: ");
        builder.append(accountId);
        builder.append(", strategyGroupName: ");
        builder.append(strategyGroupName);
        builder.append(", positions: ");
        builder.append(positions);
        if (!orders.isEmpty()) {
            builder.append(", orders: ");
            builder.append(orders);
        }
        builder.append(", customProperties: ");
        builder.append(customProperties);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void addOrder(Order order) {
        int end = this.orders.size();
        Order[] orderArray = orders.toArray(new Order[end + 1]);
        orderArray[end] = order;
        this.orders = Collections.unmodifiableList(Arrays.asList(orderArray));
    }

    @Override
    public Order removeOrder(String orderId) {
        if (orderId == null) return null;
        Order found = null;
        List<Order> newlist = new ArrayList<>();
        for (Order order : this.orders) {
            if (orderId.equals(order.getOrderId())) {
                found = order;
            } else {
                newlist.add(order);
            }
        }
        this.orders = newlist;
        return found;
    }
    
    @Override
    public void mergePosition(Position newPosition) {
    		Position existPosition = positions.stream().filter(epos -> epos.matches(newPosition)).findFirst().orElse(null);
    		if (existPosition != null) {
    			Integer newQty = newPosition.getQty() + existPosition.getQty();	
    			Position addedPosition  = StructureImplFactory.buildPosition(existPosition.getSymbol(), existPosition.getDescription(), 
    					newQty, existPosition.getPrice(), existPosition.getOptionConfig());
    			List<Position> newPositions = new ArrayList<>(this.positions);
    			int idx = this.positions.indexOf(existPosition);
    			newPositions.set(idx, addedPosition);
    			this.positions = Collections.unmodifiableList(newPositions);
    		} else {
    			List<Position> newPositions = new ArrayList<>(this.positions.size() + 1);
    			newPositions.addAll(this.positions);
    			newPositions.add(newPosition);
    			this.positions = Collections.unmodifiableList(newPositions);
    		}
    }


}
