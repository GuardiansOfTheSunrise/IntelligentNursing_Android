package com.gots.intelligentnursing.presenter.fragment;

import com.gots.intelligentnursing.activity.logined.GeographyFenceActivity;
import com.gots.intelligentnursing.view.fragment.INursingPageView;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class NursingPagePresenter extends BaseFragmentPresenter<INursingPageView> {
    public NursingPagePresenter(INursingPageView view) {
        super(view);
    }

    public void onFenceSettingButtonClicked() {
        GeographyFenceActivity.actionStart(getActivity());
    }
}
