package com.gots.intelligentnursing.business;

import android.content.Intent;
import android.widget.Toast;

import com.gots.intelligentnursing.activity.RegisterActivity;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.exception.ServerException;
import com.gots.intelligentnursing.presenter.activity.LoginPresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/5/24
 */

public class TencentLoginManager extends BaseLoginManager {

    private static final String TAG = "TencentLoginManager";

    private static final String APP_ID = "1106913426";
    private static final String SCOPE = "all";

    private static final String HINT_ON_CANCEL = "您放弃了登录";
    private static final String HINT_UNKONWN_EXCEPTION = "未知异常";
    private static final String HINT_COMPLETING_INFO = "您的信息不完善，请先补全信息";

    private Tencent mTencent;
    private IUiListener mListener;
    private RxAppCompatActivity mActivity;

    public TencentLoginManager(RxAppCompatActivity activity, OnFailureListener listener) {
        super(listener);
        mActivity = activity;
        mTencent = Tencent.createInstance(APP_ID, mActivity);
        mListener = new TencentListener();
    }

    public void login() {
        mTencent.login(mActivity, SCOPE, mListener);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, mListener);
            }
        }
    }

    @Override
    public void recycler() {
        mActivity = null;
        mTencent = null;
    }

    private class TencentListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            JSONObject jo = (JSONObject) o;

            try {
                String openid = jo.getString("openid");
                LogUtil.i(TAG, "Authorized success, openid: " + openid);
                RetrofitHelper.getInstance().user()
                        .tencentAuth(openid)
                        .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .doOnNext(ServerResponse::checkSuccess)
                        .map(ServerResponse::getData)
                        .doOnNext(token -> UserContainer.getUser().setToken(token))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                token -> LoginPresenter.notifyGetUserInfo(),
                                throwable -> onAuthException(throwable, openid)
                        );
            } catch (JSONException e) {
                LogUtil.e(TAG, "Can not get openid from JsonObject.");
                LogUtil.logExceptionStackTrace(TAG, e);
                mOnLoginReturnListener.onFailure(HINT_UNKONWN_EXCEPTION);
            }
        }

        @Override
        public void onError(UiError uiError) {
            String msg = "错误码：" + uiError.errorCode +
                    "\n错误信息：" + uiError.errorMessage +
                    "\n错误详情：" + uiError.errorDetail;
            LogUtil.i(TAG, "Login error: " + msg);
            mOnLoginReturnListener.onFailure(msg);
        }

        @Override
        public void onCancel() {
            mOnLoginReturnListener.onFailure(HINT_ON_CANCEL);
        }


        /**
         * 授权时异常处理
         * 如果异常类型为ServerException说明该第三方帐号未补全信息
         * 跳转到RegisterActivity补全信息
         */
        private void onAuthException(Throwable throwable, String openid) {
            if (throwable instanceof ServerException) {
                Toast.makeText(mActivity, HINT_COMPLETING_INFO, Toast.LENGTH_SHORT).show();
                RegisterActivity.actionStart(mActivity, RegisterActivity.MODE_TENCENT_INFO, openid);
            } else {
                mOnLoginReturnListener.onFailure(ServerRequestExceptionHandler.handle(throwable));
            }
        }
    }

}
