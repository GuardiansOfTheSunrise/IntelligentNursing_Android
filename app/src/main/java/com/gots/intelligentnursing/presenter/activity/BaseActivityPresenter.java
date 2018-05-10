package com.gots.intelligentnursing.presenter.activity;

import com.gots.intelligentnursing.view.activity.IActivityView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Presenter的基类，泛型V为View层接口
 * 内部持有着View接口的弱引用，防止内存泄漏
 * @author zhqy
 * @date 2018/3/30
 */

public abstract class BaseActivityPresenter<V extends IActivityView> {

    protected Reference<V> mViewRef;

    public BaseActivityPresenter(V view){
        mViewRef = new WeakReference<>(view);
    }

    protected V getView() {
        return mViewRef.get();
    }

    public void detachView() {
        mViewRef.clear();
        mViewRef = null;
    }

    protected RxAppCompatActivity getActivity() {
        return (RxAppCompatActivity) getView();
    }
}
