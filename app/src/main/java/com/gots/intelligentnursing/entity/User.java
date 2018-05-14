package com.gots.intelligentnursing.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息类
 * @author zhqy
 * @date 2018/3/29
 */

public class User {

    private String token;
    private UserInfo mUserInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = "Bearer " + token;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }
}
