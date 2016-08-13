package com.jkmcllc.aupair01.connect;

public interface Service<T extends Request, U extends Response> {
    public U process(T request); 
}
