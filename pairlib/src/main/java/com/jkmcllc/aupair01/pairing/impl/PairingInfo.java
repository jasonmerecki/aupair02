package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.CorePosition;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;
import com.jkmcllc.aupair01.structure.OptionType;

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
    final List<OptionLeg> longCalls = new CopyOnWriteArrayList<>();
    final List<OptionLeg> shortCalls = new CopyOnWriteArrayList<>();
    final List<OptionLeg> longPuts = new CopyOnWriteArrayList<>();
    final List<OptionLeg> shortPuts = new CopyOnWriteArrayList<>();
    private final List<StockLeg> longStocks = new CopyOnWriteArrayList<>();
    private final List<StockLeg> shortStocks = new CopyOnWriteArrayList<>();
    private List<AbstractDeliverableLeg> longDeliverables = null;
    private List<AbstractDeliverableLeg> shortDeliverables = null;
    final Set<AbstractOptionLeg> allOptions = new CopyOnWriteArraySet<>();
    final ConcurrentMap<String, AbstractLeg> allLegs = new ConcurrentHashMap<>();
    final AccountInfo accountInfo;
    
    private PairingInfo(OptionRoot optionRoot, Account account) {
        this.accountInfo = new AccountInfo(account.getAccountId());
        this.optionRoot = optionRoot;
    };
    
    static Map<String, PairingInfo> from (Account account, OptionRootStore optionRootStore) {
        Map<String, PairingInfo> pairingInfoMap = loadPositionsAndOrders(account, optionRootStore);
        return pairingInfoMap;
    }
    private static Map<String, PairingInfo> loadPositionsAndOrders (Account account, OptionRootStore optionRootStore) {
        ConcurrentMap<String, PairingInfo> pairingInfoMap = new ConcurrentHashMap<>();
        CorePosLoader cpl = new CorePosLoader(pairingInfoMap, account, optionRootStore);
        /* 
        for (CorePosition pos : account.getPositions()) {
            cpl.accept(pos);
        }
        */
        
        account.getPositions().parallelStream().forEach(cpl);
        // account.getOrders().parallelStream().forEach(order -> order.getOrderLegs().parallelStream().forEach(cpl));
        pairingInfoMap.values().forEach(p -> p.init());
        return pairingInfoMap;
    }
    
    /*
     * Used to load multiple unique positions in parallel. 
     * Assumes positions are unique; behavior is undefined if duplicate positions (including
     * a position and an order leg for theh same symbol) are loaded in parallel.
     */
    private static class CorePosLoader implements Consumer<CorePosition> {
        private final ConcurrentMap<String, PairingInfo> pairingInfoMap;
        private final Account account;
        private final OptionRootStore optionRootStore;
        
        private CorePosLoader(ConcurrentMap<String, PairingInfo> pairingInfoMap, Account account,
                OptionRootStore optionRootStore) {
            this.pairingInfoMap = pairingInfoMap;
            this.account = account;
            this.optionRootStore = optionRootStore;
        }
        private PairingInfo findInfo(OptionRoot optionRoot) {
            String optionRootSymbol = optionRoot.getOptionRootSymbol();
            PairingInfo pairingInfo = pairingInfoMap.get(optionRootSymbol);
            if (pairingInfo == null) {
                pairingInfo = new PairingInfo(optionRoot, account);
                PairingInfo newPairingInfo = pairingInfoMap.putIfAbsent(optionRootSymbol, pairingInfo);
                pairingInfo = newPairingInfo != null ? newPairingInfo : pairingInfo;
            }
            return pairingInfo;
        }
        @Override
        public void accept(CorePosition position) {
            OptionConfig optionConfig = position.getOptionConfig();
            Integer qty = position.getQty();
            String symbol = position.getSymbol();
            String description = position.getDescription();
            BigDecimal price = position.getPrice();
            int sign = Integer.signum(position.getQty());
            if (sign == 0) {
                // TODO: throw exception here, can't have zero position quantity for pairing
                return;
            } 
            if (optionConfig != null) {
                String optionRootSymbol = optionConfig.getOptionRoot();
                OptionType optionType = optionConfig.getOptionType();
                OptionRoot optionRoot = optionRootStore.findRootByRootSymbol(optionRootSymbol);
                PairingInfo pairingInfo = findInfo(optionRoot);
                AbstractOptionLeg optionLeg = null;
                if (OptionType.C.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        OptionLeg leg = new OptionLeg(symbol, description, qty, price, optionType, optionConfig, optionRoot);
                        pairingInfo.longCalls.add(leg);
                        optionLeg = leg;
                    } else {
                        OptionLeg leg = new OptionLeg(symbol, description, qty, price, optionType, optionConfig, optionRoot);
                        pairingInfo.shortCalls.add(leg);
                        optionLeg = leg;
                    }
                } else if (OptionType.P.equals(optionConfig.getOptionType())) {
                    if (sign == 1) {
                        OptionLeg leg = new OptionLeg(symbol, description, qty, price, optionType, optionConfig, optionRoot);
                        pairingInfo.longPuts.add(leg);
                        optionLeg = leg;
                    } else {
                        OptionLeg leg = new OptionLeg(symbol, description, qty, price, optionType, optionConfig, optionRoot);
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
                    PairingInfo pairingInfo = findInfo(optionRoot);
                    if (sign == 1) {
                        pairingInfo.longStocks.add(new StockLeg(position.getSymbol(), description, position.getQty(), price));
                    } else {
                        pairingInfo.shortStocks.add(new StockLeg(position.getSymbol(), description, position.getQty(), price));
                    }
                }
            }
            
        }
    }
    
    private void init() {
        if (longDeliverables == null) {
            AbstractDeliverableLeg longDeliverableLeg = AbstractDeliverableLeg.from(longStocks, optionRoot);
            if (longDeliverableLeg != null) {
                longDeliverables = Collections.singletonList(AbstractDeliverableLeg.from(longStocks, optionRoot));
            } else {
                longDeliverables = Collections.emptyList();
            }
        }
        if (shortDeliverables == null) {
            AbstractDeliverableLeg shortDeliverableLeg = AbstractDeliverableLeg.from(shortStocks, optionRoot);
            if (shortDeliverableLeg != null) {
                shortDeliverables = Collections.singletonList(AbstractDeliverableLeg.from(shortStocks, optionRoot));
            } else {
                shortDeliverables = Collections.emptyList();
            }
        }
    }
    
    void reset() {
        resetLegs(longCalls);
        resetLegs(shortCalls);
        resetLegs(longPuts);
        resetLegs(shortPuts);
        resetLegs(longStocks);
        resetLegs(shortStocks);
        longDeliverables = null;
        shortDeliverables = null;
        init();
    }
    
    private void resetLegs(Collection<? extends AbstractLeg> legs) {
        legs.parallelStream().forEach(leg -> {
            leg.resetQty();
        });
    }
    
    List<? extends Leg> getLongDeliverables() {
        return longDeliverables;
    }
    
    List<? extends Leg> getShortDeliverables() {
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
