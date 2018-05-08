package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.customview.GeofenceDrawMapView;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.presenter.GeographyFencePresenter;
import com.gots.intelligentnursing.view.IGeographyFenceView;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/26
 */

public class GeographyFenceActivity extends BaseActivity<GeographyFencePresenter> implements IGeographyFenceView {

    private static final int STATE_NORMAL = 0;
    private static final int STATE_DRAWING = 1;
    private static final int STATE_WAIT_RESULT = 2;

    private static final String HINT_ON_CONVERTING = "正在生成围栏，请稍候...";
    private static final String HINT_ON_DRAW_SUCCESS = "设置成功";
    private static final String HINT_ON_DRAW_FAILURE = "生成围栏失败，请尝试重新绘制";
    private static final String HINT_ON_COMPLETE_BUTTON_CLICKED = "您确定保存当前围栏设置吗？";
    private static final String HINT_ON_CANCEL_BUTTON_CLICKED = "您确定要放弃绘制围栏吗？";

    private static final String TOOLBAR_TITLE = "围栏设置";

    private int mDrawingState = STATE_NORMAL;

    private GeofenceDrawMapView mMapView;
    private BaiduMap mBaiduMap;

    private void initMapView() {
        mMapView = findViewById(R.id.geofence_draw_map_view_geography_fence);
        mMapView.setLocationDataList(UserContainer.getUser().getFencePointDataList());
        mMapView.setDrawResultListener(new GeofenceDrawMapView.DrawResultListener() {
            @Override
            public void onStart() {
                showProgressBar();
            }

            @Override
            public void onSuccess(List<LocationData> locationDataList) {
                mPresenter.onFenceDrawSuccess(locationDataList);
                dismissProgressBar();
                mDrawingState = STATE_NORMAL;
                invalidateOptionsMenu();
            }

            @Override
            public void onFailure() {
                Toast.makeText(GeographyFenceActivity.this, HINT_ON_DRAW_FAILURE, Toast.LENGTH_SHORT).show();
                showProgressBar();
                mDrawingState = STATE_NORMAL;
                invalidateOptionsMenu();
            }
        });
        mBaiduMap = mMapView.getMap();

        // 将地图移动至围栏区域的中心
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(mPresenter.getCenterOfFence());
        mBaiduMap.animateMapStatus(update);
    }

    @Override
    public void onFenceDrawingSuccess() {
        mMapView.confirmUpdateFenceData();
        Toast.makeText(GeographyFenceActivity.this, HINT_ON_DRAW_SUCCESS, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onException(String msg) {
        mMapView.giveUpUpdateFenceData();
        Toast.makeText(GeographyFenceActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_geography_fence);
        setToolbarTitle(TOOLBAR_TITLE);
        initMapView();

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
                Snackbar.make(mMapView, HINT_ON_COMPLETE_BUTTON_CLICKED, Snackbar.LENGTH_LONG)
                        .setAction("确定", view -> {
                            if (mMapView.completeDrawing()) {
                                mDrawingState = STATE_WAIT_RESULT;
                                invalidateOptionsMenu();
                            }
                        })
                        .show();
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
    protected GeographyFencePresenter createPresenter() {
        return new GeographyFencePresenter(this);
    }

    @Override
    protected String getProgressBarHintText() {
        return HINT_ON_CONVERTING;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GeographyFenceActivity.class);
        context.startActivity(intent);
    }
}
