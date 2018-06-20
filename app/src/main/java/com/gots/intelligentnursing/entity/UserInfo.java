package com.gots.intelligentnursing.entity;

import com.google.gson.annotations.SerializedName;
import com.gots.intelligentnursing.R;

import java.util.ArrayList;
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

    @SerializedName("height")
    private String mHeight;

    @SerializedName("weight")
    private String mWeight;

    @SerializedName("addr")
    private String mAddress;

    @SerializedName("remark")
    private String mRemarks;

    @SerializedName("equipment")
    private DeviceInfo mDeviceInfo;

    @SerializedName("fence")
    private List<LocationData> mLocationDataList;

    private List<NotificationData> mNotificationDataList = new ArrayList<>();

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

    public String getHeight() {
        return mHeight;
    }

    public void setHeight(String height) {
        mHeight = height;
    }

    public String getWeight() {
        return mWeight;
    }

    public void setWeight(String weight) {
        mWeight = weight;
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

    public List<NotificationData> getNotificationDataList() {
        return mNotificationDataList;
    }

    public void setNotificationDataList() {
        //List<NotificationData> notificationDataList= new ArrayList<>();
        mNotificationDataList.add(new NotificationData(R.drawable.ic_page_mine_item_about,"2018年6月15日","s老人发生了跌倒","0"));
        mNotificationDataList.add(new NotificationData(R.drawable.ic_page_mine_item_about,"2018年6月16日","s老人走出了电子围栏","0"));
        mNotificationDataList.add(new NotificationData(R.drawable.ic_page_mine_item_about,"2018年6月17日","s老人发生了跌倒","0"));
        mNotificationDataList.add(new NotificationData(R.drawable.ic_page_mine_item_about,"2018年6月18日","s老人心率异常","0"));
        mNotificationDataList.add(new NotificationData(R.drawable.ic_page_mine_item_about,"2018年6月20日","s老人走出了电子围栏","0"));
    }

    public void addNotificationDataList(NotificationData notificationData) {
        mNotificationDataList.add(notificationData);
    }
}
