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
    public static final String ACTION_UPUSH_GET_NOTIFICATION = "From_UmengMessageHandler_getNotification";

    /**
     * 蓝牙广播接收器接收到广播时发出
     */
    public static final String ACTION_BLUETOOTH_RECEIVER_ON_RECEIVE = "From_BluetoothReceiver_onReceive";

    /**
     * 通知LoginPresenter获取用户信息
     */
    public static final String ACTION_TO_GET_USER_INFO = "To_LoginPresenter_getUserInfo";

    public static final String ACTION_ON_GIVE_UP_COMPLETING_INFO = "From_RegisterPresenter_onDestroy";

    /**
     * token失效后自动登录失败时发出
     */
    public static final String ACTION_AUTO_LOGIN_FAILURE = "From_RWAEF_doOnError";

    public static void post(DataEvent event) {
        EventBus.getDefault().post(event);
    }
}
