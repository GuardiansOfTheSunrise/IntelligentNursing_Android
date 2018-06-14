package com.gots.intelligentnursing.presenter.activity;

import com.gots.intelligentnursing.business.FileCacheManager;
import com.gots.intelligentnursing.business.IServerConnection;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.SystemChecker;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.exception.ServerException;
import com.gots.intelligentnursing.view.activity.IMainView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/4/3
 */

public class MainPresenter extends BaseActivityPresenter<IMainView> {

    public MainPresenter(IMainView view) {
        super(view);
    }

    public void onActivityCreate() {
        attemptToLoginFromCache();
        systemCheck();
    }

    private void systemCheck() {
        new Thread(new SystemChecker()).start();
    }

    private void attemptToLoginFromCache() {
        FileCacheManager fileCacheManager = FileCacheManager.getInstance(getActivity());
        Map<String, String> map = fileCacheManager.readUsernameAndPassword();
        if (map != null) {
            String username = map.get(FileCacheManager.KEY_USERNAME);
            String password = map.get(FileCacheManager.KEY_PASSWORD);

            IServerConnection.IUserOperate userOperate = RetrofitHelper.getInstance().user();

            userOperate.login(username, password)
                    .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnNext(ServerResponse::checkSuccess)
                    .map(ServerResponse::getData)
                    .doOnNext(token -> UserContainer.getUser().setToken(token))
                    .map(token -> UserContainer.getUser().getToken())
                    .flatMap(userOperate::getUserInfo)
                    .doOnNext(ServerResponse::checkSuccess)
                    .map(ServerResponse::getData)
                    .doOnNext(this::createListWhileFencesNull)
                    .doOnNext(userInfo -> UserContainer.getUser().setUserInfo(userInfo))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userInfo -> onLoginSuccess(userInfo.getUsername()),
                            throwable -> onException(throwable, ServerRequestExceptionHandler.handle(throwable))
                    );

        }
    }

    private void createListWhileFencesNull(UserInfo userInfo) {
        if (userInfo.getLocationDataList() == null) {
            userInfo.setLocationDataList(new ArrayList<>());
        }
    }

    private void onLoginSuccess(String username) {
        if (getView() != null) {
            getView().onLoginSuccess(username);
        }
    }

    private void onException(Throwable throwable, String msg) {
        if (throwable instanceof ServerException) {
            FileCacheManager.getInstance(getActivity()).clearUsernameAndPassword();
        }
        if (getView() != null) {
            getView().onException(msg);
        }
    }
}
