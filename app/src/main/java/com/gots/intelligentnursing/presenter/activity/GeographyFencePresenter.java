package com.gots.intelligentnursing.presenter.activity;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.gots.intelligentnursing.business.RetrofitHelper;
import com.gots.intelligentnursing.business.ServerRequestExceptionHandler;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.FenceInfo;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.entity.User;
import com.gots.intelligentnursing.view.activity.IGeographyFenceView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author zhqy
 * @date 2018/4/26
 */

public class GeographyFencePresenter extends BaseActivityPresenter<IGeographyFenceView> {

    public GeographyFencePresenter(IGeographyFenceView view) {
        super(view);
    }

    /**
     * 计算用户围栏多边形的重心经纬度
     * @return 重心经纬度
     */
    public LatLng getCenterOfFence() {
        List<LocationData> fenceLocationDataList = UserContainer.getUser().getUserInfo().getFenceInfo().getFencePointDataList();
        if (fenceLocationDataList.size() == 0) {
            return null;
        }
        LocationData vertex = fenceLocationDataList.get(0);
        double sumOfLatitude = 0;
        double sumOfLongitude = 0;
        double sumOfArea = 0;
        for (int i = 0;i < fenceLocationDataList.size() - 2;i++) {
            LocationData pointSecond = fenceLocationDataList.get(i + 1);
            LocationData pointThird = fenceLocationDataList.get(i + 2);
            double area = ((pointSecond.getLatitude() - vertex.getLatitude()) *
                    (pointThird.getLongitude() - vertex.getLongitude()) -
                    (pointThird.getLatitude() - vertex.getLatitude()) *
                            (pointSecond.getLongitude() - vertex.getLongitude())) / 2.0;
            sumOfLatitude += (vertex.getLatitude() + pointSecond.getLatitude() + pointThird.getLatitude()) * area;
            sumOfLongitude += (vertex.getLongitude() + pointSecond.getLongitude() + pointThird.getLongitude()) * area;
            sumOfArea += area;
        }
        return new LatLng(sumOfLatitude / sumOfArea / 3.0, sumOfLongitude / sumOfArea / 3.0);
    }

    public void onFenceDrawSuccess(List<LocationData> fenceLocationDataList) {
        User user = UserContainer.getUser();
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>(2);
        map.put("uid", user.getUserInfo().getId());
        map.put("points", fenceLocationDataList);
        String json = gson.toJson(map);
        RetrofitHelper.getInstance().user()
                .fenceDrawing(user.getToken(), RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json))
                .compose(getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(ServerResponse::checkCode)
                .subscribe(
                        r -> onSubmitFenceSuccess(),
                        throwable -> onException(ServerRequestExceptionHandler.handle(throwable))
                );
    }

    private void onSubmitFenceSuccess() {
        if (getView() != null) {
            getView().onFenceDrawingSuccess();
        }
    }

    private void onException(String msg) {
        if (getView() != null) {
            getView().onException(msg);
        }
    }
}
