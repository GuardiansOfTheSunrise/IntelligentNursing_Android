package com.gots.intelligentnursing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.DeviceManagementActivity;
import com.gots.intelligentnursing.presenter.MinePagePresenter;
import com.gots.intelligentnursing.view.IMinePageView;

/**
 * @author Accumulei
 * @date 2018/4/17.
 */
public class MinePageFragment extends BaseFragment<MinePagePresenter> implements IMinePageView {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_mine, container, false);
        Button deviceManagementButton = view.findViewById(R.id.bt_mine_device_management);
        deviceManagementButton.setOnClickListener(v -> mPresenter.onDeviceManagementButtonClicked());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected MinePagePresenter createPresenter() {
        return new MinePagePresenter(this);
    }
}
