package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.structure.ExerciseStyle;

class TacoCat {
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
        return context;
    }
    private static JexlContext buildCommonContext(List<Leg> legs, AccountInfo accountInfo, PairingInfo pairingInfo) {
        JexlContext context = new MapContext();
        // some helpful constants
        context.set("zero",BigDecimal.ZERO);
        context.set("true",Boolean.TRUE);
        context.set("false",Boolean.FALSE);
        context.set("exerciseEuropean", ExerciseStyle.E);
        context.set("exerciseAmerican", ExerciseStyle.A);
        // context-specific stuff
        context.set("legs", legs);
        // if there is only one leg, then it can be 'leg' in the context
        if (legs.size() == 1) {
            context.set("leg",legs.get(0));
        }
        context.set("accountInfo", accountInfo);
        context.set("maintenanceMargin",BigDecimal.ZERO);
        PublicPairingInfo i = new PublicPairingInfo(pairingInfo);
        context.set("pairingInfo", i);
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
}
