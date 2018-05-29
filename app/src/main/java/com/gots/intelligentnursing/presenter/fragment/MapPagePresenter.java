package com.gots.intelligentnursing.presenter.fragment;

import android.Manifest;
import android.os.SystemClock;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.view.fragment.IMapPageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class MapPagePresenter extends BaseFragmentPresenter<IMapPageView> {

    private static final int MOVE_TO_NONE = 0;
    private static final int MOVE_TO_LOCATION = 1;
    private static final int MOVE_TO_DEVICE = 2;

    private static final String HINT_DENY_GRANT = "您拒绝了授权，该功能无法正常使用";
    private static final String HINT_LOCATION_ERROR = "定位错误，错误码:";

    /**
     * 两次刷新数据之间时间的最小间隔
     */
    private static final long THRESHOLD_REFRESH_DATA_INTERVAL_MS = 10000;

    /**
     * 用于计算是否重新获取位置数据
     */
    private long mLastRefreshDataTime = 0;

    private LocationClient mLocationClient;

    private List<String> mBottomMenuTextList = new ArrayList<>();

    private LocationData mLastLocationData;
    private LocationData mLastDeviceData;

    /**
     * 获取数据后是否移动至数据位置
     */
    private int mMoveTo = MOVE_TO_LOCATION;

    public MapPagePresenter(IMapPageView view) {
        super(view);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        initBottomMenuTextList();
        initLocationClient();
    }

    public List<String> getMenuTexts() {
        return mBottomMenuTextList;
    }

    public void onMenuButtonClick(int index) {
        switch (index) {
            case 0:
                if (mLastLocationData != null) {
                    moveTo(mLastLocationData);
                } else {
                    mMoveTo = MOVE_TO_LOCATION;
                    refreshData();
                }
                break;
            case 1:
                if (UserContainer.getUser().getUserInfo() == null) {
                    LoginActivity.actionStart(getActivity(), null);
                } else {
                    if (mLastDeviceData != null) {
                        moveTo(mLastDeviceData);
                    } else {
                        mMoveTo = MOVE_TO_DEVICE;
                        refreshData();
                    }
                }
                break;
            case 2:
                break;
            default:
        }
    }

    /**
     * 刷新手机与设备的位置数据
     */
    public void refreshData() {
        if (SystemClock.uptimeMillis() - mLastRefreshDataTime > THRESHOLD_REFRESH_DATA_INTERVAL_MS) {
            getDeviceLocation();

            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            new RxPermissions(getActivity()).request(permissions)
                    .filter(granted -> granted)
                    .switchIfEmpty(observer -> onException(HINT_DENY_GRANT))
                    .subscribe(granted -> startLocation());
        }
    }

    /**
     * 从服务器获取绑定设备的定位数据
     */
    private void getDeviceLocation() {
        if (UserContainer.getUser() != null) {
            // TODO: 2018/4/20 连接服务器获取设备位置数据
            double latitude = 36.070257;
            double longitude = 120.317581;
            mLastDeviceData = new LocationData(latitude, longitude);
            onGetDeviceLocationSuccess(mLastDeviceData);
            if (mMoveTo == MOVE_TO_DEVICE) {
                moveTo(mLastDeviceData);
                mMoveTo = MOVE_TO_NONE;
            }
        }
    }

    /**
     * 启动手机定位，重回调中获取定位数据
     */
    private void startLocation() {
        mLastRefreshDataTime = SystemClock.uptimeMillis();
        mLocationClient.start();
    }

    /**
     * 初始化底部菜单栏数据
     */
    private void initBottomMenuTextList() {
        mBottomMenuTextList.add("移动至我的位置");
        mBottomMenuTextList.add("移动至设备位置");
        mBottomMenuTextList.add("清除路线规划");
    }

    /**
     * 初始化定位客户端
     */
    private void initLocationClient() {
        LocationClientOption option = new LocationClientOption();

        // 设置定位模式，默认高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        // 设置返回经纬度坐标类型，默认gcj02，gcj02：国测局坐标，bd09ll：百度经纬度坐标，bd09：百度墨卡托坐标；
        option.setCoorType("bd09ll");

        // 设置发起定位请求的间隔，单位ms，如果设置为0，仅定位一次，默认为0，如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(0);

        // 设置是否使用gps，默认false，使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);

        // 设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(false);

        // 定位SDK内部是一个service，并放到了独立进程
        // 设置是否在stop的时候杀死这个进程，默认不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);

        // 可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);

        // 如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setWifiCacheTimeOut(5 * 60 * 1000);

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
                                data -> {
                                    mLastLocationData = data;
                                    onLocationSuccess(data);
                                    if (mMoveTo == MOVE_TO_LOCATION) {
                                        moveTo(data);
                                        mMoveTo = MOVE_TO_NONE;
                                    }
                                },
                                throwable -> getView().onException(throwable.getMessage())
                        );
            }
        };
        mLocationClient.registerLocationListener(locationListener);
    }

    /**
     * 检查定位结果
     * @param location 定位结果
     * @throws Exception 定位出错时在RxJava的onError中处理
     */
    private void checkLocationType(BDLocation location) throws Exception {
        int type = location.getLocType();
        if (type != BDLocation.TypeGpsLocation && type != BDLocation.TypeOffLineLocation &&
                type != BDLocation.TypeNetWorkLocation) {
            throw new Exception(HINT_LOCATION_ERROR + location.getLocType());
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }

    private void onLocationSuccess(LocationData data) {
        if (getView() != null) {
            getView().onLocationSuccess(data);
        }
    }

    private void onGetDeviceLocationSuccess(LocationData data) {
        if (getView() != null) {
            getView().onGetDeviceLocationSuccess(data);
        }
    }

    private void moveTo(LocationData data) {
        if (getView() != null) {
            getView().moveTo(data);
        }
    }
}
