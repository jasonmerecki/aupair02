import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public class PairingRequestOrderTest extends PairingRequestBase {
    
    @Test
    public void buildAndPair1() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder1(false);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        AccountPairingResponse accountPairingResponse = responseByAccount.get("account1");
        Map<String, List<Strategy>> account1result = accountPairingResponse.getStrategies();
        boolean found = findStrategy(account1result, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);
        found = findWorstStrategy(accountPairingResponse, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair2() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder2();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        AccountPairingResponse accountPairingResponse = responseByAccount.get("account1");
        boolean found = findWorstStrategy(accountPairingResponse, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);
        
        AccountPairingResponse accountPairingResponse2 = responseByAccount.get("account2");
        found = findWorstStrategy(accountPairingResponse2, "MSFT", "PutVerticalShort", 4, new BigDecimal("800.00"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair3() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder3();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        AccountPairingResponse accountPairingTHX = responseByAccount.get("THX-1138");
        boolean found = findWorstStrategy(accountPairingTHX, "BP", "CoveredCall", 2, new BigDecimal("0.00"));
        assertTrue(found);
        
        // let's make sure the expected orders are flagged as the 'worst'
        found = findOrderOutcome(accountPairingTHX, "MSFT", "Order_MSFT-1", false);
        assertTrue(found);
        found = findOrderOutcome(accountPairingTHX, "BP", "Order_BP-1", true);
        assertTrue(found);

        
        AccountPairingResponse accountPairingResponse2 = responseByAccount.get("account2");
        found = findWorstStrategy(accountPairingResponse2, "MSFT", "PutVerticalShort", 4, new BigDecimal("800.00"));
        assertTrue(found);
    }
    
    
}
