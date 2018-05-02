package com.gots.intelligentnursing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.business.UserContainer;
import com.gots.intelligentnursing.customview.GeofenceDrawMapView;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.presenter.GeographyFencePresenter;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.view.IGeographyFenceView;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/26
 */

public class GeographyFenceActivity extends BaseActivity<GeographyFencePresenter> implements IGeographyFenceView {

    private static final String HINT_ON_COMPLETE_BUTTON_CLICKED = "您确定保存当前围栏设置吗？";
    private static final String HINT_ON_CANCEL_BUTTON_CLICKED = "您确定要放弃绘制围栏吗？";

    private static final String TOOLBAR_TITLE = "围栏设置";

    private boolean mDrawingState = false;

    private GeofenceDrawMapView mMapView;
    private BaiduMap mBaiduMap;

    private void initMapView() {
        mMapView = findViewById(R.id.geofence_draw_map_view_geography_fence);
        mMapView.setLocationDataList(UserContainer.getUser().getFencePointDataList());
        mMapView.setDrawResultListener(new GeofenceDrawMapView.DrawResultListener() {
            @Override
            public void onSuccess(List<LocationData> locationDataList) {
                LogUtil.i("GeographyFenceActivity", Thread.currentThread().getName());
                for (LocationData data : locationDataList) {
                    LogUtil.i("GeographyFenceActivity", "lat:" + data.getLatitude() + " lng:" + data.getLongitude());
                }
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.i("GeographyFenceActivity", "onFailure");
            }
        });
        mBaiduMap = mMapView.getMap();
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
        if (mDrawingState) {
            getMenuInflater().inflate(R.menu.menu_geography_fence_on_drawing, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_geography_fence_normal, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_geography_fence_draw:
                mDrawingState = true;
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
                                mDrawingState = false;
                                invalidateOptionsMenu();
                            }
                        })
                        .show();
                return true;
            case R.id.menu_geography_fence_cancel:
                Snackbar.make(mMapView, HINT_ON_CANCEL_BUTTON_CLICKED, Snackbar.LENGTH_LONG)
                        .setAction("确定", view -> {
                            mMapView.cancelDrawing();
                            mDrawingState = false;
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

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GeographyFenceActivity.class);
        context.startActivity(intent);
    }
}
