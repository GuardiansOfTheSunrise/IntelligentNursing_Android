package com.gots.intelligentnursing.view.activity;

/**
 * @author zhqy
 * @date 2018/5/9
 */

public interface ILoginView extends IActivityView {
    /**
     * 登录成功回调
     * @param username 登录的用户名
     */
    void onLoginSuccess(String username);

    /**
     * 正在登录回调
     */
    void onLogging();

    /**
     * 出现异常回调
     * @param msg 异常原因
     */
    void onException(String msg);
}
