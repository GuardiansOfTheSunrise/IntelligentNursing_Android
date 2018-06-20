package com.gots.intelligentnursing.presenter.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.exception.BluetoothException;
import com.gots.intelligentnursing.business.BluetoothConnector;
import com.gots.intelligentnursing.receiver.BluetoothReceiver;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.view.activity.IDeviceControlView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/4/15
 */

public class DeviceControlPresenter extends BaseActivityPresenter<IDeviceControlView> {

    private static final String TAG = "DeviceControlPresenter";

    private static final int POSITION_SET_TIME = 0;
    private static final int POSITION_FIND_DEVICE = 1;

    private boolean isDeviceFound = false;

    private static final String HINT_DEVICE_NOT_FOUND = "未发现手环，请确保手环在设备的附近";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnector mConnector;

    public DeviceControlPresenter(IDeviceControlView view) {
        super(view);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        EventBus.getDefault().register(this);
    }

    public void connectDevice() {
        mBluetoothAdapter.startDiscovery();
    }

    public void onDestroy() {
        if (mConnector != null) {
            mConnector.close();
        }
        EventBus.getDefault().unregister(this);
    }

    public void onItemClicked(int position) {
        switch (position) {
            case POSITION_SET_TIME:
                String time = new SimpleDateFormat("HHmmss", Locale.CHINA).format(new Date());
                String command = "$00#" + time + "%";
                sendInstruction(command);
                break;
            case POSITION_FIND_DEVICE:
                sendInstruction("$10#%");
                break;
            default:
        }
    }

    private void sendInstruction(String instruction) {
        try {
            mConnector.write(instruction);
        } catch (BluetoothException e) {
            e.printStackTrace();
            onException(e.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(DataEvent event) {
        if (EventPoster.ACTION_BLUETOOTH_RECEIVER_ON_RECEIVE.equals(event.getAction())) {
            if (event.getCode() == BluetoothReceiver.CODE_BOND_SUCCESS) {
                Flowable.just(event.getData())
                        .observeOn(Schedulers.io())
                        .map(data -> new BluetoothConnector((BluetoothDevice) data))
                        .doOnNext(connector -> mConnector = connector)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                connector -> onConnectSuccess(),
                                throwable -> onException(throwable.getMessage())
                        );
            } else if (event.getCode() == BluetoothReceiver.CODE_ERROR) {
                onException(event.getMsg());
            } else if (event.getCode() == BluetoothReceiver.CODE_START) {
                isDeviceFound = false;
            } else if (event.getCode() == BluetoothReceiver.CODE_FOUND) {
                isDeviceFound = true;
            } else if (event.getCode() == BluetoothReceiver.CODE_FINISH) {
                mBluetoothAdapter.cancelDiscovery();
                if (!isDeviceFound) {
                    onException(HINT_DEVICE_NOT_FOUND);
                }
            }
        }
    }

    private void onConnectSuccess() {
        if (getView() != null) {
            getView().onConnectSuccess();
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }
}
