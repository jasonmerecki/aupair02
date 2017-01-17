import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingRequest.PairingRequestBuilder;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.UnderlyerType;

public class PairingRequestOrderBuilderTest {
    
    @Test 
    public void build1() {
        PairingRequest pairingRequest = buildRequestOrder1(false);
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
    }
    
    public static PairingRequest buildRequestOrder1(boolean requestAllStrategyLists) {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // MSFT holdings, 2 put option symbols
        builder.setPositionSymbol("MSFT  160115P00080000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("28.70").addPosition();
        builder.setPositionSymbol("MSFT  160115P00082000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("82.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("30.19").addPosition();
        
        
        // Order is to-close 
        // in this example, the order is to sell quantity 4 of the spread at $2.00
        //     (sell -4 quantity) * (2.00) * (100 price multiplier) = -800.00
        // this order will bring in cash, and it is represented as a reduction in margin
        // which will offset any increase in margin resulting from the pairing result
        // note that the margin could also be reduced by less, if there was $7.50 in commission & fees,
        // then the margin reduction would actually be -792.50 instead of -800.00
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
        builder.setOrderId("OrderA").setOrderDescription("Sell to close 4 MSFT 80/82 put spread @ LM 2.00")
            .setOrderMaintenanceCost("-800.00").setOrderInitialCost("-800.00")
            .addOrder();
        
        builder.addAccount("account1");

        builder.setRequestAllStrategyLists(requestAllStrategyLists);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
    @Test 
    public void build2() {
        PairingRequest pairingRequest = buildRequestOrder2();
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
    }
    
    public static PairingRequest buildRequestOrder2() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // Account 1 OrderB is buy to-open a spread
        // buying a spread will consume cash, same as an increase in margin
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
        builder.setOrderId("OrderB").setOrderDescription("Buy to open 4 MSFT 80.00/82.00 put spread @ LM 1.50")
            .setOrderMaintenanceCost("600.00").setOrderInitialCost("600.00")
            .addOrder();
        
        builder.addAccount("account1");
        
        
        
        
        // Account 2 OrderC is sell-to-open a spread
        // selling a spread will produce cash, same as an decrease in margin
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
        builder.setOrderId("OrderC").setOrderDescription("Sell to open 4 MSFT 80.00/82.00 put spread @ LM 1.50")
            .setOrderMaintenanceCost("-600.00").setOrderInitialCost("-600.00")
            .addOrder();
        
        builder.addAccount("account2");
        

        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
    
    public static PairingRequest buildRequestOrder3() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // Build BP root
        builder.setDeliverableSymbol("BP").setDeliverableQty("100").setDeliverablePrice("134.03").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("BP").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // Build CSCO root
        builder.setDeliverableSymbol("CSCO").setDeliverableQty("100").setDeliverablePrice("84.30").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("CSCO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        
        // Account THX-1138 has two MSFT positions
        builder.setPositionSymbol("MSFT  160115P00080000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("28.70").addPosition();
        builder.setPositionSymbol("MSFT  160115P00082000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("82.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("30.19").addPosition();
        
        // now will sell-to-close 3 of this spread, and also sell-to-open 2 of a different short call spread
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
        builder.setOrderId("Order_MSFT-1").setOrderDescription("Sell to close 3 MSFT 80.00/82.00 put spread @ LM 1.50")
            .setOrderMaintenanceCost("-450.00").setOrderInitialCost("-450.00")
            .addOrder();
        
        builder.setOrderLegSymbol("MSFT  160115C00070000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-2)
            .setOrderLegOptionType(OptionType.C).setOrderLegOptionStrike("70.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("1.40")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115C00072000").setOrderLegOptionRoot("MSFT").setOrderLegQty(2)
            .setOrderLegOptionType(OptionType.C).setOrderLegOptionStrike("72.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("0.50")
            .addOrderLeg();
        builder.setOrderId("Order_MSFT-2").setOrderDescription("Sell to open 2 MSFT 70.00/72.00 put spread @ LM 1.50")
            .setOrderMaintenanceCost("-300.00").setOrderInitialCost("-300.00")
            .addOrder();
        
        // Account THX-1138 also has three CSCO positions, and no orders for CSCO
        builder.setPositionSymbol("CSCO  160115C00060000").setPositionOptionRoot("CSCO").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("25.04").addPosition();
        builder.setPositionSymbol("CSCO  160115C00067500").setPositionOptionRoot("CSCO").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("67.50").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("17.50").addPosition();
        builder.setPositionSymbol("CSCO  160115C00075000").setPositionOptionRoot("CSCO").setPositionQty(-1)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("75.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("9.30").addPosition();

        // And, account THX-1138 placed an order to buy two BP covered calls
        // the covered call is interesting to estimate an order cost, because the stock leg would generally
        // cost 50% of the fill price (initial margin req) but the incoming cash from the short sale of
        // the option leg will offset at 100% of the incoming cash. The pairing library doesn't care
        // how the estimate is made, just that one exists. 
        // In this example, it's pretty much estimating that the order will fill at the market prices
        // i.e.:
        //    buy stock leg fills (200 * 134.03 * 50% margin req) = 1340.30
        //    sell option leg fills (-2 * 2.30 * 100 multiplier) = -460.00
        // so the net cost to buying power is estimated to be 880.30
        builder.setOrderLegSymbol("BP    160115C00140000").setOrderLegOptionRoot("BP").setOrderLegQty(-2)
            .setOrderLegOptionType(OptionType.C).setOrderLegOptionStrike("140.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("2.30")
            .addOrderLeg();
        builder.setOrderLegSymbol("BP").setOrderLegQty(200).setOrderLegPrice("134.03").addOrderLeg();
        builder.setOrderId("Order_BP-1").setOrderDescription("Buy to open 2 BP Covered Call @ LM 80.00")
            .setOrderMaintenanceCost("880.30").setOrderInitialCost("880.30")
            .addOrder();
        
        // now we're ready to add THX-1138
        builder.addAccount("THX-1138");
        
        
        
        
        // Account 2 OrderC is sell-to-open a spread
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
    
        // buying a spread will consume cash, same as an increase in margin
        builder.setOrderId("OrderC").setOrderDescription("Sell to open 4 MSFT 80.00/82.00 put spread @ LM 1.50")
            // selling a spread will produce cash, same as an decrease in margin
            .setOrderMaintenanceCost("-600.00").setOrderInitialCost("-600.00")
            .addOrder();
        
        builder.addAccount("account2");
        

        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
    public static PairingRequest buildRequestOrder4() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // MSFT holdings, 2 put option spreads, plus extra puts
        builder.setPositionSymbol("MSFT  160115P00050000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.50").addPosition();
        builder.setPositionSymbol("MSFT  160115P00052000").setPositionOptionRoot("MSFT").setPositionQty(7)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("52.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.32").addPosition();
        
        // order to close only the extra long
        builder.setOrderLegSymbol("MSFT  160115P00052000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("52.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("1.32")
            .addOrderLeg();
        builder.setOrderId("OrderClose-Ga").setOrderDescription("Sell to close 3 MSFT 52 puts @ LM 2.00")
            .setOrderMaintenanceCost("-600.00").setOrderInitialCost("-600.00")
            .addOrder();
        
        builder.addAccount("Account-Gallant");
        
        
        // MSFT holdings, 2 put option spreads, plus extra puts
        builder.setPositionSymbol("MSFT  160115P00050000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.50").addPosition();
        builder.setPositionSymbol("MSFT  160115P00052000").setPositionOptionRoot("MSFT").setPositionQty(7)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("52.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.32").addPosition();
        
        // first order to close only the extra long
        builder.setOrderLegSymbol("MSFT  160115P00052000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("52.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("1.32")
            .addOrderLeg();
        builder.setOrderId("OrderExtra-Go").setOrderDescription("Sell to close 3 MSFT 52 puts @ LM 2.00")
            .setOrderMaintenanceCost("-600.00").setOrderInitialCost("-600.00")
            .addOrder();
        // second order, EXACTLY the same as the first order!
        builder.setOrderLegSymbol("MSFT  160115P00052000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("52.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("1.32")
            .addOrderLeg();
        builder.setOrderId("OrderOver-Go").setOrderDescription("Sell to close 3 MSFT 52 puts @ LM 2.00")
            .setOrderMaintenanceCost("-600.00").setOrderInitialCost("-600.00")
            .addOrder();
        
        builder.addAccount("Account-Goofus");

        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
    public static PairingRequest buildRequestOrder4_1() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // MSFT holdings, 2 put spreads, plus extra puts in a different symbol
        builder.setPositionSymbol("MSFT  160115P00050000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.50").addPosition();
        builder.setPositionSymbol("MSFT  160115P00052000").setPositionOptionRoot("MSFT").setPositionQty(3)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("52.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.32").addPosition();
        builder.setPositionSymbol("MSFT  160115P00054000").setPositionOptionRoot("MSFT").setPositionQty(4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("54.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("3.30").addPosition();
    
        // order to close only the extra long
        builder.setOrderLegSymbol("MSFT  160115P00054000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("54.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("3.30")
            .addOrderLeg();
        builder.setOrderId("OrderClose-Ga").setOrderDescription("Sell to close 3 MSFT 54 puts @ LM 4.00")
            .setOrderMaintenanceCost("-1200.00").setOrderInitialCost("-1200.00")
            .addOrder();
        
        builder.addAccount("Account-Gallant");
        
        
        // MSFT holdings, 2 put spreads, plus extra puts in a different symbol
        builder.setPositionSymbol("MSFT  160115P00050000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.50").addPosition();
        builder.setPositionSymbol("MSFT  160115P00052000").setPositionOptionRoot("MSFT").setPositionQty(3)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("52.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.32").addPosition();
        builder.setPositionSymbol("MSFT  160115P00054000").setPositionOptionRoot("MSFT").setPositionQty(4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("54.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("3.30").addPosition();
    
        // first order to close only the extra long
        builder.setOrderLegSymbol("MSFT  160115P00054000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("54.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("3.30")
            .addOrderLeg();
        builder.setOrderId("OrderExtra-Go").setOrderDescription("Sell to close 3 MSFT 54 puts @ LM 2.10")
            .setOrderMaintenanceCost("-630.00").setOrderInitialCost("-630.00")
            .addOrder();
        // second order, only causes a problem in combination with the first order!
        builder.setOrderLegSymbol("MSFT  160115P00052000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-3)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("52.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("1.32")
            .addOrderLeg();
        builder.setOrderId("OrderOver-Go").setOrderDescription("Sell to close 3 MSFT 52 puts @ LM 2.00")
            .setOrderMaintenanceCost("-600.00").setOrderInitialCost("-600.00")
            .addOrder();
        
        builder.addAccount("Account-Goofus");

        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
    @Test
    public void buildRequestOrder5Test() {
        PairingRequest pairingRequest = buildRequestOrder5();
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
    }
    
    public static PairingRequest buildRequestOrder5() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        builder.setPositionSymbol("MSFT").setPositionQty(10).setPositionPrice("60.40").addPosition();

        // OrderA will close some of the existing position
        builder.setOrderLegSymbol("MSFT").setOrderLegQty(-4).setOrderLegPrice("60.40").addOrderLeg();
        builder.setOrderId("OrderA").setOrderDescription("Sell to close 4 MSFT @ LM 66.00")
            .setOrderMaintenanceCost("-132.00").setOrderInitialCost("-132.00")
            .addOrder();
        
        // OrderB will open more of the existing position
        builder.setOrderLegSymbol("MSFT").setOrderLegQty(3).setOrderLegPrice("60.40").addOrderLeg();
        builder.setOrderId("OrderB").setOrderDescription("Buy to open 3 MSFT @ LM 52.00")
            .setOrderMaintenanceCost("78.00").setOrderInitialCost("78.00")
            .addOrder();
        
        builder.addAccount("accountStockOnly");
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
    public static PairingRequest buildRequestOrder6() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // MSFT holdings, put spread symbols
        builder.setPositionSymbol("MSFT  160115P00080000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("28.70").addPosition();
        builder.setPositionSymbol("MSFT  160115P00082000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("82.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("30.19").addPosition();
        
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
        builder.setOrderId("OrderA").setOrderDescription("Buy to close 4 MSFT 80/82 put spread @ LM 0.75")
            .setOrderMaintenanceCost("300.00").setOrderInitialCost("300.00")
            .addOrder();
    
        builder.addAccount("account1");

        builder.setRequestAllStrategyLists(false);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }

    
}
