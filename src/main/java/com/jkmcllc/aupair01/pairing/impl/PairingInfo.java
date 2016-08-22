package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
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
    final List<LongCall> longCalls = new ArrayList<>();
    final List<ShortCall> shortCalls = new ArrayList<>();
    final List<LongPut> longPuts = new ArrayList<>();
    final List<ShortPut> shortPuts = new ArrayList<>();
    final List<LongStock> longStocks = new ArrayList<>();
    final List<ShortStock> shortStocks = new ArrayList<>();
    final AccountInfo accountInfo;
    
    private PairingInfo(OptionRoot optionRoot, Account account) {
        this.accountInfo = new AccountInfo(account.getAccountId());
        this.optionRootSymbol = optionRoot.getOptionRootSymbol();
        this.optionRoot = optionRoot;
    };
    
    public static Map<String, PairingInfo> from (Account account, OptionRootStore optionRootStore) {
        Map<String, PairingInfo> pairingInfoMap = new HashMap<>();
        for (Position position : account.getPositions()) {
            OptionConfig optionConfig = position.getOptionConfig();
            Integer qty = position.getQty();
            String symbol = position.getSymbol();
            int sign = Integer.signum(position.getQty());
            if (sign == 0) {
                // throw exception here
            } 
            if (optionConfig != null) {
                String optionRootSymbol = optionConfig.getOptionRoot();
                PairingInfo pairingInfo = pairingInfoMap.get(optionConfig.getOptionRoot());
                OptionRoot optionRoot = optionRootStore.findRoot(optionRootSymbol);
                if (pairingInfo == null) {
                    pairingInfo = new PairingInfo(optionRoot, account);
                    pairingInfoMap.put(optionConfig.getOptionRoot(), pairingInfo);
                }
                if (OptionType.C.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        pairingInfo.longCalls.add(new LongCall(symbol, qty, optionConfig, optionRoot));
                    } else {
                        pairingInfo.shortCalls.add(new ShortCall(symbol, qty, optionConfig, optionRoot));
                    }
                } else if (OptionType.P.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        pairingInfo.longPuts.add(new LongPut(symbol, qty, optionConfig, optionRoot));
                    } else {
                        pairingInfo.shortPuts.add(new ShortPut(symbol, qty, optionConfig, optionRoot));
                    }
                } else {
                    // throw exception here
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
                    pairingInfo.longStocks.add(new LongStock(position.getSymbol(), position.getQty()));
                } else {
                    pairingInfo.shortStocks.add(new ShortStock(position.getSymbol(), position.getQty()));
                }
                
            }
        }
        return pairingInfoMap;
    }
}
