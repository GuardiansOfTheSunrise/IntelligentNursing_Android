package com.gots.intelligentnursing.activity.logined;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.BaseActivity;
import com.gots.intelligentnursing.adapter.SimpleListItemOneAdapter;
import com.gots.intelligentnursing.presenter.activity.DeviceControlPresenter;
import com.gots.intelligentnursing.view.activity.IDeviceControlView;

import java.util.Arrays;

/**
 * @author zhqy
 * @date 2018/4/15
 */

public class DeviceControlActivity extends BaseActivity<DeviceControlPresenter> implements IDeviceControlView {

    private static final String TOOLBAR_TITLE = "设备控制";

    private static final String HINT_ON_CONNECTING = "正在连接设备，请稍后...";

    private String[] mControlStringArray = {"设置时间", "寻找手环"};

    private LinearLayout mContentLayout;

    private void initView() {
        showProgressBar();
        mContentLayout = findViewById(R.id.ll_device_control_content);

        RecyclerView controlListRecyclerView = findViewById(R.id.rv_device_control_operate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        controlListRecyclerView.setLayoutManager(linearLayoutManager);
        SimpleListItemOneAdapter adapter = new SimpleListItemOneAdapter(Arrays.asList(mControlStringArray));
        adapter.setOnItemClickedListener(position -> mPresenter.onItemClicked(position));
        controlListRecyclerView.setAdapter(adapter);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        setToolbarTitle(TOOLBAR_TITLE);
        setProgressBarHint(HINT_ON_CONNECTING);
        initView();
        mPresenter.connectDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected DeviceControlPresenter createPresenter() {
        return new DeviceControlPresenter(this);
    }

    @Override
    protected boolean isDisplayProgressBar() {
        return true;
    }

    @Override
    public void onConnectSuccess() {
        dismissProgressBar();
        mContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, DeviceControlActivity.class);
        activity.startActivity(intent);
    }
}
