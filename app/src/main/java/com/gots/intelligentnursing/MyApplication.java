package com.gots.intelligentnursing;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.Toast;

import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.tools.LogUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.message.inapp.InAppMessageManager;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Accumulei
 * @date 2018/5/3.
 */
public class MyApplication extends Application {

    private final String ACTION_UPUSH_GET_NOTIFICATION = "UmengMessageHandler.getNotification";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i("MyApplication", "onCreate");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "a0d6bf722b1b5857caa03af96580c1b6");
        initUpush();
    }

    private void initUpush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                LogUtil.i("MyApplication", "getNotification");
                EventBus.getDefault().post(new DataEvent<>(0, ACTION_UPUSH_GET_NOTIFICATION));
                Intent intent = new Intent("com.gots.intelligentnursing.NOTIFICATION_GET");
                sendBroadcast(intent);
                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom + "msg", Toast.LENGTH_LONG).show();
            }
        };

        //使用自定义的NotificationHandler
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //应用内消息默认为线上模式，若使用测试模式：
        /*InAppMessageManager.getInstance(this).setInAppMsgDebugMode(true);*/

        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                LogUtil.i("MyApplication", "device token: " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.i("MyApplication", "failed");
            }
        });
    }
}
