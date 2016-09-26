package com.jkmcllc.aupair01.pairing.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.exception.ConfigurationException;

public class StrategyConfigs {

    private static final Logger logger = LoggerFactory.getLogger(PairingService.class);
    private static final String STRATEGY_GROUP = "strategyGroup";
    private static final String STRATEGIES = "strategies";
    private static final String STRATEGY_CONFIG_PREFIX = "strategy/";
    private static final String CHILD_STRATEGIES = "childStrategies";
    private static final String CHILD_STRATEGIES_LEGS = "childStrategiesLegs";
    private static final String STRATETY_LEGS = "legs";
    private static final String STRATETY_LEGS_RATIO = "legsRatio";
    private static final String STRATETY_PATTERN = "pattern";
    private static final String STRATETY_MAINTENANCE_MARGIN = "maintenanceMargin";
    private static final String STRATETY_MARGIN_DEBUG = "marginDebug";
    
    private static StrategyConfigs strategyConfigsInstance;
    private final ConcurrentMap<String, List<List<StrategyMeta>>> strategyConfigsMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, StrategyMeta> masterStrategyMap = new ConcurrentHashMap<>();
    
    public static final String CORE = "core";
    
    public static final String SHORT_DELIVERABLES = "shortDeliverables";
    public static final String LONG_DELIVERABLES = "longDeliverables";
    public static final String LONG_CALLS = "longCalls";
    public static final String SHORT_CALLS = "shortCalls";
    public static final String LONG_PUTS = "longPuts";
    public static final String SHORT_PUTS = "shortPuts";
    public static final String LONG_STOCKS = "longStocks";
    public static final String SHORT_STOCKS = "shortStocks";
    
    public static final String WIDE_STRIKE = "sortWideStrike";
    public static final String NARROW_STRIKE = "sortNarrowStrike";
    
    private static final Map<String, StrategyMeta> ALL_SORTS_MAP = new HashMap<>();
    static {
        StrategyMeta sortMeta = new StrategyMeta(WIDE_STRIKE);
        ALL_SORTS_MAP.put(WIDE_STRIKE, sortMeta);
        sortMeta = new StrategyMeta(NARROW_STRIKE);
        ALL_SORTS_MAP.put(NARROW_STRIKE, sortMeta);
    }
    
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
            findAllStrategies(paircoreini);
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
            throw new ConfigurationException(reallyBadError);
        } catch (Exception e) {
            String reallyBadError = "Misconfigured core configuration file; exiting! " + e;
            logger.error(reallyBadError, e);
            throw new ConfigurationException(reallyBadError);
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
                StrategyMeta strategyMeta = ALL_SORTS_MAP.get(strategyName);
                if (strategyMeta != null) {
                    strategies.add(strategyMeta);
                    continue;
                }
                // try looking up an actual strategy
                strategyMeta = masterStrategyMap.get(strategyName);
                if (strategyMeta != null) {
                    strategies.add(strategyMeta);
                } else {
                    throw new ConfigurationException("Configuration for strategy which is not defined, strategyName=" + strategyName);
                }
            }
            strategiesList.add(strategies);
        }
        
        return strategiesList;
    }
    
    private void findAllStrategies(Ini inputConfig) {
        Set<String> childrenNames = inputConfig.keySet();
        childrenNames.stream().filter(name -> name.startsWith(STRATEGY_CONFIG_PREFIX) )
            .forEach(strategyConfigName -> {
                Ini.Section strategySection = inputConfig.get(strategyConfigName);
                String strategyName = strategyConfigName.replaceFirst(STRATEGY_CONFIG_PREFIX, "");
                StrategyMeta strategyMeta = loadStrategies(strategySection, strategyName);
                masterStrategyMap.put(strategyName, strategyMeta);
            }); 
        // find all of the child strategies which may exist
        for (StrategyMeta strategyMeta : masterStrategyMap.values()) {
            if (strategyMeta.childStrategiesString != null) {
                for (String childStrategyName : strategyMeta.childStrategiesString) {
                    StrategyMeta childStrategy = masterStrategyMap.get(childStrategyName);
                    if (childStrategy != null) {
                        strategyMeta.childStrategies.add(childStrategy);
                    }
                }
            }
        }
    }
    
    private StrategyMeta loadStrategies(Ini.Section strategySection, String strategyName) {
        String legs = strategySection.fetch(STRATETY_LEGS);
        String legsRatio = strategySection.fetch(STRATETY_LEGS_RATIO);
        // TODO: legs must match known names
        String childStrategiesString = strategySection.get(CHILD_STRATEGIES);
        String childStrategiesLegsString = strategySection.get(CHILD_STRATEGIES_LEGS);
        StrategyMeta strategyMeta = new StrategyMeta(strategyName, legs, legsRatio, childStrategiesString, childStrategiesLegsString);
        

        String tempPatternKey = STRATETY_PATTERN;
        List<String> nonEvalValues = strategySection.getAll(tempPatternKey);
        for (int i = 0; i < nonEvalValues.size(); i++) {
            String patternVal = strategySection.fetch(tempPatternKey, i);
            strategyMeta.addStrategyPattern(patternVal);
        }
        String tempMarginKey = STRATETY_MAINTENANCE_MARGIN;
        nonEvalValues = strategySection.getAll(tempMarginKey);
        if (nonEvalValues != null) {
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String marginVal = strategySection.fetch(tempMarginKey, i);
                strategyMeta.addMarginPattern(marginVal);
            }
        }
        String tempMarginDebugKey = STRATETY_MARGIN_DEBUG;
        nonEvalValues = strategySection.getAll(tempMarginDebugKey);
        if (nonEvalValues != null) {
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String marginVal = strategySection.fetch(tempMarginDebugKey, i);
                strategyMeta.addMarginDebugPattern(marginVal);
            }
        }
        // TODO: validation, make sure everything exists for the patterns, catch exceptions at each pattern to output unable to parse
        return strategyMeta;
        
    }
    
}
