package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.fragment.NursingPagePresenter;
import com.gots.intelligentnursing.view.fragment.INursingPageView;

/**
 * @author Accumulei
 * @date 2018/4/17.
 */
public class NursingPageFragment extends BaseFragment<NursingPagePresenter> implements INursingPageView {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_nursing, container, false);
        ImageView fenceSettingButton = view.findViewById(R.id.bt_page_nursing_fence_setting);
        fenceSettingButton.setOnClickListener(v -> mPresenter.onFenceSettingButtonClicked());
        return view;
    }

    @Override
    protected NursingPagePresenter createPresenter() {
        return new NursingPagePresenter(this);
    }
}
