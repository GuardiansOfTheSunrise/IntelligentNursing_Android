package com.gots.intelligentnursing.view.activity;

/**
 * @author zhqy
 * @date 2018/5/9
 */

public interface ILoginView extends IActivityView {
    /**
     * 登录成功回调
     */
    void onLoginSuccess();

    /**
     * 出现异常回调
     * @param msg 异常原因
     */
    void onException(String msg);
}
