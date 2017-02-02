package com.jkmcllc.aupair01.pairing.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.jexl3.JexlExpression;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jkmcllc.aupair01.exception.ConfigurationException;
import com.jkmcllc.aupair01.structure.OptionType;

public class StrategyConfigs {

    private static final Logger logger = LoggerFactory.getLogger(PairingService.class);

    private static final String CONFIG_FILE = "pairconfig";
    
    private static final String GLOBAL = "global";
    static final String CORE = "core";
    
    public static final String DEFAULT_STRATEGY_GROUP = "defaultStrategyGroup";
    public static final String TEST_LEAST_MARGIN = "testLeastMargin";
    public static final String MAINTENANCE = "maintenance";
    public static final String INITIAL = "initial";
    public static final String MAINTENANCE_PCT = "maintenancePct";
    public static final String NAKED_DELIVERABLE_PCT = "nakedDeliverablePct";
    public static final String NAKED_CASH_PCT = "nakedCashPct";

    private static final String NAKED_MARGINS = "nakedMargins";
    private static final String NAKED_CALL_MARGIN = "nakedCallMargin";
    private static final String NAKED_PUT_MARGIN = "nakedPutMargin";
    private static final String STRATEGY_GROUP = "strategyGroup";
    private static final String STRATEGY_LISTS = "strategyLists";
    private static final String STRATEGY_CONFIG_PREFIX = "strategy/";
    private static final String PARENT_STRATEGY = "parentStrategy";
    private static final String CHILD_STRATEGIES = "childStrategies";
    private static final String CHILD_STRATEGIES_LEGS = "childStrategiesLegs";
    private static final String STRATETY_LEGS = "legs";
    private static final String STRATETY_LEGS_RATIO = "legsRatio";
    private static final String PROHIBITED_STRATEGY = "prohibitedStrategy";
    private static final String STRATEGY_STRIKES_PATTERN = "strikesPattern";
    private static final String STRATEGY_WIDTH_PATTERN = "widthPattern";
    private static final String STRATEGY_EXPIRATION_PATTERN = "expirationPattern";
    private static final String STRATEGY_EXERCISE_PATTERN = "exercisePattern";
    private static final String STRATEGY_OTHER_PATTERN = "otherPattern";
    private static final String STRATETY_MAINTENANCE_MARGIN = "maintenanceMargin";
    private static final String STRATETY_MARGIN_DEBUG = "marginDebug";
    private static final String STRATETY_INITIAL_MARGIN = "initialMargin";
    private static final String LOWER_NAKED_TEST = "allowLowerNaked";
    
    private final ConcurrentMap<String, Object> globalConfiMap = new ConcurrentHashMap<>();
    private static StrategyConfigs strategyConfigsInstance;
    private final ConcurrentMap<String, StrategyGroup> strategyConfigsMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, StrategyMeta> masterStrategyMap = new ConcurrentHashMap<>();
    
    final ConcurrentMap<OptionType, List<JexlExpression>> nakedMarginMap = new ConcurrentHashMap<>();
    
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
    
    public static String getFileConfig()  {
        String fileNames = System.getProperty(CONFIG_FILE, "");
        return fileNames;
    }
    
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
            
