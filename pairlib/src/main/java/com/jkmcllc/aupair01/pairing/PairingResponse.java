package com.jkmcllc.aupair01.pairing;

import java.util.List;
import java.util.Map;

import com.jkmcllc.aupair01.connect.Response;
import com.jkmcllc.aupair01.pairing.strategy.Strategy;

public interface PairingResponse extends Response {
    public static final String NAME = "PairingResponse";
    default String responseType() {return NAME;}
    public Map<String, Map<String, List<Strategy>>> getResultsByAccount();
}
