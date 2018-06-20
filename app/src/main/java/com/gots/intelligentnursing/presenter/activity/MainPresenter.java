package com.gots.intelligentnursing.presenter.activity;

import android.Manifest;
import android.content.Intent;

import com.gots.intelligentnursing.activity.WebActivity;
import com.gots.intelligentnursing.business.FileCacheManager;
import com.gots.intelligentnursing.business.IServerConnection;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.SystemChecker;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.exception.ServerException;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.activity.IMainView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.message.PushAgent;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * @author zhqy
 * @date 2018/4/3
 */

public class MainPresenter extends BaseActivityPresenter<IMainView> {

    private static final int REQUEST_CAPTURE = 0;

    private static final String HINT_DENY_GRANT = "您拒绝了授权，该功能无法正常使用";

    public MainPresenter(IMainView view) {
        super(view);
    }

    public void onActivityCreate() {
        attemptToLoginFromCache();
        systemCheck();
    }

    public void onScanQrcodeMenuClick() {
        new RxPermissions(getActivity())
                .request(Manifest.permission.CAMERA, Manifest.permission.INTERNET)
                .filter(granted -> granted)
                .switchIfEmpty(observer -> onException(HINT_DENY_GRANT))
                .subscribe(granted -> getActivity().startActivityForResult(
                        new Intent(getActivity(), CaptureActivity.class), REQUEST_CAPTURE));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAPTURE) {
            String result = data.getExtras().getString("result");
            WebActivity.actionStart(getActivity(), result);
        }
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
                    .doOnNext(this::createListWhileNull)
                    .doOnNext(userInfo -> UserContainer.getUser().setUserInfo(userInfo))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userInfo -> onLoginSuccess(userInfo.getUsername()),
                            throwable -> onException(throwable, ServerRequestExceptionHandler.handle(throwable))
                    );

        }
    }

    private void createListWhileNull(UserInfo userInfo) {
        if (userInfo.getLocationDataList() == null) {
            userInfo.setLocationDataList(new ArrayList<>());
        }
        userInfo.setNotificationDataList(new ArrayList<>());
    }

    private void onLoginSuccess(String username) {
        PushAgent.getInstance(getActivity().getApplicationContext())
                .addAlias(String.valueOf(UserContainer.getUser().getUserInfo().getId()),"ALIAS_TYPE_CUSTOMIZED",
                        (b, s) -> LogUtil.i("MainPresenter", "addAlias"));
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

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }
}
