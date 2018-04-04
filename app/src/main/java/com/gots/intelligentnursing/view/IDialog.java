package com.gots.intelligentnursing.view;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public interface IDialog {
    /**
     * 对具有对话框的View，显示对话框
     */
    void showDialog();

    /**
     * 对具有对话框的View，取消对话框显示
     */
    void dismissDialog();
}
