package com.gots.intelligentnursing.presenter.activity;

import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.RetryWhenAuthorizationExceptionFunction;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.User;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.view.activity.IElderInfoView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/6/19
 */

public class ElderInfoPresenter extends BaseActivityPresenter<IElderInfoView> {

    public ElderInfoPresenter(IElderInfoView view) {
        super(view);
    }

    public void onSaveButtonClick(String ageString, String height, String weight, String address, String phone, String remarks) {
        final int fAge = "".equals(ageString) ? 0 : Integer.parseInt(ageString);
        final String fHeight = "".equals(height) ? null : height;
        final String fWeight = "".equals(weight) ? null : weight;
        final String fAddress = "".equals(address) ? null : address;
        final String fPhone = "".equals(phone) ? null : phone;
        final String fRemarks = "".equals(remarks) ? null : remarks;

        User user = UserContainer.getUser();
        Flowable.just(0)
                .flatMap(i -> RetrofitHelper.getInstance().user().elderInfoComplete(
                        user.getToken(), fAge, fHeight, fWeight, fAddress, fPhone, fRemarks, user.getUserInfo().getId()))
                .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .doOnNext(ServerResponse::checkAuthorization)
                .retryWhen(new RetryWhenAuthorizationExceptionFunction())
                .doOnNext(ServerResponse::checkSuccess)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        r -> onSaveSuccess(fAge, fHeight, fWeight, fAddress, fPhone, fRemarks),
                        throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                );
    }

    private void onSaveSuccess(int age, String height, String weight, String address, String phone, String remarks) {
        UserInfo userInfo = UserContainer.getUser().getUserInfo();
        userInfo.setAge(age);
        userInfo.setHeight(height);
        userInfo.setWeight(weight);
        userInfo.setAddress(address);
        userInfo.setTelephone(phone);
        userInfo.setRemarks(remarks);
        if (getView() != null) {
            getView().onSaveSuccess();
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }
}
