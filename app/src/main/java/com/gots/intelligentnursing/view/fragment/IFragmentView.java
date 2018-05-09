package com.gots.intelligentnursing.view.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.gots.intelligentnursing.view.activity.IView;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public interface IFragmentView extends IView {
    /**
     * Fragment的View层接口的父接口
     * 接口默认实现将this强转成Fragment调用getActivity()返回
     * Fragment实现接口时无需重写该方法
     * @return 返回Fragment的View层所属的的Activity
     */
    @Override
    default Activity getActivity(){
        return ((Fragment) this).getActivity();
    }
}
