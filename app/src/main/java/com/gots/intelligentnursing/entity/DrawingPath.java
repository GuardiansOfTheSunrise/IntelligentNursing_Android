package com.gots.intelligentnursing.entity;

import android.graphics.PointF;

/**
 * @author zhqy
 * @date 2018/4/26
 */

public class DrawingPath {

    private PointF mStartPoint;
    private PointF mEndPoint;

    public DrawingPath() {

    }

    public DrawingPath(float x, float y) {
        mStartPoint = new PointF(x, y);
        mEndPoint = new PointF(x, y);
    }

    public PointF getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(PointF startPoint) {
        mStartPoint = startPoint;
    }

    public PointF getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(PointF endPoint) {
        mEndPoint = endPoint;
    }
}
