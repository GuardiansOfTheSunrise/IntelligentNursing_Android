package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
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

    public OriginalLoginManager(RxAppCompatActivity activity, OnLoginReturnListener onLoginReturnListener) {
        super(onLoginReturnListener);
        mActivity = activity;
    }

    @Override
    public void recycler() {
        mActivity = null;
    }

    public void login(String username, String password) {
        IServerConnection.IUserOperate userOperate = RetrofitHelper.getInstance().user();

        userOperate.login(username, password)
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(ServerResponse::checkSuccess)
                .map(ServerResponse::getData)
                .doOnNext(token -> {
                    UserContainer.getUser().setToken(token);
                    FileCacheManager.getInstance(mActivity).saveUsernameAndPassword(username, password);
                })
                .map(token -> UserContainer.getUser().getToken())
                .flatMap(userOperate::getUserInfo)
                .doOnNext(ServerResponse::checkSuccess)
                .map(ServerResponse::getData)
                .doOnNext(this::createListWhileFencesNull)
                .doOnNext(userInfo -> UserContainer.getUser().setUserInfo(userInfo))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userInfo -> mOnLoginReturnListener.onSuccess(CHANNEL_ORIGINAL, userInfo),
                        throwable -> mOnLoginReturnListener.onFailure(ServerRequestExceptionHandler.handle(throwable))
                );
    }

    private void createListWhileFencesNull(UserInfo userInfo) {
        if (userInfo.getLocationDataList() == null) {
            userInfo.setLocationDataList(new ArrayList<>());
        }
    }
}
