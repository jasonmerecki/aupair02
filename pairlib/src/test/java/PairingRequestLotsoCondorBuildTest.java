import org.junit.Test;
import static org.junit.Assert.*;


import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingRequest.PairingRequestBuilder;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.UnderlyerType;

public class PairingRequestLotsoCondorBuildTest {
    
    @Test
    public void buildRequest3Test() {
        PairingRequest request = buildRequest3();
        assertNotNull(request);
    }

    public static PairingRequest buildRequest3() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("SPX").setDeliverableQty("100").setDeliverablePrice("2351.16").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("SPX").setOptionRootExerciseStyle(ExerciseStyle.E)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        builder.setPositionSymbol("SPX   170224P02045000").setPositionOptionRoot("SPX").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2045.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("0.125").addPosition();
        builder.setPositionSymbol("SPX   170224P02050000").setPositionOptionRoot("SPX").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2050.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("0.175").addPosition();
        builder.setPositionSymbol("SPX   170224C02365000").setPositionOptionRoot("SPX").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2365.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("3.50").addPosition();
        builder.setPositionSymbol("SPX   170224C02370000").setPositionOptionRoot("SPX").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2370.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("2.625").addPosition();
        builder.setPositionSymbol("SPX   170224C02305000").setPositionOptionRoot("SPX").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2305.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("41.40").addPosition();
        builder.setPositionSymbol("SPX   170224C02315000").setPositionOptionRoot("SPX").setPositionQty(10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2315.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("32.45").addPosition();
        builder.setPositionSymbol("SPX   170224C02175000").setPositionOptionRoot("SPX").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2175.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("SPX   170224C02180000").setPositionOptionRoot("SPX").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2180.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("SPX   170224C02400000").setPositionOptionRoot("SPX").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2400.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("1.05").addPosition();
        builder.setPositionSymbol("SPX   170224C02405000").setPositionOptionRoot("SPX").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2405.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.80").addPosition();

        
        builder.addAccount("LotsoCondor");
        
        builder.setRequestAllStrategyLists(false);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }

}
