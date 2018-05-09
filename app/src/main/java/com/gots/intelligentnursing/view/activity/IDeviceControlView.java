package com.gots.intelligentnursing.view.activity;

/**
 * @author zhqy
 * @date 2018/4/15
 */

public interface IDeviceControlView extends IView {
    /**
     * 蓝牙连接成功时回调
     */
    void onConnectSuccess();
    /**
     * presenter出现异常时的回调
     * @param msg 失败原因
     */
    void onException(String msg);
}
