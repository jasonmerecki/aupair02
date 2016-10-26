import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;


/*
 * This Java source file was auto generated by running 'gradle init --type java-library'
 *
 * @author Jason Merecki, @date 8/9/16 4:59 PM
 */
public class PairingRequestTest extends PairingRequestTestBase {
    
    @Test
    public void buildAndPair1() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest1(false);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account1234result = responseByAccount.get("account1").getStrategies();
        boolean found = findStrategy(account1234result, "MSFT", "CallVerticalShort", 2, new BigDecimal("1000"));
        assertTrue(found);
        found = findStrategy(account1234result, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account1234result, "BP", "CallButterflyShort", 4, new BigDecimal("2000"));
        assertTrue(found);
        found = findStrategy(account1234result, "BP", "CallButterflyLong", 6, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account1234result, "BP", "PutButterflyLong", 6, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account1234result, "BP", "PutButterflyShort", 4, new BigDecimal("2000"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair1_1() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest1_1();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
    }
    
    @Test
    public void buildAndPair1_2() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest1(true);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
    }
    
    @Test
    public void buildAndPair2() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest2();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes; always want 7 long call verticals even though there is enough stock to make more covered calls
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account3result = responseByAccount.get("account2").getStrategies();
        boolean found = findStrategy(account3result, "MSFT", "CallVerticalLongNoStock", 7, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account3result, "MSFT", "CoveredCall", 3, new BigDecimal("0"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair3() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest3(false);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account31result = responseByAccount.get("account3_1").getStrategies();
        boolean found = findStrategy(account31result, "GPRO", "IronButterflyShort", 5, new BigDecimal("2500"));
        assertTrue(found);
        Map<String, List<Strategy>> account3result = responseByAccount.get("account3").getStrategies();
        found = findStrategy(account3result, "GPRO", "IronButterflyLong", 6, new BigDecimal("0"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair3_1() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest3(true);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
    }
    
    @Test
    public void buildAndPair4() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest4();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account4result = responseByAccount.get("account4").getStrategies();
        boolean found = findStrategy(account4result, "GPRO", "PutUnpairedShort", 10, new BigDecimal("13430.00"));
        assertTrue(found);
        Map<String, List<Strategy>> account41result = responseByAccount.get("account4_1").getStrategies();
        found = findStrategy(account41result, "GPRO", "CallUnpairedShort", 10, new BigDecimal("7610.00"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair5() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest5();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account5result = responseByAccount.get("account5").getStrategies();
        
        // find the 5 covered call, that should have matched with the quantity -5 strike 40 calls b/c they are lower strike
        boolean found = findStrategy(account5result, "GPRO", "CoveredCall", 5, new BigDecimal("0"));
        assertTrue(found);
        
        // and find the 6 quantity covered put, that should have matched with the quantity -6 strike 45 puts b/c they are higher strike
        Map<String, List<Strategy>> account6result = responseByAccount.get("account5_1").getStrategies();
        found = findStrategy(account6result, "GPRO", "CoveredPut", 6, new BigDecimal("0"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair6() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest6();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
    }
    
    @Test
    public void buildAndPair7() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest7();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account71result = responseByAccount.get("account7_1").getStrategies();
        boolean found = findStrategy(account71result, "GPRO", "IronCondorCalendarLong", 8, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account71result, "GPRO", "PutVerticalCalendarLong", 2, new BigDecimal("0"));
        assertTrue(found);
        
        Map<String, List<Strategy>> account7result = responseByAccount.get("account7").getStrategies();
        found = findStrategy(account7result, "GPRO", "IronCondorCalendarShort", 8, new BigDecimal("8000"));
        assertTrue(found);
        found = findStrategy(account7result, "GPRO", "PutVerticalCalendarShort", 2, new BigDecimal("2000"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair8() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest8();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 4);

        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account81result = responseByAccount.get("account8_1").getStrategies();
        boolean found = findStrategy(account81result, "GPRO", "CallBrokenCondorLong", 8, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account81result, "GPRO", "CallVerticalShort", 3, new BigDecimal("3000"));
        assertTrue(found);
        
        Map<String, List<Strategy>> account8result = responseByAccount.get("account8").getStrategies();
        found = findStrategy(account8result, "GPRO", "PutBrokenCondorLong", 8, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account8result, "GPRO", "PutVerticalShort", 2, new BigDecimal("2000"));
        assertTrue(found);
        
        Map<String, List<Strategy>> account82result = responseByAccount.get("account8_2").getStrategies();
        found = findStrategy(account82result, "GPRO", "CallBrokenButterflyLong", 4, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account82result, "GPRO", "CallUnpairedLong", 4, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account82result, "GPRO", "CallUnpairedLong", 7, new BigDecimal("0"));
        assertTrue(found);
        
        Map<String, List<Strategy>> account83result = responseByAccount.get("account8_3").getStrategies();
        found = findStrategy(account83result, "GPRO", "PutBrokenButterflyLong", 5, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account83result, "GPRO", "PutUnpairedLong", 3, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account83result, "GPRO", "PutUnpairedLong", 5, new BigDecimal("0"));
        assertTrue(found);

    }
    
}
