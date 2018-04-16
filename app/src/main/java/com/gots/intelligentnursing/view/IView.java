package com.gots.intelligentnursing.view;

import android.app.Activity;

/**
 * @author zhqy
 * @date 2018/4/3
 */

public interface IView {
    /**
     * View层接口的父接口
     * 接口默认实现将this强转成Activity返回
     * Activity实现接口时无需重写该方法
     * @return 返回View层对应的Activity
     */
    default Activity getActivity(){
        return (Activity) this;
    }
}
