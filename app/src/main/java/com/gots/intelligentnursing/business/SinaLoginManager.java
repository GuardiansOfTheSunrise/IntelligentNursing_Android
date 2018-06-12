package com.gots.intelligentnursing.business;

import android.content.Intent;
import android.widget.Toast;

import com.gots.intelligentnursing.activity.RegisterActivity;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.exception.ServerException;
import com.gots.intelligentnursing.presenter.activity.LoginPresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/5/25
 */

public class SinaLoginManager extends BaseLoginManager {

    private static final String TAG = "SinaLoginManager";

    private static final String HINT_ON_CANCEL = "您放弃了登录";
    private static final String HINT_COMPLETING_INFO = "您的信息不完善，请先补全信息";

    private static final String APP_KEY = "1802881260";
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String SCOPE = null;

    private SsoHandler mSsoHandler;

    private RxAppCompatActivity mActivity;

    @Override
    public void recycler() {
        mActivity = null;
        mSsoHandler = null;
    }

    public SinaLoginManager(RxAppCompatActivity activity, OnFailureListener listener) {
        super(listener);
        mActivity = activity;
        AuthInfo authInfo = new AuthInfo(mActivity, APP_KEY, REDIRECT_URL, SCOPE);
        WbSdk.install(mActivity, authInfo);
        mSsoHandler = new SsoHandler(mActivity);

    }

    public void login() {
        mSsoHandler.authorize(new SelfWbAuthListener());
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private class SelfWbAuthListener implements WbAuthListener {

        @Override
        public void onSuccess(final Oauth2AccessToken sinaToken) {
            String uid = sinaToken.getUid();
            LogUtil.i(TAG, "Authorized success, token: " + sinaToken.getToken() +
                    " phone: " + sinaToken.getPhoneNum() + "uid：" + sinaToken.getUid());
            RetrofitHelper.getInstance().user()
                    .sinaAuth(uid)
                    .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .doOnNext(ServerResponse::checkSuccess)
                    .map(ServerResponse::getData)
                    .doOnNext(token -> UserContainer.getUser().setToken(token))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            token -> LoginPresenter.notifyGetUserInfo(),
                            throwable -> onAuthException(throwable, uid)
                    );

        }

        @Override
        public void cancel() {
            mOnLoginReturnListener.onFailure(HINT_ON_CANCEL);
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            String msg = "错误码：" + errorMessage.getErrorMessage() +
                    "\n错误信息："  + errorMessage.getErrorCode();
            LogUtil.i(TAG, "Login error: " + msg);
            mOnLoginReturnListener.onFailure(msg);
        }

        /**
         * 授权时异常处理
         * 如果异常类型为ServerException说明该第三方帐号未补全信息
         * 跳转到RegisterActivity补全信息
         */
        private void onAuthException(Throwable throwable, String openid) {
            if (throwable instanceof ServerException) {
                Toast.makeText(mActivity, HINT_COMPLETING_INFO, Toast.LENGTH_SHORT).show();
                RegisterActivity.actionStart(mActivity, RegisterActivity.MODE_SINA_INFO, openid);
            } else {
                mOnLoginReturnListener.onFailure(ServerRequestExceptionHandler.handle(throwable));
            }
        }
    }

}
