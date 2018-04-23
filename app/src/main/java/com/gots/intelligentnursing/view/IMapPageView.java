package com.gots.intelligentnursing.view;

import com.gots.intelligentnursing.entity.LocationData;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public interface IMapPageView extends IFragmentView {
    /**
     * presenter错误回调
     * @param msg 错误信息
     */
    void onException(String msg);

    /**
     * 本机定位成功回调
     * @param data 本机位置数据
     */
    void onLocationSuccess(LocationData data);

    /**
     * 获取设备位置数据成功回调
     * @param data 设备位置数据
     */
    void onGetDeviceLocationSuccess(LocationData data);
}
