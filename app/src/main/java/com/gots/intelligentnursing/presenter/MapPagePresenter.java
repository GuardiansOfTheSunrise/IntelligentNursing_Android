package com.gots.intelligentnursing.presenter;

import android.Manifest;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.view.IMapPageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Arrays;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class MapPagePresenter extends BasePresenter<IMapPageView> {

    private static final String HINT_DENY_GRANT = "您拒绝了授权，该功能无法正常使用";
    private static final String HINT_LOCATION_ERROR = "定位错误，错误码:";

    private LocationClient mLocationClient;

    public MapPagePresenter(IMapPageView view) {
        super(view);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        initLocationClient();
    }

    public void initData() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        new RxPermissions(getActivity()).request(permissions)
                .filter(granted -> granted)
                .switchIfEmpty(observer -> getView().onException(HINT_DENY_GRANT))
                .subscribe(granted -> getLocationData());
    }

    private void getLocationData() {
        mLocationClient.start();
        // TODO: 2018/4/20 连接服务器获取设备位置数据
        double latitude = 36.070257;
        double longitude = 120.317581;
        getView().onGetDeviceLocationSuccess(new LocationData(latitude, longitude));
    }

    private void initLocationClient() {
        LocationClientOption option = new LocationClientOption();

        // 可选，设置定位模式，默认高精度
        // LocationMode.Hight_Accuracy：高精度；
        // LocationMode. Battery_Saving：低功耗；
        // LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        // 可选，设置返回经纬度坐标类型，默认gcj02
        // gcj02：国测局坐标，bd09ll：百度经纬度坐标，bd09：百度墨卡托坐标；
        option.setCoorType("bd09ll");

        // 可选，设置发起定位请求的间隔，int类型，单位ms
        // 如果设置为0，则代表单次定位，即仅定位一次，默认为0
        // 如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(0);

        // 可选，设置是否使用gps，默认false
        // 使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);

        // 可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(false);

        // 可选，定位SDK内部是一个service，并放到了独立进程。
        // 设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);

        // 可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);

        // 如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setWifiCacheTimeOut(5*60*1000);

        // 可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(true);

        mLocationClient.setLocOption(option);

        BDAbstractLocationListener locationListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Flowable.just(bdLocation)
                        .doOnNext(MapPagePresenter.this::checkLocationType)
                        .map(location -> new LocationData(location.getLatitude(), location.getLongitude()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                data -> getView().onLocationSuccess(data),
                                throwable -> getView().onException(throwable.getMessage())
                        );
            }
        };
        mLocationClient.registerLocationListener(locationListener);
    }

    private void checkLocationType(BDLocation location) throws Exception {
        int type = location.getLocType();
        if (type != BDLocation.TypeGpsLocation && type != BDLocation.TypeOffLineLocation &&
                type != BDLocation.TypeNetWorkLocation) {
            throw new Exception(HINT_LOCATION_ERROR + location.getLocType());
        }
    }
}
