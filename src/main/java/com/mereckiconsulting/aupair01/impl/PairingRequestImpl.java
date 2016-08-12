package com.mereckiconsulting.aupair01.impl;

import java.util.List;

import com.mereckiconsulting.aupair01.pairing.PairingRequest;
import com.mereckiconsulting.aupair01.structure.Leg;
import com.mereckiconsulting.aupair01.structure.OptionRoot;

class PairingRequestImpl implements PairingRequest {
    private final List<Leg> legs;
    private final List<OptionRoot> optionRoots;
    
    PairingRequestImpl (List<Leg> legs, List<OptionRoot> optionRoots) {
        this.legs = legs;
        this.optionRoots = optionRoots;
    }
    
    @Override
    public List<Leg> getLegs() {
        return legs;
    }
    @Override
    public List<OptionRoot> getOptionRoots() {
        return optionRoots;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PairingRequestImpl: {legs:");
        builder.append(legs);
        builder.append(", optionRoots:");
        builder.append(optionRoots);
        builder.append("}");
        return builder.toString();
    }
}
