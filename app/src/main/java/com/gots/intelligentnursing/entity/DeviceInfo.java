package com.gots.intelligentnursing.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhqy
 * @date 2018/5/10
 */

public class DeviceInfo {

    @SerializedName("id")
    private String mId;

    @SerializedName("pwd")
    private String mBluetoothPassword;

    public DeviceInfo() {
    }

    public DeviceInfo(String id, String bluetoothPassword) {
        mId = id;
        mBluetoothPassword = bluetoothPassword;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getBluetoothPassword() {
        return mBluetoothPassword;
    }

    public void setBluetoothPassword(String bluetoothPassword) {
        mBluetoothPassword = bluetoothPassword;
    }
}
