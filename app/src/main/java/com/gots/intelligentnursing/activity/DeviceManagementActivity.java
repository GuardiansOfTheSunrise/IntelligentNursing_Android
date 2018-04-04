package com.gots.intelligentnursing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.DeviceManagementPresenter;
import com.gots.intelligentnursing.view.IDeviceManagementView;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public class DeviceManagementActivity extends BaseActivity<DeviceManagementPresenter> implements IDeviceManagementView{

    private static final String TOOLBAR_TITLE = "设备管理";

    private void initView(){
        Button addDeviceButton = findViewById(R.id.bt_device_management_add);
        addDeviceButton.setOnClickListener(v -> mPresenter.onAddButtonClicked());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);
        setToolbarTitle(TOOLBAR_TITLE);
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onQrCodeParseError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQeCodeParseSuccess(String id) {
        Toast.makeText(this, "id is " + id, Toast.LENGTH_SHORT).show();
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, DeviceManagementActivity.class);
        context.startActivity(intent);
    }
}
