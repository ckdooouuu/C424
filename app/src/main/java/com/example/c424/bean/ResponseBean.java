package com.example.c424.bean;

public class ResponseBean<T> {
    private int returnCode;
    private String respMessage;
    private T respData;
    private int errorNumber;
    private String err_info;

    public ResponseBean() {

    }

    public ResponseBean(int returnCode, String respMessage, T respData, int errorNumber, String err_info) {
        this.returnCode = returnCode;
        this.respMessage = respMessage;
        this.respData = respData;
        this.errorNumber = errorNumber;
        this.err_info = err_info;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public T getRespData() {
        return respData;
    }

    public void setRespData(T respData) {
        this.respData = respData;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public String getErr_info() {
        return err_info;
    }

    public void setErr_info(String err_info) {
        this.err_info = err_info;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "returnCode=" + returnCode +
                ", respMessage='" + respMessage + '\'' +
                ", respData=" + respData +
                ", errorNumber=" + errorNumber +
                ", err_info='" + err_info + '\'' +
                '}';
    }
}
