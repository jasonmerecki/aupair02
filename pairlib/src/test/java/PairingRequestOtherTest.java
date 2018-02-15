import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public class PairingRequestOtherTest extends PairingRequestBase {
	@Ignore
    public void buildAndPair3other1() {
        PairingRequest pairingRequest = PairingRequestBuilderOthers.buildRequest3(true, false);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account3result = responseByAccount.get("account3").getStrategies();
        boolean found = findStrategy(account3result, "GPRO", "CallCondorShort", 3, new BigDecimal("1500"));
        assertTrue(found);
        found = findStrategy(account3result, "GPRO", "CallBrokenButterflyLong", 2, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account3result, "GPRO", "CallVerticalLong", 1, new BigDecimal("0"));
        assertTrue(found);
    }
    
    @Test
    public void buildAndPair3other2() {
        PairingRequest pairingRequest = PairingRequestBuilderOthers.buildRequest3(true, true);
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        commonTestAndPrintOutput(pairingResponse, 1);
        // test outcomes
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        Map<String, List<Strategy>> account3result = responseByAccount.get("account3").getStrategies();
        boolean found = findStrategy(account3result, "GPRO", "IronButterflyShort", 4, new BigDecimal("2000"));
        assertTrue(found);
        found = findStrategy(account3result, "GPRO", "IronButterflyLong", 1, new BigDecimal("0"));
        assertTrue(found);
        found = findStrategy(account3result, "GPRO", "CallVerticalLong", 1, new BigDecimal("0"));
        assertTrue(found);
    }
    
}
