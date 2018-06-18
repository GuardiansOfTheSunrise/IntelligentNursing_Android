package com.gots.intelligentnursing.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import com.gots.intelligentnursing.business.ActivityCollector;
import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.customview.TitleCenterToolbar;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.presenter.activity.BaseActivityPresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity的基类
 * @author zhqy
 * @date 2018/3/30
 */

public abstract class BaseActivity<P extends BaseActivityPresenter> extends RxAppCompatActivity {

    private static final Pattern PATTERN_LOGINED_ACTIVITY = Pattern.compile("com.gots.intelligentnursing.activity.logined.*");

    private TitleCenterToolbar mToolbar;

    protected P mPresenter;

    private RelativeLayout mTopLayout;

    private View mProgressBarView;
    private ProgressBar mProgressBar;
    private TextView mProgressBarHintTextView;

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
        mProgressBar = findViewById(R.id.pb_base_progress_bar);
        mProgressBarHintTextView = findViewById(R.id.tv_base_progress_bar_hint);
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
        if (isDisplayProgressBar()) {
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
        ActivityCollector.add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void startActivity(Intent intent) {
        String activityClassName = intent.getComponent().getClassName();
        Matcher matcher = PATTERN_LOGINED_ACTIVITY.matcher(activityClassName);
        if (matcher.matches()) {
            if (UserContainer.getUser().getUserInfo() == null) {
                LoginActivity.actionStart(this, activityClassName);
                return;
            }
        }
        super.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        ActivityCollector.remove(this);
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
     * 重写isDisplayProgressBar()
     * 可以通过该方法设置ProgressBar的提示语
     * @param hint ProgressBar的提示语
     */
    public void setProgressBarHint(String hint) {
        if (isDisplayProgressBar()) {
            mProgressBarHintTextView.setText(hint);
        }
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
     * 子类重写getProgressBarHintText()方法后
     * 调用该方法设置ProgressBar进度
     * @param progress 进度
     */
    public void setProgressBarProgress(int progress) {
        if (mProgressBar != null) {
            mProgressBar.setProgress(progress);
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
     * 子类Activity重写该方法可以实例化居中的ProgressBar提示框
     * 通过mProgressBarView.setVisibility(View.VISIBLE)来显示
     * 默认返回false，此时不实例化ProgressBar
     * @return 是否实例化ProgressBar
     */
    protected boolean isDisplayProgressBar() {
        return false;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent (DataEvent event) {
        String action = event.getAction();
        if (action.equals(EventPoster.ACTION_UPUSH_GET_NOTIFICATION)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert by EventBus");
            builder.setMessage("Get U-Push Notification");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", null);
            builder.show();
        } else if (action.equals(EventPoster.ACTION_AUTO_LOGIN_FAILURE)) {
            ActivityCollector.finishLoginedActivity();
            String currentActivityClassName = getClass().getName();
            LoginActivity.actionStart(this, currentActivityClassName);
        }
    }
}
