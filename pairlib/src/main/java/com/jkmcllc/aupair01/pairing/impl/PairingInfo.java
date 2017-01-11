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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.jkmcllc.aupair01.store.Constants;
import com.jkmcllc.aupair01.store.OptionRootStore;
import com.jkmcllc.aupair01.structure.Account;
import com.jkmcllc.aupair01.structure.CorePosition;
import com.jkmcllc.aupair01.structure.CorePosition.CorePositionType;
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
    final ConcurrentMap<String, AbstractLeg> allLegs = new ConcurrentHashMap<>();
    final List<OrderPairingResultImpl> orderPairings = new CopyOnWriteArrayList<>();
    final AccountInfo accountInfo;
    
    private PairingInfo(OptionRoot optionRoot, Account account) {
        this.accountInfo = new AccountInfo(account.getAccountId());
        this.optionRoot = optionRoot;
    };
    
    static Map<String, PairingInfo> from (Account account, OptionRootStore optionRootStore) {
        Map<String, PairingInfo> pairingInfoMap = loadPositions(account, optionRootStore);
        return pairingInfoMap;
    }
    
    private static Map<String, PairingInfo> loadPositions (Account account, OptionRootStore optionRootStore) {
        ConcurrentMap<String, PairingInfo> pairingInfoMap = new ConcurrentHashMap<>();
        CorePosLoader cpl = new CorePosLoader(pairingInfoMap, account, optionRootStore);
        
        account.getPositions().parallelStream().forEach(cpl);
        
        // Order sorting!
        // step 1:
        // forEach order, collect into a map using collect(Collectors.toMap( or toConcurrentMap
        // where the Key is OrderPairingResultImpl and the values are the List<Leg> of legs
        Map<OrderPairingResultImpl, List<AbstractLeg>> orderLegMap = account.getOrders().parallelStream()
                .collect(Collectors.toConcurrentMap(
                ord -> {
                    return new OrderPairingResultImpl(ord.getOrderId(), ord.getOrderDescription());
                }, 
                ord -> {
                    List<AbstractLeg> legs = ord.getOrderLegs().stream().map(orderLeg -> {
                            AbstractLeg newLeg = createLeg(orderLeg, optionRootStore, true);
                            return newLeg;
                        })
                    .collect(Collectors.toList());
                    return legs;
                }));
        
        // step 2:
        // with the resulting map, the entries can be streamed,
        // and be mapped so that the key OrderPairingResultImpl will grab and set its own List<Legs>
        // and then collected into a set of OrderPairingResultImpl objects
        Set<OrderPairingResultImpl> orderInfoSet = orderLegMap.entrySet().parallelStream().map(entry -> {
            OrderPairingResultImpl orderpr = entry.getKey();
            List<AbstractLeg> legs = entry.getValue();
            orderpr.addOrderLegs(legs);
            return orderpr;
        }).collect(Collectors.toSet());

        // step 3:
        // the resulting set will be parallelStream and each OrderPairingResultImpl
        // will be assigned to the appropriate mapped PairingInfo (possibly more than one depending on the legs)
        orderInfoSet.parallelStream().forEach(ordInfo -> {
            if (ordInfo.getStockLegsCount() != 0 && ordInfo.getOptionLegsCount() == 0) {
                // only stock legs, add this to the 
                StockLeg stockLeg = (StockLeg) ordInfo.getOrderLegs().stream().findFirst().get();
                Set<OptionRoot> optionRoots = optionRootStore.findRootsByDeliverableSymbol(stockLeg.getSymbol());
                for (OptionRoot optionRoot : optionRoots) {
                    PairingInfo pairingInfo = cpl.findInfo(optionRoot);
                    // PairingInfo might be null, odd deliverables
                    if (pairingInfo != null) {
                        pairingInfo.orderPairings.add(ordInfo);
                    }
                }
            } else if (ordInfo.getOptionLegsCount() != 0) {
                // unless the order has zero legs, it must have some options in it, 
                // therefore the order will be associated with the pairing info for the option root
                OptionLeg optionLeg = (OptionLeg) ordInfo.getOrderLegs().stream()
                        .filter(l -> AbstractLeg.STOCKOPTION.equals(l.getType())).findFirst().get();
                OptionRoot optionRoot = optionLeg.getOptionRoot();
                PairingInfo pairingInfo = cpl.findInfo(optionRoot);
                pairingInfo.orderPairings.add(ordInfo);
            }
        });
        
        // need to enforce that an order with option legs cannot mix option roots?
        
        pairingInfoMap.values().forEach(p -> p.reset(false));
        return pairingInfoMap;
    }
    
    private static AbstractLeg createLeg(CorePosition position, OptionRootStore optionRootStore, boolean isOrder) {
        int sign = Integer.signum(position.getQty());
        if (sign == 0) {
            // TODO: throw exception here, can't have zero position quantity for pairing
            return null;
        } 
        Integer qty = position.getQty(), positionResetQty = position.getQty();
        if (isOrder) {
            positionResetQty = Constants.ZERO;
        }
        String symbol = position.getSymbol();
        String description = position.getDescription();
        BigDecimal price = position.getPrice();
        AbstractLeg newLeg = null;
        
        OptionConfig optionConfig = position.getOptionConfig();
        if (optionConfig != null) {
            String optionRootSymbol = optionConfig.getOptionRoot();
            OptionRoot optionRoot = optionRootStore.findRootByRootSymbol(optionRootSymbol);
            OptionType optionType = optionConfig.getOptionType();
            OptionLeg leg = new OptionLeg(symbol, description, qty, positionResetQty, price, optionType, optionConfig, optionRoot);
            newLeg = leg;
        } else if (optionConfig == null) {
            // assume it's a stock
            StockLeg leg = new StockLeg(position.getSymbol(), description, qty, positionResetQty, price);
            newLeg = leg;
        }
        return newLeg;
    }
    
    /*
     * Used to load multiple unique positions in parallel. 
     * Assumes positions are unique; behavior is undefined if duplicate positions (including
     * a position and an order leg for the same symbol) are loaded in parallel.
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
            String positionSymbol = position.getSymbol();
            boolean isOrder = (CorePositionType.ORDERLEG == position.getCorePositionType());
            if (optionConfig != null) {
                AbstractOptionLeg newLeg = (AbstractOptionLeg) createLeg(position, optionRootStore, isOrder);
                OptionRoot optionRoot = newLeg.getOptionRoot();
                PairingInfo pairingInfo = findInfo(optionRoot);
                pairingInfo.allLegs.put(positionSymbol, newLeg);
            } else {
                // assume it's a stock
                Set<OptionRoot> optionRoots = optionRootStore.findRootsByDeliverableSymbol(positionSymbol);
                AbstractLeg newLeg = createLeg(position, null, isOrder);
                for (OptionRoot optionRoot : optionRoots) {
                    PairingInfo pairingInfo = findInfo(optionRoot);
                    // PairingInfo might be null, odd deliverables
                    if (pairingInfo != null) {
                        pairingInfo.allLegs.put(positionSymbol, newLeg);
                    }
                }
            }
        }

    }
    
    private void initDeliverables() {
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
    
    void reset(boolean hardReset) {
        resetLegs(allLegs.values(), hardReset);
        longDeliverables = null;
        shortDeliverables = null;
        initDeliverables();
    }
    
    private void resetLegs(Collection<? extends AbstractLeg> legs, boolean hardReset) {
        longCalls.clear();
        shortCalls.clear();
        longPuts.clear();
        shortPuts.clear();
        longStocks.clear();
        shortStocks.clear();
        legs.parallelStream().forEach(leg -> {
            leg.resetQty(hardReset);
            sortingHat(leg);
        });
    }
    
    private void sortingHat(AbstractLeg newLeg) {
        int sign = Integer.signum(newLeg.getQty());
        if (sign == 0) {
            return;
        } 
        if (AbstractLeg.STOCKOPTION.equals(newLeg.getType())) {
            OptionLeg newOptionLeg = (OptionLeg) newLeg;
            if (OptionType.C.equals(newOptionLeg.getOptionType())) {
                if (sign == 1) {
                    this.longCalls.add(newOptionLeg);
                } else {
                    this.shortCalls.add(newOptionLeg);
                }
            } else if (OptionType.P.equals(newOptionLeg.getOptionType())) {
                if (sign == 1) {
                    this.longPuts.add(newOptionLeg);
                 } else {
                     this.shortPuts.add(newOptionLeg);
                }
            } else {
                // throw exception here
            }
        } else if (AbstractLeg.STOCK.equals(newLeg.getType()))  {
            StockLeg newStockLeg = (StockLeg) newLeg;
            if (sign == 1) {
                this.longStocks.add(newStockLeg);
            } else {
                this.shortStocks.add(newStockLeg);
            }
        }
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
