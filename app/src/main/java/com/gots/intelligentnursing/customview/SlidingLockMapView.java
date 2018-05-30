package com.gots.intelligentnursing.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.tools.UnitUtil;

import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/20
 * 用于解决在ViewPager中使用MapView导致的滑动冲突
 * 内部有一个FAB用于控制滑动的锁定状态
 */

public class SlidingLockMapView extends RelativeLayout {

    private static final int MARGIN_RIGHT_DP = 16;
    private static final int MARGIN_TOP_DP = 16;

    private static final int VIEW_OFFSET_DP = 20;

    private FloatingActionButton mFloatingActionButton;
    private MapView mMapView;
    private ImageView mExpandImageView;
    private View mBottomView;
    private LinearLayout mButtonCollectorLinearLayout;

    private Drawable mLockDrawable;
    private Drawable mUnlockDrawable;

    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 底部菜单栏展开状态
     * false表示未展开，true表示展开
     */
    private boolean mBottomMenuExpandState = false;

    /**
     * mSlidingLock为false时，滑动事件正常处理
     * 此时竖直滑动事件由MapView处理
     * 水平滑动时事件会被ViewPager拦截，导致无法滑动MapView
     * mSlidingLock为true时，在dispatchTouchEvent()中，会阻止父ViewGroup拦截事件
     * 所有滑动事件都交由MapView处理
     * 此时MapView可以正常滑动，ViewPager无法滑动
     */
    private boolean mSlidingLock = false;

    private void initBottomExtendButton(Context context) {
        mExpandImageView = new ImageView(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        lp.addRule(CENTER_HORIZONTAL);
        mExpandImageView.setLayoutParams(lp);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_expand);
        mExpandImageView.setImageDrawable(drawable);

        mExpandImageView.setOnClickListener(v -> {
            mBottomMenuExpandState = !mBottomMenuExpandState;

            if (mBottomMenuExpandState) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(mExpandImageView, "rotation", 0, 180),
                        ObjectAnimator.ofFloat(mExpandImageView, "translationY",
                                0, -(mBottomView.getHeight() + UnitUtil.dip2px(getContext(), VIEW_OFFSET_DP))),
                        ObjectAnimator.ofFloat(mBottomView, "translationY",
                                0, -(mBottomView.getHeight() + UnitUtil.dip2px(getContext(), VIEW_OFFSET_DP)))
                );
                animatorSet.setDuration(500);
                animatorSet.start();
            } else {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(mExpandImageView, "rotation", 0),
                        ObjectAnimator.ofFloat(mExpandImageView, "translationY", 0),
                        ObjectAnimator.ofFloat(mBottomView, "translationY",  0)
                );
                animatorSet.setDuration(500);
                animatorSet.start();
            }

        });
        addView(mExpandImageView);
    }

    private void initBottomMenu(Context context) {
        mBottomView = LayoutInflater.from(context).inflate(R.layout.view_sliding_lock_map_view_bottom_menu, this, false);
        mButtonCollectorLinearLayout = mBottomView.findViewById(
                R.id.ll_custom_view_sliding_lock_map_view_bottom_menu_button_collector);
        mButtonCollectorLinearLayout.setOnTouchListener((v, event) -> true);
        addView(mBottomView);
    }

    private void initFloatingActionButton(Context context) {
        mFloatingActionButton = new FloatingActionButton(context);

        LayoutParams lm = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lm.addRule(ALIGN_PARENT_RIGHT);
        lm.addRule(ALIGN_PARENT_TOP);
        lm.setMargins(0, UnitUtil.dip2px(context, MARGIN_TOP_DP), UnitUtil.dip2px(context, MARGIN_RIGHT_DP) , 0);
        mFloatingActionButton.setLayoutParams(lm);

        mLockDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_fab_lock);
        mUnlockDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_fab_unlock);

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
        mMapView.setId(R.id.map_view_custom_view_sliding_lock_map_view);
        addView(mMapView);
    }

    public SlidingLockMapView(Context context) {
        super(context);
        initMapView(context);
        initFloatingActionButton(context);
        initBottomMenu(context);
        initBottomExtendButton(context);
    }

    public SlidingLockMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMapView(context);
        initFloatingActionButton(context);
        initBottomMenu(context);
        initBottomExtendButton(context);
    }

    public SlidingLockMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMapView(context);
        initFloatingActionButton(context);
        initBottomMenu(context);
        initBottomExtendButton(context);
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

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setButtonMenuTexts(List<String> texts) {
        OnClickListener onClickListener = view -> {
            if (mOnMenuItemClickListener != null) {
                mOnMenuItemClickListener.onClick((Integer) view.getTag());
            }
        };

        for (int i = 0; i < texts.size(); i++) {
            RipplingFilletedButton button = (RipplingFilletedButton) LayoutInflater.from(getContext())
                    .inflate(R.layout.view_sliding_lock_map_view_button, mButtonCollectorLinearLayout, false);
            button.setButtonText(texts.get(i));
            button.setOnClickListener(onClickListener);
            button.setTag(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(button.getLayoutParams());
            if (i != 0) {
                lp.setMargins(0, UnitUtil.dip2px(getContext(), 10), 0,  0);
            }
            button.setLayoutParams(lp);
            mButtonCollectorLinearLayout.addView(button);
        }
    }

    public RipplingFilletedButton getButtonByIndex(int index) {
        return (RipplingFilletedButton) mButtonCollectorLinearLayout.getChildAt(index);
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

    public interface OnMenuItemClickListener {
        /**
         * 底部菜单按钮被点击回调
         * @param index 按钮的索引
         */
        void onClick(int index);
    }
}
