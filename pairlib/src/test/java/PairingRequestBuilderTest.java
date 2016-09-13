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
    
    @Test 
    public void build1() {
        PairingRequest pairingRequest = buildRequest1();
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(0).getSymbol(), "MSFT  160115C00047500");
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(4).getSymbol(), "MSFT  160115P00082000");
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(3).getOptionConfig().getOptionType(), OptionType.P);
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(6).getQty(), new Integer(6));
        
        assertEquals(pairingRequest.getAccounts().get(1).getAccountId(), "Nike account 1");
        assertEquals(pairingRequest.getAccounts().get(1).getPositions().get(1).getOptionConfig().getOptionRoot(), "NKE1");
        
    }
    
    public static PairingRequest buildRequest1() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // MSFT holdings, 3 call options symbols, 2 put option symbols, using OSI standard (https://en.wikipedia.org/wiki/Option_symbol)
        builder.setPositionSymbol("MSFT  160115C00047500").setPositionOptionRoot("MSFT").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("47.50").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("13.06").addPosition();
        builder.setPositionSymbol("MSFT  160115C00050000").setPositionOptionRoot("MSFT").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("10.40").addPosition();
        builder.setPositionSymbol("MSFT  160115C00055000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("5.01").addPosition();
        builder.setPositionSymbol("MSFT  160115P00080000").setPositionOptionRoot("MSFT").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("28.70").addPosition();
        builder.setPositionSymbol("MSFT  160115P00082000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("82.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("30.19").addPosition();
        
        // Build CSCO root
        builder.setDeliverableSymbol("CSCO").setDeliverableQty("100").setDeliverablePrice("84.30").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("CSCO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // the description can be any String,
        builder.setPositionSymbol("CSCO  160115C00060000").setPositionDescription("CSCO Jan-16 60 Call").setPositionOptionRoot("CSCO").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("25.04").addPosition();
        builder.setPositionSymbol("CSCO  160115C00067500").setPositionDescription("CSCO Jan-16 67.50 Call").setPositionOptionRoot("CSCO").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("67.50").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("17.50").addPosition();
        builder.setPositionSymbol("CSCO  160115C00075000").setPositionDescription("CSCO Jan-16 75.00 Call").setPositionOptionRoot("CSCO").setPositionQty(-1)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("75.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("9.30").addPosition();
        builder.setPositionSymbol("CSCO  160115C00030000").setPositionDescription("CSCO Jan-16 30.00 Put").setPositionOptionRoot("CSCO").setPositionQty(3)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("30.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.05").addPosition();
        builder.setPositionSymbol("CSCO  160115C00035000").setPositionDescription("CSCO Jan-16 35.00 Put").setPositionOptionRoot("CSCO").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("35.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.07").addPosition();
        // builder.setPositionSymbol("CSCO Jan-16 45.00 Put").setPositionOptionRoot("CSCO").setPositionQty(2)
        //     .setPositionOptionType(OptionType.P).setPositionOptionStrike("45.00").setPositionOptionExpiry("2016-01-15 16:00").addPosition();

        // Build BP root
        builder.setDeliverableSymbol("BP").setDeliverableQty("100").setDeliverablePrice("134.03").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("BP").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        builder.setPositionSymbol("BP    160115C00075000").setPositionOptionRoot("BP").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("75.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("59.20").addPosition();
        builder.setPositionSymbol("BP    160115C00080000").setPositionOptionRoot("BP").setPositionQty(-16)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("54.67").addPosition();
        builder.setPositionSymbol("BP    160115C00085000").setPositionOptionRoot("BP").setPositionQty(17)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("85.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("49.21").addPosition();
        builder.setPositionSymbol("BP    160115C00090000").setPositionOptionRoot("BP").setPositionQty(-4)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("90.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("44.30").addPosition();
    
        builder.setPositionSymbol("BP    160115P00045000").setPositionOptionRoot("BP").setPositionQty(6)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("45.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.04").addPosition();
        builder.setPositionSymbol("BP    160115P00050000").setPositionOptionRoot("BP").setPositionQty(-16)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("BP    160115P00055000").setPositionOptionRoot("BP").setPositionQty(17)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.30").addPosition();
        builder.setPositionSymbol("BP    160115P00060000").setPositionOptionRoot("BP").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.75").addPosition();

        builder.addAccount("account1234");
        
        // Build NKE root (non-standard deliverables, maybe an acquisition)
        builder.setDeliverableSymbol("NKE").setDeliverableQty("100").setDeliverablePrice("65.20").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setDeliverableSymbol("LULU").setDeliverableQty("45").setDeliverablePrice("40.93").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("NKE1").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // add some non-standard options
        builder.setPositionSymbol("NKE1  160115C00055000").setPositionOptionRoot("NKE1").setPositionQty(7)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("74.09").addPosition();
        builder.setPositionSymbol("NKE1  160115C00060000").setPositionOptionRoot("NKE1").setPositionQty(-3)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("60.22").addPosition();
        
        builder.addAccount("Nike account 1");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest2() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // MSFT holdings, 3 call options symbols, 2 put option symbols, using OSI standard (https://en.wikipedia.org/wiki/Option_symbol)
        builder.setPositionSymbol("MSFT  160115C00047500").setPositionOptionRoot("MSFT").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("47.50").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("12.68").addPosition();
        builder.setPositionSymbol("MSFT  160115C00050000").setPositionOptionRoot("MSFT").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("9.89").addPosition();
        builder.setPositionSymbol("MSFT  160115C00055000").setPositionOptionRoot("MSFT").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("5.89").addPosition();

        builder.addAccount("account1234");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest3() {
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

        builder.addAccount("account3");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest4() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        builder.setPositionSymbol("GPRO  160115P00060000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("14.56").addPosition();
        builder.setPositionSymbol("GPRO  160115C00040000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.57").addPosition();
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115P00060000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.75").addPosition();

        builder.addAccount("account3");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
}
