package com.gots.intelligentnursing.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.gots.intelligentnursing.exception.BluetoothException;
import com.gots.intelligentnursing.business.BluetoothConnector;
import com.gots.intelligentnursing.receiver.BluetoothReceiver;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.view.IDeviceControlView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/4/15
 */

public class DeviceControlPresenter extends BasePresenter<IDeviceControlView> {

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
        if(mConnector != null){
            mConnector.close();
        }
        EventBus.getDefault().unregister(this);
    }

    public void onItemClicked(int position) {
        switch(position) {
            case POSITION_SET_TIME:
                // TODO: 2018/4/16 通过对话框设置时间
                sendInstruction("$00#230415%");
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
            getView().onException(e.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventHandler(DataEvent<BluetoothDevice> event) {
        if(event.getCode() == BluetoothReceiver.CODE_BOND_SUCCESS) {
            Flowable.just(event.getData())
                    .observeOn(Schedulers.io())
                    .map(BluetoothConnector::new)
                    .doOnNext(connector -> mConnector = connector)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            connector -> getView().onConnectSuccess(),
                            throwable -> getView().onException(throwable.getMessage())
                    );
        } else if(event.getCode() == BluetoothReceiver.CODE_ERROR) {
            getView().onException(event.getMsg());
        } else if(event.getCode() == BluetoothReceiver.CODE_START) {
            isDeviceFound = false;
        } else if(event.getCode() == BluetoothReceiver.CODE_FOUND) {
            isDeviceFound = true;
        } else if (event.getCode() == BluetoothReceiver.CODE_FINISH) {
            mBluetoothAdapter.cancelDiscovery();
            if(!isDeviceFound) {
                getView().onException(HINT_DEVICE_NOT_FOUND);
            }
        }
    }
}
