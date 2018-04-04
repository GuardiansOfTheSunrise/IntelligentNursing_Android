package com.gots.intelligentnursing.view;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public interface IDeviceManagementView extends IView{

    /**
     * presenter对识别二维码结果解析失败后的回调
     * @param msg 失败原因
     */
    void onQrCodeParseError(String msg);

    /**
     * presenter对识别二维码结果解析成功后的回调
     * @param id 设备id
     */
    void onQeCodeParseSuccess(String id);
}
