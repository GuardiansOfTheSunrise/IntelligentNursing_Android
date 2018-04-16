package com.gots.intelligentnursing.presenter;

import com.gots.intelligentnursing.activity.DeviceManagementActivity;
import com.gots.intelligentnursing.view.IMainView;

/**
 * @author zhqy
 * @date 2018/4/3
 */

public class MainPresenter extends BasePresenter<IMainView> {

    public MainPresenter(IMainView view) {
        super(view);
    }

    public void onButtonClicked() {
        DeviceManagementActivity.actionStart(getView().getActivity());
    }
}
