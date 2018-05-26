package com.gots.intelligentnursing.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gots.intelligentnursing.tools.LogUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

    private Tencent mTencent;
    private IUiListener mListener;
    private RxAppCompatActivity mActivity;

    public TencentLoginManager(RxAppCompatActivity activity, OnLoginReturnListener listener) {
        super(listener);
        mActivity = activity;
        mTencent = Tencent.createInstance(APP_ID, mActivity);
        mListener = new TencentListener();
    }

    public void login(Activity from) {
        mTencent.login(from, SCOPE, mListener);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);

        if(requestCode == Constants.REQUEST_API) {
            if(resultCode == Constants.REQUEST_LOGIN) {
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

        private void handleOnAuthorizedComplete(String openid, String accessToken, String expiresIn) {
            mTencent.setOpenId(openid);
            mTencent.setAccessToken(accessToken, expiresIn);
            UserInfo userInfo = new UserInfo(mActivity, mTencent.getQQToken());
            userInfo.getUserInfo(this);
        }

        private void handleOnGetUserInfoComplete(JSONObject jsonObject) {
            try {
                String nickname = jsonObject.getString("nickname");
                String headUrl = jsonObject.getString("figureurl_qq_2");
                // TODO: 2018/5/25  
                mOnLoginReturnListener.onSuccess(CHANNEL_TENCENT, null);
            } catch (JSONException e) {
                LogUtil.logExceptionStackTrace(TAG, e);
                mOnLoginReturnListener.onFailure(e.getMessage());
            }
        }

        @Override
        public void onComplete(Object o) {
            JSONObject jo = (JSONObject) o;
            String openid = null;
            String accessToken = null;
            String expiresIn = null;
            try {
                openid = jo.getString("openid");
                accessToken = jo.getString("access_token");
                expiresIn = jo.getString("expires_in");
            } catch (JSONException e) {
                if (openid != null) {
                    LogUtil.e(TAG, "Data lost.");
                    LogUtil.logExceptionStackTrace(TAG, e);
                    mOnLoginReturnListener.onFailure(HINT_UNKONWN_EXCEPTION);
                    return;
                } else {
                    LogUtil.i(TAG, "Not authorized callback or data lost.");
                }
            }
            if (openid != null && accessToken != null && expiresIn != null) {
                handleOnAuthorizedComplete(openid, accessToken, expiresIn);
            } else if (openid == null && accessToken == null && expiresIn == null) {
                handleOnGetUserInfoComplete(jo);
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
    }

}
