package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.Position;

class PairingInfo {
    final String optionRootSymbol;
    final OptionRoot optionRoot;
    final Comparator<AbstractOptionLeg> ASC_DATE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        return o1.getOptionConfig().getExpiryDate().compareTo(o2.getOptionConfig().getExpiryDate());
    };
    final Comparator<AbstractOptionLeg> ASC_STRIKE_ASC_DATE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        return o1.getOptionConfig().getStrike().compareTo(o2.getOptionConfig().getStrike());
    };
    final Comparator<AbstractOptionLeg> DESC_STRIKE_ASC_DATE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        return o2.getOptionConfig().getStrike().compareTo(o1.getOptionConfig().getStrike());
    };
    final List<LongCall> longCalls = new ArrayList<>();
    final List<ShortCall> shortCalls = new ArrayList<>();
    final List<LongPut> longPuts = new ArrayList<>();
    final List<ShortPut> shortPuts = new ArrayList<>();
    final List<LongStock> longStocks = new ArrayList<>();
    final List<ShortStock> shortStocks = new ArrayList<>();
    final List<AbstractOptionLeg> allOptions = new ArrayList<>();
    final AccountInfo accountInfo;
    
    private PairingInfo(OptionRoot optionRoot, Account account) {
        this.accountInfo = new AccountInfo(account.getAccountId());
        this.optionRootSymbol = optionRoot.getOptionRootSymbol();
        this.optionRoot = optionRoot;
    };
    
    static Map<String, PairingInfo> from (Account account, OptionRootStore optionRootStore) {
        Map<String, PairingInfo> pairingInfoMap = new HashMap<>();
        for (Position position : account.getPositions()) {
            OptionConfig optionConfig = position.getOptionConfig();
            Integer qty = position.getQty();
            String symbol = position.getSymbol();
            String description = position.getDescription();
            int sign = Integer.signum(position.getQty());
            if (sign == 0) {
                // TODO: throw exception here, can't have zero position qaty for pairing
            } 
            if (optionConfig != null) {
                String optionRootSymbol = optionConfig.getOptionRoot();
                PairingInfo pairingInfo = pairingInfoMap.get(optionConfig.getOptionRoot());
                OptionRoot optionRoot = optionRootStore.findRoot(optionRootSymbol);
                AbstractOptionLeg optionLeg = null;
                if (pairingInfo == null) {
                    pairingInfo = new PairingInfo(optionRoot, account);
                    pairingInfoMap.put(optionConfig.getOptionRoot(), pairingInfo);
                }
                if (OptionType.C.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        LongCall leg = new LongCall(symbol, description, qty, optionConfig, optionRoot);
                        pairingInfo.longCalls.add(leg);
                        optionLeg = leg;
                    } else {
                        ShortCall leg = new ShortCall(symbol, description, qty, optionConfig, optionRoot);
                        pairingInfo.shortCalls.add(leg);
                        optionLeg = leg;
                    }
                } else if (OptionType.P.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        LongPut leg = new LongPut(symbol, description, qty, optionConfig, optionRoot);
                        pairingInfo.longPuts.add(leg);
                        optionLeg = leg;
                    } else {
                        ShortPut leg = new ShortPut(symbol, description, qty, optionConfig, optionRoot);
                        pairingInfo.shortPuts.add(leg);
                        optionLeg = leg;
                    }
                } else {
                    // throw exception here
                }
                if (optionLeg != null) {
                    pairingInfo.allOptions.add(optionLeg);
                }
            } else {
                // assume it's a stock
                // for POC, assume stock is underlyer symbol too
                String optionRootSymbol = position.getSymbol();
                PairingInfo pairingInfo = pairingInfoMap.get(position.getSymbol());
                if (pairingInfo == null) {
                    OptionRoot optionRoot = optionRootStore.findRoot(optionRootSymbol);
                    pairingInfo = new PairingInfo(optionRoot, account);
                    pairingInfoMap.put(position.getSymbol(), pairingInfo);
                }
                if (sign == 1) {
                    pairingInfo.longStocks.add(new LongStock(position.getSymbol(), description, position.getQty()));
                } else {
                    pairingInfo.shortStocks.add(new ShortStock(position.getSymbol(), description, position.getQty()));
                }
            }
        }
        return pairingInfoMap;
    }
    
    List<? extends Leg> getLegsByType(String type) {
        switch (type) {
        case StrategyConfigs.LONG_CALLS:
            return longCalls;
        case StrategyConfigs.SHORT_CALLS:
            return shortCalls;
        case StrategyConfigs.LONG_PUTS:
            return longPuts;
        case StrategyConfigs.SHORT_PUTS:
            return shortPuts;
        case StrategyConfigs.LONG_STOCKS:
            return longStocks;
        case StrategyConfigs.SHORT_STOCKS:
            return shortStocks;
        }
        // TODO: throw a meaningful exception here
        return null;
    }
}
