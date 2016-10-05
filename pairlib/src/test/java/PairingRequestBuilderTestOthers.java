import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingRequest.PairingRequestBuilder;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.UnderlyerType;

public class PairingRequestBuilderTestOthers {
    
    public static PairingRequest buildRequest3(boolean requestAllStrategyLists, boolean addExtra) {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        builder.setPositionSymbol("GPRO  160115P00045000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("45.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        builder.setPositionSymbol("GPRO  160115C00040000").setPositionOptionRoot("GPRO").setPositionQty(2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-7)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("3.03").addPosition();
        builder.setPositionSymbol("GPRO  160115C00055000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.75").addPosition();
        builder.setPositionSymbol("GPRO  160115P00030000").setPositionOptionRoot("GPRO").setPositionQty(1)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("30.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.04").addPosition();
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(-1)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.73").addPosition();
        builder.setPositionSymbol("GPRO  160115P00055000").setPositionOptionRoot("GPRO").setPositionQty(1)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("9.57").addPosition();
        builder.setPositionSymbol("GPRO  160115C00070000").setPositionOptionRoot("GPRO").setPositionQty(-3)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.08").addPosition();

        builder.setPositionSymbol("GPRO  160115C00080000").setPositionOptionRoot("GPRO").setPositionQty(-6)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        builder.setPositionSymbol("GPRO  160115C00085000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("85.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        builder.setPositionSymbol("GPRO  160115C00085000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("85.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        builder.setPositionSymbol("GPRO  160115C00090000").setPositionOptionRoot("GPRO").setPositionQty(-6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("90.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();

        /* margin changes a lot with this iron butterfly */
        if (addExtra) {
            builder.setPositionSymbol("GPRO  160115C00120000").setPositionOptionRoot("GPRO").setPositionQty(4)
                .setPositionOptionType(OptionType.P).setPositionOptionStrike("120.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
            builder.setPositionSymbol("GPRO  160115C00125000").setPositionOptionRoot("GPRO").setPositionQty(-4)
                .setPositionOptionType(OptionType.P).setPositionOptionStrike("125.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
            builder.setPositionSymbol("GPRO  160115C00125000").setPositionOptionRoot("GPRO").setPositionQty(-4)
                .setPositionOptionType(OptionType.C).setPositionOptionStrike("125.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
            builder.setPositionSymbol("GPRO  160115C00130000").setPositionOptionRoot("GPRO").setPositionQty(4)
                .setPositionOptionType(OptionType.C).setPositionOptionStrike("130.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        }
        
        builder.addAccount("account3");
        
        builder.setRequestAllStrategyLists(requestAllStrategyLists);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }

}
