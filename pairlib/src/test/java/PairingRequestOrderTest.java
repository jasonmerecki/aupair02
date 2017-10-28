import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jkmcllc.aupair01.pairing.AccountPairingRequest;
import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import com.jkmcllc.aupair01.structure.Order;

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
        found = findOrderOutcome(accountPairingTHX, "MSFT", "Order_MSFT-1", false, new BigDecimal("0"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingTHX, "MSFT", "Order_MSFT-2", true, new BigDecimal("400.00"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingTHX, "BP", "Order_BP-1", true, new BigDecimal("0"));
        assertTrue(found);

        
        AccountPairingResponse accountPairingResponse2 = responseByAccount.get("account2");
        found = findWorstStrategy(accountPairingResponse2, "MSFT", "PutVerticalShort", 4, new BigDecimal("800.00"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair4() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder4();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        
        // this is really the important one, even if I'm making a bit of a joke about it.
        // Two account holders, Goofus and Gallant, start with the same positions
        //    qty 4 long put spread
        //    qty 3 long unpaired puts
        // both accounts place a sell-to-close 3 long puts to increase buying power
        // but Goofus places *the exact same order* again, the sell-to-close 3 long puts
        // now the worst outcome for Goofus is that both orders fill, for a net decrease in buying power,
        // because the long legs of the spread will be sold and leave naked short puts. 
        
        // There's an interesting user experience choice here, and the pairing library can
        // support both. When Goofus places the second order, the 'cost' of the order could
        // be identical to the first, because there is no guarantee which order will fill first
        // and therefore no guarantee which order will release BP first. The individual order
        // is returned here with no margin because, if it fills by itself, it will
        // not add margin and be BP-releasing.  But naturally, the worst-case pairing will
        // show the net increase in margin because it considers the worst outcome from all
        // orders. So it's also possible to show Goofus the total change in buying power
        // as a result of placing the order. 
        
        AccountPairingResponse accountPairingGa = responseByAccount.get("Account-Gallant");
        boolean found = findWorstStrategy(accountPairingGa, "MSFT", "PutVerticalLong", 4, new BigDecimal("0.00"));
        assertTrue(found);
        
        AccountPairingResponse accountPairingGo = responseByAccount.get("Account-Goofus");
        found = findWorstStrategy(accountPairingGo, "MSFT", "PutUnpairedShort", 3, new BigDecimal("1650.00"));
        assertTrue(found);
        found = findWorstStrategy(accountPairingGo, "MSFT", "PutVerticalLong", 1, new BigDecimal("0.00"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingGo, "MSFT", "OrderOver-Go", true, new BigDecimal("0"));
        assertTrue(found);
        
    }
    
    @Test
    public void buildAndPair4_1() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder4_1();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 2);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        
        // this is a variation on the Goofus/Gallant example above, except for having
        // extra of the long put leg of a spread, the accounts hold extra long puts in a
        // different symbol. If the long spread leg is closed by itself, then the spread
        // will be re-paired using the extra long leg of the different symbol. But if 
        // the long spread leg and the long extra leg are closed, then the net impact is
        // BP-consuming. 
        // Again this is an interesting user experience question for the order preview
        // and reported impact. Each order by itself will release BP, and there is no
        // guarantee about which order will fill first, or if the orders will fill
        // at all. My opinion is that the best thing to do is show what the order impact
        // will be if it executes by itself, but calculate buying power based on
        // the worst-case outcome, which is that both orders execute in this case. 
        
        AccountPairingResponse accountPairingGa = responseByAccount.get("Account-Gallant");
        boolean found = findWorstStrategy(accountPairingGa, "MSFT", "PutVerticalLong", 4, new BigDecimal("0.00"));
        assertTrue(found);
        
        AccountPairingResponse accountPairingGo = responseByAccount.get("Account-Goofus");
        found = findWorstStrategy(accountPairingGo, "MSFT", "PutUnpairedShort", 3, new BigDecimal("1650.00"));
        assertTrue(found);
        found = findWorstStrategy(accountPairingGo, "MSFT", "PutVerticalLong", 1, new BigDecimal("0.00"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingGo, "MSFT", "OrderOver-Go", true, new BigDecimal("0"));
        assertTrue(found);
        
    }
    
    @Test
    public void buildAndPair5() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder5();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        
        // the worst-case outcome also works if the account only holds stocks and no options
        
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        AccountPairingResponse accountPairingResponse = responseByAccount.get("accountStockOnly");
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
    
    @Test
    public void buildAndPair6() {
        
        // this one confirms that when an order is BP-releasing due to option strategy changes,
        // that the order is not part of order reserves
        AccountPairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder6();
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponse = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponse);

        // the order is buy-to-close a short put spread
        // the order is releasing because closing the short spread releases 800 of requirement
        // but only costs 300 to buy out
        Map<String, List<Strategy>> account1result = accountPairingResponse.getStrategies();
        boolean found = findStrategy(account1result, "MSFT", "PutVerticalShort", 4, new BigDecimal("800.00"));
        assertTrue(found);
        found = findOrderOutcome(accountPairingResponse, "MSFT", "OrderA", false, new BigDecimal("-800.00"));
        assertTrue(found);
        
        // now we are going to add only one new order to the request
        Order newOrder = PairingRequestOrderBuilderTest.buildOrder6();
        pairingRequest.getAccount().addOrder(newOrder);
        commonPrintInput(pairingRequest);
        AccountPairingResponse accountPairingResponseWithOrder = pairingService.processAccountRequest(pairingRequest);
        commonTestAndPrintOutput(accountPairingResponseWithOrder);
        
        found = findOrderOutcome(accountPairingResponseWithOrder, "MSFT", "OrderB", true, new BigDecimal("800.00"));
        assertTrue(found);
        
    }
    
    @Test
    public void buildAndPairPFE() {
        
        // this one confirms that when an order is BP-releasing due to option strategy changes,
        // that the order is not part of order reserves
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrderPFE();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.processRequest(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
    }
}
