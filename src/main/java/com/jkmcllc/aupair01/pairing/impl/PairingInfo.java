package com.jkmcllc.aupair01.pairing.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.Position;

class PairingInfo {
    String optionRoot;
    List<LongCall> longCalls = new ArrayList<>();
    List<ShortCall> shortCalls = new ArrayList<>();
    List<LongPut> longPuts = new ArrayList<>();
    List<ShortPut> shortPuts = new ArrayList<>();
    List<LongStock> longStocks = new ArrayList<>();
    List<ShortStock> shortStocks = new ArrayList<>();
    AccountInfo accountInfo;
    
    private PairingInfo() {};
    
    public static Map<String, PairingInfo> from (Account account) {
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
                PairingInfo pairingInfo = pairingInfoMap.get(optionConfig.getOptionRoot());
                if (pairingInfo == null) {
                    pairingInfo = new PairingInfo();
                    pairingInfo.accountInfo = new AccountInfo(account.getAccountId());
                    pairingInfoMap.put(optionConfig.getOptionRoot(), pairingInfo);
                }
                if (OptionType.C.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        pairingInfo.longCalls.add(new LongCall(symbol, qty, optionConfig));
                    } else {
                        pairingInfo.shortCalls.add(new ShortCall(symbol, qty, optionConfig));
                    }
                } else if (OptionType.P.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        pairingInfo.longPuts.add(new LongPut(symbol, qty, optionConfig));
                    } else {
                        pairingInfo.shortPuts.add(new ShortPut(symbol, qty, optionConfig));
                    }
                } else {
                    // throw exception here
                }
            } else {
                // assume it's a stock
                // for POC, assume stock is underlyer symbol too
                PairingInfo pairingInfo = pairingInfoMap.get(position.getSymbol());
                if (pairingInfo == null) {
                    pairingInfo = new PairingInfo();
                    pairingInfo.accountInfo = new AccountInfo(account.getAccountId());
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
