package com.gots.intelligentnursing.presenter.activity;

import com.gots.intelligentnursing.business.IServerConnection;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.view.activity.ILoginView;
import com.trello.rxlifecycle2.android.ActivityEvent;

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
                .doOnNext(token -> UserContainer.getUser().setToken(token))
                .map(token -> UserContainer.getUser().getToken())
                .flatMap(userOperate::getUserInfo)
                .doOnNext(ServerResponse::checkCode)
                .map(ServerResponse::getData)
                .doOnNext(userInfo -> UserContainer.getUser().setUserInfo(userInfo))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userInfo -> onLoginSuccess(),
                        throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                );

    }

    private void onLoginSuccess() {
        if (getView() != null) {
            getView().onLoginSuccess();
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }
}
