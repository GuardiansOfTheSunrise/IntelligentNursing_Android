package com.gots.intelligentnursing.view.activity;

/**
 * @author zhqy
 * @date 2018/4/26
 */

public interface IGeographyFenceView extends IActivityView {
    /**
     * 围栏绘制成功并成功提交到服务器的回调
     */
    void onFenceDrawingSuccess();

    /**
     * 出现异常回调
     *
     * @param msg 异常原因
     */
    void onException(String msg);
}
