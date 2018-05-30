package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.DrivingRouteOverlay;
import com.gots.intelligentnursing.business.OverlayManager;
import com.gots.intelligentnursing.business.WalkingRouteOverlay;
import com.gots.intelligentnursing.customview.SlidingLockMapView;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.presenter.fragment.MapPagePresenter;
import com.gots.intelligentnursing.view.fragment.IMapPageView;

import java.util.Arrays;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class MapPageFragment extends BaseFragment<MapPagePresenter> implements IMapPageView {

    private static final String[] BOTTOM_MENU_TEXT = {"移动至我的位置", "移动至设备位置", "创建路线规划"};
    private static final String TEXT_ROUTE_CREATE = "创建路线规划";
    private static final String TEXT_ROUTE_CLEAR = "清除路线规划";

    private static final String HINT_ON_PLANNING_WALK_ROUTE = "根据您与老人的距离，给你推荐了步行的方案";
    private static final String HINT_ON_PLANNING_DRIVE_ROUTE = "根据您与老人的距离，给你推荐了驾车的方案";

    private SlidingLockMapView mMapView;
    private BaiduMap mBaiduMap;
    private OverlayManager mOverlayManager;

    private Marker mDeviceMarker;


    @Override
    public void onException(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationSuccess(LocationData data) {
        // 设置我的坐标点
        MyLocationData myLocationData = new MyLocationData.Builder()
                .latitude(data.getLatitude())
                .longitude(data.getLongitude())
                .build();
        mBaiduMap.setMyLocationData(myLocationData);
    }

    @Override
    public void onGetDeviceLocationSuccess(LocationData data) {
        // 删除原先的标记物
        if (mDeviceMarker != null) {
            mDeviceMarker.remove();
        }

        LatLng point = new LatLng(data.getLatitude(), data.getLongitude());

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_person);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        mDeviceMarker = (Marker) mBaiduMap.addOverlay(option);
    }

    @Override
    public void moveTo(LocationData data) {
        // 将地图移动至我的位置
        LatLng ll = new LatLng(data.getLatitude(), data.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
    }

    private void initMapView(View view) {
        mMapView = view.findViewById(R.id.map_view_page_map);
        mMapView.setButtonMenuTexts(Arrays.asList(BOTTOM_MENU_TEXT));
        mMapView.setOnMenuItemClickListener(index -> {
            mPresenter.onMenuButtonClick(index);
        });
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        // 初始化路径规划器
        mPresenter.setupRoutePlanningHelper();
    }

    @Override
    public void onGetWalkRoutePlanningSuccess(WalkingRouteLine line) {
        WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(mBaiduMap);
        walkingRouteOverlay.setData(line);
        mOverlayManager = walkingRouteOverlay;
        mOverlayManager.addToMap();
        mMapView.getButtonByIndex(BOTTOM_MENU_TEXT.length - 1)
                .setButtonText(TEXT_ROUTE_CLEAR);
        Toast.makeText(getActivity(), HINT_ON_PLANNING_WALK_ROUTE, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetDriveRoutePlanningSuccess(DrivingRouteLine line) {
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(mBaiduMap);
        drivingRouteOverlay.setData(line);
        mOverlayManager = drivingRouteOverlay;
        mOverlayManager.addToMap();
        mMapView.getButtonByIndex(BOTTOM_MENU_TEXT.length - 1)
                .setButtonText(TEXT_ROUTE_CLEAR);
        Toast.makeText(getActivity(), HINT_ON_PLANNING_DRIVE_ROUTE, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearRoutePlanning() {
        if (mOverlayManager != null) {
            mOverlayManager.removeFromMap();
            mOverlayManager = null;
            mMapView.getButtonByIndex(BOTTOM_MENU_TEXT.length - 1)
                    .setButtonText(TEXT_ROUTE_CREATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_page_map, container, false);
        initMapView(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mPresenter.recycler();
    }

    /**
     * 当Fragment显示时更新位置数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mPresenter.refreshData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected MapPagePresenter createPresenter() {
        return new MapPagePresenter(this);
    }
}
