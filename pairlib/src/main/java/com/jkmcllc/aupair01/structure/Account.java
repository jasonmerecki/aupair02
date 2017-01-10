package com.jkmcllc.aupair01.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface Account {
    public List<Position> getPositions();
    public List<Order> getOrders();
    public String getAccountId();
    public Map<String, String> getCustomProperties();
    public String getStrategyGroupName();
    
    public class AccountBuilder {
        private List<Position> legs;
        private List<Order> orders;
        private String accountId;
        private Map<String, String> customProperties = new HashMap<String, String>();
        private String strategyGroupName;
        private AccountBuilder() {};
        public AccountBuilder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }
        public AccountBuilder setAccountPositions(List<Position> legs) {
            this.legs = legs;
            String hasDupe = CorePosition.findDuplicate(legs);
            if (hasDupe != null) {
                throw new BuilderException("Invalid account configuration, has duplicate position information: " + hasDupe);
            }
            return this;
        }
        public AccountBuilder setAccountOrders(List<Order> orders) {
            this.orders = orders;
            return this;
        }
        public AccountBuilder setStrategyGroupName(String strategyGroupName) {
            this.strategyGroupName = strategyGroupName;
            return this;
        }
        public AccountBuilder addAccountProperty(String name, String value) {
            customProperties.put(name, value);
            return this;
        }
        public Account build() {
            if (legs == null) {
                legs = Collections.emptyList();
            }
            if (accountId == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Account, missing data: ");
                if (accountId == null) {
                    missing.add("accountId");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            Account account = StructureImplFactory.buildAccount(accountId, legs, orders, strategyGroupName, customProperties);
            accountId = null;
            legs = new ArrayList<>();
            orders = new ArrayList<>();
            customProperties = new HashMap<String, String>();
            strategyGroupName = null;
            return account;
        }
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("AccountBuilder {legs: ");
            builder.append(legs);
            builder.append(", accountId: ");
            builder.append(accountId);
            builder.append(", strategyGroupName: ");
            builder.append(strategyGroupName);
            builder.append("}");
            return builder.toString();
        }
    }
    public static AccountBuilder newBuilder() {
        return new AccountBuilder();
    }
    
}