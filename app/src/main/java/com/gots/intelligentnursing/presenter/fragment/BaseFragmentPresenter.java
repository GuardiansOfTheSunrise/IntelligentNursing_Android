package com.gots.intelligentnursing.presenter.fragment;

import com.gots.intelligentnursing.view.fragment.IFragmentView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Presenter的基类，泛型V为View层接口
 * 内部持有着View接口的弱引用，防止内存泄漏
 * @author zhqy
 * @date 2018/3/30
 */

public abstract class BaseFragmentPresenter<V extends IFragmentView> {

    protected Reference<V> mViewRef;

    public BaseFragmentPresenter(V view){
        mViewRef = new WeakReference<>(view);
    }

    protected V getView() {
        return mViewRef.get();
    }

    public void detachView() {
        mViewRef.clear();
        mViewRef = null;
    }

    protected RxFragment getFragment() {
        return (RxFragment) getView();
    }

    protected RxAppCompatActivity getActivity() {
        return (RxAppCompatActivity) getFragment().getActivity();
    }
}
