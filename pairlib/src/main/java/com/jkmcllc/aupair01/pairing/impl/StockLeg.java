package com.jkmcllc.aupair01.pairing.impl;

import java.math.BigDecimal;

public class StockLeg extends AbstractStockLeg {
    protected StockLeg(String symbol, String description, Integer qty, Integer positionResetQty, BigDecimal price,
            BigDecimal equityMaintenanceMargin, BigDecimal equityInitialMargin) {
        super(symbol, description, qty, positionResetQty, price, equityMaintenanceMargin, equityInitialMargin);
    }
    @Override
    protected Leg newLegWith(Integer used) {
        StockLeg t = new StockLeg(this.symbol, this.description, used, this.positionResetQty, this.price,
                this.equityMaintenanceMargin, this.equityInitialMargin);
        return t;
    }
}
