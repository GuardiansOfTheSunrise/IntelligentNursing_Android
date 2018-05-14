package com.gots.intelligentnursing.presenter.activity;

import com.gots.intelligentnursing.business.FileCacheManager;
import com.gots.intelligentnursing.business.IServerConnection;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.FenceInfo;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.view.activity.ILoginView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/5/9
 */

public class LoginPresenter extends BaseActivityPresenter<ILoginView> {

    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void onLoginButtonClicked(String username, String password) {
        IServerConnection.IUserOperate userOperate = RetrofitHelper.getInstance().user();

        userOperate.login(username, password)
                .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(ServerResponse::checkCode)
                .map(ServerResponse::getData)
                .doOnNext(token -> {
                    UserContainer.getUser().setToken(token);
                    FileCacheManager.getInstance(getActivity()).saveUsernameAndPassword(username, password);
                })
                .map(token -> UserContainer.getUser().getToken())
                .flatMap(userOperate::getUserInfo)
                .doOnNext(ServerResponse::checkCode)
                .map(ServerResponse::getData)
                // TODO: 2018/5/11 根据服务器数据格式决定
                .doOnNext(this::createListWhileFencesNull)
                .doOnNext(userInfo -> UserContainer.getUser().setUserInfo(userInfo))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userInfo -> onLoginSuccess(userInfo.getUsername()),
                        throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                );

    }

    private void createListWhileFencesNull(UserInfo userInfo) {
        if (userInfo.getFenceInfo() == null) {
            FenceInfo fenceInfo = new FenceInfo();
            fenceInfo.setFencePointDataList(new ArrayList<>());
            userInfo.setFenceInfo(fenceInfo);
        }
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
}
