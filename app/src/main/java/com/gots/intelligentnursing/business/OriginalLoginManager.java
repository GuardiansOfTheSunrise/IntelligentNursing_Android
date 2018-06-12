package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.presenter.activity.LoginPresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/5/25
 */

public class OriginalLoginManager extends BaseLoginManager {

    private RxAppCompatActivity mActivity;

    public OriginalLoginManager(RxAppCompatActivity activity, OnFailureListener onLoginReturnListener) {
        super(onLoginReturnListener);
        mActivity = activity;
    }

    @Override
    public void recycler() {
        mActivity = null;
    }

    public void login(String username, String password) {
        RetrofitHelper.getInstance().user()
                .login(username, password)
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(ServerResponse::checkSuccess)
                .map(ServerResponse::getData)
                .doOnNext(token -> {
                    UserContainer.getUser().setToken(token);
                    FileCacheManager.getInstance(mActivity).saveUsernameAndPassword(username, password);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        token -> LoginPresenter.notifyGetUserInfo(),
                        throwable -> mOnLoginReturnListener.onFailure(ServerRequestExceptionHandler.handle(throwable))
                );
    }

}
