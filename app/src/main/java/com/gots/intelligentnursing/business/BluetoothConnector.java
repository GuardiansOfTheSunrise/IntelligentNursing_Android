package com.gots.intelligentnursing.business;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.gots.intelligentnursing.exception.BluetoothException;
import com.gots.intelligentnursing.tools.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author zhqy
 * @date 2018/4/15
 */

public class BluetoothConnector {

    private static final String TAG = "BluetoothConnector";

    private static final String STR_BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private static final String HINT_SOCKET_ESTABLISH_FAILURE = "连接建立失败";
    private static final String HINT_SOCKET_NOT_ESTABLISH = "连接未建立";
    private static final String HINT_SEND_FAILURE = "发送失败";

    private BluetoothSocket mSocket;
    private OutputStream mOutputStream;

    public BluetoothConnector(BluetoothDevice device) throws BluetoothException {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(STR_BLUETOOTH_UUID));
            mSocket.connect();
        } catch (IOException e) {
            mSocket = null;
            e.printStackTrace();
            throw new BluetoothException(HINT_SOCKET_ESTABLISH_FAILURE);
        }
        try {
            mOutputStream = mSocket.getOutputStream();
        } catch (IOException getOutputStreamException) {
            getOutputStreamException.printStackTrace();
            try {
                mSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
            mSocket = null;
            mOutputStream = null;
            throw new BluetoothException(HINT_SOCKET_ESTABLISH_FAILURE);
        }
    }

    public void write(String str) throws BluetoothException {
        if(mOutputStream == null){
            throw new BluetoothException(HINT_SOCKET_NOT_ESTABLISH);
        }
        try {
            mOutputStream.write(str.getBytes());
        } catch (IOException e) {
            close();
            e.printStackTrace();
            throw new BluetoothException(HINT_SEND_FAILURE);
        }
    }

    public void close() {
        try {
            mOutputStream.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
