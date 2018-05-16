package com.gots.intelligentnursing.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/5/10
 */

public class UserInfo {

    @SerializedName("id")
    private int mId;

    @SerializedName("userName")
    private String mUsername;

    private String mPassword;

    @SerializedName("tel")
    private String mTelephone;

    @SerializedName("age")
    private int mAge;

    @SerializedName("addr")
    private String mAddress;

    @SerializedName("remark")
    private String mRemarks;

    @SerializedName("equipment")
    private DeviceInfo mDeviceInfo;

    @SerializedName("fence")
    private List<LocationData> mLocationDataList;

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

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public void setTelephone(String telephone) {
        mTelephone = telephone;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getRemarks() {
        return mRemarks;
    }

    public void setRemarks(String remarks) {
        mRemarks = remarks;
    }

    public DeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
    }

    public List<LocationData> getLocationDataList() {
        return mLocationDataList;
    }

    public void setLocationDataList(List<LocationData> locationDataList) {
        mLocationDataList = locationDataList;
    }
}
