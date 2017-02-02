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
        PairingRequest pairingRequest = buildRequest1(false);
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(0).getSymbol(), "MSFT  160115C00047500");
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(4).getSymbol(), "MSFT  160115P00082000");
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(3).getOptionConfig().getOptionType(), OptionType.P);
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(6).getQty(), new Integer(6));
        
        pairingRequest = buildRequest1_1();
        System.out.println(pairingRequest);
        assertNotNull(pairingRequest);
        assertEquals(pairingRequest.getAccounts().get(0).getAccountId(), "Nike account 1");
        assertEquals(pairingRequest.getAccounts().get(0).getPositions().get(1).getOptionConfig().getOptionRoot(), "NKE1");
        
    }
    
    public static PairingRequest buildRequest1(boolean requestAllStrategyLists) {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
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
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
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
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
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

        builder.addAccount("account1");

        builder.setRequestAllStrategyLists(requestAllStrategyLists);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest1_1() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build NKE root (non-standard deliverables, maybe an acquisition)
        builder.setDeliverableSymbol("NKE").setDeliverableQty("100").setDeliverablePrice("65.20").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setDeliverableSymbol("LULU").setDeliverableQty("45").setDeliverablePrice("40.93").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("NKE1").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // add some non-standard options
        builder.setPositionSymbol("NKE1  160115C00055000").setPositionOptionRoot("NKE1").setPositionQty(7)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("74.09").addPosition();
        builder.setPositionSymbol("NKE1  160115C00060000").setPositionOptionRoot("NKE1").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("60.22").addPosition();
        builder.setPositionSymbol("NKE").setPositionQty(430).setPositionPrice("65.20").addPosition();
        builder.setPositionSymbol("LULU").setPositionQty(205).setPositionPrice("40.93").addPosition();
        
        builder.addAccount("Nike account 1");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest2() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        
        // Build MSFT root, first deliverables then root information
        builder.setDeliverableSymbol("MSFT").setDeliverableQty("100").setDeliverablePrice("60.40").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("MSFT").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        builder.setPositionSymbol("MSFT  160115C00047500").setPositionOptionRoot("MSFT").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("47.50").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("12.68").addPosition();
        builder.setPositionSymbol("MSFT  160115C00050000").setPositionOptionRoot("MSFT").setPositionQty(7)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("9.89").addPosition();
        builder.setPositionSymbol("MSFT  160115C00055000").setPositionOptionRoot("MSFT").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("5.89").addPosition();
        builder.setPositionSymbol("MSFT").setPositionQty(960).setPositionPrice("60.40").addPosition();
        
        builder.addAccount("account2");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest3(boolean requestAllStrategyLists) {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();

        builder.setPositionSymbol("GPRO  160115P00045000").setPositionOptionRoot("GPRO").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("45.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-6)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-7)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("3.03").addPosition();
        builder.setPositionSymbol("GPRO  160115C00055000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("55.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.75").addPosition();

        builder.addAccount("account3_1");
        
        builder.setPositionSymbol("GPRO  160115C00080000").setPositionOptionRoot("GPRO").setPositionQty(-6)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("80.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        builder.setPositionSymbol("GPRO  160115P00085000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("85.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        builder.setPositionSymbol("GPRO  160115C00085000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("85.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        builder.setPositionSymbol("GPRO  160115C00090000").setPositionOptionRoot("GPRO").setPositionQty(-6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("90.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.50").addPosition();
        
        builder.addAccount("account3");
        
        builder.setRequestAllStrategyLists(requestAllStrategyLists);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest4() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // margin = max(((4600.00 * 0.2 + -600.0000) * 10) + 1030.0000, (40.00 * 100.00 * 0.01) + 1030.0000)
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        // margin = max(((4600.00 * 0.2 + 0) * 10) + 4230.0000, (50.00 * 100.00 * 0.01) + 4230.0000)
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        // margin = max(((4600.00 * 0.2 + 0) * 10) + 14560.0000, (60.00 * 100.00 * 0.01) + 14560.0000)
        builder.setPositionSymbol("GPRO  160115P00060000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("14.56").addPosition();
        
        builder.addAccount("account4");
        
        // margin = max(((4600.00 * 0.2 + 0) * 10) + 6570.0000, (4600.00 * 10 * 0.1) + 6570.0000)
        builder.setPositionSymbol("GPRO  160115C00040000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.57").addPosition();
        // margin = max(((4600.00 * 0.2 + -400.0000) * 10) + 2410.0000, (4600.00 * 10 * 0.1) + 2410.0000)
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();
        // margin = max(((4600.00 * 0.2 + -1400.000) * 10) + 750.0000, (4600.00 * 10 * 0.1) + 750.0000)
        builder.setPositionSymbol("GPRO  160115P00060000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("0.75").addPosition();

        builder.addAccount("account4_1");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest5() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // let's see... how about if we have stock to cover some but not all short calls, with the remainder paired with some long calls
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00040000").setPositionOptionRoot("GPRO").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.57").addPosition();
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-7)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO").setPositionQty(960).setPositionPrice("46.00").addPosition();
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();

        builder.addAccount("account5");
        
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(-4)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO").setPositionQty(-960).setPositionPrice("46.00").addPosition();
        builder.setPositionSymbol("GPRO  160115P00045000").setPositionOptionRoot("GPRO").setPositionQty(-6)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("45.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00038000").setPositionOptionRoot("GPRO").setPositionQty(3)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("38.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00040000").setPositionOptionRoot("GPRO").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("6.57").addPosition();
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();

        builder.addAccount("account5_1");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest6() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // iron condors
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115C00070000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();

 
        builder.addAccount("account6");
        
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115C00070000").setPositionOptionRoot("GPRO").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();

        builder.addAccount("account6_1");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest7() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // iron calendar condors
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115C00070000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();

 
        builder.addAccount("account7");
        
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115C00070000").setPositionOptionRoot("GPRO").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-01-15 16:00").setPositionPrice("2.41").addPosition();

        builder.addAccount("account7_1");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest8() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("GPRO").setDeliverableQty("100").setDeliverablePrice("46.00").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("GPRO").setOptionRootExerciseStyle(ExerciseStyle.A)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        // broken condor and butterfly
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115P00060000").setPositionOptionRoot("GPRO").setPositionQty(-8)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115P00075000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("75.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();

 
        builder.addAccount("account8");
        
        builder.setPositionSymbol("GPRO  160115C00035000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("35.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(-11)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();
        builder.setPositionSymbol("GPRO  160115C00070000").setPositionOptionRoot("GPRO").setPositionQty(12)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("70.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();

        builder.addAccount("account8_1");
        
        builder.setPositionSymbol("GPRO  160115C00035000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("35.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115C00050000").setPositionOptionRoot("GPRO").setPositionQty(-8)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115C00060000").setPositionOptionRoot("GPRO").setPositionQty(11)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("60.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();

        builder.addAccount("account8_2");
        
        builder.setPositionSymbol("GPRO  160115P00040000").setPositionOptionRoot("GPRO").setPositionQty(10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("40.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("1.03").addPosition();
        builder.setPositionSymbol("GPRO  160115P00050000").setPositionOptionRoot("GPRO").setPositionQty(-10)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("50.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("4.23").addPosition();
        
        builder.setPositionSymbol("GPRO  160115P00075000").setPositionOptionRoot("GPRO").setPositionQty(8)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("75.00").setPositionOptionExpiry("2016-02-19 16:00").setPositionPrice("2.41").addPosition();
    
        builder.addAccount("account8_3");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    public static PairingRequest buildRequest9() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();
        // an interesting scenario, this should be five ShortIronButterfly strategies
        // even though it's possible to also create a LongPutButterfly and spreads with the same options
        // the short iron butterfly strategies are more optimal
        builder.setDeliverableSymbol("RUT").setDeliverableQty("100").setDeliverablePrice("1359.30").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("RUTW").setOptionRootExerciseStyle(ExerciseStyle.E)
            .setOptionRootUnderlyerType(UnderlyerType.I).setOptionRootMultiplier("100.00").addOptionRoot();

        builder.setPositionSymbol("RUTW  161209P01300000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1300.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.55").addPosition();
        builder.setPositionSymbol("RUTW  161209C01350000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1350.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("14.00").addPosition();
        builder.setPositionSymbol("RUTW  161209P01350000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1350.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("3.80").addPosition();
        builder.setPositionSymbol("RUTW  161209C01400000").setPositionOptionRoot("RUTW").setPositionQty(4)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1400.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.275").addPosition();
        
        builder.setPositionSymbol("RUTW  161209P01160000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1160.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.125").addPosition();
        builder.setPositionSymbol("RUTW  161209P01280000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1280.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.25").addPosition();
        builder.setPositionSymbol("RUTW  161209C01280000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1280.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("80.10").addPosition();
        
        builder.setPositionSymbol("RUTW  161209P01220000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1220.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.40").addPosition();
        builder.setPositionSymbol("RUTW  161209P01320000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1320.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("3.075").addPosition(); // 1
        builder.setPositionSymbol("RUTW  161209C01320000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1320.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("42.35").addPosition();
        builder.setPositionSymbol("RUTW  161209C01420000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1420.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.575").addPosition();
            
        builder.setPositionSymbol("RUTW  161209P01260000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1260.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.20").addPosition();
        builder.setPositionSymbol("RUTW  161209P01325000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1325.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.675").addPosition(); // 2
        builder.setPositionSymbol("RUTW  161209C01325000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1325.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("40.45").addPosition();
        builder.setPositionSymbol("RUTW  161209C01390000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1390.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("1.15").addPosition();
        
        builder.setPositionSymbol("RUTW  161209P01250000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1250.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.175").addPosition();
        builder.setPositionSymbol("RUTW  161209P01310000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("1310.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("0.475").addPosition();
        builder.setPositionSymbol("RUTW  161209C01310000").setPositionOptionRoot("RUTW").setPositionQty(-2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1310.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("50.30").addPosition();
        builder.setPositionSymbol("RUTW  161209C01370000").setPositionOptionRoot("RUTW").setPositionQty(2)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("1370.00")
            .setPositionOptionExpiry("2016-12-09 16:00").setPositionPrice("3.25").addPosition();
        
        builder.addAccount("accountAllShortIronButterfly");
        
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    
}
