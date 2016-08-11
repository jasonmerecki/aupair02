package com.mereckiconsulting.aupair01.pairing;

import java.util.List;

import com.mereckiconsulting.aupair01.structure.Leg;
import com.mereckiconsulting.aupair01.structure.OptionRoot;

public class PairingRequestImpl implements PairingRequest {
    private final List<Leg> legs;
    private final List<OptionRoot> optionRoots;
    
    public PairingRequestImpl (List<Leg> legs, List<OptionRoot> optionRoots) {
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
}
