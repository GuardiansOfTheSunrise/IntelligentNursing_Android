package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.UserInfo;

/**
 * @author zhqy
 * @date 2018/5/24
 */

public abstract class BaseLoginManager {

    protected OnLoginReturnListener mOnLoginReturnListener;

    public static final int CHANNEL_ORIGINAL = 0;
    public static final int CHANNEL_TENCENT = 1;
    public static final int CHANNEL_SINA = 2;

    public interface OnLoginReturnListener {
        void onSuccess(int channel, UserInfo userInfo);
        void onFailure(String msg);
    }

    public BaseLoginManager(OnLoginReturnListener onLoginReturnListener) {
        mOnLoginReturnListener = onLoginReturnListener;
    }

    public abstract void recycler();
}
