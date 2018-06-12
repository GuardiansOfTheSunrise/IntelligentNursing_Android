package com.gots.intelligentnursing.presenter.activity;

import android.content.Intent;

import com.gots.intelligentnursing.activity.LoginActivity;
import com.gots.intelligentnursing.business.BaseLoginManager;
import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.business.OriginalLoginManager;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.SinaLoginManager;
import com.gots.intelligentnursing.business.TencentLoginManager;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.view.activity.ILoginView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * LoginPresenter只负责将token转换成UserInfo
 * 获取token由各个LoginManager以及RegisterPresenter完成
 * 获取到token后由Event通知LoginPresenter获取UserInfo完成登录
 *
 * @author zhqy
 * @date 2018/5/9
 */

public class LoginPresenter extends BaseActivityPresenter<ILoginView> {

    private static final String HINT_ON_INPUT_ERROR = "用户名/密码为空";
    private static final String HINT_ON_GIVE_UP_LOGIN = "您放弃了登录";

    private OriginalLoginManager mOriginalLoginManager;
    private TencentLoginManager mTencentLoginManager;
    private SinaLoginManager mSinaLoginManager;

    public LoginPresenter(ILoginView view) {
        super(view);
        EventBus.getDefault().register(this);
        BaseLoginManager.OnFailureListener listener = this::onException;
        mOriginalLoginManager = new OriginalLoginManager(getActivity(), listener);
        mTencentLoginManager = new TencentLoginManager(getActivity(), listener);
        mSinaLoginManager = new SinaLoginManager(getActivity(), listener);
    }

    public void onTplImageViewClicked(int tag) {
        onLogging();
        switch (tag) {
            case LoginActivity.CODE_TPL_QQ:
                mTencentLoginManager.login();
                break;
            case LoginActivity.CODE_TPL_SINA:
                mSinaLoginManager.login();
            default:
        }
    }

    public void onLoginButtonClicked(String username, String password) {
        if ("".equals(username) || "".equals(password)) {
            onException(HINT_ON_INPUT_ERROR);
        } else {
            onLogging();
            mOriginalLoginManager.login(username, password);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencentLoginManager.handleActivityResult(requestCode, resultCode, data);
        mSinaLoginManager.handleActivityResult(requestCode, resultCode, data);
    }

    public void onDestroy() {
        mSinaLoginManager.recycler();
        mTencentLoginManager.recycler();
        mOriginalLoginManager.recycler();
        EventBus.getDefault().unregister(this);
    }

    private void onLoginSuccess(String username) {
        if (getView() != null) {
            getView().onLoginSuccess(username);
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }

    private void onLogging() {
        if (getView() != null) {
            getView().onLogging();
        }
    }

    private void createListWhileFencesNull(UserInfo userInfo) {
        if (userInfo.getLocationDataList() == null) {
            userInfo.setLocationDataList(new ArrayList<>());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(DataEvent event) {
        String action = event.getAction();
        if (EventPoster.ACTION_TO_GET_USER_INFO.equals(action)) {
            onLogging();
            RetrofitHelper.getInstance().user().getUserInfo(UserContainer.getUser().getToken())
                    .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .doOnNext(ServerResponse::checkSuccess)
                    .map(ServerResponse::getData)
                    .doOnNext(this::createListWhileFencesNull)
                    .doOnNext(userInfo -> UserContainer.getUser().setUserInfo(userInfo))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userInfo -> onLoginSuccess(userInfo.getUsername()),
                            throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                    );
        } else if (EventPoster.ACTION_ON_GIVE_UP_COMPLETING_INFO.equals(action)) {
            onException(HINT_ON_GIVE_UP_LOGIN);
        }
    }

    public static void notifyGetUserInfo() {
        EventPoster.post(new DataEvent(EventPoster.ACTION_TO_GET_USER_INFO));
    }

}
