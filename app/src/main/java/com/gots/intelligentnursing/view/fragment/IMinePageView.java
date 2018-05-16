package com.gots.intelligentnursing.view.fragment;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public interface IMinePageView extends IFragmentView {

    /**
     * 登出成功回调
     */
    void onLogoutSuccess();

    /**
     * presenter错误回调
     * @param msg 错误信息
     */
    void onException(String msg);
}
