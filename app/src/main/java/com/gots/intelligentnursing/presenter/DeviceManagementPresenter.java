package com.gots.intelligentnursing.presenter;

import android.content.Intent;

import com.gots.intelligentnursing.business.QrCodeResultParser;
import com.gots.intelligentnursing.exception.ParseException;
import com.gots.intelligentnursing.view.IDeviceManagementView;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import static android.app.Activity.RESULT_OK;

/**
 * @author zhqy
 * @date 2018/4/1
 */

public class DeviceManagementPresenter extends BasePresenter<IDeviceManagementView> {

    private static final int REQUEST_CAPTURE = 0;

    public DeviceManagementPresenter(IDeviceManagementView view) {
        super(view);
    }

    public void onAddButtonClicked(){
        Intent intent = new Intent(getView().getActivity(), CaptureActivity.class);
        getView().getActivity().startActivityForResult(intent,REQUEST_CAPTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case REQUEST_CAPTURE:
                    String result = data.getExtras().getString("result");
                    try {
                        String id = QrCodeResultParser.parse(result);
                        getView().onQeCodeParseSuccess(id);
                    } catch (ParseException e) {
                        getView().onQrCodeParseError(e.getMessage());
                    }
                    break;
                default:
            }
        }
    }
}
