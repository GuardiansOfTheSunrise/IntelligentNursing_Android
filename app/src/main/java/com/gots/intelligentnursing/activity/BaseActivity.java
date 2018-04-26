package com.gots.intelligentnursing.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.gots.intelligentnursing.customview.TitleCenterToolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.BasePresenter;

/**
 * Activity的基类
 * 泛型P为Activity对应的Presenter且必须为BasePresenter的子类
 * @author zhqy
 * @date 2018/3/30
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    private TitleCenterToolbar mToolbar;

    protected P mPresenter;

    private void initToolbar() {
        if (isDisplayToolbar()) {
            mToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            if (isDisplayBackButton()) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeAsUpIndicator(getHomeAsUpIndicator());
                }
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (isDisplayToolbar()) {
            super.setContentView(R.layout.activity_base_toolbar);
            initToolbar();
        } else {
            super.setContentView(R.layout.activity_base_no_toolbar);
        }
        LinearLayout linearLayout = findViewById(R.id.layout_linear_base);
        LayoutInflater.from(this).inflate(layoutResID, linearLayout, true);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
     * @return 默认返回0表示使用默认样式
     * 如需改变样式，则重写该方法返回图片资源id，如R.drawable.menu
     * 注意，当需要改变样式并做出非默认响应时
     * 在子类Activity中需要重写onOptionsItemSelected()方法并拦截事件
     * BaseActivity默认处理是关闭当前Activity
     */
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_arrow_back;
    }
}
