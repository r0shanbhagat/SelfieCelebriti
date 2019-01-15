package com.base.commonframework.baselibrary.network;

import android.content.Context;
import androidx.annotation.NonNull;

import com.base.commonframework.baselibrary.utility.NetworkUtility;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Gautam Sharma on 18-02-2016.
 */
public final class CacheControlInterceptor implements Interceptor {
    private static int maxCacheAgeInMinute = 5;
    private final String TAG = CacheControlInterceptor.class.getSimpleName();
    private Context mContext;
    private int maxStaleInDay = 28; // tolerate 4-weeks stale

    /**
     * @param mContext
     */
    private CacheControlInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Instantiates a new Cache control interceptor.
     *
     * @param mContext            the m context
     * @param maxCacheAgeInMinute the max cache age in minute
     * @param maxStaleInDay       the max stale
     */
    public CacheControlInterceptor(Context mContext, int maxCacheAgeInMinute, int maxStaleInDay) {
        this.mContext = mContext;
        CacheControlInterceptor.maxCacheAgeInMinute = maxCacheAgeInMinute;
        this.maxStaleInDay = maxStaleInDay;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder request = originalRequest.newBuilder();
        if (originalRequest.header("fresh") != null) {
            request.cacheControl(CacheControl.FORCE_NETWORK);
        }
        Response response = chain.proceed(request.build());
        return response.newBuilder().removeHeader("Pragma").removeHeader("Expires").removeHeader("Cache-Control").header(
                "Cache-Control", cacheResponseControl()).build();
    }

    @NonNull
    private String cacheResponseControl() {
        String cacheHeaderValue;
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            cacheHeaderValue = "public, max-age=" + TimeUnit.MINUTES.toSeconds(maxCacheAgeInMinute);
        } else {
            cacheHeaderValue = "public, only-if-cached, max-stale=" + TimeUnit.DAYS.toSeconds(maxStaleInDay);
        }
        return cacheHeaderValue;
    }
}