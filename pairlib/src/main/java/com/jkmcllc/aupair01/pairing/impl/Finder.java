package com.jkmcllc.aupair01.pairing.impl;

import java.util.List;

import com.jkmcllc.aupair01.pairing.strategy.Strategy;

interface Finder {
    List<? extends Strategy> find();
    <T extends Finder> T newInstance(PairingInfo pairingInfo);
}
