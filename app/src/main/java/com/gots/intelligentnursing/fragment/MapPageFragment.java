package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.MapPagePresenter;
import com.gots.intelligentnursing.view.IMapPageView;

/**
 * @author Accumulei
 * @date 2018/4/12.
 */
public class MapPageFragment extends BaseFragment<MapPagePresenter> implements IMapPageView {

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    public void onException(String msg) {

    }

    @Override
    public void onInitDataSuccess() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_page_map, container, false);
        mMapView = view.findViewById(R.id.map_view_page_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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
