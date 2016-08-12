package com.jkmcllc.aupair01.structure;

import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface Account {
    public List<Leg> getLegs();
    public String getAccountId();
    
    public class AccountBuilder {
        private List<Leg> legs = new ArrayList<>();
        private String accountId;
        private AccountBuilder() {};
        public AccountBuilder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }
        public AccountBuilder setAccountLegs(List<Leg> legs) {
            this.legs = legs;
            return this;
        }
        public Account build() {
            if (legs == null || legs.isEmpty() || accountId == null) {
                List<String> missing = new ArrayList<>();
                StringBuilder err = new StringBuilder("Cannot build Account, missing data: ");
                if (legs == null) {
                    missing.add("legs");
                }
                if (accountId == null) {
                    missing.add("accountId");
                }
                err.append(missing);
                throw new BuilderException(err.toString());
            }
            Account account = StructureImplFactory.buildAccount(accountId, legs);
            accountId = null;
            legs = new ArrayList<>();
            return account;
        }
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("AccountBuilder {legs:");
            builder.append(legs);
            builder.append(", accountId:");
            builder.append(accountId);
            builder.append("}");
            return builder.toString();
        }
    }
    public static AccountBuilder newBuilder() {
        return new AccountBuilder();
    }
    
}