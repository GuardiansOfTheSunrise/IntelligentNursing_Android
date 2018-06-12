package com.gots.intelligentnursing.business;

/**
 * @author zhqy
 * @date 2018/5/24
 */

public abstract class BaseLoginManager {

    protected OnFailureListener mOnLoginReturnListener;


    public interface OnFailureListener {
        /**
         * 获取token失败时回调
         *
         * @param msg 失败原因
         */
        void onFailure(String msg);
    }

    public BaseLoginManager(OnFailureListener onLoginReturnListener) {
        mOnLoginReturnListener = onLoginReturnListener;
    }

    /**
     * 用于释放资源
     */
    public abstract void recycler();
}
