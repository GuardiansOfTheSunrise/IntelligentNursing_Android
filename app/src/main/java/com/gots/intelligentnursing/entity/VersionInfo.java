package com.gots.intelligentnursing.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhqy
 * @date 2018/6/14
 */

public class VersionInfo {

    @SerializedName("versionCode")
    private int mVersionCode;

    @SerializedName("versionName")
    private String mVersionName;

    @SerializedName("versionInfo")
    private String mVersionInfo;

    public int getVersionCode() {
        return mVersionCode;
    }

    public void setVersionCode(int versionCode) {
        mVersionCode = versionCode;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public void setVersionName(String versionName) {
        mVersionName = versionName;
    }

    public String getVersionInfo() {
        return mVersionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        mVersionInfo = versionInfo;
    }
}
