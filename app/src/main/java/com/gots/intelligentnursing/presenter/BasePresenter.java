package com.gots.intelligentnursing.presenter;

import com.gots.intelligentnursing.view.IView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Presenter的基类，泛型V为View层接口
 * 内部持有着View接口的弱引用，防止内存泄漏
 * @author zhqy
 * @date 2018/3/30
 */

public abstract class BasePresenter<V extends IView> {

    protected Reference<V> mViewRef;

    public BasePresenter(V view){
        mViewRef = new WeakReference<V>(view);
    }

    protected V getView(){
        return mViewRef.get();
    }

    public void detachView(){
        mViewRef.clear();
        mViewRef = null;
    }
}
