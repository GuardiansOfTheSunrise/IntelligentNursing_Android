package com.gots.intelligentnursing.entity;

/**
 * @author zhqy
 * @date 2018/4/17
 */

public class DataEvent {

    private String mAction;
    private int mCode;
    private String mMsg;
    private Object mData;


    public DataEvent(String action) {
        mAction = action;
    }

    public DataEvent(String action, Object data) {
        mAction = action;
        mData = data;
    }

    public DataEvent(String action, int code) {
        mAction = action;
        mCode = code;
    }

    public DataEvent(String action, int code, String msg) {
        mAction = action;
        mCode = code;
        mMsg = msg;
    }

    public DataEvent(String action, int code, Object data) {
        mAction = action;
        mCode = code;
        mData = data;
    }

    public DataEvent(String action, int code, String msg, Object data) {
        mAction = action;
        mCode = code;
        mMsg = msg;
        mData = data;
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

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        mData = data;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }
}
