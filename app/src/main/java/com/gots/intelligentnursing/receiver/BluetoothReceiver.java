package com.gots.intelligentnursing.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.exception.BluetoothException;
import com.gots.intelligentnursing.tools.BluetoothUtil;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.tools.LogUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zhqy
 * @date 2018/4/17
 */

public class BluetoothReceiver extends BroadcastReceiver {

    public static final int CODE_FOUND = 0;
    public static final int CODE_BOND_SUCCESS = 1;
    public static final int CODE_ERROR = 2;
    public static final int CODE_START = 3;
    public static final int CODE_FINISH = 4;

    private static final String TAG = "BluetoothReceiver";
    private static final String DEVICE_NAME = "ZhiHu-W1";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if(BluetoothDevice.ACTION_FOUND.equals(action)) {
            LogUtil.i(TAG, "receive ACTION_FOUND broadcast");
            if(device.getName().equals(DEVICE_NAME)) {
                EventBus.getDefault().post(new DataEvent<>(CODE_FOUND));
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    try {
                        BluetoothUtil.createBond(device.getClass(), device);
                    } catch (BluetoothException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new DataEvent<>(CODE_ERROR, e.getMessage()));
                    }
                } else if(device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    EventBus.getDefault().post(new DataEvent<>(CODE_BOND_SUCCESS, device));
                }
            }
        } else if(BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
            LogUtil.i(TAG, "receive ACTION_PAIRING_REQUEST broadcast");
            if(device.getName().equals(DEVICE_NAME)) {
                try {
                    BluetoothUtil.setPairingConfirmation(device.getClass(), device, true);
                    abortBroadcast();
                    BluetoothUtil.setPin(device.getClass(), device, UserContainer.getUser().getBindingDevicePassword());
                    EventBus.getDefault().post(new DataEvent<>(CODE_BOND_SUCCESS, device));
                } catch (BluetoothException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new DataEvent<>(CODE_ERROR, e.getMessage()));
                }
            }
        } else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            LogUtil.i(TAG, "receive ACTION_DISCOVERY_STARTED broadcast");
            EventBus.getDefault().post(new DataEvent<>(CODE_START));
        } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            LogUtil.i(TAG, "receive ACTION_DISCOVERY_FINISHED broadcast");
            EventBus.getDefault().post(new DataEvent<>(CODE_FINISH));
        }
    }
}
