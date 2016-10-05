import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.impl.PairingService;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public class PairingRequestTestBase {
    
    protected PairingService pairingService = null;
    
    @Before
    public void setUp() {
        pairingService = PairingService.getInstance();
    }

    protected void commonTestAndPrintOutput(PairingResponse pairingResponse, int accountsInRequest) {
        StringBuilder sb = new StringBuilder();
        assertNotNull(pairingResponse);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        assertNotNull(responseByAccount);
        assertEquals(accountsInRequest, responseByAccount.size());
        for (Map.Entry<String, AccountPairingResponse> entry : responseByAccount.entrySet()) {
            String accountId = entry.getKey();
            AccountPairingResponse accountResponse = entry.getValue();
            Map<String, List<Strategy>> strategies = accountResponse.getStrategies();
            BigDecimal totalMaintMargin = accountResponse.getTotalMaintenanceMargin();
            BigDecimal totalInitialMargin = accountResponse.getTotalInitialMargin();
            Map<String, Map<String, List<Strategy>>> allStrategyListResults = accountResponse.getAllStrategyListResults();
            sb.append("AccountID '").append(accountId).append("' total initial margin=").append(totalInitialMargin).append(" total maintenance margin=").append(totalMaintMargin);
            if (allStrategyListResults == null) {
                sb.append(", strategies:").append("\n");
                for (Map.Entry<String, List<Strategy>> entry2 : strategies.entrySet()) {
                    sb.append("Option root '").append(entry2.getKey()).append("'\n");
                    for (Strategy strategy : entry2.getValue()) {
                        sb.append(strategy).append("\n");
                    }
                } 
                sb.append("all strategy groups by root:").append("\n");
                sb.append(accountResponse.getStrategyGroupByRoot()).append("\n");
            } else {
                sb.append("' all strategy lists:").append("\n");
                for (Map.Entry<String, Map<String, List<Strategy>>> entry2 : allStrategyListResults.entrySet()) {
                    sb.append("Option root '").append(entry2.getKey()).append("'\n");
                    for (Map.Entry<String, List<Strategy>> strategyGroupList : entry2.getValue().entrySet()) {
                        sb.append(strategyGroupList.getKey()).append("\n");
                        for (Strategy strategy : strategyGroupList.getValue()) {
                            sb.append(strategy).append("\n");
                        }
                    }
                } 
            }
        }
        System.out.println(sb.toString());
    }

    protected void commonPrintInput(PairingRequest pairingRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("Input for PairingRequest\n");
        pairingRequest.getOptionRoots().values().forEach( root -> {
            sb.append(root.toString());
        });
        sb.append("\n");
        pairingRequest.getAccounts().forEach( act -> {
            sb.append("Account: ").append(act.getAccountId()).append("\nPositions:\n");
            act.getPositions().forEach( pos -> {
                sb.append(pos).append("\n");
            });
        });
        System.out.println(sb.toString());
    }

    protected boolean findStrategy(Map<String, List<Strategy>> strategyMap, String optionRoot, String strategyName, Integer quantity, BigDecimal margin) {
        boolean found = false;
        List<Strategy> strategyResultList = strategyMap.get(optionRoot);
        for (Strategy strategy : strategyResultList) {
            if (strategyName.equals(strategy.getStrategyName())) {
                BigDecimal strategyMargin = strategy.getMaintenanceMargin();
                Integer strategyQuantity = strategy.getQuantity();
                found = (strategyMargin.compareTo(margin) == 0 && strategyQuantity.compareTo(quantity) == 0);
                if (found) break;
            }
        }
        return found;
    }

}
