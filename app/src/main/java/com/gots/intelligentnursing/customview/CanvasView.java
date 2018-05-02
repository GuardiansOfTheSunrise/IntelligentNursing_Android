package com.gots.intelligentnursing.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gots.intelligentnursing.entity.DrawingPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhqy
 * @date 2018/4/27
 */

public class CanvasView extends View {

    private DrawingPath mCurrentPath;
    private List<DrawingPath> mPathList = new ArrayList<>();

    private Paint mSolidLinePaint;
    private Paint mDashedLinePaint;

    /**
     * 绘制状态
     * 为false时未开始绘制，触摸事件不处理
     * 为true时开始绘制，处理触摸事件，并会触发draw()
     */
    private boolean mDrawingState = false;

    private boolean drawLoopPath = false;

    private void initPaint() {
        mSolidLinePaint = new Paint();
        mSolidLinePaint.setColor(Color.RED);
        mSolidLinePaint.setStyle(Paint.Style.STROKE);
        mSolidLinePaint.setStrokeWidth(10);

        mDashedLinePaint = new Paint();
        mDashedLinePaint.setColor(Color.RED);
        mDashedLinePaint.setStyle(Paint.Style.STROKE);
        mDashedLinePaint.setStrokeWidth(10);
        mDashedLinePaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
    }

    public CanvasView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initPaint();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initPaint();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initPaint();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mDrawingState && super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mPathList.size() == 0) {
                    // 绘制第一条路径时，起点为触摸按下时的坐标点
                    mCurrentPath = new DrawingPath(event.getX(), event.getY());
                } else {
                    // 绘制非第一条路径时，起点为上一条路径的终点
                    DrawingPath lastPath = mPathList.get(mPathList.size() - 1);
                    mCurrentPath = new DrawingPath(lastPath.getEndPoint().x, lastPath.getEndPoint().y);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurrentPath.getEndPoint().set(event.getX(), event.getY());
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                // 起点等于终点，表明未绘制路径
                if (mCurrentPath.getStartPoint().x != mCurrentPath.getEndPoint().x ||
                        mCurrentPath.getStartPoint().y != mCurrentPath.getEndPoint().y) {
                    mPathList.add(mCurrentPath);
                }
                mCurrentPath = null;
                return false;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPathList != null) {
            for (DrawingPath path : mPathList) {
                canvas.drawLine(path.getStartPoint().x, path.getStartPoint().y,
                        path.getEndPoint().x, path.getEndPoint().y, mSolidLinePaint);
            }
        }
        if (mCurrentPath != null) {
            canvas.drawLine(mCurrentPath.getStartPoint().x, mCurrentPath.getStartPoint().y,
                    mCurrentPath.getEndPoint().x, mCurrentPath.getEndPoint().y, mSolidLinePaint);
            if (mPathList != null && mPathList.size() != 0) {
                DrawingPath firstPath = mPathList.get(0);
                canvas.drawLine(mCurrentPath.getEndPoint().x, mCurrentPath.getEndPoint().y,
                        firstPath.getStartPoint().x, firstPath.getStartPoint().y, mDashedLinePaint);
            }
        } else {
            if (mPathList != null && mPathList.size() > 1) {
                DrawingPath firstPath = mPathList.get(0);
                DrawingPath lastPath = mPathList.get(mPathList.size() - 1);
                canvas.drawLine(lastPath.getEndPoint().x, lastPath.getEndPoint().y,
                        firstPath.getStartPoint().x, firstPath.getStartPoint().y, mDashedLinePaint);
            }
        }
        if (drawLoopPath) {
            drawLoopPath = false;
        }
    }

    /**
     * 删除上一条路径
     * 用于实现撤销的功能
     */
    public void deleteLastPath() {
        if (mPathList.size() != 0) {
            mPathList.remove(mPathList.size() - 1);
            invalidate();
        }
    }

    /**
     * 清除已绘制的所有路径
     */
    public void clearAllPath() {
        mPathList.clear();
        mCurrentPath = null;
        invalidate();
    }

    public void setDrawingState(boolean drawingState) {
        mDrawingState = drawingState;
    }
}
