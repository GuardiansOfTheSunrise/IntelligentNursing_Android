package com.gots.intelligentnursing.presenter.activity;

import android.content.Intent;

import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.business.BaseLoginManager;
import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.business.OriginalLoginManager;
import com.gots.intelligentnursing.business.SinaLoginManager;
import com.gots.intelligentnursing.business.TencentLoginManager;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.view.activity.ILoginView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;


/**
 * @author zhqy
 * @date 2018/5/9
 */

public class LoginPresenter extends BaseActivityPresenter<ILoginView> {

    private static final String TAG = "LoginPresenter";

    private static final String HINT_ON_INPUT_ERROR = "用户名/密码为空";

    private OriginalLoginManager mOriginalLoginManager;
    private TencentLoginManager mTencentLoginManager;
    private SinaLoginManager mSinaLoginManager;

    public LoginPresenter(ILoginView view) {
        super(view);
        EventBus.getDefault().register(this);
        BaseLoginManager.OnLoginReturnListener listener = new BaseLoginManager.OnLoginReturnListener() {
            @Override
            public void onSuccess(int channel, UserInfo userInfo) {
                if (channel == BaseLoginManager.CHANNEL_ORIGINAL) {
                    onLoginSuccess(userInfo.getUsername());
                }
            }

            @Override
            public void onFailure(String msg) {
                onException(msg);
            }
        };
        mOriginalLoginManager = new OriginalLoginManager(getActivity(), listener);
        mTencentLoginManager = new TencentLoginManager(getActivity(), listener);
        mSinaLoginManager = new SinaLoginManager(getActivity(), listener);
    }

    public void onTplImageViewClicked(int tag) {
        switch (tag) {
            case LoginActivity.CODE_TPL_QQ:
                mTencentLoginManager.login(getActivity());
                break;
            case LoginActivity.CODE_TPL_SINA:
                mSinaLoginManager.login();
            default:
        }
    }

    public void onLoginButtonClicked(String username, String password) {
        if ("".equals(username) || "".equals(password)) {
            onException(HINT_ON_INPUT_ERROR);
        } else {
            mOriginalLoginManager.login(username, password);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencentLoginManager.handleActivityResult(requestCode, resultCode, data);
        mSinaLoginManager.handleActivityResult(requestCode, resultCode, data);
    }

    public void onDestroy() {
        mSinaLoginManager.recycler();
        mTencentLoginManager.recycler();
        mOriginalLoginManager.recycler();
        EventBus.getDefault().unregister(this);
    }

    private void onLoginSuccess(String username) {
        if (getView() != null) {
            getView().onLoginSuccess(username);
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }

    private void onLogging() {
        if (getView() != null) {
            getView().onLogging();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent (DataEvent event) {
        String action = event.getAction();
        if (EventPoster.ACTION_ON_REGISTER_SUCCESS.equals(action)) {
            onLogging();
            Map<String, String> uap = (Map<String, String>) event.getData();
            String username = uap.get("username");
            String password = uap.get("password");
            mOriginalLoginManager.login(username, password);
        }
    }
}
