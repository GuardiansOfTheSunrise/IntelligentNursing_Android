package com.gots.intelligentnursing.presenter.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import com.gots.intelligentnursing.activity.DeviceControlActivity;
import com.gots.intelligentnursing.business.QrCodeResultParser;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.entity.DeviceInfo;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.User;
import com.gots.intelligentnursing.exception.ParseException;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.view.activity.IDeviceManagementView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public class DeviceManagementPresenter extends BaseActivityPresenter<IDeviceManagementView> {

    private static final int REQUEST_CAPTURE = 0;
    private static final int REQUEST_OPEN_BLUETOOTH = 1;

    private static final String HINT_BLUETOOTH_NOT_SUPPORT = "您的设备不支持蓝牙功能，无法进行此操作";
    private static final String HINT_DENY_GRANT = "您拒绝了授权，该功能无法正常使用";


    public DeviceManagementPresenter(IDeviceManagementView view) {
        super(view);
    }

    public void onAddDeviceButtonClicked() {
        // 申请相机权限
        // filter操作符拦截以后将会触发switchIfEmpty操作符
        new RxPermissions(getActivity())
                .request(Manifest.permission.CAMERA)
                .filter(granted -> granted)
                .switchIfEmpty(observer -> onException(HINT_DENY_GRANT))
                .subscribe(granted -> getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_CAPTURE));
    }

    private void startControlActivityWithRequestPermissions() {
        // 6.0以上需要位置权限
        new RxPermissions(getActivity())
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .filter(granted -> granted)
                .switchIfEmpty(observer -> onException(HINT_DENY_GRANT))
                .subscribe(granted -> DeviceControlActivity.actionStart(getActivity()));
    }

    public void onConnectButtonClicked() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // 如果适配器为null时表示设备不支持蓝牙功能
        if (adapter == null) {
            onException(HINT_BLUETOOTH_NOT_SUPPORT);
        } else {
            if (!adapter.isEnabled()) {
                // 打开蓝牙
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                getActivity().startActivityForResult(enabler, REQUEST_OPEN_BLUETOOTH);
            } else {
                startControlActivityWithRequestPermissions();
            }
        }
    }

    public void onDeleteButtonClicked() {
        User user = UserContainer.getUser();
        RetrofitHelper.getInstance().device()
                .unbind(user.getToken(), user.getUserInfo().getId())
                .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(ServerResponse::checkCode)
                .doOnNext(resp -> UserContainer.getUser().getUserInfo().setDeviceInfo(null))
                .subscribe(
                        r -> onUnbindSuccess(),
                        throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE:
                    String result = data.getExtras().getString("result");
                    try {
                        String deviceId = QrCodeResultParser.parse(result);
                        bindDevice(deviceId);
                    } catch (ParseException e) {
                        onException(e.getMessage());
                    }
                    break;
                case REQUEST_OPEN_BLUETOOTH:
                    startControlActivityWithRequestPermissions();
                    break;
                default:
            }
        }
    }

    private void bindDevice(String deviceId) {
        User user = UserContainer.getUser();
        RetrofitHelper.getInstance().device()
                .bind(user.getToken(), user.getUserInfo().getId(), deviceId)
                .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .doOnNext(ServerResponse::checkCode)
                .map(ServerResponse::getData)
                .doOnNext(pwd -> {
                    DeviceInfo deviceInfo = new DeviceInfo(deviceId, pwd);
                    UserContainer.getUser().getUserInfo().setDeviceInfo(deviceInfo);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pwd -> onBindSuccess(deviceId),
                        throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                );
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }

    private void onBindSuccess(String id) {
        if (getView() != null) {
            getView().onBindSuccess(id);
        }
    }

    private void onUnbindSuccess() {
        if (getView() != null) {
            getView().onUnbindSuccess();
        }
    }


}
