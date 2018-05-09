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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.customview.SlidingLockMapView;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.presenter.fragment.MapPagePresenter;
import com.gots.intelligentnursing.view.fragment.IMapPageView;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class MapPageFragment extends BaseFragment<MapPagePresenter> implements IMapPageView {

    private SlidingLockMapView mMapView;
    private BaiduMap mBaiduMap;

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

        // 将地图移动至我的位置
        LatLng ll = new LatLng(data.getLatitude(), data.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
    }

    @Override
    public void onGetDeviceLocationSuccess(LocationData data) {
        LatLng point = new LatLng(data.getLatitude(), data.getLongitude());

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_person);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        // 删除标记物
        // mBaiduMap.clear();
    }

    private void initMapView(View view) {
        mMapView = view.findViewById(R.id.map_view_page_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_page_map, container, false);
        initMapView(view);
        mPresenter.initData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
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
