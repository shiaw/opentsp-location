package com.navinfo.tasktracker.nilocation.entity;

import com.navinfo.tasktracker.nilocation.calculate.InterfaceResultCode;

import java.io.Serializable;

/**
 *@author zhangyue
 */
public class SuccessResponse<T> implements Serializable {

    private String message;
    private Integer resultCode;
    private T data;
    public SuccessResponse(){
        this.resultCode = ReturnCode.OK.code();
        this.message = ReturnCode.OK.message();
    }
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

    public <T extends SuccessResponse> T fillResult(InterfaceResultCode result)
    {
        this.setResultCode(result.code());
        this.setMessage(result.message());
        return (T)this;
    }

}