package com.gots.intelligentnursing.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.gots.intelligentnursing.exception.BluetoothException;
import com.gots.intelligentnursing.business.BluetoothConnector;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.IDeviceControlView;

/**
 * @author zhqy
 * @date 2018/4/15
 */

public class DeviceControlPresenter extends BasePresenter<IDeviceControlView> {

    private static final String TAG = "DeviceControlPresenter";

    private static final int POSITION_SET_TIME = 0;
    private static final int POSITION_FIND_DEVICE = 1;

    private static final String HINT_DEVICE_NOT_FOUND = "未发现手环\n请确保手环在设备的附近";

    private static final String BLUETOOTH_NAME = "";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnector mConnector;

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device == null || device.getName() == null) {
                    return;
                }
                String name = device.getName();
                if(name != null && name.equals(BLUETOOTH_NAME)) {
                    mBluetoothAdapter.cancelDiscovery();
                    getActivity().unregisterReceiver(mBluetoothReceiver);
                    try {
                        mConnector = new BluetoothConnector(device);
                        getView().onConnectSuccess();
                    } catch (BluetoothException e) {
                        e.printStackTrace();
                        getView().onException(e.getMessage());
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                LogUtil.i(TAG, "Bluetooth discovery finish, no matching device.");
                mBluetoothAdapter.cancelDiscovery();
                getView().onException(HINT_DEVICE_NOT_FOUND);
                getActivity().unregisterReceiver(mBluetoothReceiver);
            }
        }
    };

    public DeviceControlPresenter(IDeviceControlView view) {
        super(view);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connectDevice() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mBluetoothReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }

    public void disconnect() {
        mConnector.close();
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
}
