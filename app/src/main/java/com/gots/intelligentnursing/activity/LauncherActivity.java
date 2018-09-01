package com.gots.intelligentnursing.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gots.intelligentnursing.R;

/**
 * @author zhqy
 * @date 2018/7/20
 */

public class LauncherActivity extends AppCompatActivity {

    /**
     * onCreate()在Application初始化完成后才被调用
     * 初始化完成前显示启动页
     * 初始化完成后调用onCreate()跳转到MainActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        MainActivity.actionStart(this);
        finish();
    }
}
