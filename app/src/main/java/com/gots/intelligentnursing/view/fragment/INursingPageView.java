package com.gots.intelligentnursing.view.fragment;

import com.gots.intelligentnursing.customview.LineChartPager;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public interface INursingPageView extends IFragmentView {

    /**
     * 获取心率折线图数据成功回调
     * @param lineChartDataList 心率折线图数据
     */
    void onGetChartDataSuccess(List<LineChartPager.LineChartData> lineChartDataList);

    /**
     * 出现异常回调
     * @param msg 异常原因
     */
    void onException(String msg);
}
