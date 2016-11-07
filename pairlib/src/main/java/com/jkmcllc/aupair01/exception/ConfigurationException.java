package com.jkmcllc.aupair01.exception;

public class ConfigurationException extends RuntimeException {
    private static final long serialVersionUID = -2029737660136377054L;
    public ConfigurationException(String message) {
        super(message);
    }
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
