package com.jkmcllc.aupair01.pairing.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrategyConfigs {

    private static final Logger logger = LoggerFactory.getLogger(PairingService.class);
    private static final String STRATEGY_GROUP = "strategyGroup";
    private static final String CHILD_STRATEGIES = ".childStrategies";
    private static final String CHILD_STRATEGIES_LEGS = ".childStrategiesLegs";
    private static final String STRATEGIES = "strategies";
    private static final String STRATETY_LEGS = ".legs";
    private static final String SORT = ".sort";
    private static final String STRATETY_LEGS_RATIO = ".legsRatio";
    private static final String STRATETY_PATTERN = ".pattern";
    private static final String STRATETY_MARGIN = ".margin";
    
    private static StrategyConfigs strategyConfigsInstance;
    private final ConcurrentMap<String, List<List<StrategyMeta>>> strategyConfigsMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, StrategyMeta> masterMap = new ConcurrentHashMap<>();
    
    public static final String CORE = "core";
    
    public static final String SHORT_STOCKS = "shortStocks";
    public static final String LONG_STOCKS = "longStocks";
    public static final String LONG_CALLS = "longCalls";
    public static final String SHORT_CALLS = "shortCalls";
    public static final String LONG_PUTS = "longPuts";
    public static final String SHORT_PUTS = "shortPuts";
    
    public static final String WIDE_STRIKE = "sortWideStrike";
    public static final String NARROW_STRIKE = "sortNarrowStrike";

    
    private StrategyConfigs() {};
    
    public static StrategyConfigs getInstance() {
        if (strategyConfigsInstance == null) {
            synchronized (StrategyConfigs.class) {
                if (strategyConfigsInstance == null) {
                    strategyConfigsInstance = new StrategyConfigs();
                }
            }
            Reader reader = new InputStreamReader(strategyConfigsInstance.getClass().getClassLoader()
                    .getResourceAsStream("paircore.ini"));
            strategyConfigsInstance.loadConfigs(reader, true);
        }
        return strategyConfigsInstance;
    }
    
    public List<List<StrategyMeta>> getStrategyGroup(String groupName) {
        List<List<StrategyMeta>> group = strategyConfigsMap.get(groupName);
        if (group == null) {
            logger.warn("Unknown strategy group \"" + groupName + "\" requested, using core group instead.");
            group = strategyConfigsMap.get(CORE);
        }
        return group;
    }
    
    private void loadConfigs(Reader reader, boolean core) {
        try {
            Ini paircoreini = new Ini(reader);
            // initialize core
            Ini.Section strategies = paircoreini.get(STRATEGY_GROUP);
            if (core) {
                Ini.Section coreStrategies = strategies.getChild(CORE);
                List<List<StrategyMeta>> strategiesList = buildStrategyMeta(coreStrategies);
                strategyConfigsMap.put(CORE, strategiesList);
            } else {
                for (String groupName : strategies.childrenNames()) {
                    Ini.Section coreStrategies = strategies.getChild(groupName);
                    List<List<StrategyMeta>> strategiesList = buildStrategyMeta(coreStrategies);
                    strategyConfigsMap.put(CORE, strategiesList);
                }
            }
            
        } catch (IOException e) {
            String reallyBadError = "Missing core configuration file; exiting! " + e;
            logger.error(reallyBadError, e);
            throw new RuntimeException(reallyBadError);
        } catch (Exception e) {
            String reallyBadError = "Misconfigured core configuration file; exiting! " + e;
            logger.error(reallyBadError, e);
            throw new RuntimeException(reallyBadError);
        }
    }
    
    private List<List<StrategyMeta>> buildStrategyMeta(Ini.Section strategyGroup) {
        List<String> strategyNameStrings = strategyGroup.getAll(STRATEGIES);
        
        List<List<StrategyMeta>> strategiesList = new ArrayList<>(strategyNameStrings.size());
        for (String strategyNameString : strategyNameStrings) {
            List<String> strategyNames = Arrays.asList(strategyNameString.split(","));
            List<StrategyMeta> strategies = new ArrayList<>(strategyNames.size());
            for (String strategyName : strategyNames) {
                strategyName = strategyName.trim();
                StrategyMeta strategyMeta = masterMap.get(strategyName);
                if (strategyMeta != null) {
                    strategies.add(strategyMeta);
                    continue;
                } else {
                    String legs = strategyGroup.fetch(strategyName + STRATETY_LEGS);
                    String legsRatio = strategyGroup.fetch(strategyName + STRATETY_LEGS_RATIO);
                    // TODO: legs must match known names
                    String childStrategiesString = strategyGroup.get(strategyName + CHILD_STRATEGIES);
                    String childStrategiesLegsString = strategyGroup.get(strategyName + CHILD_STRATEGIES_LEGS);
                    String sort = strategyGroup.get(strategyName + SORT);
                    strategyMeta = new StrategyMeta(strategyName, legs, legsRatio, childStrategiesString, childStrategiesLegsString, sort);
                    masterMap.put(strategyName, strategyMeta);
                    strategies.add(strategyMeta);
                }
                String tempPatternKey = strategyName + STRATETY_PATTERN;
                List<String> nonEvalValues = strategyGroup.getAll(tempPatternKey);
                for (int i = 0; i < nonEvalValues.size(); i++) {
                    String patternVal = strategyGroup.fetch(tempPatternKey, i);
                    strategyMeta.addStrategyPattern(patternVal);
                }
                String tempMarginKey = strategyName + STRATETY_MARGIN;
                nonEvalValues = strategyGroup.getAll(tempMarginKey);
                for (int i = 0; i < nonEvalValues.size(); i++) {
                    String marginVal = strategyGroup.fetch(tempMarginKey, i);
                    strategyMeta.addMarginPattern(marginVal);
                }
                // TODO: validation, make sure everything exists for the patterns, catch exceptions at each pattern to output unable to parse
            }
            // find all of the child strategies which may exist
            for (StrategyMeta strategyMeta : strategies) {
                if (strategyMeta.childStrategiesString != null) {
                    for (String childStrategyName : strategyMeta.childStrategiesString) {
                        StrategyMeta childStrategy = masterMap.get(childStrategyName);
                        if (childStrategy != null) {
                            strategyMeta.childStrategies.add(childStrategy);
                        }
                    }
                }
            }
            strategiesList.add(strategies);
        }
        
        return strategiesList;
    }
    
}