            String fileNames = getFileConfig();
            if (fileNames != null && fileNames.isEmpty() == false) {
                String[] fileNamesArray = fileNames.split(",");
                for (String fileName : fileNamesArray) {
                    logger.info("loading custom configuration file: " + fileName);
                    try {
                        File file = new File(fileName);
                        Reader fileReader = new FileReader(file);
                        strategyConfigsInstance.loadConfigs(fileReader, false);
                    } catch (Exception e) {
                        throw new ConfigurationException("Could not load properties from file: " + fileName, e);
                    }
                }
            }
                    
        }
        return strategyConfigsInstance;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getGlobalConfig(GlobalConfigType<T> typeValue) {
        return (T) globalConfiMap.get(typeValue.getTypeName());
    }
    
    public StrategyGroup getStrategyGroup(String groupName) {
        StrategyGroup group = strategyConfigsMap.get(groupName);
        if (group == null) {
            logger.warn("Unknown strategy group \"" + groupName + "\" requested, using core group instead.");
            group = strategyConfigsMap.get(CORE);
        }
        return group;
    }
    
    private void loadConfigs(Reader reader, boolean core) {
        String coreName = core ? "core" : "";
        try {
            Ini iniFile = new Ini(reader);
            findAllStrategies(iniFile);
            findNakedMargins(iniFile);
            findGlobalConfigs(iniFile);
            // initialize core
            Ini.Section strategies = iniFile.get(STRATEGY_GROUP);
            if (core) {
                Ini.Section coreStrategies = strategies.getChild(CORE);
                List<StrategyGroupLists> strategiesList = buildStrategyMeta(coreStrategies);
                StrategyGroup coreGroup = new StrategyGroup(CORE, strategiesList);
                strategyConfigsMap.put(CORE, coreGroup);
            } 
            for (String groupName : strategies.childrenNames()) {
                if (CORE.equals(groupName)) {
                    continue;
                }
                Ini.Section coreStrategies = strategies.getChild(groupName);
                List<StrategyGroupLists> strategiesList = buildStrategyMeta(coreStrategies);
                
                StrategyGroup thisGroup = new StrategyGroup(groupName, strategiesList);
                String testLeastMargin = coreStrategies.get(TEST_LEAST_MARGIN);
                if ( testLeastMargin != null) {
                    if (MAINTENANCE.equals(testLeastMargin) == false && INITIAL.equals(testLeastMargin) == false) {
                        throw new ConfigurationException("Bad testLeastMargin defined: " + testLeastMargin);
                    }
                    thisGroup.testLeastMargin = testLeastMargin;
                } 
                strategyConfigsMap.put(groupName, thisGroup);
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

            strategyMeta = new StrategyMeta(strategyName, legs, legsRatio,  childStrategiesString, childStrategiesLegsString);
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

        boolean prohibitedStrategy = false;
        String prohibitedString = strategySection.get(PROHIBITED_STRATEGY);
        if (prohibitedString != null) {
            if (prohibitedString.isEmpty() == false) {
                prohibitedStrategy = Boolean.parseBoolean(prohibitedString);
            } else {
                prohibitedStrategy = true;
            }
        }
        strategyMeta.prohibitedStrategy = prohibitedStrategy;
        
        boolean allowLowerNaked = true;
        String allowLowerNakedString = strategySection.get(LOWER_NAKED_TEST);
        if (allowLowerNakedString != null) {
            if (allowLowerNakedString.isEmpty() == false) {
                allowLowerNaked = Boolean.parseBoolean(allowLowerNakedString);
            } else {
                allowLowerNaked = true;
            }
        }
        strategyMeta.allowLowerNaked = allowLowerNaked;
        
        masterStrategyMap.put(strategyMeta.strategyName, strategyMeta);
        return strategyMeta;
        
    }
    
    private void findNakedMargins(Ini iniFile) {
        Ini.Section nakeds = iniFile.get(NAKED_MARGINS);
        if (nakeds == null || nakeds.isEmpty()) {
            return;
        }
        List<String> nonEvalValues = nakeds.getAll(NAKED_CALL_MARGIN);
        if (nonEvalValues != null && nonEvalValues.size() > 0) {
            List<JexlExpression> nakedExpressions = new ArrayList<>(nonEvalValues.size());
            for (int i = 0; i < nakeds.size(); i++) {
                String pattern = nakeds.fetch(NAKED_CALL_MARGIN, i);
                JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
                nakedExpressions.add(p);
            }
            nakedMarginMap.putIfAbsent(OptionType.C, nakedExpressions);
        }
        nonEvalValues = nakeds.getAll(NAKED_PUT_MARGIN);
        if (nonEvalValues != null && nonEvalValues.size() > 0) {
            List<JexlExpression> nakedExpressions = new ArrayList<>(nonEvalValues.size());
            for (int i = 0; i < nakeds.size(); i++) {
                String pattern = nakeds.fetch(NAKED_PUT_MARGIN, i);
                JexlExpression p = TacoCat.getJexlEngine().createExpression(pattern);
                nakedExpressions.add(p);
            }
            nakedMarginMap.put(OptionType.P, nakedExpressions);
        }
    }
    
    private void findGlobalConfigs(Ini iniFile) {
        Ini.Section globals = iniFile.get(GLOBAL);
        if (globals == null || globals.isEmpty()) {
            return;
        }
        String defaultStrategyGroup = globals.get(DEFAULT_STRATEGY_GROUP);
        if (defaultStrategyGroup == null && globalConfiMap.get(DEFAULT_STRATEGY_GROUP) == null) {
            throw new ConfigurationException("No default strategy group defined");
        } else if (defaultStrategyGroup != null) {
            globalConfiMap.put(DEFAULT_STRATEGY_GROUP, defaultStrategyGroup);
        }
        String testLeastMargin = globals.get(TEST_LEAST_MARGIN);
        if ( (testLeastMargin == null && globalConfiMap.get(TEST_LEAST_MARGIN) == null) 
                || (MAINTENANCE.equals(testLeastMargin) == false && INITIAL.equals(testLeastMargin) == false) ) {
            throw new ConfigurationException("No least margin test defined");
        }
        globalConfiMap.put(TEST_LEAST_MARGIN, testLeastMargin);
        
        String maintenancePctString = globals.get(MAINTENANCE_PCT);
        if ( (maintenancePctString == null && globalConfiMap.get(MAINTENANCE_PCT) == null) ) {
            throw new ConfigurationException("No maintenance percent defined");
        } else if (maintenancePctString != null) {
            BigDecimal maintenancePct = null;
            try {
                maintenancePct = new BigDecimal(maintenancePctString);
            } catch (Exception e) {
                throw new ConfigurationException("Invalid maintenance percent defined: " + maintenancePctString);
            }
            globalConfiMap.put(MAINTENANCE_PCT, maintenancePct);
        }
        
        String nakedDeliverablePctString = globals.get(NAKED_DELIVERABLE_PCT);
        if ( (nakedDeliverablePctString == null && globalConfiMap.get(NAKED_DELIVERABLE_PCT) == null) ) {
            throw new ConfigurationException("No naked deliverable percent defined");
        } else if (nakedDeliverablePctString != null) {
            BigDecimal nakedDeliverablePct = null;
            try {
                nakedDeliverablePct = new BigDecimal(nakedDeliverablePctString);
            } catch (Exception e) {
                throw new ConfigurationException("Invalid naked deliverable percent defined: " + nakedDeliverablePctString);
            }
            globalConfiMap.put(NAKED_DELIVERABLE_PCT, nakedDeliverablePct);
        }
        String nakedCashPctString = globals.get(NAKED_CASH_PCT);
        if ( (nakedCashPctString == null && globalConfiMap.get(NAKED_CASH_PCT) == null) ) {
            throw new ConfigurationException("No naked cash percent defined");
        } else if (nakedCashPctString != null) {
            BigDecimal nakedCashPct = null;
            try {
                nakedCashPct = new BigDecimal(nakedCashPctString);
            } catch (Exception e) {
                throw new ConfigurationException("Invalid naked cash percent defined: " + nakedCashPctString);
            }
            globalConfiMap.put(NAKED_CASH_PCT, nakedCashPct);
        }
    }
    

}
