package com.gots.intelligentnursing.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.tools.UnitUtil;

/**
 * @author zhqy
 * @date 2018/4/20
 * 用于解决在ViewPager中使用MapView导致的滑动冲突
 * 内部有一个FAB用于控制滑动的锁定状态
 */

public class SlidingLockMapView extends RelativeLayout{

    private static final int MARGIN_RIGHT_DP = 16;
    private static final int MARGIN_TOP_DP = 16;

    private FloatingActionButton mFloatingActionButton;
    private MapView mMapView;

    private Drawable mLockDrawable;
    private Drawable mUnlockDrawable;

    /**
     * mSlidingLock为false时，滑动事件正常处理
     * 此时竖直滑动事件由MapView处理
     * 水平滑动时事件会被ViewPager拦截，导致无法滑动MapView
     * mSlidingLock为true时，在dispatchTouchEvent()中，会阻止父ViewGroup拦截事件
     * 所有滑动事件都交由MapView处理
     * 此时MapView可以正常滑动，ViewPager无法滑动
     */
    private boolean mSlidingLock = false;

    private void initFloatingActionButton(Context context) {
        mFloatingActionButton = new FloatingActionButton(context);

        LayoutParams lm = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lm.addRule(ALIGN_PARENT_RIGHT);
        lm.addRule(ALIGN_PARENT_TOP);
        lm.setMargins(0, UnitUtil.dip2px(context, MARGIN_TOP_DP), UnitUtil.dip2px(context, MARGIN_RIGHT_DP) , 0);
        mFloatingActionButton.setLayoutParams(lm);

        mLockDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_fab_lock);
        mUnlockDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_fab_lock);

        mFloatingActionButton.setImageDrawable(mLockDrawable);

        mFloatingActionButton.setOnClickListener(v -> {
            mSlidingLock = !mSlidingLock;
            if (mSlidingLock) {
                mFloatingActionButton.setImageDrawable(mUnlockDrawable);
            } else {
                mFloatingActionButton.setImageDrawable(mLockDrawable);
            }
        });
        addView(mFloatingActionButton);
    }

    private void initMapView(Context context) {
        mMapView = new MapView(context);
        LayoutParams lm = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMapView.setLayoutParams(lm);
        addView(mMapView);
    }

    public SlidingLockMapView(Context context) {
        super(context);
        initMapView(context);
        initFloatingActionButton(context);
    }

    public SlidingLockMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMapView(context);
        initFloatingActionButton(context);
    }

    public SlidingLockMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMapView(context);
        initFloatingActionButton(context);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mSlidingLock) {
            switch(ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                default:
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
