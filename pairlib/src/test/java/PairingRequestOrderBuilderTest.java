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
        builder.setOrderLegSymbol("MSFT  160115P00080000").setOrderLegOptionRoot("MSFT").setOrderLegQty(4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("80.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("28.70").addOrderLeg();
        builder.setPositionSymbol("MSFT  160115P00082000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setOrderLegOptionType(OptionType.P).setOrderLegOptionStrike("82.00").setOrderLegOptionExpiry("2016-01-15 16:00").setOrderLegPrice("30.19").addOrderLeg();
    
        builder.setOrderEquityInitialMargin("-800.00").setOrderEquityMaintenanceMargin("-800.00")
            .setOrderId("OrderA").setOrderDescription("Sell to close MSFT 80/82 put spread @ LM 2.00")
            .addOrder();
        
        builder.addAccount("account1");

        builder.setRequestAllStrategyLists(requestAllStrategyLists);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }
}
