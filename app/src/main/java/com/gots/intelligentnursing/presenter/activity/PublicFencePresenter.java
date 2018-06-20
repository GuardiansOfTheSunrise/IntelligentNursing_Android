package com.gots.intelligentnursing.presenter.activity;

import com.baidu.mapapi.model.LatLng;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.view.activity.IPublicFenceView;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/6/20
 */

public class PublicFencePresenter extends BaseActivityPresenter<IPublicFenceView> {

    public LatLng getCurrentLocation() {
        LocationData location = UserContainer.getUser().getLocation();
        if (location != null) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        return null;
    }

    public void onFenceDrawSuccess(List<LocationData> fenceLocationDataList, String describe) {
        // TODO: 2018/6/20 上传服务器
        onSubmitFenceSuccess();
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

    public PublicFencePresenter(IPublicFenceView view) {
        super(view);
    }
}
