package com.gots.intelligentnursing.view.activity;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public interface IDeviceManagementView extends IActivityView {

    /**
     * presenter出现异常时的回调
     *
     * @param msg 失败原因
     */
    void onException(String msg);

    /**
     * presenter绑定设备成功回调
     *
     * @param id 设备id
     */
    void onBindSuccess(String id);

    /**
     * presenter解除设备绑定成功回调
     */
    void onUnbindSuccess();
}
