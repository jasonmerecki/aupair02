package com.jkmcllc.aupair01.structure.impl;

import java.util.Map;

import com.jkmcllc.aupair01.pairing.AccountPairingResponse;
import com.jkmcllc.aupair01.pairing.PairingResponse;

class PairingResponseImpl implements PairingResponse {

    private final Map<String, AccountPairingResponse> accountResultMap ;
    
    PairingResponseImpl(Map<String, AccountPairingResponse> resultMap) {
        this.accountResultMap = resultMap;
    };
    
    @Override
    public Map<String, AccountPairingResponse> getResultsByAccount() {
        return accountResultMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PairingResponseImpl {accountResultMap: ");
        builder.append(accountResultMap);
        builder.append("}");
        return builder.toString();
    }
    
    

}
