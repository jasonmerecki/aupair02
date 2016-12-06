import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingRequest.PairingRequestBuilder;
import com.jkmcllc.aupair01.structure.DeliverableType;
import com.jkmcllc.aupair01.structure.ExerciseStyle;
import com.jkmcllc.aupair01.structure.OptionType;
import com.jkmcllc.aupair01.structure.UnderlyerType;


/*
 * This Java source file was auto generated by running 'gradle init --type java-library'
 *
 * @author Jason Merecki, @date 8/9/16 4:59 PM
 */
public class PairingRequestBuilderCustomConfig {
    
    public static PairingRequest buildCustomConfig() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // MSFT root used for this example
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // cash secured put, prohibited short call
        builder.setPositionSymbol("MSFT  160115C00070000").setPositionOptionRoot("MSFT").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.01").addPosition();
        builder.setPositionSymbol("MSFT  160115P00070000").setPositionOptionRoot("MSFT").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("3.03").addPosition();
        builder.setAccountStrategyGroupName("optionLevelCashRestricted");
        builder.addAccount("CashAcct1");

        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
        
    }

}