package com.gots.intelligentnursing.customview;

import android.content.Context;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
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

    private static final int STATE_NORMAL = 0;
    private static final int STATE_DRAWING = 1;
    private static final int STATE_WAIT_RESULT = 2;

    /**
     * 两次转换点之间间隔的阈值
     */
    private static final long THRESHOLD_INTERVAL_BETWEEN_POINT_MS = 1000;

    private static final String HINT_ON_POINT_LESS = "至少需要绘制两条路径哦";

    private MapView mMapView;
    private CanvasView mCanvasView;

    /**
     * 绘制过程的围栏位置数据
     */
    private List<LocationData> mDrawingLocationDataList = new ArrayList<>();

    /**
     * 有效的围栏位置数据
     * 当绘制成功后mDrawingLocationDataList会添加到mValidLocationDataList中
     * mDrawingLocationDataList被清空
     */
    private List<LocationData> mValidLocationDataList;

    /**
     * 高速触摸下防止丢失点
     * 对坐标点作缓存不转换
     * 其中的点在完成时才逐一转换
     */
    private SparseArray<PointF> mOverflowPointCache = new SparseArray<>();

    /**
     * 绘制状态
     * STATE_NORMAL：未绘制，此时事件交由MapView处理
     * STATE_DRAWING：正在绘制，事件由CanvasView处理，并获取坐标点在MapView上模拟点击
     * STATE_WAIT_RESULT：已绘制完正在转换，拦截事件
     */
    private int mDrawingState = STATE_NORMAL;

    /**
     * 上一次Down事件的坐标
     * 用于与Up事件坐标比较
     * Down和Up坐标相同时表明未绘制路径
     */
    private PointF mLastDownPoint = null;

    /**
     * 用于标识是否是转换缓存点
     * 如果是缓存点则把经纬度插入到mPointPosition指定的位置
     */
    private boolean mIsCachetPoint = false;

    /**
     * 在缓存点转换时
     * 用于标识该缓存点在点序列中的位置
     * 并把转换的经纬度插入到列表指定位置中
     */
    private int mPointPosition;

    /**
     * 绘制完成时等待确认标志位
     */
    private boolean mWaitConfirm;

    /**
     * 用于标识一个缓存点转换是否结束
     * 当一个点转换结束时
     * 才开始下一个点的转换
     * 多线程环境加volatile避免取值错误
     */
    private volatile boolean mIsConvertFinish;

    /**
     * 上一个转换点的时间
     * 当新到来点的时间与上一个转换点的时间差小于阈值时
     * 把点添加到缓存中不做转换防止高频下点丢失
     */
    private long mLastConvertPointTimeMills;

    /**
     * 围栏点的数量
     */
    private int mPointCount;

    /**
     * 围栏绘制结果回调接口
     */
    private DrawResultListener mDrawResultListener;

    /**
     * 将位置信息添加到位置列表
     * 如果是缓存点，则插入到列表指定位置
     */
    private void getLocationOnMapClicked(LatLng latLng) {
        // 转换第一个点
        // 当执行完成绘制后，才开始转换第一个点
        if (mIsCachetPoint) {
            mDrawingLocationDataList.add(mPointPosition, new LocationData(latLng.latitude, latLng.longitude));
            mIsConvertFinish = true;
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

    /**
     * 用于在地图上绘制围栏区域
     * @param locationDataList 用户的围栏数据列表
     */
    public void setLocationDataList(List<LocationData> locationDataList) {
        mValidLocationDataList = locationDataList;
        drawFenceRegionInMap();
    }

    /**
     * 开始绘制
     */
    public void startDrawing() {
        mDrawingState = STATE_DRAWING;
        mIsCachetPoint = false;
        mWaitConfirm = false;
        mCanvasView.clearAllPath();
        mPointCount = 0;
        mDrawingLocationDataList.clear();
        mOverflowPointCache.clear();
        mCanvasView.setDrawingState(true);
        mMapView.getMap().clear();
        mMapView.getMap().getUiSettings().setAllGesturesEnabled(false);
    }

    /**
     * 撤销上一条路径的绘制
     * 清除CanvasView中的上一条路径
     * 并在list或cache中清除路径的相关点
     */
    public void undoDrawing() {
        mCanvasView.deleteLastPath();
        // 起点一定在cache中
        // 第2个点一定在list中
        if (mOverflowPointCache.keyAt(mOverflowPointCache.size() - 1) == mPointCount - 1) {
            // 如果在cache中，则一定在末尾，且末尾的元素的key应等于点的数量减一
            mOverflowPointCache.removeAt(mOverflowPointCache.size() - 1);
        } else {
            // 如果在list中，也一定在末尾
            mDrawingLocationDataList.remove(mDrawingLocationDataList.size() - 1);
        }
        mPointCount--;
        if (mPointCount == 1) {
            mOverflowPointCache.clear();
            mPointCount--;
        }
    }

    /**
     * 完成绘制
     * 如果成功则在地图上转换成围栏区域
     * @return 完成结果，false表示未完成
     */
    public boolean completeDrawing() {
        // 完成绘制至少需要2条路径，即3个点
        if (mPointCount < 3) {
            Toast.makeText(getContext(), HINT_ON_POINT_LESS, Toast.LENGTH_SHORT).show();
            return false;
        }
        mDrawingState = STATE_WAIT_RESULT;
        mCanvasView.setDrawingState(false);
        mCanvasView.clearAllPath();
        convertCachePoint();
        return true;
    }

    /**
     *  取消绘制
     */
    public void cancelDrawing() {
        mDrawingState = STATE_NORMAL;
        mOverflowPointCache.clear();
        mCanvasView.setDrawingState(false);
        mCanvasView.clearAllPath();
        drawFenceRegionInMap();
        mMapView.getMap().getUiSettings().setAllGesturesEnabled(true);
    }

    /**
     * 确认更新围栏数据
     * 当围栏绘制成功后会置标志位，此时地图尚未更新
     * 等待用户确认是否更新，如果确认则更新围栏
     * 否则不更新数据并重新绘制旧区域
     * 如果未绘制完成调用该方法不起作用
     * 作用和giveUpUpdateFenceData()相反
     * 设置这组方法的目的为了解决网络问题时新围栏未上传至服务器
     * 而旧围栏失效的问题
     */
    public void confirmUpdateFenceData() {
        if (mWaitConfirm) {
            mWaitConfirm = false;

            // 清空旧的围栏位置数据
            mValidLocationDataList.clear();
            // 将围栏位置数据添加到有效列表中
            mValidLocationDataList.addAll(mDrawingLocationDataList);
            // 清空绘制位置列表数据，此时数据在有效位置列表中
            mDrawingLocationDataList.clear();
            // 将新围栏绘制于地图上
            drawFenceRegionInMap();
        }
    }

    /**
     * 放弃更新围栏数据
     * 描述参照confirmUpdateFenceData()
     */
    public void giveUpUpdateFenceData() {
        if (mWaitConfirm) {
            mWaitConfirm = false;

            // 清除新绘的围栏数据
            mDrawingLocationDataList.clear();
            // 将原围栏绘制于地图上
            drawFenceRegionInMap();
        }
    }

    /**
     * 执行完成绘制时调用
     * 根据位置位置列表及缓存列表的长度判断是否丢失点
     * 若未丢失则转换cache中的点
     */
    private void convertCachePoint() {
        // 正常情况下，只有起点没有转换成位置
        if (mDrawingLocationDataList.size() + mOverflowPointCache.size() == mPointCount) {
            LogUtil.i(TAG, "The size of location list add the size of cache list is equal to point count.");
            // 如果相等，表明没有丢失点，将起点进行转换
            // 设置标志位，用于在点击回调中将起始点插入到起始位置
            mIsCachetPoint = true;
            if (mDrawResultListener != null) {
                mDrawResultListener.onStart();
            }
            new Thread(() -> {
                for (int i = 0;i <mOverflowPointCache.size();i++) {
                    mIsConvertFinish = false;

                    mPointPosition = mOverflowPointCache.keyAt(i);
                    PointF point = mOverflowPointCache.valueAt(i);
                    MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN, point.x, point.y, 0);
                    MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_UP, point.x, point.y, 0);
                    mMapView.post(() -> {
                        mMapView.dispatchTouchEvent(downEvent);
                        mMapView.dispatchTouchEvent(upEvent);
                    });
                    // 等待转换结束标志
                    while (!mIsConvertFinish) {}
                    downEvent.recycle();
                    upEvent.recycle();
                }
                mOverflowPointCache.clear();
                post(() -> {
                    mDrawingState = STATE_NORMAL;
                    mMapView.getMap().getUiSettings().setAllGesturesEnabled(true);
                    if (mDrawingLocationDataList.size() == mPointCount) {
                        LogUtil.i(TAG, "Covert success.");
                        LogUtil.i(TAG, "The size of location list is " + mDrawingLocationDataList.size() + ".");
                        LogUtil.i(TAG, "The amount of point is " + mPointCount + ".");

                        mWaitConfirm = true;

                        if (mDrawResultListener != null) {
                            mDrawResultListener.onSuccess(mValidLocationDataList);
                        }
                    } else {
                        LogUtil.i(TAG, "Covert failure.");
                        LogUtil.i(TAG, "The size of location list is " + mDrawingLocationDataList.size() + ".");
                        LogUtil.i(TAG, "The count of point is " + mPointCount + ".");
                        LogUtil.i(TAG, "The size of cache is " + mOverflowPointCache.size() + ".");
                        // 将原围栏重新绘制与地图上
                        drawFenceRegionInMap();
                        if (mDrawResultListener != null) {
                            mDrawResultListener.onFailure();
                        }
                    }
                });
            }).start();
        } else {
            LogUtil.i(TAG, "Covert failure.");
            LogUtil.i(TAG, "The size of location list is " + mDrawingLocationDataList.size() + ".");
            LogUtil.i(TAG, "The count of point is " + mPointCount + ".");
            LogUtil.i(TAG, "The size of cache is " + mOverflowPointCache.size() + ".");

            // 将原围栏重新绘制与地图上
            drawFenceRegionInMap();
            mMapView.getMap().getUiSettings().setAllGesturesEnabled(true);
            // 如果不相等，转换时丢失点
            if (mDrawResultListener != null) {
                LogUtil.i(TAG, "Covert failure.");
                mDrawResultListener.onFailure();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        if (mDrawingState == STATE_DRAWING) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 记录下DOWN事件位置，用于与UP事件比较
                mLastDownPoint = new PointF(ev.getX(), ev.getY());
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (mLastDownPoint != null) {
                    // 如果DOWN事件与UP事件同一个位置，代表未绘制路径
                    if (ev.getX() != mLastDownPoint.x || ev.getY() != mLastDownPoint.y) {
                        if (mPointCount == 0) {
                            // 第一条路径，需要获取起点和终点
                            // 同时转换频率过高会丢失点，所以把起点缓存，只转换终点
                            mOverflowPointCache.put(mPointCount, mLastDownPoint);
                            mPointCount++;
                        }
                        long currentTimeMills = System.currentTimeMillis();
                        // 判断上一个转换点与当前的时间差，大于阈值则转换，小于阈值则添加到缓存
                        // 防止高频触摸导致点丢失
                        if (currentTimeMills - mLastConvertPointTimeMills > THRESHOLD_INTERVAL_BETWEEN_POINT_MS) {
                            // 大于阈值进行转换
                            // 生成模拟的DOWN事件，与UP事件结合实现点击效果，触发Map的点击回调
                            ev.setAction(MotionEvent.ACTION_DOWN);
                            mMapView.dispatchTouchEvent(ev);
                            ev.setAction(MotionEvent.ACTION_UP);
                            mMapView.dispatchTouchEvent(ev);
                            // 记录下时间
                            mLastConvertPointTimeMills = currentTimeMills;
                        } else {
                            // 小于阈值添加到缓存，等到完成时再逐一转换
                            mOverflowPointCache.put(mPointCount, new PointF(ev.getX(), ev.getY()));
                        }
                        mPointCount++;
                    }
                    mLastDownPoint = null;
                }
            }
        }
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDrawingState == STATE_WAIT_RESULT || super.onInterceptTouchEvent(ev);
    }

    /**
     * 绘制结果监听器
     */
    public interface DrawResultListener {
        /**
         * 开始转换回调
         * 大量点转换时是耗时操作，可以创建进度栏提示
         * 同步回调
         */
        void onStart();

        /**
         * 围栏设置成功回调
         * 同步回调
         * @param locationDataList 围栏点的经纬度数据列表
         */
        void onSuccess(List<LocationData> locationDataList);

        /**
         * 围栏设置失败回调
         * 同步回调
         */
        void onFailure();
    }
}
