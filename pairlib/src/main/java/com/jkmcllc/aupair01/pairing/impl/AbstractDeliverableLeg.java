package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.jkmcllc.aupair01.structure.Deliverable;
import com.jkmcllc.aupair01.structure.OptionRoot;

class AbstractDeliverableLeg extends AbstractLeg {

    private final Map<AbstractStockLeg, BigDecimal> stockLegsAndDeliverableQty;
    
    static AbstractDeliverableLeg from(Collection<? extends AbstractStockLeg> deliverableLegs, OptionRoot optionRoot) {
        Map<AbstractStockLeg, BigDecimal> deliverableLegsAndQty = new HashMap<>();
        AbstractDeliverableLeg deliverableLeg = null;
        deliverableLegs.forEach(d -> {
            optionRoot.getDeliverables().getStockDeliverableList().stream().filter(d1 -> 
            d1.getSymbol().equals(d.getSymbol())
            ).forEach(d1 -> deliverableLegsAndQty.put(d, d1.getQty()));
        });
        
        if (deliverableLegsAndQty.size() != optionRoot.getDeliverables().getStockDeliverableList().size()) {
            // this means there are not enough stock legs to fulfill the deliverables
            deliverableLeg = new AbstractDeliverableLeg("(none)", "(none)", 
                    0, BigDecimal.ZERO, Collections.emptyMap());
        } else if (deliverableLegsAndQty.size() == 1
                && deliverableLegs.size() == 1) {
            // most common case is that this is a single deliverable, optimize for that case
            AbstractStockLeg stockLeg = deliverableLegs.iterator().next();
            BigDecimal delivQty = deliverableLegsAndQty.get(stockLeg);
            BigDecimal availQty = (new BigDecimal(stockLeg.origQty)).setScale(0)
                    .divide(delivQty.setScale(0), RoundingMode.FLOOR);
            deliverableLeg = new AbstractDeliverableLeg(stockLeg.symbol, stockLeg.description, 
                    availQty.intValue(), stockLeg.price, deliverableLegsAndQty);
        } else {
            // multiple stock deliverables
            StringJoiner symbolJoin = new StringJoiner("-");
            StringJoiner descriptionJoin = new StringJoiner("-");
            deliverableLegs.forEach(d -> { 
                symbolJoin.add(d.getSymbol()); 
                descriptionJoin.add(d.getDescription()); 
                });
            deliverableLeg = new AbstractDeliverableLeg(symbolJoin.toString(), descriptionJoin.toString(), 
                    0, BigDecimal.ZERO, deliverableLegsAndQty);
        }
        return deliverableLeg;
    }
    
    private AbstractDeliverableLeg(String symbol, String description, Integer origQty, BigDecimal price,
            Map<AbstractStockLeg, BigDecimal> stockLegsAndDeliverableQty) {
        super(symbol, description, origQty, price);
        this.stockLegsAndDeliverableQty = stockLegsAndDeliverableQty;
    }
    
    @Override
    protected Leg newLegWith(Integer used) {
        BigDecimal usedDecimal = new BigDecimal(used) ;
        Map<AbstractStockLeg, BigDecimal> reducedMap = 
                stockLegsAndDeliverableQty.entrySet().stream().collect(Collectors.toMap( e -> {
                    AbstractStockLeg leg = e.getKey();
                    BigDecimal reduceAmt = usedDecimal.multiply(e.getValue());
                    leg = (AbstractStockLeg) leg.reduceBy(reduceAmt.intValue());
                    return leg;
        }, v -> v.getValue() ));
        
        AbstractDeliverableLeg deliverableLeg = new AbstractDeliverableLeg(this.symbol, this.description, 
                used, this.price, reducedMap);
        return deliverableLeg;
    }

    @Override
    public String getType() {
        return AbstractLeg.DELIVERABLE;
    }

    @Override
    public BigDecimal getLegValue() {
        // TODO calcualte this
        return BigDecimal.ZERO;
    }

    @Override
    public Collection<? extends Leg> getMultiLegs() {
        if (stockLegsAndDeliverableQty.size() == 1) {
            return null;
        }
        return stockLegsAndDeliverableQty.keySet();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("deliverableLeg: ");
        builder.append(stockLegsAndDeliverableQty.keySet().toString());
        return builder.toString();
    }

}
