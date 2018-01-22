package com.navinfo.tasktracker.rprest.entity;

import java.io.Serializable;

/**
 *@author zhangyue
 */
public class SuccessResponse<T> implements Serializable {

    private String message;
    private Integer resultCode;
    private T data;

    public SuccessResponse(){}
    public SuccessResponse(Integer resultCode, String message){
        this.message = message;
        this.resultCode = resultCode;
    }
    /**
     * Construction Method
     * @param resultCode
     * @param message
     */
    public SuccessResponse(Integer resultCode, String message, T data){
        this.message = message;
        this.resultCode = resultCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}