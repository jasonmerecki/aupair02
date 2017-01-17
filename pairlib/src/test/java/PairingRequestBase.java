import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.OrderPairingResult;
import com.jkmcllc.aupair01.pairing.PairingRequest;
import com.jkmcllc.aupair01.pairing.PairingResponse;
import com.jkmcllc.aupair01.pairing.WorstCaseOrderOutcome;
import com.jkmcllc.aupair01.pairing.impl.PairingService;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public class PairingRequestBase {
    
    protected PairingService pairingService = null;
    
    @Before
    public void setUp() {
        pairingService = PairingService.getInstance();
    }

    protected void commonTestAndPrintOutput(PairingResponse pairingResponse, int accountsInRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("Output for PairingRequest\n");
        assertNotNull(pairingResponse);
        Map<String, AccountPairingResponse> responseByAccount = pairingResponse.getResultsByAccount();
        assertNotNull(responseByAccount);
        assertEquals(accountsInRequest, responseByAccount.size());
        for (Map.Entry<String, AccountPairingResponse> entry : responseByAccount.entrySet()) {
            String accountId = entry.getKey();
            AccountPairingResponse accountResponse = entry.getValue();
            Map<String, List<Strategy>> strategies = accountResponse.getStrategies();
            Map<String, Map<String, List<Strategy>>> allStrategyListResults = accountResponse.getAllStrategyListResults();
            sb.append("AccountID '").append(accountId);
            if (allStrategyListResults == null) {
                BigDecimal totalMaintMargin = accountResponse.getTotalMaintenanceMargin();
                BigDecimal totalInitialMargin = accountResponse.getTotalInitialMargin();
                sb.append("'\n- Positions -\ntotal initial margin=").append(totalInitialMargin).append(" total maintenance margin=").append(totalMaintMargin);
                sb.append("\nposition strategies:").append("\n");
                if (strategies.entrySet().isEmpty()) {
                    sb.append("(none)\n");
                }
                for (Map.Entry<String, List<Strategy>> entry2 : strategies.entrySet()) {
                    String optionRoot = entry2.getKey();
                    sb.append("Option root '").append(optionRoot).append("'\n");
                    if (entry2.getValue().isEmpty()) {
                        sb.append("(no strategies)\n");
                    }
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
            
            if (accountResponse.getWorstCaseOrderOutcomes() != null) {
                sb.append("- Orders -\n");
                accountResponse.getWorstCaseOrderOutcomes().entrySet().forEach( e -> {
                    
                    WorstCaseOrderOutcome worstOrderOutcome = e.getValue();
                    String optionRoot = e.getKey();
                    sb.append("Option root '").append(optionRoot).append("'\n");
                    List<Strategy> worstOrderStrategies = worstOrderOutcome.getStrategies();
                    BigDecimal totalInitialMargin = AccountPairingResponse.getInitialMargin(worstOrderStrategies);
                    BigDecimal totalMaintMargin = AccountPairingResponse.getMaintenanceMargin(worstOrderStrategies);
                    List<OrderPairingResult> worstSelectedOrders = worstOrderOutcome.getOrders().stream().filter(o -> o.isWorstCaseOutcome()).collect(Collectors.toList());
                    BigDecimal costInitialWorstOrders = OrderPairingResult.getOrderInitialCost(worstSelectedOrders);
                    BigDecimal costMaintenanceWorstOrders = OrderPairingResult.getOrderMaintenanceCost(worstSelectedOrders);
                    sb.append("total *worst* initial margin=").append(totalInitialMargin)
                        .append(", total *worst* maintenance margin=").append(totalMaintMargin)
                        .append(", total *worst* initial cost=").append(costInitialWorstOrders)
                        .append(", total *worst* maintenance cost=").append(costMaintenanceWorstOrders);
                    sb.append("\nworstOrderStrategies:").append("\n");
                    if (worstOrderStrategies.isEmpty()) {
                        sb.append("(none)\n");
                    }
                    for (Strategy strategy : worstOrderStrategies) {
                        sb.append(strategy).append("\n");
                    }
                    sb.append("order details:").append("\n");
                    for (OrderPairingResult order : worstOrderOutcome.getOrders()) {
                        sb.append(order).append("\n");
                    }
                });
            }
            sb.append("\n");
        }
        sb.append("****************************************\n");
        System.out.println(sb.toString());
    }

    protected void commonPrintInput(PairingRequest pairingRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("Input for PairingRequest\n");
        pairingRequest.getOptionRoots().values().forEach( root -> {
            sb.append(root.toString()).append("\n");
        });
        sb.append("\n");
        pairingRequest.getAccounts().forEach( act -> {
            sb.append("Account: ").append(act.getAccountId()).append("\n- Positions -\n");
            if (act.getPositions().isEmpty()) {
                sb.append("(none)\n");
            }
            act.getPositions().forEach( pos -> {
                sb.append(pos).append("\n");
            });
            if (!act.getOrders().isEmpty()) {
                sb.append("- Orders -\n");
                act.getOrders().forEach( ord -> {
                    sb.append(ord).append("\n");
                });
            }
            sb.append("\n");
        });
        sb.append("\n");
        System.out.println(sb.toString());
    }

    protected boolean findStrategy(Map<String, List<Strategy>> strategyMap, String optionRoot, String strategyName, Integer quantity, BigDecimal margin) {
        boolean found = false;
        List<Strategy> strategyResultList = strategyMap.get(optionRoot);
        found = findStrategy(strategyResultList, strategyName, quantity, margin);
        return found;
    }
    
    protected boolean findWorstStrategy(AccountPairingResponse accountPairingResponse, String optionRoot, String strategyName, Integer quantity, BigDecimal margin) {
        boolean found = false;
        if (accountPairingResponse.getWorstCaseOrderOutcomes() != null) {
           Map<String, WorstCaseOrderOutcome> worstOutMap = accountPairingResponse.getWorstCaseOrderOutcomes();
           if (worstOutMap.get(optionRoot) != null) {
               WorstCaseOrderOutcome worstOut = worstOutMap.get(optionRoot);
               List<Strategy> strategyResultList = worstOut.getStrategies();
               found = findStrategy(strategyResultList, strategyName, quantity, margin);
           }
        }
        return found;
    }
    
    protected boolean findOrderOutcome(AccountPairingResponse accountPairingResponse, String optionRoot, String orderId, boolean isWorstCase, BigDecimal totalMaintenanceMargin) {
        boolean found = false;
        if (accountPairingResponse.getWorstCaseOrderOutcomes() != null) {
           Map<String, WorstCaseOrderOutcome> worstOutMap = accountPairingResponse.getWorstCaseOrderOutcomes();
           if (worstOutMap.get(optionRoot) != null) {
               WorstCaseOrderOutcome worstOut = worstOutMap.get(optionRoot);
               Optional<? extends OrderPairingResult> order = worstOut.getOrders().stream().filter( o -> o.getOrderId().equals(orderId)).findFirst();
               found = (order.isPresent() && order.get().isWorstCaseOutcome() == isWorstCase);
               if (found) {
                   OrderPairingResult orderResult = order.get();
                   found = orderResult.getMaintenanceMargin().compareTo(totalMaintenanceMargin) == 0;
               }
           }
        }

        return found;
    }
    
    private boolean findStrategy(List<Strategy> strategyResultList, String strategyName, Integer quantity, BigDecimal margin) {
        boolean found = false;
        if (strategyResultList != null) {
            for (Strategy strategy : strategyResultList) {
                if (strategyName.equals(strategy.getStrategyName())) {
                    BigDecimal strategyMargin = strategy.getMaintenanceMargin();
                    Integer strategyQuantity = strategy.getQuantity();
                    found = (strategyMargin.compareTo(margin) == 0 && strategyQuantity.compareTo(quantity) == 0);
                    if (found) break;
                }
            }
        }
        return found;
    }

}
