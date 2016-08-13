package com.jkmcllc.aupair01.pairing;

import com.jkmcllc.aupair01.connect.Service;
import com.jkmcllc.aupair01.pairing.impl.PairingServiceImpl;

public interface PairingService extends Service<PairingRequest, PairingResponse> {

    @Override
    default public PairingResponse process(PairingRequest request) {
        PairingService pairingService = PairingServiceImpl.getInstance();
        PairingResponse response = pairingService.process(request);
        return response;
    }

}
