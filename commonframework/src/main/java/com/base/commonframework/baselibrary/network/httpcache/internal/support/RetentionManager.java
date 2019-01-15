package com.base.commonframework.baselibrary.network.httpcache.internal.support;

import android.content.Context;
import android.content.SharedPreferences;

import com.base.commonframework.baselibrary.network.httpcache.HttpCacheInterceptorUtil;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpCacheContentProvider;
import com.base.commonframework.utility.CommonFrameworkUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 * This Class basically flush the cache Database after the Period .
 * It will clear all the req/res logs from the table before date(now).
 */

public class RetentionManager {

    private static final String LOG_TAG = "HttpCache";
    private static final String PREFS_NAME = "httpCachePreferences";
    private static final String KEY_LAST_CLEANUP = "last_cleanup";

    private static long lastCleanup;

    private final Context context;
    private final long period;//In Millisecond
    private final long cleanupFrequency;//In Millisecond
    private final SharedPreferences prefs;

    public RetentionManager(Context context, HttpCacheInterceptorUtil.Period retentionPeriod) {
        this.context = context;
        period = toMillis(retentionPeriod);
        prefs = context.getSharedPreferences(PREFS_NAME, 0);
        cleanupFrequency = (retentionPeriod == HttpCacheInterceptorUtil.Period.ONE_HOUR) ?
                TimeUnit.MINUTES.toMillis(30) : TimeUnit.HOURS.toMillis(2);
    }

    public synchronized void doMaintenance() {
        if (period > 0) {
            long now = new Date().getTime();
            if (isCleanupDue(now)) {
                CommonFrameworkUtil.showLog(LOG_TAG, "Performing data retention maintenance...");
                deleteSince(getThreshold(now));
                updateLastCleanup(now);
            }
        }
    }

    private long getLastCleanup(long fallback) {
        if (lastCleanup == 0) {
            lastCleanup = prefs.getLong(KEY_LAST_CLEANUP, fallback);
        }
        return lastCleanup;
    }

    private void updateLastCleanup(long time) {
        lastCleanup = time;
        prefs.edit().putLong(KEY_LAST_CLEANUP, time).apply();
    }

    private void deleteSince(long threshold) {
        int rows = context.getContentResolver().delete(HttpCacheContentProvider.TRANSACTION_URI,
                "requestDate <= ?", new String[]{String.valueOf(threshold)});
        CommonFrameworkUtil.showLog(LOG_TAG, rows + " transactions deleted");
    }

    private boolean isCleanupDue(long now) {
        return (now - getLastCleanup(now)) > cleanupFrequency;
    }

    private long getThreshold(long now) {
        return (period == 0) ? now : now - period;
    }

    private long toMillis(HttpCacheInterceptorUtil.Period period) {
        switch (period) {
            case ONE_HOUR:
                return TimeUnit.HOURS.toMillis(1);
            case ONE_DAY:
                return TimeUnit.DAYS.toMillis(1);
            case ONE_WEEK:
                return TimeUnit.DAYS.toMillis(7);
            default:
                return 0;
        }
    }
}
