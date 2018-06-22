package com.gots.intelligentnursing.presenter.fragment;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.logined.DeviceManagementActivity;
import com.gots.intelligentnursing.business.FileCacheManager;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.MineItem;
import com.gots.intelligentnursing.entity.User;
import com.gots.intelligentnursing.view.fragment.IMinePageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class MinePagePresenter extends BaseFragmentPresenter<IMinePageView> {

    private static final String HINT_ON_NO_LOGIN = "您还没有登录哦";

    private static final int LOCATION_MINE_LIST_DEVICE_MANAGEMENT = 1;
    private static final int LOCATION_MINE_LIST_SETTING = 2;
    private static final int LOCATION_MINE_LIST_CHECK_UPDATE = 3;
    private static final int LOCATION_MINE_LIST_HELP = 5;
    private static final int LOCATION_MINE_LIST_SUGGEST = 6;
    private static final int LOCATION_MINE_LIST_ABOUT = 7;
    private static final int LOCATION_MINE_LIST_LOGOUT = 9;

    public MinePagePresenter(IMinePageView view) {
        super(view);
    }

    public List<MineItem> getDataList() {
        List<MineItem> mineItemList = new ArrayList<>();
        MineItem blank = new MineItem();
        mineItemList.add(blank);

        mineItemList.add(new MineItem(R.drawable.ic_page_mine_item_manage, "设备管理"));
        mineItemList.add(new MineItem(R.drawable.ic_page_mine_item_settings,"设置"));
        mineItemList.add(new MineItem(R.drawable.ic_page_mine_item_update,"检查更新"));
        mineItemList.add(blank);

        mineItemList.add(new MineItem(R.drawable.ic_page_mine_item_help, "帮助"));
        mineItemList.add(new MineItem(R.drawable.ic_page_mine_item_comment, "意见建议"));
        mineItemList.add(new MineItem(R.drawable.ic_page_mine_item_about,"关于"));
        mineItemList.add(blank);

        mineItemList.add(new MineItem(R.drawable.ic_page_mine_item_quit, "退出登录"));
        return mineItemList;
    }

    public void onItemClicked(int position) {
        switch (position) {
            case LOCATION_MINE_LIST_DEVICE_MANAGEMENT:
                DeviceManagementActivity.actionStart(getActivity());
                break;
            case LOCATION_MINE_LIST_SETTING:
                onException("设置");
                break;
            case LOCATION_MINE_LIST_CHECK_UPDATE:
                onException("当前是最新版本");
                break;
            case LOCATION_MINE_LIST_HELP:
                onException("帮助");
                break;
            case LOCATION_MINE_LIST_SUGGEST:
                onException("意见建议");
                break;
            case LOCATION_MINE_LIST_ABOUT:
                onException("关于");
                break;
            case LOCATION_MINE_LIST_LOGOUT:
                User user = UserContainer.getUser();
                if (user.getUserInfo() == null) {
                    onException(HINT_ON_NO_LOGIN);
                } else {
                    user.init();
                    FileCacheManager.getInstance(getActivity()).clearUsernameAndPassword();
                    onLogoutSuccess();
                }
                break;
                default:
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
