package com.gots.intelligentnursing.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息类
 * @author zhqy
 * @date 2018/3/29
 */

public class User {

    private int mId;
    private String mUsername;
    private String mBindingDeviceId;
    private String mBindingDevicePassword;
    private List<LocationData> mFencePointDataList;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getBindingDeviceId() {
        return mBindingDeviceId;
    }

    public void setBindingDeviceId(String bindingDeviceId) {
        mBindingDeviceId = bindingDeviceId;
    }

    public String getBindingDevicePassword() {
        return mBindingDevicePassword;
    }

    public void setBindingDevicePassword(String bindingDevicePassword) {
        mBindingDevicePassword = bindingDevicePassword;
    }

    public List<LocationData> getFencePointDataList() {
        return mFencePointDataList;
    }

    public void setFencePointDataList(List<LocationData> fencePointDataList) {
        mFencePointDataList = fencePointDataList;
    }
}
