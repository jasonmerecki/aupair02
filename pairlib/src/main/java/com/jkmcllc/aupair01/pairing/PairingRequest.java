package com.jkmcllc.aupair01.pairing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.connect.Request;
import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.Position;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.Order;
import com.jkmcllc.aupair01.structure.Order.OrderBuilder;
import com.jkmcllc.aupair01.structure.OrderLeg;
import com.jkmcllc.aupair01.structure.OrderLeg.OrderLegBuilder;
import com.jkmcllc.aupair01.structure.UnderlyerType;
import com.jkmcllc.aupair01.structure.Account.AccountBuilder;
import com.jkmcllc.aupair01.structure.CorePosition;
import com.jkmcllc.aupair01.structure.Position.PositionBuilder;
import com.jkmcllc.aupair01.structure.OptionRoot.OptionRootBuilder;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface PairingRequest extends Request {
    final String NAME = "PairingRequest";
    default String requestType() {return NAME;}
    
    /**
     * Returns the List<Account> associated with this request. 
     * 
     * @returnthe List<Account> associated with this request. 
     */
    List<Account> getAccounts();
    
    /**
     * Returns a Map of option roots, where the key is the option root
     * String and the value is the OptionRoot object, for those option
     * root objects built as part of this request.
     * @return a Map of option roots
     */
    Map<String, OptionRoot> getOptionRoots();
    
    /**
     * Returns the sate of the request all strategy lists flag. This would
     * typically be set to true during debugging or testing a new strategy
     * list added to a strategy group, to ensure the new list prodcues
     * a more optimal pairing outcome. 
     * 
     * @return sate of the request all strategy lists flag
     */
    boolean isRequestAllStrategyLists();

    public class PairingRequestBuilder {
        protected List<Account> accounts = new ArrayList<>();
        protected Map<String, Position> accountPositions = new LinkedHashMap<>();
        protected List<Order> accountOrders = new ArrayList<>();
        protected Map<String, OptionRoot> optionRoots = new HashMap<>();
        protected PositionBuilder positionBuilder = Position.newBuilder();
        protected OptionRootBuilder optionRootBuilder = OptionRoot.newBuilder();
        protected AccountBuilder accountBuilder = Account.newBuilder();
        protected OrderLegBuilder orderLegBuilder = OrderLeg.newBuilder();
        protected OrderBuilder orderBuilder = Order.newBuilder();
        
        protected boolean requestAllStrategyLists = false;
        
        protected PairingRequestBuilder() {}
        
        public PairingRequestBuilder setPositionSymbol(String symbol) {
            positionBuilder.setSymbol(symbol);
            return this;
        }
        public PairingRequestBuilder setPositionDescription(String description) {
            positionBuilder.setDescription(description);
            return this;
        }
        public PairingRequestBuilder setPositionQty(Integer qty) {
            positionBuilder.setQty(qty);
            return this;
        }
        public PairingRequestBuilder setPositionPrice(String price) {
            positionBuilder.setPositionPrice(price);
            return this;
        }
        public PairingRequestBuilder setPositionOptionRoot(String optionRoot) {
            positionBuilder.setOptionRoot(optionRoot);
            return this;
        }
        public PairingRequestBuilder setPositionOptionType(OptionType optionType) {
            positionBuilder.setOptionType(optionType);
            return this;
        }
        public PairingRequestBuilder setPositionOptionStrike(String strike) {
            positionBuilder.setOptionStrike(strike);
            return this;
        }
        public PairingRequestBuilder setPositionOptionExpiry(String expiry) {
            positionBuilder.setOptionExpiry(expiry);
            return this;
        }
        public PairingRequestBuilder addPosition() {
        		Position newPosition = positionBuilder.build();
        		Position existPosition = accountPositions.put(newPosition.getSymbol(), newPosition);
        		if (existPosition != null && existPosition.matches(newPosition)) {
        			// combine the two position quantities
        			Integer newQty = newPosition.getQty() + existPosition.getQty();	
        			newPosition = StructureImplFactory.buildPosition(existPosition.getSymbol(), existPosition.getDescription(), 
        					newQty, existPosition.getPrice(), existPosition.getOptionConfig());
        			accountPositions.put(newPosition.getSymbol(), newPosition);
        		}
            return this;
        }
        
        public PairingRequestBuilder setAccountStrategyGroupName(String strategyGroupName) {
            accountBuilder.setStrategyGroupName(strategyGroupName);
            return this;
        }
        public PairingRequestBuilder addAccountCustomProperty(String name, String value) {
            accountBuilder.addAccountProperty(name, value);
            return this;
        }
        
        public PairingRequestBuilder setOrderLegSymbol(String symbol) {
            orderLegBuilder.setSymbol(symbol);
            return this;
        }
        public PairingRequestBuilder setOrderLegDescription(String description) {
            orderLegBuilder.setDescription(description);
            return this;
        }
        public PairingRequestBuilder setOrderLegQty(Integer qty) {
            orderLegBuilder.setQty(qty);
            return this;
        }
        public PairingRequestBuilder setOrderLegPrice(String price) {
            orderLegBuilder.setOrderLegPrice(price);
            return this;
        }
        public PairingRequestBuilder setOrderLegOptionRoot(String optionRoot) {
            orderLegBuilder.setOptionRoot(optionRoot);
            return this;
        }
        public PairingRequestBuilder setOrderLegOptionType(OptionType optionType) {
            orderLegBuilder.setOptionType(optionType);
            return this;
        }
        public PairingRequestBuilder setOrderLegOptionStrike(String strike) {
            orderLegBuilder.setOptionStrike(strike);
            return this;
        }
        public PairingRequestBuilder setOrderLegOptionExpiry(String expiry) {
            orderLegBuilder.setOptionExpiry(expiry);
            return this;
        }
        
        public PairingRequestBuilder setOrderMaintenanceCost(String equityMaintenanceMargin) {
            orderBuilder.setOrderMaintenanceCost(equityMaintenanceMargin);
            return this;
        }
        public PairingRequestBuilder setOrderInitialCost(String equityInitialMargin) {
            orderBuilder.setOrderInitialCost(equityInitialMargin);
            return this;
        }
        public PairingRequestBuilder addOrderLeg() {
            orderBuilder.addOrderLeg(orderLegBuilder.build());
            return this;
        }
        

        public PairingRequestBuilder setOrderId(String orderId) {
            orderBuilder.setOrderId(orderId);
            return this;
        }
        public PairingRequestBuilder setOrderDescription(String orderDescription) {
            orderBuilder.setOrderDescription(orderDescription);
            return this;
        }
        public PairingRequestBuilder addOrder() {
            accountOrders.add(orderBuilder.build());
            return this;
        }
        
        public PairingRequestBuilder addAccount(String accountId) {
            return addAccountInternal(accountId);
        }
        public PairingRequestBuilder addAccount(String accountId, String strategyGroupName) {
            accountBuilder.setStrategyGroupName(strategyGroupName);
            return addAccountInternal(accountId);
        }
        
        protected PairingRequestBuilder addAccountInternal(String accountId) {
            accountBuilder.setAccountId(accountId);
            List<Position> newPositions = new ArrayList<>(accountPositions.values());
            accountBuilder.setAccountPositions(newPositions);
            accountBuilder.setAccountOrders(accountOrders);
            accounts.add(accountBuilder.build());
            accountPositions = new HashMap<>();
            accountOrders = new ArrayList<>();
            return this;
        }
        
        public PairingRequestBuilder addAccount(Account account) {
            accounts.add(account);
            return this;
        }
        
        public PairingRequestBuilder setOptionRootSymbol(String optionRootSymbol) {
            optionRootBuilder.setOptionRootSymbol(optionRootSymbol);
            return this;
        }
        public PairingRequestBuilder setOptionRootExerciseStyle(ExerciseStyle exerciseStyle) {
            optionRootBuilder.setExerciseStyle(exerciseStyle);
            return this;
        }
        public PairingRequestBuilder setOptionRootUnderlyerType(UnderlyerType underlyerType) {
            optionRootBuilder.setUnderlyerType(underlyerType);
            return this;
        }
        /**
         * @deprecated use {@link setOptionRootUnderlyerType}
         */
        public PairingRequestBuilder setOptionRootnderlyerType(UnderlyerType underlyerType) {
            optionRootBuilder.setUnderlyerType(underlyerType);
            return this;
        }
        public PairingRequestBuilder setOptionRootMultiplier(String multiplierString) {
            optionRootBuilder.setMultiplier(multiplierString);
            return this;
        }
        public PairingRequestBuilder setDeliverableSymbol(String symbol) {
            optionRootBuilder.setDeliverableSymbol(symbol);
            return this;
        }
        public PairingRequestBuilder setDeliverableQty(String qty) {
            optionRootBuilder.setDeliverableQty(qty);
            return this;
        }
        public PairingRequestBuilder setDeliverablePrice(String price) {
            optionRootBuilder.setDeliverablePrice(price);
            return this;
        }
        public PairingRequestBuilder setDeliverableType(DeliverableType deliverableType) {
            optionRootBuilder.setDeliverableType(deliverableType);
            return this;
        }
        public PairingRequestBuilder addDeliverable() {
            optionRootBuilder.addDeliverable();
            return this;
        }
        public PairingRequestBuilder addOptionRoot() {
            OptionRoot optionRoot = optionRootBuilder.build();
            optionRoots.put(optionRoot.getOptionRootSymbol(), optionRoot);
            return this;
        } 
        
        public PairingRequestBuilder addOptionRoot(OptionRoot optionRoot) {
            optionRoots.put(optionRoot.getOptionRootSymbol(), optionRoot);
            return this;
        }
        public PairingRequestBuilder setRequestAllStrategyLists(boolean requestAllStrategyLists) {
            this.requestAllStrategyLists = requestAllStrategyLists;
            return this;
        }
        
        public PairingRequest build() {
            for (Account account : accounts) {
                for (CorePosition position : account.getPositions()) {
                    if (position.getOptionConfig() != null) {
                        String optionRootSymbol = position.getOptionConfig().getOptionRoot();
                        OptionRoot root = optionRoots.get(optionRootSymbol);
                        if (root == null) {
                            throw new BuilderException("Option position with no matching root configuration: " + position);
                        }
                    }
                }
            }
            PairingRequest pairingRequest = StructureImplFactory.buildPairingRequest(accounts, optionRoots, requestAllStrategyLists);
            
            positionBuilder = Position.newBuilder();
            optionRootBuilder = OptionRoot.newBuilder();
            accountBuilder = Account.newBuilder();
            orderLegBuilder = OrderLeg.newBuilder();
            orderBuilder = Order.newBuilder();
            accountOrders = new ArrayList<>();
            optionRoots = new HashMap<>();
            accounts = new ArrayList<>();
            
            return pairingRequest;
        }
    }
    public static PairingRequestBuilder newBuilder() {
        return new PairingRequestBuilder();
    }
}