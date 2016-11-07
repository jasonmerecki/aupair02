package com.jkmcllc.aupair01.structure.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;
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
    public static Position buildPosition(String symbol, String description, Integer qty, BigDecimal price, OptionConfig optionConfig) {
        return new PositionImpl(symbol, description, qty, price, optionConfig);
    }
    public static Deliverables buildDeliverables(List<Deliverable> deliverableList) {
        return new DeliverablesImpl(deliverableList);
    }
    public static Deliverable buildDeliverable(String symbol, BigDecimal qty, BigDecimal price, DeliverableType type) {
        return new DeliverableImpl(symbol, qty, price, type);
    }
    public static OptionConfig buildOptionConfig(String optionRoot, OptionType optionType, 
            String strike, BigDecimal strikePrice, String expiry, LocalDateTime expiryTimeLocal, Date expiryDate) {
        return new OptionConfigImpl(optionRoot, optionType, strike, strikePrice, expiry, expiryTimeLocal, expiryDate);
    }
    public static OptionRoot buildOptionRoot(String optionRootSymbol, ExerciseStyle exerciseStyle, UnderlyerType underlyerType,
            BigDecimal multiplier, Deliverables deliverables, BigDecimal nakedDeliverablePct, BigDecimal nakedCashPct) {
        return new OptionRootImpl(optionRootSymbol, exerciseStyle, underlyerType, multiplier, deliverables, nakedDeliverablePct, nakedCashPct);
    }
    public static Account buildAccount(String accountId, List<Position> legs, String strategyGroupName, Map<String, String> customProperties) {
        return new AccountImpl(accountId, legs, strategyGroupName, customProperties);
    }
    
    public static PairingRequest buildPairingRequest(List<Account> accounts, Map<String, OptionRoot> optionRoots, boolean requestAllStrategyLists) {
        return new PairingRequestImpl(accounts, optionRoots, requestAllStrategyLists);
    }
    public static AccountPairingResponse buildAccountPairingResponse(Map<String, List<Strategy>> resultMap, Map<String, String> strategyGroupByRoot, Map<String, Map<String, List<Strategy>>> allStrategyListResultMap) {
        return new AccountPairingResponseImpl(resultMap, strategyGroupByRoot, allStrategyListResultMap);
    }
    public static PairingResponse buildPairingResponse(Map<String, AccountPairingResponse> resultMap) {
        return new PairingResponseImpl(resultMap);
    }
}
