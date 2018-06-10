package com.gots.intelligentnursing.view.activity;

/**
 * @author zhqy
 * @date 2018/6/9
 */

public interface IRegisterView extends IActivityView {

    /**
     * 出现异常回调
     * @param msg 异常原因
     */
    void onException(String msg);

    /**
     * 获取验证码成功回调
     */
    void onGettingVerifySuccess();

    /**
     * 注册成功回调
     */
    void onRegisterSuccess();

    /**
     * 获取验证码按钮时间更新回调
     * @param second 倒计时秒数
     */
    void updateVerifyButtonTime(int second);
}
