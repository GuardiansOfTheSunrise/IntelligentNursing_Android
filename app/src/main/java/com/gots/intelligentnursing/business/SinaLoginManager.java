package com.gots.intelligentnursing.business;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.gots.intelligentnursing.entity.SinaUserInfo;
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
    private static final String HINT_ON_INTERFACE_ERROR = "新浪接口错误";

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

    public SinaLoginManager(RxAppCompatActivity activity, OnLoginReturnListener listener) {
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
        public void onSuccess(final Oauth2AccessToken token) {
            String accessToken = token.getToken();
            long uid = Long.parseLong(token.getUid());
            LogUtil.i(TAG, "Authorized success, token: " + accessToken +
                    " phone: " + token.getPhoneNum() + "uid：" + uid);

            RetrofitHelper.getInstance().thirdParty()
                    .getUserInfoFromSina(accessToken, uid)
                    .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .doOnNext(sinaUserInfo -> sinaUserInfo.log(TAG))
                    .filter(sinaUserInfo -> !sinaUserInfo.isError())
                    .observeOn(AndroidSchedulers.mainThread())
                    .switchIfEmpty(s -> mOnLoginReturnListener.onFailure(HINT_ON_INTERFACE_ERROR))
                    .doOnNext(sinaUserInfo -> {
                        Toast.makeText(mActivity, "uid:" + sinaUserInfo.getUid() + "\nurl:" +
                                sinaUserInfo.getProfileImageUrl() + "\nname:" + sinaUserInfo.getName(), Toast.LENGTH_LONG).show();
                    })
                    .subscribe(sinaUserInfo -> mOnLoginReturnListener.onSuccess(CHANNEL_SINA, null));
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
    }

}
