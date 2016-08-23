package com.jkmcllc.aupair01.pairing;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.jkmcllc.aupair01.structure.UnderlyerType;
import com.jkmcllc.aupair01.structure.Account.AccountBuilder;
import com.jkmcllc.aupair01.structure.Position.PositionBuilder;
import com.jkmcllc.aupair01.structure.OptionRoot.OptionRootBuilder;
import com.jkmcllc.aupair01.structure.impl.StructureImplFactory;

public interface PairingRequest extends Request {
    public static final String NAME = "PairingRequest";
    default String requestType() {return NAME;}
    List<Account> getAccounts();
    Map<String, OptionRoot> getOptionRoots();

    public class PairingRequestBuilder {
        private final List<Account> accounts = new ArrayList<>();
        private List<Position> legs = new ArrayList<>();
        private final Map<String, OptionRoot> optionRoots = new HashMap<>();
        private final PositionBuilder positionBuilder = Position.newBuilder();
        private final OptionRootBuilder optionRootBuilder = OptionRoot.newBuilder();
        private final AccountBuilder accountBuilder = Account.newBuilder();
        
        private PairingRequestBuilder() {}
        
        public PairingRequestBuilder setPositionSymbol(String symbol) {
            positionBuilder.setSymbol(symbol);
            return this;
        }
        public PairingRequestBuilder setPositionQty(Integer qty) {
            positionBuilder.setQty(qty);
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
            legs.add(positionBuilder.build());
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
        
        public PairingRequest build() {
            for (Account account : accounts) {
                for (Position position : account.getPositions()) {
                    if (position.getOptionConfig() != null) {
                        String optionRootSymbol = position.getOptionConfig().getOptionRoot();
                        OptionRoot root = optionRoots.get(optionRootSymbol);
                        if (root == null) {
                            throw new BuilderException("Option position with no matching root configuration: " + position);
                        }
                    }
                }
            }
            PairingRequest pairingRequest = StructureImplFactory.buildPairingRequest(accounts, optionRoots);
            return pairingRequest;
        }
    }
    public static PairingRequestBuilder newBuilder() {
        return new PairingRequestBuilder();
    }
}