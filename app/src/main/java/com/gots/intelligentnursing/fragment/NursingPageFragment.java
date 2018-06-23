package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.customview.LineChartPager;
import com.gots.intelligentnursing.presenter.fragment.NursingPagePresenter;
import com.gots.intelligentnursing.view.fragment.INursingPageView;

import java.util.List;

/**
 * @author Accumulei
 * @date 2018/4/17.
 */
public class NursingPageFragment extends BaseFragment<NursingPagePresenter> implements INursingPageView {

    private LineChartPager mLineChartPager;
    private TextView mHintTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_nursing, container, false);
        mHintTextView = view.findViewById(R.id.tv_page_nursing_hint);
        ImageView fenceSettingButton = view.findViewById(R.id.bt_page_nursing_fence_setting);
        fenceSettingButton.setOnClickListener(v -> mPresenter.onFenceSettingButtonClick());
        ImageView elderInfoButton = view.findViewById(R.id.bt_page_nursing_elder_info);
        elderInfoButton.setOnClickListener(v -> mPresenter.onElderInfoButtonClick());
        mLineChartPager = view.findViewById(R.id.line_chart_pager_page_nursing);
        mPresenter.getChartData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserContainer.getUser().getUserInfo() == null) {
            mHintTextView.setVisibility(View.VISIBLE);
            mLineChartPager.setVisibility(View.GONE);
        } else {
            mHintTextView.setVisibility(View.GONE);
            mLineChartPager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (UserContainer.getUser().getUserInfo() == null) {
                mHintTextView.setVisibility(View.VISIBLE);
                mLineChartPager.setVisibility(View.GONE);
            } else {
                mHintTextView.setVisibility(View.GONE);
                mLineChartPager.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onGetChartDataSuccess(List<LineChartPager.LineChartData> lineChartDataList) {
        mLineChartPager.setData(lineChartDataList);
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected NursingPagePresenter createPresenter() {
        return new NursingPagePresenter(this);
    }
}
