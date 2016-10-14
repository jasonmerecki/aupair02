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
public class PairingRequestTestAllStrategies extends PairingRequestTestBase {
   
    @Test
    public void buildAndPairEachAmerican() {
        PairingRequest pairingRequest = PairingRequestBuilderAllStrategies.buildEachStrategyAmerican();
        commonPrintInput(pairingRequest);
        PairingResponse pairingResponse = pairingService.service(pairingRequest);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        commonTestAndPrintOutput(pairingResponse, responseByAccount.size());
        
        
        boolean found = false;
        Map<String, List<Strategy>> singleAccountResult = null;
        
        singleAccountResult = responseByAccount.get("LongBoxSpread").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "LongBoxSpread", 5, new BigDecimal("0"));
        assertTrue(found);
        
        singleAccountResult = responseByAccount.get("ShortBoxSpread").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "ShortBoxSpread", 5, new BigDecimal("0"));
        assertTrue(found);
        
        singleAccountResult = responseByAccount.get("Collar").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "Collar", 5, new BigDecimal("0"));
        assertTrue(found);
        
        singleAccountResult = responseByAccount.get("Conversion").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "Conversion", 5, new BigDecimal("3000"));
        assertTrue(found);
        
        singleAccountResult = responseByAccount.get("Reversal").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "Reversal", 5, new BigDecimal("3000"));
        assertTrue(found);
        
        singleAccountResult = responseByAccount.get("ProtectivePut").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "ProtectivePut", 5, new BigDecimal("0"));
        assertTrue(found);
        
        singleAccountResult = responseByAccount.get("ProtectiveCall").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "ProtectiveCall", 5, new BigDecimal("0"));
        assertTrue(found);
        
        singleAccountResult = responseByAccount.get("PutCashSecured").getStrategies();
        found = findStrategy(singleAccountResult, "MSFT", "PutCashSecured", 5, new BigDecimal("30000"));
        assertTrue(found);
        
    }
    
}
