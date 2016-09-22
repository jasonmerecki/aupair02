package com.jkmcllc.aupair01.store;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.Deliverables;
import com.jkmcllc.aupair01.structure.OptionRoot;

public class OptionRootStore {
    // internal lookup maps
    private ConcurrentMap<String, Set<OptionRoot>> optionRootByDeliverableMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, OptionRoot> optionRootMap = new ConcurrentHashMap<>();
    private static OptionRootStore optionRootStore;
    private static boolean usePermStore = false;
    private OptionRootStore() {};
    private static OptionRootStore getPermInstance() {
        if (optionRootStore == null) {
            synchronized (OptionRootStore.class) {
                if (optionRootStore == null) {
                    optionRootStore = new OptionRootStore();
                }
            }
        }
        return optionRootStore;
    }
    public static OptionRootStore getInstance() {
        if (usePermStore) {
            return getPermInstance();
        }
        OptionRootStore optionRootStore = new OptionRootStore();
        return optionRootStore;
    }
    public OptionRoot findRootByRootSymbol(String optionRootSymbol) {
        if (usePermStore) {
            return getPermInstance().optionRootMap.get(optionRootSymbol);
        }
        return optionRootMap.get(optionRootSymbol);
    }
    public Set<OptionRoot> findRootsByDeliverableSymbol(String deliverableSymbol) {
        Set<OptionRoot> rootSet = optionRootByDeliverableMap.get(deliverableSymbol);
        return rootSet != null ? rootSet : Collections.emptySet();
    }
    public void addRoot(OptionRoot optionRoot) {
        OptionRootStore store = null;
        if (usePermStore) {
            store = getPermInstance();
        } else {
            store = this; 
        }
        Deliverables deliverables = optionRoot.getDeliverables();
        for (Deliverable deliverable : deliverables.getDeliverableList()) {
            String deliverableSymbol = deliverable.getSymbol();
            Set<OptionRoot> rootForDeliverable = optionRootByDeliverableMap.get(deliverable.getSymbol());
            if (rootForDeliverable == null) {
                rootForDeliverable = new HashSet<>();
                Set<OptionRoot> newRootForDeliverable = optionRootByDeliverableMap.putIfAbsent(deliverableSymbol, rootForDeliverable);
                if (newRootForDeliverable != null) {
                    rootForDeliverable = newRootForDeliverable;
                }
            }
            rootForDeliverable.add(optionRoot);
        }
        store.optionRootMap.put(optionRoot.getOptionRootSymbol(), optionRoot);
    }
    public void addRoots(Map<String, OptionRoot> optionRoots) {
        for (Map.Entry<String, OptionRoot> entry : optionRoots.entrySet()) {
            addRoot(entry.getValue());
        }
    }
}
