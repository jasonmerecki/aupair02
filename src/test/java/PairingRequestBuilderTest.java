import org.junit.Test;
import static org.junit.Assert.*;

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
public class PairingRequestBuilderTest {
    @Test public void build1() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).addOptionRoot();
        
        // MSFT holdings, 3 call options symbols, 2 put option symbols, using OSI standard (https://en.wikipedia.org/wiki/Option_symbol)
        builder.setPositionSymbol("MSFT  160115C00047500").setPositionOptionRoot("MSFT").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("47.50").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        builder.setPositionSymbol("MSFT  160115C00050000").setPositionOptionRoot("MSFT").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        builder.setPositionSymbol("MSFT  160115C00055000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        builder.setPositionSymbol("MSFT  160115P00080000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        builder.setPositionSymbol("MSFT  160115P00082000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("82.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        
        // Build CSCO root
        builder.setDeliverableSymbol("CSCO").setDeliverableQty("100").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("CSCO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).addOptionRoot();
        
        // the symbol can be any String, including the standard OSI symbol or a description
        builder.setPositionSymbol("CSCO Jan-16 60 Call").setPositionOptionRoot("CSCO").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        builder.setPositionSymbol("CSCO Jan-16 67.50 Call").setPositionOptionRoot("CSCO").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("67.50").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
    
        builder.addAccount("account1234");
        
        // Build NKE root (non-standard deliverables, maybe an acquisition)
        builder.setDeliverableSymbol("NKE").setDeliverableQty("100").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setDeliverableSymbol("LULU").setDeliverableQty("45").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("NKE1").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).addOptionRoot();
        
        // add some non-standard options
        builder.setPositionSymbol("NKE1  160115C00055000").setPositionOptionRoot("NKE1").setPositionQty(7)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        builder.setPositionSymbol("NKE1  160115C00060000").setPositionOptionRoot("NKE1").setPositionQty(-3)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();
        
        builder.addAccount("Nike account 1");
        
        PairingRequest pairingRequest = builder.build();
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(0).getSymbol(), "MSFT  160115C00047500");
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(4).getSymbol(), "MSFT  160115P00082000");
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(3).getOptionConfig().getOptionType(), OptionType.P);
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(6).getQty(), new Integer(6));
        
        assertEquals(pairingRequest.getAccounts().get(1).getAccountId(), "Nike account 1");
        assertEquals(pairingRequest.getAccounts().get(1).getPositions().get(1).getOptionConfig().getOptionRoot(), "NKE1");
        
    }
}
