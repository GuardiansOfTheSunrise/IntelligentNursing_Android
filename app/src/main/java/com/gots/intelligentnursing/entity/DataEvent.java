package com.gots.intelligentnursing.entity;

/**
 * @author zhqy
 * @date 2018/4/17
 */

public class DataEvent<T> {

    private int code;
    private String msg;
    private T data;
    private String action;

    public DataEvent(int code, String action) {
        this.code = code;
        this.action = action;
    }

    public DataEvent(int code, String msg, String action) {
        this.code = code;
        this.msg = msg;
        this.action = action;
    }

    public DataEvent(int code, T data, String action) {
        this.code = code;
        this.data = data;
        this.action = action;
    }

    public DataEvent(int code, String msg, T data, String action) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
