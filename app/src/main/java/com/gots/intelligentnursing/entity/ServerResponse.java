package com.gots.intelligentnursing.entity;

import com.gots.intelligentnursing.exception.ServerException;

/**
 * 服务器返回的数据格式
 * code为0时表示成功的请求，并把相关数据置于data中
 * code不为0表示失败的请求，msg中有错误信息
 * @author zhqy
 * @date 2018/3/29
 */

public class ServerResponse<T> {

    public static final int CODE_SUCCESS = 0;

    private int code;
    private String msg;
    private T data;

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

    public void checkCode() throws ServerException {
        if (code != CODE_SUCCESS) {
            throw new ServerException(msg);
        }
    }
}
