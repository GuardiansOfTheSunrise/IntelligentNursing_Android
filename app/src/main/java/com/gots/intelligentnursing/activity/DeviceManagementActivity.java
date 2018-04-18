package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.presenter.DeviceManagementPresenter;
import com.gots.intelligentnursing.view.IDeviceManagementView;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public class DeviceManagementActivity extends BaseActivity<DeviceManagementPresenter> implements IDeviceManagementView {

    private static final String TOOLBAR_TITLE = "设备管理";

    private RelativeLayout mNotBindingLayout;
    private LinearLayout mIsBindingLayout;
    private TextView mDeviceIdTextView;

    private void initView() {
        setToolbarTitle(TOOLBAR_TITLE);

        mNotBindingLayout = findViewById(R.id.layout_device_management_not_binding);
        mIsBindingLayout = findViewById(R.id.layout_device_management_is_binding);
        mDeviceIdTextView = findViewById(R.id.tv_device_management_id);

        FloatingActionButton addDeviceButton = findViewById(R.id.fab_device_management_add);
        addDeviceButton.setOnClickListener(v -> mPresenter.onAddDeviceButtonClicked());

        Button connectButton = findViewById(R.id.bt_device_management_connect);
        connectButton.setOnClickListener(v ->mPresenter.onConnectButtonClicked());

        Button deleteButton = findViewById(R.id.bt_device_management_delete);
        deleteButton.setOnClickListener(v -> mPresenter.onDeleteButtonClicked());

        setLayoutDisplay(UserContainer.getUser().getBindingDeviceId() != null);
    }

    private void setLayoutDisplay(boolean isBinding) {
        if(isBinding) {
            mIsBindingLayout.setVisibility(View.VISIBLE);
            mNotBindingLayout.setVisibility(View.GONE);
            mDeviceIdTextView.setText(UserContainer.getUser().getBindingDeviceId());
        } else {
            mIsBindingLayout.setVisibility(View.GONE);
            mNotBindingLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode,data);
    }

    @Override
    protected DeviceManagementPresenter createPresenter() {
        return new DeviceManagementPresenter(this);
    }

    @Override
    public void onException(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBindSuccess(String id) {
        setLayoutDisplay(true);
        mDeviceIdTextView.setText(id);
    }

    @Override
    public void onUnbindSuccess() {
        setLayoutDisplay(false);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DeviceManagementActivity.class);
        context.startActivity(intent);
    }
}
