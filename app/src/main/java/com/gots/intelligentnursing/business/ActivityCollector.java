package com.gots.intelligentnursing.business;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhqy
 * @date 2018/5/16
 */

public class ActivityCollector {

    private static final Pattern PATTERN_LOGINED_ACTIVITY = Pattern.compile("com.gots.intelligentnursing.activity.logined.*");

    private static List<Activity> mActivityList = new ArrayList<>();

    public static void add(Activity activity) {
        mActivityList.add(activity);
    }

    public static void remove(Activity activity) {
        mActivityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    public static void finishLoginedActivity() {
        for (Activity activity : mActivityList) {
            Matcher matcher = PATTERN_LOGINED_ACTIVITY.matcher(activity.getClass().getName());
            if (matcher.matches()) {
                activity.finish();
            }
        }
    }

}
