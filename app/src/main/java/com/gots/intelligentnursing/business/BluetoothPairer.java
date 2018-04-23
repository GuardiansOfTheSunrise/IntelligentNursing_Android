package com.gots.intelligentnursing.business;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.gots.intelligentnursing.exception.BluetoothException;
import com.gots.intelligentnursing.tools.LogUtil;

/**
 * @author zhqy
 * @date 2018/4/17
 * 通过反射用于蓝牙自动配对
 */

public class BluetoothPairer {

    private static final String TAG = "BluetoothPairer";

    private static final String HINT_NO_SUCH_METHOD_EXCEPTION = "蓝牙发生错误，错误码：0";
    private static final String HINT_ILLEGAL_ACCESS_EXCEPTION = "蓝牙发生错误，错误码：1";
    private static final String HINT_INVOCATION_TARGET_EXCEPTION = "蓝牙发生错误，错误码：2";

    private static final int MAX_PASSWORD_LENGTH = 16;

    /**
     * 与设备配对
     */
    public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws BluetoothException {
        boolean returnValue;
        Method createBondMethod;
        try {
            createBondMethod = btClass.getMethod("createBond");
            returnValue = (Boolean) createBondMethod.invoke(btDevice);
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "do createBond(), catch NoSuchMethodException");
            e.printStackTrace();
            throw new BluetoothException(HINT_NO_SUCH_METHOD_EXCEPTION);
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "do createBond(), catch IllegalAccessException");
            e.printStackTrace();
            throw new BluetoothException(HINT_ILLEGAL_ACCESS_EXCEPTION);
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getTargetException();
            LogUtil.e(TAG, "do createBond(), catch InvocationTargetException, actual exception type is " +
                    throwable.getClass().getName() + ", message is " + throwable.getMessage());
            e.printStackTrace();
            throw new BluetoothException(HINT_INVOCATION_TARGET_EXCEPTION);
        }
        return returnValue;
    }

    public static boolean setPin(Class<? extends BluetoothDevice> btClass, BluetoothDevice btDevice, String pin)
            throws BluetoothException {
        boolean returnValue;
        Method removeBondMethod;
        try {
            removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            returnValue = (Boolean) removeBondMethod.invoke(btDevice, new Object[]{convertPinToBytes(pin)});
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "do setPin(), catch NoSuchMethodException");
            e.printStackTrace();
            throw new BluetoothException(HINT_NO_SUCH_METHOD_EXCEPTION);
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "do setPin(), catch IllegalAccessException");
            e.printStackTrace();
            throw new BluetoothException(HINT_ILLEGAL_ACCESS_EXCEPTION);
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getTargetException();
            LogUtil.e(TAG, "do setPin(), catch InvocationTargetException, actual exception type is " +
                    throwable.getClass().getName() + ", message is " + throwable.getMessage());
            e.printStackTrace();
            throw new BluetoothException(HINT_INVOCATION_TARGET_EXCEPTION);
        }
        return returnValue;
    }

    /**
     * 确认配对
     * 7.0以上系统在反射中会抛出InvocationTargetException异常
     * 实际异常类型为SecurityException
     * 因缺少BLUETOOTH_PRIVILEGED权限导致
     * 但暂时未发现不能配对的情况
     */
    public static void setPairingConfirmation(Class<?> btClass, BluetoothDevice device, boolean isConfirm) throws BluetoothException {
        Method setPairingConfirmation;
        try {
            setPairingConfirmation = btClass.getDeclaredMethod("setPairingConfirmation", boolean.class);
            setPairingConfirmation.invoke(device, isConfirm);
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "do setPairingConfirmation(), catch NoSuchMethodException");
            e.printStackTrace();
            throw new BluetoothException(HINT_NO_SUCH_METHOD_EXCEPTION);
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "do setPairingConfirmation(), catch IllegalAccessException");
            e.printStackTrace();
            throw new BluetoothException(HINT_ILLEGAL_ACCESS_EXCEPTION);
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getTargetException();
            LogUtil.e(TAG, "do setPairingConfirmation(), catch InvocationTargetException, actual exception type is " +
                    throwable.getClass().getName() + ", message is " + throwable.getMessage());
            e.printStackTrace();
            if(!(throwable instanceof SecurityException)) {
                throw new BluetoothException(HINT_INVOCATION_TARGET_EXCEPTION);
            }
        }
    }

    /**
     * 将String类型的pin码转成byte数组
     */
    private static byte[] convertPinToBytes(String pin) {
        if (pin == null) {
            return null;
        }
        byte[] pinBytes;
        try {
            pinBytes = pin.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        if (pinBytes.length <= 0 || pinBytes.length > MAX_PASSWORD_LENGTH) {
            return null;
        }
        return pinBytes;
    }
}