package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionConfig;
import com.jkmcllc.aupair01.structure.OptionRoot;

class TacoCat {
    public static final String NAKED_LEG = "nakedLeg";
    
    private final JexlEngine jexlEngine = (new JexlBuilder()).cache(512).strict(true).silent(false).create();
    private static TacoCat cat;
    private TacoCat() {};
    private static TacoCat getTacoCat() {
        if (cat == null) {
            synchronized (TacoCat.class) {
                if (cat == null) {
                    cat = new TacoCat();
                }
            }
        }
        return cat;
    }
    static JexlEngine getJexlEngine() {
        return getTacoCat().jexlEngine;
    }
    static JexlContext buildPairingContext(List<Leg> legs, AccountInfo accountInfo, PairingInfo pairingInfo) {
        JexlContext context = buildCommonContext(legs, accountInfo, pairingInfo);
        return context;
    }
    static JexlContext buildMarginContext(List<Leg> legs, AccountInfo accountInfo, PairingInfo pairingInfo, Strategy strategy) {
        JexlContext context = buildCommonContext(legs, accountInfo, pairingInfo);
        context.set("strategy",strategy);
        context.set("strategyQuantity",new BigDecimal(strategy.getQuantity()));
        // special holder objects to swap out of context
        context.set(NAKED_LEG,new NakedOptionLegWrapper());
        if (pairingInfo.allLegs != null && !pairingInfo.allLegs.isEmpty() ) {
            Optional<AbstractLeg> optLegForNaked = pairingInfo.allLegs.values().stream()
                    .filter(abLeg -> AbstractLeg.STOCKOPTION.equals(abLeg.getType())).findAny();
            if (optLegForNaked.isPresent()) {
                AbstractOptionLeg legForNaked = (AbstractOptionLeg) optLegForNaked.get();
                BigDecimal nakedDeliverablePct = legForNaked.getOptionRoot().getNakedDeliverablePct();
                if (nakedDeliverablePct != null) {
                    context.set(GlobalConfigType.NAKED_DELIVERABLE_PCT.getTypeName(), nakedDeliverablePct);
                }
                BigDecimal nakedCashPct = legForNaked.getOptionRoot().getNakedCashPct();
                if (nakedCashPct != null) {
                    context.set(GlobalConfigType.NAKED_CASH_PCT.getTypeName(), nakedCashPct);
                }
            }
        }
        
        return context;
    }
    private static JexlContext buildCommonContext(List<Leg> legs, AccountInfo accountInfo, PairingInfo pairingInfo) {
        JexlContext context = new MapContext();
        // some helpful constants
        context.set("zero",BigDecimal.ZERO);
        context.set("true",Boolean.TRUE);
        context.set("false",Boolean.FALSE);
        context.set("exerciseEuropean", ExerciseStyle.StyleType.E);
        context.set("exerciseAmerican", ExerciseStyle.StyleType.A);
        // context-specific stuff
        context.set("legs", legs);
        context.set("accountInfo", accountInfo);
        context.set("maintenanceMargin",BigDecimal.ZERO);
        PublicPairingInfo i = new PublicPairingInfo(pairingInfo);
        context.set("pairingInfo", i);
        context.set(GlobalConfigType.NAKED_DELIVERABLE_PCT.getTypeName(), StrategyConfigs.getInstance().getGlobalConfig(GlobalConfigType.NAKED_DELIVERABLE_PCT));
        context.set(GlobalConfigType.NAKED_CASH_PCT.getTypeName(), StrategyConfigs.getInstance().getGlobalConfig(GlobalConfigType.NAKED_CASH_PCT));
        return context;
    }
    public static class PublicPairingInfo {
        private final PairingInfo pairingInfo;
        PublicPairingInfo (PairingInfo pairingInfo) {
            this.pairingInfo = pairingInfo;
        }
        public List<? extends Leg> getLongDeliverables() {
            return pairingInfo.getLongDeliverables();
        }
        
        public List<? extends Leg> getShortDeliverables() {
            return pairingInfo.getShortDeliverables();
        }
    }
    public static class NakedOptionLegWrapper {
        AbstractOptionLeg leg;
        public Integer getQty() {
            return leg.getQty();
        }
        public String getSymbol() {
            return leg.getSymbol();
        }
        public String getType() {
            return leg.getType();
        }
        public OptionConfig getOptionConfig() {
            return leg.getOptionConfig();
        }
        public OptionRoot getOptionRoot() {
            return leg.getOptionRoot();
        }
        public BigDecimal getOtmAmount() {
            return leg.getOtmAmount();
        }
        public BigDecimal getItmAmount() {
            return leg.getItmAmount();
        }
        public BigDecimal getDeliverablesValue() {
            return leg.getDeliverablesValue() ;
        }
        public BigDecimal getCashDeliverableValue() {
            return leg.getCashDeliverableValue();
        }
        public BigDecimal getStrikePrice() {
            return leg.getStrikePrice();
        }
        public BigDecimal getMultiplier() {
            return leg.getMultiplier();
        }
        public BigDecimal getLegValue() {
            return leg.getLegValue();
        }
        
    }
}
