package com.gots.intelligentnursing.entity;

/**
 * @author zhqy
 * @date 2018/4/17
 */

public class DataEvent<T> {
    private int code;
    private String msg;
    private T data;

    public DataEvent() {
    }

    public DataEvent(int code) {
        this.code = code;
    }

    public DataEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DataEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public DataEvent(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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
}
