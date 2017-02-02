package com.jkmcllc.aupair01.pairing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jkmcllc.aupair01.exception.BuilderException;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.CorePosition;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.Order;
import com.jkmcllc.aupair01.structure.OrderLeg;
import com.jkmcllc.aupair01.structure.Position;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface AccountPairingRequest extends PairingRequest {
    public static final String NAME = "AccountPairingRequest";
    default String requestType() {return NAME;}
    Account getAccount();
    Map<String, OptionRoot> getOptionRoots();
    boolean isRequestAllStrategyLists();
    
    public class AccountPairingRequestBuilder extends PairingRequestBuilder {
        protected AccountPairingRequestBuilder() {}
        public PairingRequestBuilder addAccount(String accountId) {
            throw new BuilderException("Cannot add multiple accounts in AccountPairingRequestBuilder: " + accountId);
        }
        public PairingRequest build() {
            throw new BuilderException("Must build with a single account ID for AccountPairingRequestBuilder ");
        }
        public AccountPairingRequest build(String accountId) {
            super.addAccountInternal(accountId);
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
            AccountPairingRequest pairingRequest = StructureImplFactory.buildAccountPairingRequest(accounts, optionRoots, requestAllStrategyLists);
            
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
    
    public static AccountPairingRequestBuilder newBuilder() {
        return new AccountPairingRequestBuilder();
    }
    
}
