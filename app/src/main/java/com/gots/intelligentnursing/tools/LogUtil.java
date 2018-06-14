package com.gots.intelligentnursing.tools;

import android.util.Log;

/**
 * 日志工具类
 * @author zhqy
 * @date 2018/4/2
 */

public class LogUtil {

    public static final int LEVEL_VERBOSE = 1;
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_WARN = 4;
    public static final int LEVEL_ERROR = 5;
    public static final int LEVEL_NOTHING = 6;

    public static int level = LEVEL_VERBOSE;

    public static void v(String tag, String msg) {
        if (level <= LEVEL_VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= LEVEL_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= LEVEL_INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= LEVEL_WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= LEVEL_ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void logExceptionStackTrace(String tag, Throwable e) {
        if (level <= LEVEL_ERROR) {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackArray = e.getStackTrace();
            for (StackTraceElement element : stackArray) {
                sb.append(element.toString()).append("\n");
            }
            Log.e(tag, sb.toString());
        }

    }
}
