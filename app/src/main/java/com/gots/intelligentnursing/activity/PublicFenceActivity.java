package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.customview.GeofenceDrawMapView;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.presenter.activity.PublicFencePresenter;
import com.gots.intelligentnursing.view.activity.IPublicFenceView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhqy
 * @date 2018/6/20
 */

public class PublicFenceActivity extends BaseActivity<PublicFencePresenter> implements IPublicFenceView {

    private static final int STATE_NORMAL = 0;
    private static final int STATE_DRAWING = 1;
    private static final int STATE_WAIT_RESULT = 2;

    private static final String HINT_ON_CONVERTING = "正在生成围栏，请稍候...";
    private static final String HINT_ON_DRAW_SUCCESS = "提交成功，核查通过之后就能生效了，感谢您的贡献";
    private static final String HINT_ON_DRAW_FAILURE = "生成围栏失败，请尝试重新绘制";
    private static final String HINT_ON_CANCEL_BUTTON_CLICKED = "您确定要放弃绘制围栏吗？";
    private static final String HINT_ON_ENTER = "当您发现危险区域时，可以在此绘制该区域并提交给我们，审核通过后" +
            "该围栏将作为所有用户的公共危险区域并预警，感谢您对其他用户做出的贡献！";
    private static final String HINT_ON_DIALOG_EDIT_TEXT = "您可以描述一下该区域的情况，以便我们核实";
    private static final String DIALOG_TITLE = "描述一下围栏吧";
    private static final String TOOLBAR_TITLE = "公共围栏";

    private int mDrawingState = STATE_NORMAL;

    private GeofenceDrawMapView mMapView;
    private AlertDialog mDescribeDialog;
    private EditText mDialogEditText;

    private String mFenceDescribe;

    private void showHintDialog() {
        TextView textView = new TextView(this);
        textView.setText(HINT_ON_ENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        new AlertDialog.Builder(this)
                .setView(textView)
                .setPositiveButton("确定", null)
                .show();
    }

    private void initDescribeDialog() {
        mDialogEditText = new EditText(this);
        mDialogEditText.setHint(HINT_ON_DIALOG_EDIT_TEXT);
        mDialogEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        mDescribeDialog = new AlertDialog.Builder(this)
                .setTitle(DIALOG_TITLE)
                .setView(mDialogEditText)
                .setPositiveButton("确定", (dialogInterface, i) -> onDialogButtonClick())
                .setNegativeButton("取消",null)
                .setCancelable(false)
                .create();
    }

    private void onDialogButtonClick() {
        if ("".equals(mDialogEditText.getText().toString())) {
            Toast.makeText(this, DIALOG_TITLE, Toast.LENGTH_SHORT).show();
        } else if (mMapView.completeDrawing()) {
            mFenceDescribe = mDialogEditText.getText().toString();
            mDrawingState = STATE_WAIT_RESULT;
            invalidateOptionsMenu();
        }
    }

    private void initMapView() {
        mMapView = findViewById(R.id.geofence_draw_map_view_public_fence);
        mMapView.setLocationDataList(new ArrayList<>());
        mMapView.setDrawResultListener(new GeofenceDrawMapView.DrawResultListener() {
            @Override
            public void onStart() {
                showProgressBar();
            }

            @Override
            public void onSuccess(List<LocationData> locationDataList) {
                mPresenter.onFenceDrawSuccess(locationDataList, mFenceDescribe);
                mDrawingState = STATE_NORMAL;
                invalidateOptionsMenu();
            }

            @Override
            public void onFailure() {
                Toast.makeText(PublicFenceActivity.this, HINT_ON_DRAW_FAILURE, Toast.LENGTH_SHORT).show();
                dismissProgressBar();
                mDrawingState = STATE_NORMAL;
                invalidateOptionsMenu();
                mFenceDescribe = null;
            }
        });
        BaiduMap baiduMap = mMapView.getMap();

        LatLng latLng = mPresenter.getCurrentLocation();
        if (latLng != null) {
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
        }
    }

    @Override
    public void onFenceDrawingSuccess() {
        dismissProgressBar();
        mMapView.giveUpUpdateFenceData();
        mFenceDescribe = null;
        Toast.makeText(this, HINT_ON_DRAW_SUCCESS, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onException(String msg) {
        dismissProgressBar();
        mMapView.giveUpUpdateFenceData();
        mFenceDescribe = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_public_fence);
        setProgressBarHint(HINT_ON_CONVERTING);
        setToolbarTitle(TOOLBAR_TITLE);
        initDescribeDialog();
        initMapView();
        showHintDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_geography_fence_normal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (mDrawingState == STATE_DRAWING) {
            getMenuInflater().inflate(R.menu.menu_geography_fence_on_drawing, menu);
        } else if (mDrawingState == STATE_NORMAL) {
            getMenuInflater().inflate(R.menu.menu_geography_fence_normal, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_geography_fence_draw:
                mDrawingState = STATE_DRAWING;
                invalidateOptionsMenu();
                mMapView.startDrawing();
                return true;
            case R.id.menu_geography_fence_undo:
                mMapView.undoDrawing();
                return true;
            case R.id.menu_geography_fence_complete:
                mDescribeDialog.show();
                return true;
            case R.id.menu_geography_fence_cancel:
                Snackbar.make(mMapView, HINT_ON_CANCEL_BUTTON_CLICKED, Snackbar.LENGTH_LONG)
                        .setAction("确定", view -> {
                            mMapView.cancelDrawing();
                            mDrawingState = STATE_NORMAL;
                            invalidateOptionsMenu();
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected boolean isDisplayProgressBar() {
        return true;
    }


    @Override
    protected PublicFencePresenter createPresenter() {
        return new PublicFencePresenter(this);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PublicFenceActivity.class);
        context.startActivity(intent);
    }
}
