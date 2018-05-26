package com.gots.intelligentnursing.presenter.activity;

import android.content.Intent;

import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.business.BaseLoginManager;
import com.gots.intelligentnursing.business.FileCacheManager;
import com.gots.intelligentnursing.business.IServerConnection;
import com.gots.intelligentnursing.business.OriginalLoginManager;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.SinaLoginManager;
import com.gots.intelligentnursing.business.TencentLoginManager;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.activity.ILoginView;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

}
