package com.jkmcllc.aupair01.pairing.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
    private static final String STRATEGY_LISTS = "strategyLists";
    private static final String STRATEGY_CONFIG_PREFIX = "strategy/";
    private static final String PARENT_STRATEGY = "parentStrategy";
    private static final String CHILD_STRATEGIES = "childStrategies";
    private static final String CHILD_STRATEGIES_LEGS = "childStrategiesLegs";
    private static final String STRATETY_LEGS = "legs";
    private static final String STRATETY_LEGS_RATIO = "legsRatio";
    private static final String STRATEGY_STRIKES_PATTERN = "strikesPattern";
    private static final String STRATEGY_WIDTH_PATTERN = "widthPattern";
    private static final String STRATEGY_EXPIRATION_PATTERN = "expirationPattern";
    private static final String STRATEGY_EXERCISE_PATTERN = "exercisePattern";
    private static final String STRATEGY_OTHER_PATTERN = "otherPattern";
    private static final String STRATETY_MAINTENANCE_MARGIN = "maintenanceMargin";
    private static final String STRATETY_MARGIN_DEBUG = "marginDebug";
    private static final String STRATETY_INITIAL_MARGIN = "intialMargin";
    
    private static StrategyConfigs strategyConfigsInstance;
    private final ConcurrentMap<String, List<StrategyGroupLists>> strategyConfigsMap = new ConcurrentHashMap<>();
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
    
    static final Set<String> ALL_LEG_LIST_NAMES = new HashSet<>( Arrays.asList(SHORT_DELIVERABLES, LONG_DELIVERABLES,
            LONG_CALLS, SHORT_CALLS, LONG_PUTS, SHORT_PUTS, LONG_STOCKS, SHORT_STOCKS) );
    
    public static final String WIDE_STRIKE = "sortWideStrike";
    public static final String NARROW_STRIKE = "sortNarrowStrike";
    
    static final Set<String> ALL_SORT_NAMES = new HashSet<>( Arrays.asList(WIDE_STRIKE, NARROW_STRIKE) );
    
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
    
    public List<StrategyGroupLists> getStrategyGroup(String groupName) {
        List<StrategyGroupLists> group = strategyConfigsMap.get(groupName);
        if (group == null) {
            logger.warn("Unknown strategy group \"" + groupName + "\" requested, using core group instead.");
            group = strategyConfigsMap.get(CORE);
        }
        return group;
    }
    
    private void loadConfigs(Reader reader, boolean core) {
        String coreName = core ? "core" : "";
        try {
            Ini paircoreini = new Ini(reader);
            findAllStrategies(paircoreini);
            // initialize core
            Ini.Section strategies = paircoreini.get(STRATEGY_GROUP);
            if (core) {
                Ini.Section coreStrategies = strategies.getChild(CORE);
                List<StrategyGroupLists> strategiesList = buildStrategyMeta(coreStrategies);
                strategyConfigsMap.put(CORE, strategiesList);
            } 
            for (String groupName : strategies.childrenNames()) {
                if (CORE.equals(groupName)) {
                    continue;
                }
                Ini.Section coreStrategies = strategies.getChild(groupName);
                List<StrategyGroupLists> strategiesList = buildStrategyMeta(coreStrategies);
                strategyConfigsMap.put(groupName, strategiesList);
            }
            
        } catch (IOException e) {
            String reallyBadError = "Missing " + coreName + " configuration file; exiting! " + e;
            logger.error(reallyBadError, e);
            throw new ConfigurationException(reallyBadError);
        } catch (Exception e) {
            String reallyBadError = "Misconfigured " + coreName + " configuration file; exiting! " + e;
            logger.error(reallyBadError, e);
            throw new ConfigurationException(reallyBadError);
        }
    }
    
    private List<StrategyGroupLists> buildStrategyMeta(Ini.Section strategyGroup) {
        
        String strategyListsString = strategyGroup.get(STRATEGY_LISTS);
        if (strategyListsString == null) {
            throw new ConfigurationException("Configuration for strategy group with no strategy lists, strategyGroup=" + strategyGroup.getName() );
        }
        String[] strategyLists = strategyListsString.split(",");
        if (strategyLists == null || strategyLists.length == 0) {
            throw new ConfigurationException("Configuration for strategy group with no strategy lists, strategyGroup=" + strategyGroup.getName() );
        }
        List<StrategyGroupLists> strategiesList = new ArrayList<>();
        for (int i = 0; i < strategyLists.length; i++) {
            String strategyListName = strategyLists[i].trim();
            List<String> strategyNameStrings = strategyGroup.getAll(strategyListName);
            List<StrategyMeta> strategies = new ArrayList<>();
            for (String strategyNameString : strategyNameStrings) {
                List<String> strategyNames = Arrays.asList(strategyNameString.split(","));
                
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
            }
            StrategyGroupLists strategyGroupLists = new StrategyGroupLists(strategyListName, strategies);
            strategiesList.add(strategyGroupLists);
        }

        
        return strategiesList;
    }
    
    private void findAllStrategies(Ini inputConfig) {
        Set<String> childrenNames = inputConfig.keySet();
        childrenNames.stream().filter(name -> name.startsWith(STRATEGY_CONFIG_PREFIX) )
            .forEach(strategyConfigName -> {
                loadStrategies(inputConfig, strategyConfigName);
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
    
    private StrategyMeta loadStrategies(Ini inputConfig, String strategyConfigName) {
        StrategyMeta strategyMeta = null;
        Ini.Section strategySection = inputConfig.get(strategyConfigName);
        String parentStrategyName = strategySection.fetch(PARENT_STRATEGY);
        String strategyName = strategyConfigName.replaceFirst(STRATEGY_CONFIG_PREFIX, "");
        if (parentStrategyName != null ) {
            StrategyMeta parentMeta = masterStrategyMap.get(parentStrategyName);
            if (parentMeta == null) {
                String parentStrategyConfigName = STRATEGY_CONFIG_PREFIX + parentStrategyName;
                parentMeta = loadStrategies(inputConfig, parentStrategyConfigName);
            }
            if (parentMeta == null) {
                throw new ConfigurationException("Configuration for parent strategy which is not defined, strategyName=" + strategyName + " and parent=" + parentStrategyName);
            }
            strategyMeta = parentMeta.copy(strategyName);
        } else {
            String legs = strategySection.fetch(STRATETY_LEGS);
            String legsRatio = strategySection.fetch(STRATETY_LEGS_RATIO);
            String childStrategiesString = strategySection.get(CHILD_STRATEGIES);
            String childStrategiesLegsString = strategySection.get(CHILD_STRATEGIES_LEGS);
            strategyMeta = new StrategyMeta(strategyName, legs, legsRatio, childStrategiesString, childStrategiesLegsString);
        }


        String tempPatternKey = STRATEGY_STRIKES_PATTERN;
        List<String> nonEvalValues = strategySection.getAll(tempPatternKey);
        if ( nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.strikesPatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String patternVal = strategySection.fetch(tempPatternKey, i);
                strategyMeta.addStrikesPattern(patternVal);
            }
        } 
        
        tempPatternKey = STRATEGY_WIDTH_PATTERN;
        nonEvalValues = strategySection.getAll(tempPatternKey);
        if ( nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.widthPatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String patternVal = strategySection.fetch(tempPatternKey, i);
                strategyMeta.addWidthPattern(patternVal);
            }
        } 
        
        tempPatternKey = STRATEGY_EXPIRATION_PATTERN;
        nonEvalValues = strategySection.getAll(tempPatternKey);
        if ( nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.expirationPatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String patternVal = strategySection.fetch(tempPatternKey, i);
                strategyMeta.addExpirationPattern(patternVal);
            }
        } 
        
        tempPatternKey = STRATEGY_EXERCISE_PATTERN;
        nonEvalValues = strategySection.getAll(tempPatternKey);
        if ( nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.exercisePatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String patternVal = strategySection.fetch(tempPatternKey, i);
                strategyMeta.addExercisePattern(patternVal);
            }
        } 
        
        tempPatternKey = STRATEGY_OTHER_PATTERN;
        nonEvalValues = strategySection.getAll(tempPatternKey);
        if ( nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.otherPatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String patternVal = strategySection.fetch(tempPatternKey, i);
                strategyMeta.addOtherPattern(patternVal);
            }
        } 

        String tempMarginKey = STRATETY_MAINTENANCE_MARGIN;
        nonEvalValues = strategySection.getAll(tempMarginKey);
        if ( nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.maintenanceMarginPatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String marginVal = strategySection.fetch(tempMarginKey, i);
                strategyMeta.addMarginPattern(marginVal);
            }
        } else if (parentStrategyName == null) {
            throw new ConfigurationException("Configuration for strategy has no maintenance margin, strategyName=" + strategyName );
        }
        String tempMarginDebugKey = STRATETY_MARGIN_DEBUG;
        nonEvalValues = strategySection.getAll(tempMarginDebugKey);
        if (nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.marginDebugPatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String marginVal = strategySection.fetch(tempMarginDebugKey, i);
                strategyMeta.addMarginDebugPattern(marginVal);
            }
        }
        tempMarginKey = STRATETY_INITIAL_MARGIN;
        nonEvalValues = strategySection.getAll(tempMarginKey);
        if ( nonEvalValues != null && nonEvalValues.size() > 0) {
            if (parentStrategyName != null) {
                strategyMeta.initialMarginPatterns.clear();
            } 
            for (int i = 0; i < nonEvalValues.size(); i++) {
                String marginVal = strategySection.fetch(tempMarginKey, i);
                strategyMeta.addInitialMarginPattern(marginVal);
            }
        } 

        masterStrategyMap.put(strategyMeta.strategyName, strategyMeta);
        return strategyMeta;
        
    }
    
}
