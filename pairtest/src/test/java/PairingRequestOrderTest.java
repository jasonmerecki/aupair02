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
        Map<String, List<Strategy>> account1234result = responseByAccount.get("account1").getStrategies();
        boolean found = findStrategy(account1234result, "MSFT", "PutVerticalLong", 4, new BigDecimal("0"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair2() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder2(false);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);

    }
    
    @Test
    public void buildAndPair3() {
        PairingRequest pairingRequest = PairingRequestOrderBuilderTest.buildRequestOrder3(false);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);

    }
    
}
