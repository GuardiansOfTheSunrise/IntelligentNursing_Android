package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.fragment.MinePagePresenter;
import com.gots.intelligentnursing.view.fragment.IMinePageView;

/**
 * @author Accumulei
 * @date 2018/4/17.
 */
public class MinePageFragment extends BaseFragment<MinePagePresenter> implements IMinePageView {

    private static final String HINT_ON_LOGOUT_SUCCESS = "您已退出登录";

    private void initButton(View view) {
        Button deviceManagementButton = view.findViewById(R.id.bt_page_mine_device_management);
        deviceManagementButton.setOnClickListener(v -> mPresenter.onDeviceManagementButtonClicked());
        Button logoutButton = view.findViewById(R.id.bt_page_mine_logout);
        logoutButton.setOnClickListener(v -> mPresenter.onLogoutButtonClicked());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_mine, container, false);
        initButton(view);
        return view;
    }

    @Override
    public void onLogoutSuccess() {
        Toast.makeText(getActivity(), HINT_ON_LOGOUT_SUCCESS, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected MinePagePresenter createPresenter() {
        return new MinePagePresenter(this);
    }
}
