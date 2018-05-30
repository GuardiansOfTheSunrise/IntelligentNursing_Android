package com.gots.intelligentnursing.view.fragment;

import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
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

    /**
     * 将地图移动至一个点
     * @param data 需要移动至的点
     */
    void moveTo(LocationData data);

    /**
     * 获取到步行路径规划回调，在地图上显示规划路径
     * @param line 步行路径
     */
    void onGetWalkRoutePlanningSuccess(WalkingRouteLine line);

    /**
     * 获取到驾驶路径规划回调，在地图上显示规划路径
     * @param line 驾驶路径
     */
    void onGetDriveRoutePlanningSuccess(DrivingRouteLine line);

    /**
     * 清除已规划的路径
     */
    void clearRoutePlanning();
}
