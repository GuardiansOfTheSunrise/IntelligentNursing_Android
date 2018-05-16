package com.gots.intelligentnursing.presenter.fragment;

import com.gots.intelligentnursing.activity.logined.DeviceManagementActivity;
import com.gots.intelligentnursing.business.FileCacheManager;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.User;
import com.gots.intelligentnursing.view.fragment.IMinePageView;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class MinePagePresenter extends BaseFragmentPresenter<IMinePageView> {

    private static final String HINT_ON_NO_LOGIN = "您还没有登录哦";

    public MinePagePresenter(IMinePageView view) {
        super(view);
    }

    public void onDeviceManagementButtonClicked() {
        DeviceManagementActivity.actionStart(getActivity());
    }

    public void onLogoutButtonClicked() {
        User user = UserContainer.getUser();
        if (user.getUserInfo() == null) {
            onException(HINT_ON_NO_LOGIN);
        } else {
            user.init();
            FileCacheManager.getInstance(getActivity()).clearUsernameAndPassword();
            onLogoutSuccess();
        }
    }

    private void onLogoutSuccess() {
        if (getView() != null) {
            getView().onLogoutSuccess();
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }
}
