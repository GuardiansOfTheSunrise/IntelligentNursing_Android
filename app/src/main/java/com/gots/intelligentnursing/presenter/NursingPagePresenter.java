package com.gots.intelligentnursing.presenter;

import com.gots.intelligentnursing.activity.GeographyFenceActivity;
import com.gots.intelligentnursing.view.INursingPageView;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class NursingPagePresenter extends BasePresenter<INursingPageView> {
    public NursingPagePresenter(INursingPageView view) {
        super(view);
    }

    public void onFenceSettingButtonClicked() {
        GeographyFenceActivity.actionStart(getActivity());
    }
}
