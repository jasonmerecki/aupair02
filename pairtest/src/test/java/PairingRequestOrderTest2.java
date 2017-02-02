import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jkmcllc.aupair01.pairing.AccountPairingRequest;
import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public class PairingRequestOrderTest2 extends PairingRequestBase {
    
    @Test
    public void buildAndPair1() {
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest2.buildRequestOrder1(false);
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);
        // test outcomes
        Map<String, List<Strategy>> account1result = accountPairingResponse.getStrategies();
        boolean found = findStrategy(account1result, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);
        found = findWorstStrategy(accountPairingResponse, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair2() {
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest2.buildRequestOrder2();
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);

        boolean found = findWorstStrategy(accountPairingResponse, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);

        assertTrue(found);
    }
    
    @Test
    public void buildAndPair2_1() {
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest2.buildRequestOrder2_1();
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);
        boolean found = findWorstStrategy(accountPairingResponse, "MSFT", "PutVerticalShort", 4, new BigDecimal("800.00"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair3() {
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest2.buildRequestOrder3();
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);

        boolean found = findWorstStrategy(accountPairingResponse, "BP", "CoveredCall", 2, new BigDecimal("0.00"));
        assertTrue(found);
        
        // let's make sure the expected orders are flagged as the 'worst'
        found = findOrderOutcome(accountPairingResponse, "MSFT", "Order_MSFT-1", false, new BigDecimal("0"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingResponse, "MSFT", "Order_MSFT-2", true, new BigDecimal("400.00"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingResponse, "BP", "Order_BP-1", true, new BigDecimal("0"));
        assertTrue(found);

    }
    
    @Test
    public void buildAndPair4() {
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest2.buildRequestOrder4();
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);

        boolean found = findWorstStrategy(accountPairingResponse, "MSFT", "PutVerticalLong", 4, new BigDecimal("0.00"));
        assertTrue(found);

    }
    
    @Test
    public void buildAndPair4_1() {
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest2.buildRequestOrder4_1();
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);
        
        boolean found = findWorstStrategy(accountPairingResponse, "MSFT", "PutUnpairedShort", 3, new BigDecimal("1650.00"));
        assertTrue(found);
        found = findWorstStrategy(accountPairingResponse, "MSFT", "PutVerticalLong", 1, new BigDecimal("0.00"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingResponse, "MSFT", "OrderOver-Go", true, new BigDecimal("0"));
        assertTrue(found);
        
    }
    
    @Test
    public void buildAndPair5() {
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest2.buildRequestOrder5();
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);
        
        // the worst-case outcome also works if the account only holds stocks and no options
        
        Map<String, List<Strategy>> account1result = accountPairingResponse.getStrategies();
        boolean found = findStrategy(account1result, "MSFT", "StockUnpairedLong", 10, new BigDecimal("0"));
        assertTrue(found);
        
        found = findWorstStrategy(accountPairingResponse, "MSFT", "StockUnpairedLong", 13, new BigDecimal("0.00"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingResponse, "MSFT", "OrderB", true, new BigDecimal("0"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingResponse, "MSFT", "OrderA", false, new BigDecimal("0"));
        assertTrue(found);
        
    }
    
    
    
}
