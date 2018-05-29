package com.gots.intelligentnursing.entity;

/**
 * @author zhqy
 * @date 2018/4/17
 */

public class DataEvent<T> {

    private int mCode;
    private String mMsg;
    private T mData;
    private String mAction;

    public DataEvent(String action) {
        mAction = action;
    }

    public DataEvent(int code, String action) {
        mCode = code;
        mAction = action;
    }

    public DataEvent(int code, String msg, String action) {
        mCode = code;
        mMsg = msg;
        mAction = action;
    }

    public DataEvent(int code, T data, String action) {
        mCode = code;
        mData = data;
        mAction = action;
    }

    public DataEvent(int code, String msg, T data, String action) {
        mCode = code;
        mMsg = msg;
        mData = data;
        mAction = action;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }
}
