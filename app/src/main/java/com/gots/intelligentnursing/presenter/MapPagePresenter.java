package com.gots.intelligentnursing.presenter;

import android.Manifest;

import com.gots.intelligentnursing.view.IMapPageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author zhqy
 * @date 2018/4/19
 */

public class MapPagePresenter extends BasePresenter<IMapPageView> {

    private static final String HINT_DENY_GRANT = "您拒绝了授权，该功能无法正常使用";

    public MapPagePresenter(IMapPageView view) {
        super(view);
    }

    public void initData() {
        new RxPermissions(getActivity()).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(granted -> granted)
                .switchIfEmpty(observer -> getView().onException(HINT_DENY_GRANT))
                .subscribe(granted -> getLocationData());
    }

    private void getLocationData() {

    }
}
