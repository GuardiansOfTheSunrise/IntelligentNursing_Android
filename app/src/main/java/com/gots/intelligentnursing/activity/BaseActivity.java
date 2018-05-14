package com.gots.intelligentnursing.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import com.gots.intelligentnursing.MyApplication;
import com.gots.intelligentnursing.customview.TitleCenterToolbar;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.presenter.activity.BaseActivityPresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Activity的基类
 * @author zhqy
 * @date 2018/3/30
 */

public abstract class BaseActivity<P extends BaseActivityPresenter> extends RxAppCompatActivity {

    private TitleCenterToolbar mToolbar;

    protected P mPresenter;

    private RelativeLayout mTopLayout;

    private View mProgressBarView;

    private UpushBroadcastReceiver mUpushBroadcastReceiver;

    private void initToolbarView() {
        mToolbar = findViewById(R.id.toolbar_base);
        setSupportActionBar(mToolbar);
        if (isDisplayBackButton()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(getHomeAsUpIndicator());
            }
        }
    }

    private void initContentViewWithToolbar(int layoutResID) {
        mTopLayout = findViewById(R.id.rl_relative_base);
        View contentView = LayoutInflater.from(this).inflate(layoutResID, mTopLayout, false);
        ViewGroup.LayoutParams viewGroupLayoutParams = contentView.getLayoutParams();
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(viewGroupLayoutParams);
        relativeLayoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar_base);
        contentView.setLayoutParams(relativeLayoutParams);
        mTopLayout.addView(contentView);
    }

    private void initContentViewWithoutToolbar(int layoutResID) {
        mTopLayout = findViewById(R.id.rl_relative_base);
        LayoutInflater.from(this).inflate(layoutResID, mTopLayout, true);
    }

    private void initProgressBarView() {
        mProgressBarView = LayoutInflater.from(this)
                .inflate(R.layout.view_base_progress_bar, mTopLayout, false);
        ViewGroup.LayoutParams viewGroupLayoutParams = mProgressBarView.getLayoutParams();
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(viewGroupLayoutParams);
        relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBarView.setLayoutParams(relativeLayoutParams);
        mTopLayout.addView(mProgressBarView);
        TextView hintTextView = findViewById(R.id.tv_base_progress_bar_hint);
        hintTextView.setText(getProgressBarHintText());
        mProgressBarView.setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (isDisplayToolbar()) {
            super.setContentView(R.layout.activity_base_toolbar);
            initToolbarView();
            initContentViewWithToolbar(layoutResID);
        } else {
            super.setContentView(R.layout.activity_base_no_toolbar);
            initContentViewWithoutToolbar(layoutResID);
        }
        if (getProgressBarHintText() != null) {
            initProgressBarView();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置沉浸式状态栏浅色图标
            // 只适用于6.0以上系统
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mPresenter = createPresenter();
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        registerUpushBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        unregisterUpushBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 设置Toolbar的title
     * @param title 需要设置的title
     */
    public void setToolbarTitle(String title) {
        if (!isDisplayToolbar()) {
            return;
        }
        mToolbar.setTitle(title);
    }

    /**
     * 子类重写getProgressBarHintText()方法后
     * 调用该方法显示ProgressBar
     * 不重写getProgressBarHintText()该方法不起作用
     */
    public void showProgressBar() {
        if (mProgressBarView != null) {
            mProgressBarView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 子类重写getProgressBarHintText()方法后
     * 调用该方法取消显示ProgressBar
     * 不重写getProgressBarHintText()该方法不起作用
     */
    public void dismissProgressBar() {
        if (mProgressBarView != null) {
            mProgressBarView.setVisibility(View.GONE);
        }
    }

    /**
     * 用于创建Activity所对应的Presenter，在onCreate()中会被调用
     * @return 返回该Activity所对应的Presenter
     */
    protected abstract P createPresenter();

    /**
     * 子类Activity重写该方法可以设置是否显示Toolbar
     * @return 默认返回true表示显示Toolbar，如不需要Toolbar，则重写该方法返回false
     */
    protected boolean isDisplayToolbar() {
        return true;
    }

    /**
     * 子类Activity重写该方法可以设置是否显示Toolbar的返回按钮
     * @return 默认返回true表示显示返回按钮，如不需要返回按钮，则重写该方法返回false
     */
    protected boolean isDisplayBackButton() {
        return true;
    }

    /**
     * 子类Activity重写该方法可以设置是否显示返回按钮样式
     * @return 默认返回使用灰色箭头样式
     * 如需改变样式，则重写该方法返回图片资源id，如R.drawable.menu
     * 注意，当需要改变样式并做出非默认响应时
     * 在子类Activity中需要重写onOptionsItemSelected()方法并拦截事件
     * BaseActivity默认处理是关闭当前Activity
     */
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_arrow_back;
    }

    /**
     * 子类Activity重写该方法可以实例化居中的ProgressBar提示框
     * 通过mProgressBarView.setVisibility(View.VISIBLE)来显示
     * 默认返回null，此时不实例化ProgressBar，不可调用mProgressBarView
     * 重写该方法返回提示语，如无需提示语则返回""即可
     * @return ProgressBar的提示语
     */
    protected String getProgressBarHintText() {
        return null;
    }

    class UpushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert Testing");
            builder.setMessage("Get U-Push Notification");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", null);
            //builder.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent (DataEvent event) {
        LogUtil.i("MyApplication","onUserEvent");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert by EventBus");
        builder.setMessage("Get U-Push Notification");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void registerUpushBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.gots.intelligentnursing.NOTIFICATION_GET");
        mUpushBroadcastReceiver = new UpushBroadcastReceiver();
        registerReceiver(mUpushBroadcastReceiver, intentFilter);
    }

    private void unregisterUpushBroadcastReceiver() {
        if (mUpushBroadcastReceiver != null) {
            unregisterReceiver(mUpushBroadcastReceiver);
        }
    }
}
