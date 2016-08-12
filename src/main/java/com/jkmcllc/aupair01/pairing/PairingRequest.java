package com.jkmcllc.aupair01.pairing;

import java.util.ArrayList;
import java.util.List;

import com.jkmcllc.aupair01.connect.Request;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.Leg;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.UnderlyerType;
import com.jkmcllc.aupair01.structure.Account.AccountBuilder;
import com.jkmcllc.aupair01.structure.Leg.LegBuilder;
import com.jkmcllc.aupair01.structure.OptionRoot.OptionRootBuilder;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface PairingRequest extends Request {
    public static final String NAME = "PairingRequest";
    default String requestType() {return NAME;}
    List<Account> getAccounts();
    List<OptionRoot> getOptionRoots();

    public class PairingRequestBuilder {
        private final List<Account> accounts = new ArrayList<>();
        private List<Leg> legs = new ArrayList<>();
        private final List<OptionRoot> optionRoots = new ArrayList<>();
        private final LegBuilder legBuilder = Leg.newBuilder();
        private final OptionRootBuilder optionRootBuilder = OptionRoot.newBuilder();
        private final AccountBuilder accountBuilder = Account.newBuilder();
        
        private PairingRequestBuilder() {}
        
        public PairingRequestBuilder setLegSymbol(String symbol) {
            legBuilder.setSymbol(symbol);
            return this;
        }
        public PairingRequestBuilder setLegQty(Integer qty) {
            legBuilder.setQty(qty);
            return this;
        }
        public PairingRequestBuilder setLegOptionRoot(String optionRoot) {
            legBuilder.setOptionRoot(optionRoot);
            return this;
        }
        public PairingRequestBuilder setLegOptionType(OptionType optionType) {
            legBuilder.setOptionType(optionType);
            return this;
        }
        public PairingRequestBuilder setLegOptionStrike(String strike) {
            legBuilder.setOptionStrike(strike);
            return this;
        }
        public PairingRequestBuilder setLegOptionExpiry(String expiry) {
            legBuilder.setOptionExpiry(expiry);
            return this;
        }
        public PairingRequestBuilder addLeg() {
            legs.add(legBuilder.build());
            return this;
        }
        
        public PairingRequestBuilder addAccount(String accountId) {
            accountBuilder.setAccountId(accountId);
            accountBuilder.setAccountLegs(legs);
            accounts.add(accountBuilder.build());
            legs = new ArrayList<>();
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
        public PairingRequestBuilder setOptionRootnderlyerType(UnderlyerType underlyerType) {
            optionRootBuilder.setUnderlyerType(underlyerType);
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
        public PairingRequestBuilder setDeliverableType(DeliverableType deliverableType) {
            optionRootBuilder.setDeliverableType(deliverableType);
            return this;
        }
        public PairingRequestBuilder addDeliverable() {
            optionRootBuilder.addDeliverable();
            return this;
        }
        public PairingRequestBuilder addOptionRoot() {
            optionRoots.add(optionRootBuilder.build());
            return this;
        } 
        
        public PairingRequestBuilder addOptionRoot(OptionRoot optionRoot) {
            optionRoots.add(optionRoot);
            return this;
        }
        
        public PairingRequest build() {
            PairingRequest pairingRequest = StructureImplFactory.buildPairingRequest(accounts, optionRoots);
            return pairingRequest;
        }
    }
    public static PairingRequestBuilder newBuilder() {
        return new PairingRequestBuilder();
    }
}