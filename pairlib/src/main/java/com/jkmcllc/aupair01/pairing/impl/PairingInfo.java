package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.Position;

class PairingInfo {
    private static final Comparator<AbstractOptionLeg> ASC_DATE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        return o1.getOptionConfig().getExpiryDate().compareTo(o2.getOptionConfig().getExpiryDate());
    };
    private static final Comparator<AbstractOptionLeg> ASC_STRIKE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        return o1.getOptionConfig().getStrikePrice().compareTo(o2.getOptionConfig().getStrikePrice());
    };
    private static final Comparator<AbstractOptionLeg> DESC_STRIKE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        return -(o1.getOptionConfig().getStrikePrice().compareTo(o2.getOptionConfig().getStrikePrice()));
    };
    private static final Comparator<AbstractOptionLeg> ASC_STRIKE_ASC_DATE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        int s1 = ASC_STRIKE.compare(o1, o2);
        if (s1 != 0) return s1;
        return ASC_DATE.compare(o1, o2);
    };
    private static final Comparator<AbstractOptionLeg> DESC_STRIKE_ASC_DATE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        int s1 = DESC_STRIKE.compare(o1, o2);
        if (s1 != 0) return s1;
        return ASC_DATE.compare(o1, o2);
    };
    private static final Comparator<AbstractOptionLeg> ASC_DATE_ASC_STRIKE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        int s1 = ASC_DATE.compare(o1, o2);
        if (s1 != 0) return s1;
        return ASC_STRIKE.compare(o1, o2);
    };
    private static final Comparator<AbstractOptionLeg> ASC_DATE_DESC_STRIKE = (AbstractOptionLeg o1, AbstractOptionLeg o2)-> {
        int s1 = ASC_DATE.compare(o1, o2);
        if (s1 != 0) return s1;
        return DESC_STRIKE.compare(o1, o2);
    };
    final OptionRoot optionRoot;
    final List<LongCall> longCalls = new ArrayList<>();
    final List<ShortCall> shortCalls = new ArrayList<>();
    final List<LongPut> longPuts = new ArrayList<>();
    final List<ShortPut> shortPuts = new ArrayList<>();
    private final List<LongStock> longStocks = new ArrayList<>();
    private final List<ShortStock> shortStocks = new ArrayList<>();
    private List<AbstractDeliverableLeg> longDeliverables = null;
    private List<AbstractDeliverableLeg> shortDeliverables = null;
    final List<AbstractOptionLeg> allOptions = new ArrayList<>();
    final AccountInfo accountInfo;
    
    private PairingInfo(OptionRoot optionRoot, Account account) {
        this.accountInfo = new AccountInfo(account.getAccountId());
        this.optionRoot = optionRoot;
    };
    
    static Map<String, PairingInfo> from (Account account, OptionRootStore optionRootStore) {
        Map<String, PairingInfo> pairingInfoMap = new HashMap<>();
        for (Position position : account.getPositions()) {
            OptionConfig optionConfig = position.getOptionConfig();
            Integer qty = position.getQty();
            String symbol = position.getSymbol();
            String description = position.getDescription();
            BigDecimal price = position.getPrice();
            int sign = Integer.signum(position.getQty());
            if (sign == 0) {
                // TODO: throw exception here, can't have zero position qaty for pairing
            } 
            if (optionConfig != null) {
                String optionRootSymbol = optionConfig.getOptionRoot();
                PairingInfo pairingInfo = pairingInfoMap.get(optionConfig.getOptionRoot());
                OptionRoot optionRoot = optionRootStore.findRootByRootSymbol(optionRootSymbol);
                AbstractOptionLeg optionLeg = null;
                if (pairingInfo == null) {
                    pairingInfo = new PairingInfo(optionRoot, account);
                    pairingInfoMap.put(optionConfig.getOptionRoot(), pairingInfo);
                }
                if (OptionType.C.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        LongCall leg = new LongCall(symbol, description, qty, price, optionConfig, optionRoot);
                        pairingInfo.longCalls.add(leg);
                        optionLeg = leg;
                    } else {
                        ShortCall leg = new ShortCall(symbol, description, qty, price, optionConfig, optionRoot);
                        pairingInfo.shortCalls.add(leg);
                        optionLeg = leg;
                    }
                } else if (OptionType.P.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        LongPut leg = new LongPut(symbol, description, qty, price, optionConfig, optionRoot);
                        pairingInfo.longPuts.add(leg);
                        optionLeg = leg;
                    } else {
                        ShortPut leg = new ShortPut(symbol, description, qty, price, optionConfig, optionRoot);
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
                String positionSymbol = position.getSymbol();
                Set<OptionRoot> optionRoots = optionRootStore.findRootsByDeliverableSymbol(positionSymbol);
                for (OptionRoot optionRoot : optionRoots) {
                    String optionRootSymbol = optionRoot.getOptionRootSymbol();
                    PairingInfo pairingInfo = pairingInfoMap.get(optionRootSymbol);
                    if (pairingInfo == null) {
                        pairingInfo = new PairingInfo(optionRoot, account);
                        pairingInfoMap.put(optionRootSymbol, pairingInfo);
                    }
                    if (sign == 1) {
                        pairingInfo.longStocks.add(new LongStock(position.getSymbol(), description, position.getQty(), price));
                    } else {
                        pairingInfo.shortStocks.add(new ShortStock(position.getSymbol(), description, position.getQty(), price));
                    }
                }

            }
        }
        return pairingInfoMap;
    }
    
    List<? extends Leg> getLongDeliverables() {
        if (longDeliverables == null) {
            longDeliverables = Collections.singletonList(AbstractDeliverableLeg.from(longStocks, optionRoot));
        }
        return longDeliverables;
    }
    
    List<? extends Leg> getShortDeliverables() {
        if (shortDeliverables == null) {
            shortDeliverables = Collections.singletonList(AbstractDeliverableLeg.from(shortStocks, optionRoot));
        }
        return shortDeliverables;
    }
    
    void sortNarrowStrike() {
        Collections.sort(longCalls, ASC_STRIKE_ASC_DATE);
        Collections.sort(shortCalls, ASC_STRIKE_ASC_DATE);
        Collections.sort(longPuts, DESC_STRIKE_ASC_DATE);
        Collections.sort(shortPuts, DESC_STRIKE_ASC_DATE); 
    }
    
    void sortWideStrike() {
        Collections.sort(longCalls, ASC_STRIKE_ASC_DATE);
        Collections.sort(shortCalls, DESC_STRIKE_ASC_DATE);
        Collections.sort(longPuts, DESC_STRIKE_ASC_DATE);
        Collections.sort(shortPuts, ASC_STRIKE_ASC_DATE); 
    }
    
    void sort3() {
        Collections.sort(longCalls, ASC_DATE_ASC_STRIKE);
        Collections.sort(shortCalls, ASC_DATE_ASC_STRIKE);
        Collections.sort(longPuts, ASC_DATE_DESC_STRIKE);
        Collections.sort(shortPuts, ASC_DATE_DESC_STRIKE); 
    }
    
    void sortBy(String sortKey) {
        switch (sortKey) {
        case StrategyConfigs.WIDE_STRIKE:
            sortWideStrike();
            break;
        case StrategyConfigs.NARROW_STRIKE:
            sortNarrowStrike();
            break;
        }
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
        case StrategyConfigs.LONG_DELIVERABLES:
            return getLongDeliverables();
        case StrategyConfigs.SHORT_DELIVERABLES:
            return getShortDeliverables();
        }
        // TODO: throw a meaningful exception here
        return null;
    }
}
