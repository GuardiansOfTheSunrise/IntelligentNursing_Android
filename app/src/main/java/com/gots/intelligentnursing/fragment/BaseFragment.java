package com.gots.intelligentnursing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gots.intelligentnursing.presenter.activity.BaseActivityPresenter;
import com.gots.intelligentnursing.presenter.fragment.BaseFragmentPresenter;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public abstract class BaseFragment<P extends BaseFragmentPresenter> extends RxFragment {

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    /**
     * 用于创建Fragment所对应的Presenter，在onCreate()中会被调用
     * @return 返回该Fragment所对应的Presenter
     */
    protected abstract P createPresenter();
}
