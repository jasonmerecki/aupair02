package com.jkmcllc.aupair01.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jkmcllc.aupair01.structure.OptionRoot;

public class OptionRootStore {
    private volatile ConcurrentMap<String, OptionRoot> optionRootMap = new ConcurrentHashMap<>();
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
    public OptionRoot findRoot(String optionRootSymbol) {
        if (usePermStore) {
            return getPermInstance().optionRootMap.get(optionRootSymbol);
        }
        return optionRootMap.get(optionRootSymbol);
    }
    public void addRoot(OptionRoot optionRoot) {
        if (usePermStore) {
            getPermInstance().optionRootMap.put(optionRoot.getOptionRootSymbol(), optionRoot);
        } else {
            optionRootMap.put(optionRoot.getOptionRootSymbol(), optionRoot);
        }
    }
    public void addRoots(Map<String, OptionRoot> optionRoots) {
        if (usePermStore) {
            getPermInstance().optionRootMap.putAll(optionRoots);
        } else {
            optionRootMap.putAll(optionRoots);
        }
    }
}
