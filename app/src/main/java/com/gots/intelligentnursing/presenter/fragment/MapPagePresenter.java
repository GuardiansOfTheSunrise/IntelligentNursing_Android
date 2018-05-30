package com.gots.intelligentnursing.presenter.fragment;

import android.Manifest;
import android.os.SystemClock;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.RoutePlanningHelper;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.view.fragment.IMapPageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

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
    private static final String HINT_ON_PLANNING_LACK_MINE_LOCATION = "获取您的位置信息失败，无法为您规划路径";
    private static final String HINT_ON_PLANNING_LACK_DEICE_LOCATION = "获取设备的位置信息失败，无法为您规划路径";

    /**
     * 两次刷新数据之间时间的最小间隔
     */
    private static final long THRESHOLD_REFRESH_DATA_INTERVAL_MS = 10000;

    /**
     * 用于计算是否重新获取位置数据
     */
    private long mLastRefreshDataTime = 0;

    private LocationClient mLocationClient;

    private LocationData mLastLocationData;
    private LocationData mLastDeviceData;

    /**
     * 获取数据后是否移动至数据位置
     */
    private int mMoveTo = MOVE_TO_LOCATION;

    private RoutePlanningHelper mRoutePlanningHelper;

    /**
     * 路径是否已经生成，false表示未生成
     */
    private boolean mRouteCreate = false;

    public MapPagePresenter(IMapPageView view) {
        super(view);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        initLocationClient();
    }

    public void onMenuButtonClick(int index) {
        switch (index) {
            case 0:
                onMoveToMyLocationClick();
                break;
            case 1:
                onMoveToDeviceLocationClick();
                break;
            case 2:
                onCreateOrClearRouteClick();
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
     * 初始化路径规划器
     * 调用此方法前需要调用SDKInitializer.initialize()
     */
    public void setupRoutePlanningHelper() {
        mRoutePlanningHelper = new RoutePlanningHelper(new RoutePlanningHelper.OnPlanningResultListener() {
            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                if (drivingRouteResult != null && drivingRouteResult.getRouteLines() != null &&
                        drivingRouteResult.getRouteLines().size() >= 1) {
                    onGetDriveRoutePlanningSuccess(drivingRouteResult.getRouteLines().get(0));
                }
            }

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                if (walkingRouteResult != null && walkingRouteResult.getRouteLines() != null &&
                        walkingRouteResult.getRouteLines().size() >= 1) {
                    onGetWalkRoutePlanningSuccess(walkingRouteResult.getRouteLines().get(0));
                }

            }
        });
    }

    public void recycler() {
        mRoutePlanningHelper.destroy();
    }

    /**
     * 从服务器获取绑定设备的定位数据
     */
    private void getDeviceLocation() {
        UserInfo userInfo = UserContainer.getUser().getUserInfo();
        if (userInfo != null && userInfo.getDeviceInfo() != null) {
            // TODO: 2018/4/20 连接服务器获取设备位置数据
            /*
            RetrofitHelper.getInstance().device()
                    .getDeviceLocation(UserContainer.getUser().getToken(), userInfo.getDeviceInfo().getId())
            */
            /*
            double latitude = 35.944489;
            double longitude = 120.185498;
            */
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

    private void onMoveToMyLocationClick() {
        if (mLastLocationData != null) {
            moveTo(mLastLocationData);
        } else {
            mMoveTo = MOVE_TO_LOCATION;
            refreshData();
        }
    }

    private void onMoveToDeviceLocationClick() {
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
    }

    private void onCreateOrClearRouteClick() {
        if (!mRouteCreate) {
            routePlanning();
        } else {
            clearRoutePlanning();
        }
        mRouteCreate = !mRouteCreate;
    }

    private void routePlanning() {
        if (mLastLocationData == null) {
            onException(HINT_ON_PLANNING_LACK_MINE_LOCATION);
        } else if (mLastDeviceData == null) {
            onException(HINT_ON_PLANNING_LACK_DEICE_LOCATION);
        } else {
            mRoutePlanningHelper.planning(mLastLocationData, mLastDeviceData);
        }
    }

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

    private void onGetWalkRoutePlanningSuccess(WalkingRouteLine line) {
        if (getView() != null) {
            getView().onGetWalkRoutePlanningSuccess(line);
        }
    }

    private void onGetDriveRoutePlanningSuccess(DrivingRouteLine line) {
        if (getView() != null) {
            getView().onGetDriveRoutePlanningSuccess(line);
        }
    }

    private void clearRoutePlanning() {
        if (getView() != null) {
            getView().clearRoutePlanning();
        }
    }
}
