import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.impl.PairingService;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;


/*
 * This Java source file was auto generated by running 'gradle init --type java-library'
 *
 * @author Jason Merecki, @date 8/9/16 4:59 PM
 */
public class PairingRequestTest {
   
    private PairingService pairingService = null;
    
    @Before
    public void setUp() {
        pairingService = PairingService.getInstance();
    }
    
    @Test
    public void buildAndPair1() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest1();
        System.out.println("Input for " + pairingRequest + "");
        System.out.println("");
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        // test outcomes
        Map<String, Map<String, List<Strategy>>> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account1234result = responseByAccount.get("account1234");
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
    public void buildAndPair2() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest2();
        System.out.println("Input for " + pairingRequest + "");
        System.out.println("");
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
    }
    
    @Test
    public void buildAndPair3() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest3();
        System.out.println("Input for " + pairingRequest + "");
        System.out.println("");
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, Map<String, List<Strategy>>> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account3result = responseByAccount.get("account3");
        boolean found = findStrategy(account3result, "GPRO", "IronButterflyShort", 4, new BigDecimal("2000"));
        assertTrue(found);
        found = findStrategy(account3result, "GPRO", "IronButterflyLong", 1, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account3result, "GPRO", "CallVerticalShort", 1, new BigDecimal("500"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair4() {
        PairingRequest pairingRequest = PairingRequestBuilderTest.buildRequest4();
        System.out.println("Input for " + pairingRequest + "");
        System.out.println("");
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
    }
    
    private void commonTestAndPrintOutput(PairingResponse pairingResponse, int accountsInRequest) {
        assertNotNull(pairingResponse);
        Map<String, Map<String, List<Strategy>>> responseByAccount = pairingResponse.getResultsByAccount();
        assertNotNull(responseByAccount);
        assertEquals(accountsInRequest, responseByAccount.size());
        for (Map.Entry<String, Map<String, List<Strategy>>> entry : responseByAccount.entrySet()) {
            String accountId = entry.getKey();
            System.out.println("Strateiges for account '" + accountId + "'");
            for (Map.Entry<String, List<Strategy>> entry2 : entry.getValue().entrySet()) {
                System.out.println("Option root '" + entry2.getKey() + "'");
                for (Strategy strategy : entry2.getValue()) {
                    System.out.println(strategy);
                }
            }
        }
    }
    
    private boolean findStrategy(Map<String, List<Strategy>> strategyMap, String optionRoot, String strategyName, 
            Integer quantity, BigDecimal margin) {
        boolean found = false;
        List<Strategy> strategyResultList = strategyMap.get(optionRoot);
        for (Strategy strategy : strategyResultList) {
            if (strategyName.equals(strategy.getStrategyName())) {
                BigDecimal strategyMargin = strategy.getMargin();
                Integer strategyQuantity = strategy.getQuantity();
                found = (strategyMargin.compareTo(margin) == 0 && strategyQuantity.compareTo(quantity) == 0);
                if (found) break;
            }
        }
        return found;
    }
    
}
