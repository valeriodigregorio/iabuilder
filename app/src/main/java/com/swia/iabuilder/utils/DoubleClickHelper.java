package com.swia.iabuilder.utils;

import android.os.SystemClock;

import java.util.HashMap;
import java.util.Map;

public class DoubleClickHelper {

    private static final long MIN_TIME_DELAY_IN_MS = 1000L;
    private static final Map<String, Long> lastClickTimeInMsMap = new HashMap<>();

    private static String getTag() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[4];
        return e.toString();
    }

    private static boolean testAndSet(String tag, long timeDelayInMs) {
        Long lastClickTimeInMs = lastClickTimeInMsMap.get(tag);
        if (lastClickTimeInMs == null) {
            lastClickTimeInMs = 0L;
        }

        long currentTimeInMs = SystemClock.elapsedRealtime();
        long elapsedTimeInMs = currentTimeInMs - lastClickTimeInMs;
        if (elapsedTimeInMs < timeDelayInMs) {
            return false;
        }

        lastClickTimeInMsMap.put(tag, SystemClock.elapsedRealtime());
        return true;
    }

    public static boolean testAndSet(long timeDelayInMs) {
        return testAndSet(getTag(), timeDelayInMs);
    }

    public static boolean testAndSet() {
        return testAndSet(getTag(), MIN_TIME_DELAY_IN_MS);
    }
}
