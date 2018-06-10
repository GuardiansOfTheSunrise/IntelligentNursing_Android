package com.gots.intelligentnursing.presenter.activity;

import android.os.Handler;
import android.os.Message;

import com.gots.intelligentnursing.business.EventPoster;
import com.gots.intelligentnursing.business.IServerConnection;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.VerificationCodeTimer;
import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.view.activity.IRegisterView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/6/9
 */

public class RegisterPresenter extends BaseActivityPresenter<IRegisterView> {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONFIRM = "confirm";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_VERIFY = "verify";

    private static final String HINT_ON_INFO_LACK = "请将信息补全完整";
    private static final String HINT_ON_PASSWORD_NOT_EQUAL_TO_CONFIRM = "两次密码不一致";

    private Handler mTimeHandler = new TimerHandler(this);

    public RegisterPresenter(IRegisterView view) {
        super(view);
    }

    public void initVerifyTimer() {
        if (VerificationCodeTimer.getInstance().secondLeft() != 0) {
            updateVerifyButtonTime(VerificationCodeTimer.getInstance().secondLeft());
            Message message = Message.obtain();
            mTimeHandler.sendMessageDelayed(message, 1000);
        }
    }

    public void recycler() {
        mTimeHandler.removeCallbacksAndMessages(null);
    }

    public void register(Map<String, String> input) {
        String username = input.get(KEY_USERNAME);
        String password = input.get(KEY_PASSWORD);
        String confirm = input.get(KEY_CONFIRM);
        String phone = input.get(KEY_PHONE);
        String verify = input.get(KEY_VERIFY);
        if ("".equals(username) || "".equals(password) || "".equals(confirm) ||
                "".equals(phone) || "".equals(verify)) {
            onException(HINT_ON_INFO_LACK);
        } else if (!password.equals(confirm)) {
            onException(HINT_ON_PASSWORD_NOT_EQUAL_TO_CONFIRM);
        } else {
            IServerConnection.IUserOperate userOperate = RetrofitHelper.getInstance().user();
            userOperate.register(username, password, phone, verify)
                    .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnNext(ServerResponse::checkSuccess)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            resp -> onRegisterSuccess(username, password),
                            throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                    );
        }
    }

    public void getVerify(String username, String phone) {
        if ("".equals(username) || "".equals(phone)) {
            onException(HINT_ON_INFO_LACK);
        } else {
            IServerConnection.IUserOperate userOperate = RetrofitHelper.getInstance().user();
            userOperate.getVerify(username, phone)
                    .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnNext(ServerResponse::checkSuccess)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            resp -> onGettingVerifySuccess(),
                            throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                    );
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }

    private void onGettingVerifySuccess() {
        VerificationCodeTimer.getInstance().updateLastGettingTime();
        updateVerifyButtonTime(60);
        Message message = Message.obtain();
        mTimeHandler.sendMessageDelayed(message, 1000);
        if (getView() != null) {
            getView().onGettingVerifySuccess();
        }
    }

    private void onRegisterSuccess(String username, String password) {
        Map<String, String> uap = new HashMap<>(2);
        uap.put("username", username);
        uap.put("password", password);
        EventPoster.post(new DataEvent(EventPoster.ACTION_ON_REGISTER_SUCCESS, uap));
        if (getView() != null) {
            getView().onRegisterSuccess();
        }
    }

    private void updateVerifyButtonTime(int second) {
        if (getView() != null) {
            getView().updateVerifyButtonTime(second);
        }
    }

    private static class TimerHandler extends Handler {

        private WeakReference<RegisterPresenter> mReference;

        private TimerHandler(RegisterPresenter presenter) {
            mReference = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mReference.get() != null) {
                mReference.get().updateVerifyButtonTime(VerificationCodeTimer.getInstance().secondLeft());
                Message message = Message.obtain();
                mReference.get().mTimeHandler.sendMessageDelayed(message, 1000);
            }
        }
    }
}
