package com.gots.intelligentnursing.entity;

import com.google.gson.annotations.SerializedName;
import com.gots.intelligentnursing.tools.LogUtil;

/**
 * @author zhqy
 * @date 2018/5/25
 */

public class SinaUserInfo {

    @SerializedName("error_code")
    private int errorCode;

    @SerializedName("error")
    private String errorMessage;

    @SerializedName("idstr")
    private String uid;

    @SerializedName("screen_name")
    private String name;

    @SerializedName("profile_image_url")
    private String profileImageUrl;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isError() {
        return errorCode != 0;
    }

    public void log(String tag) {
        if (errorCode != 0) {
            LogUtil.e(tag, "Error: Code is " + errorCode + ". Message is " + errorMessage);
        } else {
            String msg = "Success: Uid is " + uid + ". Name is " + name + ". ProfileImageUrl is " + profileImageUrl;
            LogUtil.i(tag, msg);
        }
    }
}
