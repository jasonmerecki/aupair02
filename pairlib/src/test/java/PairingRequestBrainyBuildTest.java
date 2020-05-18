import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingRequest.PairingRequestBuilder;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.UnderlyerType;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class  PairingRequestBrainyBuildTest {
    
    @Test
    public void buildBrainyBuildTest() {
        PairingRequest request = buildRequest1();
        assertNotNull(request);
    }

    public static PairingRequest buildRequest1() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        // configure the option root
        builder.setDeliverableSymbol("FB").setDeliverableQty("100").setDeliverablePrice("210.88").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("FB").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();

        // long iron condor
        builder.setPositionSymbol("FB    200717P00120000").setPositionOptionRoot("FB").setPositionQty(1)
                .setPositionOptionType(OptionType.P).setPositionOptionStrike("120.00")
                .setPositionOptionExpiry("2020-07-17 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("FB    200717P00140000").setPositionOptionRoot("FB").setPositionQty(-1)
                .setPositionOptionType(OptionType.P).setPositionOptionStrike("140.00")
                .setPositionOptionExpiry("2020-07-17 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("FB    200717P00160000").setPositionOptionRoot("FB").setPositionQty(-1)
                .setPositionOptionType(OptionType.C).setPositionOptionStrike("160.00")
                .setPositionOptionExpiry("2020-07-17 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("FB    200717P00180000").setPositionOptionRoot("FB").setPositionQty(1)
                .setPositionOptionType(OptionType.C).setPositionOptionStrike("180.00")
                .setPositionOptionExpiry("2020-07-17 16:00").setPositionPrice("0.70").addPosition();


        // call calendar
        builder.setPositionSymbol("FB    200821P00150000").setPositionOptionRoot("FB").setPositionQty(1)
                .setPositionOptionType(OptionType.C).setPositionOptionStrike("150.00")
                .setPositionOptionExpiry("2020-08-21 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("FB    200619P00150000").setPositionOptionRoot("FB").setPositionQty(-1)
                .setPositionOptionType(OptionType.C).setPositionOptionStrike("150.00")
                .setPositionOptionExpiry("2020-06-19 16:00").setPositionPrice("0.70").addPosition();


        builder.setAccountStrategyGroupName("brokenIronPairing");
        builder.addAccount("BrainyCondor");
        
        builder.setRequestAllStrategyLists(false);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    


}
