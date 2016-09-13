package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

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
    static JexlContext buildPairingContext(List<Leg> legs, AccountInfo accountInfo) {
        JexlContext context = buildCommonContext(legs, accountInfo);
        return context;
    }
    static JexlContext buildMarginContext(List<Leg> legs, AccountInfo accountInfo, Strategy strategy) {
        JexlContext context = buildCommonContext(legs, accountInfo);
        context.set("strategy",strategy);
        context.set("strategyQuantity",new BigDecimal(strategy.getQuantity()));
        return context;
    }
    private static JexlContext buildCommonContext(List<Leg> legs, AccountInfo accountInfo) {
        JexlContext context = new MapContext();
        context.set("legs", legs);
        context.set("accountInfo", accountInfo);
        context.set("zero",BigDecimal.ZERO);
        context.set("margin",BigDecimal.ZERO);
        return context;
        
    }
}
