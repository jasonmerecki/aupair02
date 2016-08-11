package com.mereckiconsulting.aupair01.pairing;

import java.util.ArrayList;
import java.util.List;

import com.mereckiconsulting.aupair01.structure.Deliverables;
import com.mereckiconsulting.aupair01.structure.Deliverables.DeliverablesBuilder;
import com.mereckiconsulting.aupair01.structure.Leg;
import com.mereckiconsulting.aupair01.structure.OptionRoot;
import com.mereckiconsulting.aupair01.structure.OptionRoot.OptionRootBuilder;

public interface PairingRequest {
    List<Leg> getLegs();
    List<OptionRoot> getOptionRoots();

    public class PairingRequestBuilder {
        private final List<Leg> legs = new ArrayList<>();
        private final List<OptionRoot> optionRoots = new ArrayList<>();
        private PairingRequestBuilder() {}
        private final DeliverablesBuilder deliverablesBuilder = Deliverables.newBuilder();
        private final OptionRootBuilder optionRootBuilder = OptionRoot.newBuilder();
    }
}