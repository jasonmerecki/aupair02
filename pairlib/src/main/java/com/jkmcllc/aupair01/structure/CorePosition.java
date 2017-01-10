package com.jkmcllc.aupair01.structure;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface CorePosition {
    enum CorePositionType{ORDERLEG, POSITION};
    CorePositionType getCorePositionType();
    String getSymbol();
    String getDescription();
    Integer getQty();
    BigDecimal getPrice();
    OptionConfig getOptionConfig();
    BigDecimal getEquityMaintenanceMargin();
    BigDecimal getEquityInitialMargin();

    static String findDuplicate(Collection<? extends CorePosition> corePositions) {
        Set<OptionConfig> uniqConfigs = new HashSet<>();
        Set<String> uniqSymbols = new HashSet<>();
        String dupe = null;
        for (CorePosition pos : corePositions) {
            String symbol = pos.getSymbol();
            if (uniqSymbols.contains(symbol)) {
                dupe = symbol;
                break;
            }
            uniqSymbols.add(symbol);
            OptionConfig optConfig = pos.getOptionConfig();
            if (optConfig != null) {
                if (uniqConfigs.contains(optConfig)) {
                    dupe = optConfig.toString();
                    break;
                }
                uniqConfigs.add(optConfig);
            }
        };
        return dupe;
    }

}