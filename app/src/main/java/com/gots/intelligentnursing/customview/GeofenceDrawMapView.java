package com.gots.intelligentnursing.customview;

import android.content.Context;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.gots.intelligentnursing.entity.LocationData;
import com.gots.intelligentnursing.tools.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/26
 */

public class GeofenceDrawMapView extends RelativeLayout {

    private static final String TAG = "GeofenceDrawMapView";

    private static final String HINT_ON_CONVERT_FAILURE = "生成围栏失败，请尝试重新绘制";
    private static final String HINT_ON_POINT_LESS = "至少需要绘制两条路径哦";

    private MapView mMapView;
    private CanvasView mCanvasView;

    /**
     * 绘制状态
     * 为true时表示正在绘制
     * 为false时表示尚未开始绘制
     */
    private boolean mDrawingState = false;

    /**
     * 上一次Down事件的坐标
     * 用于与Up事件坐标比较
     * Down和Up坐标相同时表明未绘制路径
     */
    private PointF mLastDownPoint = null;

    /**
     * Map点击回调中用于标识是否第一个点
     * 如果是第一个点则插入到列表起始位置
     */
    private boolean mIsFirstPoint = false;

    /**
     * 围栏点经纬度数据列表
     */
    private List<LocationData> mDrawingLocationDataList = new ArrayList<>();

    private List<LocationData> mValidLocationDataList;

    /**
     * 第一个点的坐标
     * 最后再转换第一个点的经纬度
     */
    private PointF mFirstCoordinatePoint = null;

    /**
     * 围栏点的数量
     */
    private int mPointCount = 0;

    private DrawResultListener mDrawResultListener;

    /**
     * 将位置信息添加到列表
     * 如果是转换起点且长度正确
     * 则在地图上绘制区域并回调
     * @param latLng 位置信息
     */
    private void getLocationOnMapClicked(LatLng latLng) {
        // 转换第一个点
        // 当执行完成绘制后，才开始转换第一个点
        if (mIsFirstPoint) {
            mIsFirstPoint = false;
            mDrawingLocationDataList.add(0, new LocationData(latLng.latitude, latLng.longitude));
            if (mDrawingLocationDataList.size() == mPointCount) {
                LogUtil.i(TAG, "Covert success.");
                LogUtil.i(TAG, "The size of location list is " + mDrawingLocationDataList.size() + ".");
                LogUtil.i(TAG, "The amount of point is " + mPointCount + ".");
                // 清空旧的围栏位置数据
                mValidLocationDataList.clear();
                // 将围栏位置数据添加到有效列表中
                mValidLocationDataList.addAll(mDrawingLocationDataList);
                // 清空绘制位置列表数据，此时数据在有效位置列表中
                mDrawingLocationDataList.clear();

                // 在地图上绘制区域
                drawFenceRegionInMap();

                if (mDrawResultListener != null) {
                    mDrawResultListener.onSuccess(mValidLocationDataList);
                }
            } else {
                if (mDrawResultListener != null) {
                    LogUtil.i(TAG, "Covert failure.");
                    LogUtil.i(TAG, "The size of location list is " + mDrawingLocationDataList.size() + ".");
                    LogUtil.i(TAG, "The amount of point is " + mPointCount + ".");
                    mDrawResultListener.onFailure(HINT_ON_CONVERT_FAILURE);
                }
            }
        } else {
            mDrawingLocationDataList.add(new LocationData(latLng.latitude, latLng.longitude));
        }
    }

    /**
     * 在地图上绘制围栏区域
     */
    private void drawFenceRegionInMap() {
        if (mValidLocationDataList != null && mValidLocationDataList.size() != 0) {
            List<LatLng> latLngList = new ArrayList<>();
            for (LocationData data : mValidLocationDataList) {
                latLngList.add(new LatLng(data.getLatitude(), data.getLongitude()));
            }

            //构建用户绘制多边形的Option对象
            OverlayOptions polygonOption = new PolygonOptions()
                    .points(latLngList)
                    .stroke(new Stroke(5, 0xAAFF0000))
                    .fillColor(0xAAFFD300);

            //在地图上添加多边形Option，用于显示
            mMapView.getMap().addOverlay(polygonOption);
        }
    }

