package com.navinfo.opentsp.platform.location.protocol.common;

/**
 * @author Lenovo
 * @date 2016-12-13
 * @modify
 * @copyright
 */
public class LCCustomException extends Exception {
    private String errorMessage = "";
    private static final long serialVersionUID =1L;

    public LCCustomException(String errorMessage){
        super(errorMessage);
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}