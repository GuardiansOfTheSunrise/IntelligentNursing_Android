package com.gots.intelligentnursing.presenter;

import com.gots.intelligentnursing.activity.DeviceManagementActivity;
import com.gots.intelligentnursing.view.IMinePageView;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class MinePagePresenter extends BasePresenter<IMinePageView> {

    public MinePagePresenter(IMinePageView view) {
        super(view);
    }

    public void onDeviceManagementButtonClicked() {
        DeviceManagementActivity.actionStart(getActivity());
    }

}
