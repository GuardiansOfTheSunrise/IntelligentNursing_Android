package com.gots.intelligentnursing.entity;

/**
 * 用户信息类
 * @author zhqy
 * @date 2018/3/29
 */

public class User {

    private String username;
    private String bindingDeviceId;
    private String bindingDevicePassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBindingDeviceId() {
        return bindingDeviceId;
    }

    public void setBindingDeviceId(String bindingDeviceId) {
        this.bindingDeviceId = bindingDeviceId;
    }

    public String getBindingDevicePassword() {
        return bindingDevicePassword;
    }

    public void setBindingDevicePassword(String bindingDevicePassword) {
        this.bindingDevicePassword = bindingDevicePassword;
    }
}
