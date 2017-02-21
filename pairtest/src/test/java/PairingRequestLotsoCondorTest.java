import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public class PairingRequestLotsoCondorTest extends PairingRequestBase  {
    
    @Test
    public void buildAndPair1() {
        PairingRequest pairingRequest = PairingRequestLotsoCondorBuildTest.buildRequest3();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        AccountPairingResponse accountPairingResponse = responseByAccount.get("LotsoCondor");
        Map<String, List<Strategy>> account1result = accountPairingResponse.getStrategies();
        boolean found = findStrategy(account1result, "SPXW", "IronCondorShort", 5, new BigDecimal("2500.00"));
        assertTrue(found);

    }
    
    @Test
    public void buildAndPair4() {
        PairingRequest pairingRequest = PairingRequestLotsoCondorBuildTest.buildRequest4();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        AccountPairingResponse accountPairingResponse = responseByAccount.get("LotsoCondor");
        Map<String, List<Strategy>> account1result = accountPairingResponse.getStrategies();
        boolean found = findStrategy(account1result, "SPXW", "IronBrokenCondorShort", 5, new BigDecimal("12500.00"));
        assertTrue(found);
        
    }
    
    
}
