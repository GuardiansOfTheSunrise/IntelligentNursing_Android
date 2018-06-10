package com.gots.intelligentnursing.business;

import android.os.SystemClock;

/**
 * @author zhqy
 * @date 2018/6/9
 */

public class VerificationCodeTimer {

    private static final int GETTING_INTERVAL_SECOND = 60;

    private long mLastGettingTimeMs = 0;

    public void updateLastGettingTime() {
        mLastGettingTimeMs = SystemClock.uptimeMillis();
    }

    public int secondLeft() {
        if (mLastGettingTimeMs == 0) {
            return 0;
        } else {
            long currentTimeMs = SystemClock.uptimeMillis();
            int sec = (int) ((currentTimeMs - mLastGettingTimeMs) / 1000);
            if (sec > GETTING_INTERVAL_SECOND) {
                mLastGettingTimeMs = 0;
                return 0;
            } else {
                return GETTING_INTERVAL_SECOND - sec;
            }
        }
    }

    private VerificationCodeTimer() {}

    public static VerificationCodeTimer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final VerificationCodeTimer INSTANCE = new VerificationCodeTimer();
    }
}
