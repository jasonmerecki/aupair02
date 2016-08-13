package com.jkmcllc.aupair01.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jkmcllc.aupair01.structure.OptionRoot;

public class OptionRootStore {
    private volatile ConcurrentMap<String, OptionRoot> optionRootMap = new ConcurrentHashMap<>();
    private static OptionRootStore optionRootStore;
    private OptionRootStore() {};
    private static OptionRootStore getInstance() {
        if (optionRootStore == null) {
            synchronized (OptionRootStore.class) {
                if (optionRootStore == null) {
                    optionRootStore = new OptionRootStore();
                }
            }
        }
        return optionRootStore;
    }
    public static OptionRoot findRoot(String optionRootSymbol) {
        return getInstance().optionRootMap.get(optionRootSymbol);
    }
    public static void addRoot(OptionRoot optionRoot) {
        getInstance().optionRootMap.put(optionRoot.getOptionRootSymbol(), optionRoot);
    }
}
