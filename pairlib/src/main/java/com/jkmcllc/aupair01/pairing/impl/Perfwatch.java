package com.jkmcllc.aupair01.pairing.impl;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Perfwatch {
    private ConcurrentMap<String, Long> cumeMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Long> startMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Integer> countMap = new ConcurrentHashMap<>();
    
    public void reset() {
        cumeMap.clear();
        startMap.clear();
        countMap.clear();
    }
    
    public void start(String group) {
        Long now = (new Date()).getTime();
        startMap.put(group, now);
    }
    
    public void stop(String group) {
        Long now = (new Date()).getTime();
        Long start = startMap.get(group);
        if (start != null) {
            Long execTime = now - start;
            cumeMap.merge(group, execTime, (e, n) -> n+e);
        }
        countMap.merge(group, 1, (e, n) -> n+e);
    }
    
    public String toString() {
        return "Perfwatch: { cumeMap: " + cumeMap + "} { countMap: " + countMap + "}";
    }
}
