package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.jkmcllc.aupair01.structure.OptionRoot;

class AbstractDeliverableLeg extends AbstractLeg {

    private final Map<AbstractStockLeg, BigDecimal> stockLegsAndDeliverableQty;
    private final OptionRoot optionRoot;
    private BigDecimal legValue;
    
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
            return null;
        } else if (deliverableLegsAndQty.size() == 1
                && deliverableLegs.size() == 1) {
            // most common case is that this is a single deliverable, optimize for that case
            AbstractStockLeg stockLeg = deliverableLegs.iterator().next();
            BigDecimal delivQty = deliverableLegsAndQty.get(stockLeg);
            BigDecimal availQty = (new BigDecimal(stockLeg.resetQty)).setScale(0)
                    .divide(delivQty.setScale(0), RoundingMode.DOWN);
            deliverableLeg = new AbstractDeliverableLeg(stockLeg.symbol, stockLeg.description, 
                    availQty.intValue(), optionRoot.getDeliverables().getDeliverablesValue(), 
                    optionRoot, deliverableLegsAndQty);
        } else {
            // multiple stock deliverables
            StringJoiner symbolJoin = new StringJoiner("-");
            StringJoiner descriptionJoin = new StringJoiner("-");
            deliverableLegs.stream().collect(Collectors.toMap( (d -> { 
                symbolJoin.add(d.getSymbol()); 
                descriptionJoin.add(d.getDescription()); 
                return d;
                }), v -> v.getQty()));
            BigDecimal availLegs = deliverableLegs.stream().map( stockLeg -> {
                symbolJoin.add(stockLeg.getSymbol()); 
                descriptionJoin.add(stockLeg.getDescription()); 
                
                BigDecimal delivQty = deliverableLegsAndQty.get(stockLeg);
                BigDecimal availQty = (new BigDecimal(stockLeg.resetQty)).setScale(0)
                        .divide(delivQty.setScale(0), RoundingMode.FLOOR);
                
                return availQty;
            }).collect(Collectors.minBy( (qty1, qty2) -> {
                return qty1.compareTo(qty2);
            })).get();
            
            
            deliverableLeg = new AbstractDeliverableLeg(symbolJoin.toString(), descriptionJoin.toString(), 
                    availLegs.intValue(), optionRoot.getDeliverables().getDeliverablesValue(), 
                    optionRoot, deliverableLegsAndQty);
        }
        return deliverableLeg;
    }
    
    private AbstractDeliverableLeg(String symbol, String description, Integer origQty, BigDecimal price,
            OptionRoot optionRoot, Map<AbstractStockLeg, BigDecimal> stockLegsAndDeliverableQty) {
        super(symbol, description, origQty, origQty, price);
        this.optionRoot = optionRoot;
        this.stockLegsAndDeliverableQty = stockLegsAndDeliverableQty;
    }
    
    @Override
    protected Leg newLegWith(Integer used) {
        BigDecimal usedDecimal = new BigDecimal(used) ;
        Map<AbstractStockLeg, BigDecimal> reducedMap = 
                stockLegsAndDeliverableQty.entrySet().stream().collect(Collectors.toMap( e -> {
                    AbstractStockLeg leg = e.getKey();
                    BigDecimal reduceAmt = usedDecimal.multiply(e.getValue());
                    // if reducing short deliverables by a negative number, then the actual short leg should be reduced 
                    // by the positive quantity used (like the strategy level quantity)
                    leg = (AbstractStockLeg) leg.reduceBy(reduceAmt.abs().intValue());
                    return leg;
        }, v -> v.getValue() ));
        
        AbstractDeliverableLeg deliverableLeg = new AbstractDeliverableLeg(this.symbol, this.description, 
                used, this.price, this.optionRoot, reducedMap);
        return deliverableLeg;
    }

    @Override
    public String getType() {
        return AbstractLeg.DELIVERABLE;
    }

    @Override
    public BigDecimal getLegValue() {
        if (legValue == null) {
            BigDecimal deliverablesValue = optionRoot.getDeliverables().getDeliverablesValue();
            legValue = deliverablesValue.multiply(new BigDecimal(this.qty));
        }
        return legValue;
    }

    @Override
    public Collection<? extends Leg> getMultiLegs() {
        return stockLegsAndDeliverableQty.keySet();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeliverableLeg: {");
        builder.append("qty: ").append(this.qty);
        builder.append(", price: ").append(this.price);
        builder.append(", multiLegs:").append(stockLegsAndDeliverableQty.keySet().toString());
        builder.append("}");
        return builder.toString();
    }

}
