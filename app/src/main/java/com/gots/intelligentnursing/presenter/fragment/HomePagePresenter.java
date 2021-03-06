package com.gots.intelligentnursing.presenter.fragment;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.entity.NewsInfo;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.view.fragment.IHomePageView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class HomePagePresenter extends BaseFragmentPresenter<IHomePageView> {

    private static final int ELDER_STATE_NORMAL = 0;
    private static final int ELDER_STATE_ABNORMAL = 1;
    public static int elderState = ELDER_STATE_NORMAL;

    private static final String TAG = "HomePagePresenter";

    public HomePagePresenter(IHomePageView view) {
        super(view);
    }

    public void getNewsData() {
        RetrofitHelper.getInstance().system()
                .getNewsData()
                .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .doOnNext(ServerResponse::checkSuccess)
                .map(ServerResponse::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetNewsSuccess,
                        throwable -> onException(ServerRequestExceptionHandler.handle(throwable)));
    }

    public void getPictureData() {
        List<Integer> pictureResList = new ArrayList<>();
        pictureResList.add(R.drawable.bg_home_page_convenient_banner);
        pictureResList.add(R.drawable.bg_home_page_convenient_banner1);
        onGetPictureSuccess(pictureResList);
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }

    private void onGetNewsSuccess(List<NewsInfo> newsInfoList) {
        if (getView() != null) {
            getView().onGetNewsSuccess(newsInfoList);
        }
    }

    private void onGetPictureSuccess(List<Integer> pictureResList) {
        if (getView() != null) {
            getView().onGetPictureSuccess(pictureResList);
        }
    }

    public void onMyNursingButtonClicked() {
    }
}
