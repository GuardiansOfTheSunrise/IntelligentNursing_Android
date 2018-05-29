package com.gots.intelligentnursing.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息类
 * @author zhqy
 * @date 2018/3/29
 */

public class User {

    private String mToken;
    private UserInfo mUserInfo;

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        if (token == null) {
            mToken = null;
        } else {
            mToken = "Bearer " + token;
        }
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public void init() {
        mToken = null;
        mUserInfo = null;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
