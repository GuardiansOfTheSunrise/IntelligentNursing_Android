package com.gots.intelligentnursing.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.MainPresenter;
import com.gots.intelligentnursing.view.IMainView;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public class MainActivity extends BaseActivity<MainPresenter> implements IMainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        Button button = findViewById(R.id.bt_main_device_management);
        button.setOnClickListener(v -> startActivity(new Intent(this, DeviceManagementActivity.class)));
    }

    @Override
    protected boolean displayBackButton() {
        return false;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