    private void initMapView(Context context) {
        mMapView = new MapView(context);
        LayoutParams lm = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMapView.setLayoutParams(lm);
        mMapView.showZoomControls(false);
        addView(mMapView);

        mMapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                getLocationOnMapClicked(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                LatLng latLng = mapPoi.getPosition();
                getLocationOnMapClicked(latLng);
                return false;
            }
        });
    }

    private void initCanvasView(Context context) {
        mCanvasView = new CanvasView(context);
        LayoutParams lm = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCanvasView.setLayoutParams(lm);
        mCanvasView.setDrawingState(false);
        addView(mCanvasView);
    }

    public GeofenceDrawMapView(Context context) {
        super(context);
        initMapView(context);
        initCanvasView(context);
    }

    public GeofenceDrawMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMapView(context);
        initCanvasView(context);
    }

    public GeofenceDrawMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMapView(context);
        initCanvasView(context);
    }

    public BaiduMap getMap() {
        return mMapView.getMap();
    }

    public void onResume() {
        mMapView.onResume();
    }

    public void onPause() {
        mMapView.onPause();
    }

    public void onDestroy() {
        mMapView.onDestroy();
    }

    public void setDrawResultListener(DrawResultListener drawResultListener) {
        mDrawResultListener = drawResultListener;
    }

    public void setLocationDataList(List<LocationData> locationDataList) {
        mValidLocationDataList = locationDataList;
        drawFenceRegionInMap();
    }

    /**
     * 开始绘制
     * 清除CanvasView所有已绘制的路径并设置CanvasView绘制状态
     * 关闭MapView的手势操作，避免影响绘制
     */
    public void startDrawing() {
        mDrawingState = true;
        mIsFirstPoint = false;
        mCanvasView.clearAllPath();
        mFirstCoordinatePoint = null;
        mPointCount = 0;
        mDrawingLocationDataList.clear();
        mCanvasView.setDrawingState(true);
        mMapView.getMap().clear();
        mMapView.getMap().getUiSettings().setAllGesturesEnabled(false);
    }

    /**
     * 撤销上一条路径的绘制
     */
    public void undoDrawing() {
        mCanvasView.deleteLastPath();
        if (mDrawingLocationDataList.size() > 1) {
            mDrawingLocationDataList.remove(mDrawingLocationDataList.size() - 1);
            mPointCount--;
        } else if (mDrawingLocationDataList.size() == 1) {
            mDrawingLocationDataList.remove(0);
            mFirstCoordinatePoint = null;
            mPointCount -= 2;
        }
    }

    /**
     * 完成绘制
     * 关闭CanvasView绘制状态并转换成回环路径
     * @return 完成结果，false表示未完成
     */
    public boolean completeDrawing() {
        // 完成绘制至少需要2条路径，即3个点
        if (mPointCount < 3) {
            Toast.makeText(getContext(), HINT_ON_POINT_LESS, Toast.LENGTH_SHORT).show();
            return false;
        }
        mDrawingState = false;
        mCanvasView.setDrawingState(false);
        mCanvasView.clearAllPath();
        convertFirstPoint();
        mMapView.getMap().getUiSettings().setAllGesturesEnabled(true);
        return true;
    }

    /**
     *  取消绘制
     *  关闭CanvasView绘制状态并清除CanvasView所有已绘制的路径
     *  开启MapView的手势操作
     */
    public void cancelDrawing() {
        mDrawingState = false;
        mFirstCoordinatePoint = null;
        mCanvasView.setDrawingState(false);
        mCanvasView.clearAllPath();
        drawFenceRegionInMap();
        mMapView.getMap().getUiSettings().setAllGesturesEnabled(true);
    }

    /**
     * 执行完成绘制时调用
     * 根据位置列表长度判断是否丢失点
     * 若为丢失则转换起点
     */
    private void convertFirstPoint() {
        // 正常情况下，只有起点没有转换成位置
        if (mDrawingLocationDataList.size() == mPointCount - 1) {
            LogUtil.i(TAG, "The size of location list is one smaller than the size of coordinate list.");
            // 如果相等，表明没有丢失点，将起点进行转换
            // 设置标志位，用于在点击回调中将起始点插入到起始位置
            mIsFirstPoint = true;

            MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN, mFirstCoordinatePoint.x, mFirstCoordinatePoint.y, 0);
            mMapView.dispatchTouchEvent(downEvent);
            MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_UP, mFirstCoordinatePoint.x, mFirstCoordinatePoint.y, 0);
            mMapView.dispatchTouchEvent(upEvent);
        } else {
            LogUtil.i(TAG, "There are points loss while converting coordinate to location.");
            LogUtil.i(TAG, "The size of location list is " + mDrawingLocationDataList.size() + ".");
            LogUtil.i(TAG, "The amount of point is " + mPointCount + ".");
            // 如果不相等，转换时丢失点
            if (mDrawResultListener != null) {
                LogUtil.i(TAG, "Covert failure.");
                mDrawResultListener.onFailure(HINT_ON_CONVERT_FAILURE);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        if (mDrawingState) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 记录下DOWN事件位置，用于与UP事件比较
                mLastDownPoint = new PointF(ev.getX(), ev.getY());
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (mLastDownPoint != null) {
                    // 如果DOWN事件与UP事件同一个位置，代表未绘制路径
                    if (ev.getX() != mLastDownPoint.x || ev.getY() != mLastDownPoint.y) {
                        if (mPointCount == 0) {
                            // 第一条路径，需要获取起点坐标

                            // 过高频率的点击事件会无法处理而导致丢失点
                            // 起点只记录坐标不做转换
                            mFirstCoordinatePoint = mLastDownPoint;
                            mPointCount++;
                        }
                        mPointCount++;

                        // 生成模拟的DOWN事件，与UP事件结合实现点击效果，触发Map的点击回调
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        mMapView.dispatchTouchEvent(ev);
                        ev.setAction(MotionEvent.ACTION_UP);
                        mMapView.dispatchTouchEvent(ev);
                    }
                    mLastDownPoint = null;
                }
            }
        }
        return result;
    }

    /**
     * 绘制结果监听器
     */
    public interface DrawResultListener {
        /**
         * 围栏设置成功回调
         * 同步回调
         * @param locationDataList 围栏点的经纬度数据列表
         */
        void onSuccess(List<LocationData> locationDataList);

        /**
         * 围栏设置失败回调
         * 同步回调
         * @param msg 失败原因
         */
        void onFailure(String msg);
    }
}
