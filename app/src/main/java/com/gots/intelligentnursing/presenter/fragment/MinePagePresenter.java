package com.gots.intelligentnursing.presenter.fragment;

import com.gots.intelligentnursing.activity.DeviceManagementActivity;
import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.presenter.activity.BaseActivityPresenter;
import com.gots.intelligentnursing.view.fragment.IMinePageView;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class MinePagePresenter extends BaseFragmentPresenter<IMinePageView> {

    public MinePagePresenter(IMinePageView view) {
        super(view);
    }

    public void onDeviceManagementButtonClicked() {
        DeviceManagementActivity.actionStart(getActivity());
    }

}
