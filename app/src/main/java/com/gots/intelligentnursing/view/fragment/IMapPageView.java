package com.gots.intelligentnursing.view.fragment;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.entity.SeekHelpInfo;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public interface IMapPageView extends IFragmentView {

    /**
     * 本机定位成功回调
     *
     * @param data 本机位置数据
     */
    void onLocationSuccess(LocationData data);

    /**
     * 获取设备位置数据成功回调
     *
     * @param data 设备位置数据
     */
    void onGetDeviceLocationSuccess(LocationData data);

    /**
     * 将地图移动至一个点
     *
     * @param data 需要移动至的点
     */
    void moveTo(LocationData data);

    /**
     * 获取到步行路径规划回调，在地图上显示规划路径
     *
     * @param line 步行路径
     */
    void onGetWalkRoutePlanningSuccess(WalkingRouteLine line);

    /**
     * 获取到驾驶路径规划回调，在地图上显示规划路径
     *
     * @param line 驾驶路径
     */
    void onGetDriveRoutePlanningSuccess(DrivingRouteLine line);

    /**
     * 获取求救信息列表成功回调
     *
     * @param seekHelpInfoList 求救信息列表
     */
    void onGetSeekHelpInfoDataSuccess(List<SeekHelpInfo> seekHelpInfoList);

    /**
     * 获取热力图数据回调
     *
     * @param regionList 热力图区域数据
     */
    void onGetHeatMapDataSuccess(List<List<LatLng>> regionList);

    /**
     * 清除已规划的路径
     */
    void clearRoutePlanning();
}
