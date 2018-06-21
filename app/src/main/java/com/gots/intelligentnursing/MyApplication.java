package com.gots.intelligentnursing;

import android.app.Notification;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.gots.intelligentnursing.adapter.MyNotificationAdapter;
import com.gots.intelligentnursing.business.CrashHandler;
import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.entity.NotificationData;
import com.gots.intelligentnursing.entity.User;
import com.gots.intelligentnursing.tools.LogUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;

import static com.gots.intelligentnursing.business.EventPoster.ACTION_UPUSH_GET_NOTIFICATION;

/**
 * @author Accumulei
 * @date 2018/5/3.
 */
public class MyApplication extends MultiDexApplication {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        new CrashHandler().init(getApplicationContext());
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "a0d6bf722b1b5857caa03af96580c1b6");
        initUpush();
    }


    private void initUpush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                LogUtil.i(TAG, "getNotification");
                LogUtil.i(TAG, "text: " + uMessage.text);
                LogUtil.i(TAG, "title: " + uMessage.title);
                EventPoster.post(new DataEvent(ACTION_UPUSH_GET_NOTIFICATION, 0, uMessage.title, uMessage.text));

                UserContainer.getUser().getUserInfo().addNotificationDataList(new NotificationData(uMessage.title, 0));
                UserContainer.getUser().getUserInfo().addNotificationDataList(new NotificationData());
                int size = UserContainer.getUser().getUserInfo().getNotificationDataList().size();
                LogUtil.i(TAG, "size of nlist: " + String.valueOf(size));

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
                LogUtil.i(TAG, "Push agent register success, device token: " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.i(TAG, "Push agent register failed");
            }
        });
    }
}
