package com.gots.intelligentnursing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.presenter.BasePresenter;

/**
 * Activity的基类
 * 泛型P为Activity对应的Presenter且必须为BasePresenter的子类
 * @author zhqy
 * @date 2018/3/30
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity{

    protected P mPresenter;

    protected void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(displayBackButton()){
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                if(homeAsUpIndicator() != null){
                    actionBar.setHomeAsUpIndicator(homeAsUpIndicator());
                }
            }
        }
    }

    protected boolean displayBackButton(){
        return true;
    }

    protected Integer homeAsUpIndicator(){
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 用于创建Activity所对应的Presenter，在onCreate()中会被调用
     * @return 返回该Activity所对应的Presenter
     */
    protected abstract P createPresenter();
}
