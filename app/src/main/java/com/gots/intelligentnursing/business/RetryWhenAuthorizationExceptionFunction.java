package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.DataEvent;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.UserInfo;
import com.gots.intelligentnursing.exception.AuthorizationException;
import com.gots.intelligentnursing.exception.ServerException;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/5/15
 */

public class RetryWhenAuthorizationExceptionFunction implements Function<Flowable<Throwable>, Publisher<?>> {

    @Override
    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) throws Exception {
        return throwableFlowable.flatMap((Function<Throwable, Publisher<?>>) throwable -> {
            if (throwable instanceof AuthorizationException) {
                UserInfo userInfo = UserContainer.getUser().getUserInfo();
                return RetrofitHelper.getInstance().user()
                        .login(userInfo.getUsername(), userInfo.getPassword())
                        .subscribeOn(Schedulers.io())
                        .doOnNext(ServerResponse::checkSuccess)
                        .doOnError(t -> {
                            if (t instanceof ServerException) {
                                FileCacheManager.getInstance(null).clearUsernameAndPassword();
                                UserContainer.getUser().init();
                                EventPoster.post(new DataEvent(EventPoster.ACTION_AUTO_LOGIN_FAILURE));
                            }
                        })
                        .map(ServerResponse::getData)
                        .doOnNext(token -> UserContainer.getUser().setToken(token));
            } else {
                return Flowable.error(throwable);
            }
        });
    }

}
