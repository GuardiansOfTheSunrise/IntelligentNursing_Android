package com.gots.intelligentnursing.view.activity;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public interface IMainView extends IActivityView {

    /**
     * 登录成功回调
     *
     * @param username 登录的用户名
     */
    void onLoginSuccess(String username);
}
