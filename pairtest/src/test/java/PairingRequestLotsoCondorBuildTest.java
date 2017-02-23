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

        builder.setDeliverableSymbol("SPXW").setDeliverableQty("100").setDeliverablePrice("2344.76").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("SPXW").setOptionRootExerciseStyle(ExerciseStyle.E)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
        
        builder.setPositionSymbol("SPXW  170224P02045000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2045.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("0.125").addPosition();
        builder.setPositionSymbol("SPXW  170224P02050000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2050.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("0.175").addPosition();
        builder.setPositionSymbol("SPXW  170224C02365000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2365.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("3.50").addPosition();
        builder.setPositionSymbol("SPXW  170224C02370000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2370.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("2.625").addPosition();
        
        builder.setPositionSymbol("SPXW  170224C02305000").setPositionOptionRoot("SPXW").setPositionQty(-10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2305.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("41.40").addPosition();
        builder.setPositionSymbol("SPXW  170224C02315000").setPositionOptionRoot("SPXW").setPositionQty(10)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2315.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("32.45").addPosition();
        
        builder.setPositionSymbol("SPXW  170228P02175000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2175.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("SPXW  170228P02180000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2180.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("SPXW  170228C02400000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2400.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("1.05").addPosition();
        builder.setPositionSymbol("SPXW  170228C02405000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2405.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.80").addPosition();
        
        builder.setPositionSymbol("SPXW  170221P02110000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2110.00")
            .setPositionOptionExpiry("2017-02-21 16:00").setPositionPrice("0.10").addPosition();
        builder.setPositionSymbol("SPXW  170221P02115000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2115.00")
            .setPositionOptionExpiry("2017-02-21 16:00").setPositionPrice("0.10").addPosition();
        builder.setPositionSymbol("SPXW  170221C02340000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2340.00")
            .setPositionOptionExpiry("2017-02-21 16:00").setPositionPrice("10.35").addPosition();
        builder.setPositionSymbol("SPXW  170221C02345000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2345.00")
            .setPositionOptionExpiry("2017-02-21 16:00").setPositionPrice("7.40").addPosition();

        builder.setPositionSymbol("SPXW  170224P02105000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2105.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("0.25").addPosition();
        builder.setPositionSymbol("SPXW  170224P02110000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2110.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("0.30").addPosition();
        builder.setPositionSymbol("SPXW  170224C02350000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2350.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("8.25").addPosition();
        builder.setPositionSymbol("SPXW  170224C02355000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2355.00")
            .setPositionOptionExpiry("2017-02-24 16:00").setPositionPrice("6.30").addPosition();
        
        builder.setPositionSymbol("SPXW  170228P0211000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2110.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.40").addPosition();
        builder.setPositionSymbol("SPXW  170228P0211500").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2115.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.425").addPosition();
        builder.setPositionSymbol("SPXW  170228C0237000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2370.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("4.60").addPosition();
        builder.setPositionSymbol("SPXW  170228C0237500").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2375.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("3.60").addPosition();
        
        // broken condor (if supported, then add to the custom configuration)
        builder.setPositionSymbol("SPXW  170303P02110000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2110.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("SPXW  170303P02115000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2115.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("0.75").addPosition();
        builder.setPositionSymbol("SPXW  170303C02375000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2375.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("4.90").addPosition();
        builder.setPositionSymbol("SPXW  170303C02365000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2365.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("7.50").addPosition();
        

        builder.setPositionSymbol("SPXW  170228P0212500").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2125.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.45").addPosition();
        builder.setPositionSymbol("SPXW  170228P0213000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2130.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("0.50").addPosition(); 
        builder.setPositionSymbol("SPXW  170228C0236000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2360.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("7.25").addPosition(); 
        builder.setPositionSymbol("SPXW  170228C0236500").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2365.00")
            .setPositionOptionExpiry("2017-02-28 16:00").setPositionPrice("5.80").addPosition(); 

                
        builder.setPositionSymbol("SPXW  170310P02135000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2135.00")
            .setPositionOptionExpiry("2017-03-10 16:00").setPositionPrice("1.475").addPosition();
        builder.setPositionSymbol("SPXW  170310P02140000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2140.00")
            .setPositionOptionExpiry("2017-03-10 16:00").setPositionPrice("1.525").addPosition();
        builder.setPositionSymbol("SPXW  170310C02375000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2375.00")
            .setPositionOptionExpiry("2017-03-10 16:00").setPositionPrice("6.90").addPosition();
        builder.setPositionSymbol("SPXW  170310C02380000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2380.00")
            .setPositionOptionExpiry("2017-03-10 16:00").setPositionPrice("5.70").addPosition();
            

        builder.setPositionSymbol("SPXW  170303P02185000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2185.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.175").addPosition();
        builder.setPositionSymbol("SPXW  170303P02190000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2190.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.225").addPosition();
        
        builder.setPositionSymbol("SPXW  170303P02195000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2195.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.30").addPosition();
        builder.setPositionSymbol("SPXW  170303P02170000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2170.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.05").addPosition();

                
        builder.setPositionSymbol("SPXW  170228C0233500").setPositionOptionRoot("SPXW").setPositionQty(-6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2335.00")
            .setPositionOptionExpiry("2017-02-27 16:00").setPositionPrice("18.00").addPosition(); 
        builder.setPositionSymbol("SPXW  170228C0234000").setPositionOptionRoot("SPXW").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2340.00")
            .setPositionOptionExpiry("2017-02-27 16:00").setPositionPrice("14.80").addPosition(); 

        builder.setPositionSymbol("SPXW  170228C0235000").setPositionOptionRoot("SPXW").setPositionQty(6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2350.00")
            .setPositionOptionExpiry("2017-03-07 16:00").setPositionPrice("29.30").addPosition(); 
        builder.setPositionSymbol("SPXW  170228C0234500").setPositionOptionRoot("SPXW").setPositionQty(-6)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2345.00")
            .setPositionOptionExpiry("2017-03-07 16:00").setPositionPrice("32.10").addPosition(); 
        
        builder.setAccountStrategyGroupName("brokenIronPairing");
        builder.addAccount("LotsoCondor");
        
        builder.setRequestAllStrategyLists(false);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    
    
    public static PairingRequest buildRequest4() {
        PairingRequestBuilder builder = PairingRequest.newBuilder();

        builder.setDeliverableSymbol("SPXW").setDeliverableQty("100").setDeliverablePrice("2344.76").setDeliverableType(DeliverableType.S).addDeliverable();
        builder.setOptionRootSymbol("SPXW").setOptionRootExerciseStyle(ExerciseStyle.E)
            .setOptionRootUnderlyerType(UnderlyerType.S).setOptionRootMultiplier("100.00").addOptionRoot();
       
        
        // broken condor, by itself will select 5000 req
        builder.setPositionSymbol("SPXW  170303P02110000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2110.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("0.70").addPosition();
        builder.setPositionSymbol("SPXW  170303P02115000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2115.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("0.75").addPosition();
        builder.setPositionSymbol("SPXW  170303C02375000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2375.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("4.90").addPosition();
        builder.setPositionSymbol("SPXW  170303C02365000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.C).setPositionOptionStrike("2365.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("7.50").addPosition();

        
        /* long five-wide spread , zero req */
        builder.setPositionSymbol("SPXW  170303P02185000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2185.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.175").addPosition();
        builder.setPositionSymbol("SPXW  170303P02190000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2190.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.225").addPosition();
            
            
        
        // this is a 12500 spread, which would normally be a total of 17500
        // but the long five-wide spread can be broken apart, 
        builder.setPositionSymbol("SPXW  170303P02195000").setPositionOptionRoot("SPXW").setPositionQty(-5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2195.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.30").addPosition();
        builder.setPositionSymbol("SPXW  170303P02170000").setPositionOptionRoot("SPXW").setPositionQty(5)
            .setPositionOptionType(OptionType.P).setPositionOptionStrike("2170.00")
            .setPositionOptionExpiry("2017-03-03 16:00").setPositionPrice("1.05").addPosition();
            
        
        builder.setAccountStrategyGroupName("brokenIronPairing");
        builder.addAccount("LotsoCondorSmall");
        
        builder.setRequestAllStrategyLists(false);
        PairingRequest pairingRequest = builder.build();
        return pairingRequest;
    }
    

}
