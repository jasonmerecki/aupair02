package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.Deliverables;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.Position;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.UnderlyerType;

public final class StructureImplFactory {
    public static Position buildPosition(String symbol, Integer qty, OptionConfig optionConfig) {
        return new PositionImpl(symbol, qty, optionConfig);
    }
    public static Deliverables buildDeliverables(List<Deliverable> deliverableList) {
        return new DeliverablesImpl(deliverableList);
    }
    public static Deliverable buildDeliverable(String symbol, BigDecimal qty, DeliverableType type) {
        return new DeliverableImpl(symbol, qty, type);
    }
    public static OptionConfig buildOptionConfig(String optionRoot, OptionType optionType, 
            String strike, BigDecimal strikePrice, String expiry, LocalDateTime expiryTime) {
        return new OptionConfigImpl(optionRoot, optionType, strike, strikePrice, expiry, expiryTime);
    }
    public static OptionRoot buildOptionRoot(String optionRootSymbol, ExerciseStyle exerciseStyle, UnderlyerType underlyerType,
            Deliverables deliverables) {
        return new OptionRootImpl(optionRootSymbol, exerciseStyle, underlyerType, deliverables);
    }
    public static Account buildAccount(String accountId, List<Position> legs) {
        return new AccountImpl(accountId, legs);
    }
    
    public static PairingRequest buildPairingRequest(List<Account> accounts, List<OptionRoot> optionRoots) {
        return new PairingRequestImpl(accounts, optionRoots);
    }
}
