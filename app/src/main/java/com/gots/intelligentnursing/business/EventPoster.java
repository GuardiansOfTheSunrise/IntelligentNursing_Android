package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.DataEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zhqy
 * @date 2018/5/16
 */

public class EventPoster {

    /**
     * 友盟收到推送时发出
     */
    public static final String ACTION_UPUSH_GET_NOTIFICATION = "UmengMessageHandler.getNotification";

    /**
     * 蓝牙广播接收器接收到广播时发出
     */
    public static final String ACTION_BLUETOOTH_RECEIVER_ON_RECEIVE = "BluetoothReceiver.onReceive";

    /**
     * token失效后自动登录失败时发出
     */
    public static final String ACTION_AUTO_LOGIN_FAILURE = "RWAEF.doOnError";

    public static void post(DataEvent event) {
        EventBus.getDefault().post(event);
    }
}
