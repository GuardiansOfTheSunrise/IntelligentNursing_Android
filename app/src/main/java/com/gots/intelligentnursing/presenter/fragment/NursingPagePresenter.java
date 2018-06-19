package com.gots.intelligentnursing.presenter.fragment;

import com.gots.intelligentnursing.activity.logined.ElderInfoActivity;
import com.gots.intelligentnursing.activity.logined.GeographyFenceActivity;
import com.gots.intelligentnursing.customview.LineChartPager;
import com.gots.intelligentnursing.view.fragment.INursingPageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class NursingPagePresenter extends BaseFragmentPresenter<INursingPageView> {
    public NursingPagePresenter(INursingPageView view) {
        super(view);
    }

    public void onFenceSettingButtonClick() {
        GeographyFenceActivity.actionStart(getActivity());
    }

    public void onElderInfoButtonClick() {
        ElderInfoActivity.actionStart(getActivity());
    }

    public void getChartData() {
        // 此处为模拟数据
        // TODO: 2018/6/17 从服务器获取数据
        List<LineChartPager.LineChartData> lineChartDataList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        float[] x1 = new float[12];
        for (int i = x1.length - 1; i >= 0; i--) {
            x1[i] = hour - 2 * (12 - i);
        }
        float[] y1 = {85, 89, 93, 95, 98, 102, 96, 99, 94, 86, 92, 88};
        lineChartDataList.add(new LineChartPager.LineChartData("近24h心率", x1, y1, 77, 108,
                (value, axis) -> String.valueOf((int) ((value + 24) % 24)), null));

        float[] x2 = new float[7];
        float[] y2 = {88, 97, 95, 100, 91, 85, 92};
        for (int  i = x2.length - 1; i >= 0; i--) {
            x2[i] = dayOfWeek - (6 - i);
        }
        String[] dayString = {"日", "一", "二", "三", "四", "五", "六"};
        lineChartDataList.add(new LineChartPager.LineChartData("近7天心率", x2, y2, 77, 108,
                (value, axis) -> dayString[(int) (value + 7) % 7], null));
        onGetChartDataSuccess(lineChartDataList);
    }

    private void onGetChartDataSuccess(List<LineChartPager.LineChartData> lineChartDataList) {
        if (getView() != null) {
            getView().onGetChartDataSuccess(lineChartDataList);
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }


}
