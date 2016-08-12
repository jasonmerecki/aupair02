package com.mereckiconsulting.aupair01.pairing;

import java.util.ArrayList;
import java.util.List;

import com.mereckiconsulting.aupair01.impl.ImplFactory;
import com.mereckiconsulting.aupair01.structure.DeliverableType;
import com.mereckiconsulting.aupair01.structure.ExerciseStyle;
import com.mereckiconsulting.aupair01.structure.Leg;
import com.mereckiconsulting.aupair01.structure.Leg.LegBuilder;
import com.mereckiconsulting.aupair01.structure.OptionRoot;
import com.mereckiconsulting.aupair01.structure.OptionType;
import com.mereckiconsulting.aupair01.structure.UnderlyerType;
import com.mereckiconsulting.aupair01.structure.OptionRoot.OptionRootBuilder;

public interface PairingRequest {
    List<Leg> getLegs();
    List<OptionRoot> getOptionRoots();

    public class PairingRequestBuilder {
        private final List<Leg> legs = new ArrayList<>();
        private final List<OptionRoot> optionRoots = new ArrayList<>();
        private final LegBuilder legBuilder = Leg.newBuilder();
        private final OptionRootBuilder optionRootBuilder = OptionRoot.newBuilder();
        
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
        
        public PairingRequest build() {
            PairingRequest pairingRequest = ImplFactory.buildPairingRequest(legs, optionRoots);
            return pairingRequest;
        }
    }
    public static PairingRequestBuilder newBuilder() {
        return new PairingRequestBuilder();
    }
}