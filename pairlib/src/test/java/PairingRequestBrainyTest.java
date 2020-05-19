import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PairingRequestBrainyTest extends PairingRequestBase  {
    
    
    @Test
    public void buildAndPair1() {
        PairingRequest pairingRequest = PairingRequestBrainyBuildTest.buildRequest1();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        AccountPairingResponse accountPairingResponse = responseByAccount.get("BrainyCondor");
         Map<String, List<Strategy>> account1result = accountPairingResponse.getStrategies();
        boolean found = findStrategy(account1result, "FB", "IronCondorShort", 1, new BigDecimal("2000.00"));
        assertTrue(found);
    }
    

}
