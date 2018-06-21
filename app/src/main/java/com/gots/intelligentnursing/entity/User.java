package com.gots.intelligentnursing.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户类
 * 理论只拥有一个实例，被UserContainer所持有
 * UserContainer.getUser()理论一直不为空，无论是否登录
 * 判断登录与否最好判断User的mUserInfo是否为空
 * 存在mToken不为空而UserInfo为空的可能（登录成功但获取信息时失败）
 * @author zhqy
 * @date 2018/3/29
 */

public class User {

    private String mToken;
    private UserInfo mUserInfo;
    private LocationData mLocation;

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

    public LocationData getLocation() {
        return mLocation;
    }

    public void setLocation(LocationData location) {
        mLocation = location;
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
