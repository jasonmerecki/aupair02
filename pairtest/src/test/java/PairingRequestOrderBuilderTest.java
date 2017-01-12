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
        
        
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
    
        // Order is to-close 
        // in this example, the order is to sell quantity 4 of the spread at $2.00
        //     (sell -4 quantity) * (2.00) * (100 price multiplier) = -800.00
        // this order will bring in cash, and it is represented as a reduction in margin
        // which will offset any increase in margin resulting from the pairing result
        // note that the margin could also be reduced by less, if there was $7.50 in commission & fees,
        // then the margin reduction would actually be -792.50 instead of -800.00
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
        PairingRequest pairingRequest = buildRequestOrder2(false);
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
    }
    
    public static PairingRequest buildRequestOrder2(boolean requestAllStrategyLists) {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // Order is to-open a spread
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70")
            .addOrderLeg();
        builder.setOrderLegSymbol("MSFT  160115P00082000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19")
            .addOrderLeg();
    
        // buying a spread will consume cash, same as an increase in margin
        builder.setOrderId("OrderB").setOrderDescription("Buy to open 4 MSFT 80.00/82.00 put spread @ LM 1.50")
            // buying a spread will consume cash, same as an increase in margin
            .setOrderMaintenanceCost("600.00").setOrderInitialCost("600.00")
            .addOrder();
        
        builder.addAccount("account1");

        builder.setRequestAllStrategyLists(requestAllStrategyLists);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
    public static PairingRequest buildRequestOrder3(boolean requestAllStrategyLists) {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // Order is sell-to-open a spread
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
        
        builder.addAccount("account1");

        builder.setRequestAllStrategyLists(requestAllStrategyLists);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
    
}
