/*
 * Copyright (C) 2015 Square, Inc, 2017 Jeff Gilfelt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.base.commonframework.baselibrary.network.httpcache;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.base.commonframework.BuildConfig;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpCacheContentProvider;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpTransaction;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.LocalCupboard;
import com.base.commonframework.baselibrary.network.httpcache.internal.support.NotificationHelper;
import com.base.commonframework.baselibrary.network.httpcache.internal.support.RetentionManager;
import com.base.commonframework.utility.CommonFrameworkUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * An OkHttp Interceptor which persists and displays HTTP activity in your application for later inspection.
 */
public final class HttpCacheInterceptor implements Interceptor {

    private static final String TAG = HttpCacheInterceptor.class.getSimpleName();
    private static final HttpCacheInterceptorUtil.Period DEFAULT_RETENTION = HttpCacheInterceptorUtil.Period.ONE_WEEK;
    public final Logger logger;
    private final NotificationHelper notificationHelper;
    private Context mContext;
    private RetentionManager retentionManager;
    private boolean showNotification;
    private boolean isCacheEnable;
    private long cacheMaxAge;
    private int maxRetryCount = 1;
    private long startNwResponse;
    private Map headerMap;


    /**
     * Instantiates a new HttpCache interceptor.
     *
     * @param mContext         the m context
     * @param cacheMaxAge      the cache max age
     * @param isCacheEnable    the is cache enable
     * @param showNotification the show notification
     */
    public HttpCacheInterceptor(Context mContext, int maxRetryCount, long cacheMaxAge, boolean isCacheEnable, boolean showNotification) {
        this.mContext = mContext;
        this.maxRetryCount = maxRetryCount;
        this.cacheMaxAge = TimeUnit.MINUTES.toMillis(cacheMaxAge);
        this.isCacheEnable = isCacheEnable;
        this.showNotification = showNotification;
        notificationHelper = new NotificationHelper(mContext);
        retentionManager = new RetentionManager(mContext, DEFAULT_RETENTION);
        logger = Logger.DEFAULT;
    }

    public static String protocol(String protocol) {
        return TextUtils.isEmpty(protocol) ? "HTTP/1.1" : protocol;
    }

    public void setNetworkAuthorizationHeader(Map headerMap) {
        this.headerMap = headerMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (null != headerMap && !headerMap.isEmpty()) {
            Request.Builder builder = chain.request().newBuilder();
            Iterator entries = headerMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (!TextUtils.isEmpty(entry.getKey().toString())) {
                    builder.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            //setServerSyncTime(builder);
            request = builder.build();
        }
        HttpTransaction transaction = HttpCacheInterceptorUtil.getRequestTransaction(mContext, request);
        HttpCacheInterceptorUtil.showRequestLog(transaction, logger);
        //Check this request already send to API
        CustomHttpCacheModel customModel = HttpCacheInterceptorUtil.getAvailableResponse(mContext, cacheMaxAge, transaction);
        if (null != customModel && customModel.isStatus()) {
            startNwResponse = System.nanoTime();
            Response response = new Response.Builder()
                    .code(HttpCacheInterceptorUtil.SUCCESS_STATUS_CODE)
                    .message(customModel.getHttpTransaction().getResponseBody())
                    .request(request)
                    .protocol(Protocol.get(customModel.getHttpTransaction().getProtocol()))
                    .body(ResponseBody.create(MediaType.parse(customModel.getHttpTransaction().getResponseContentType()), customModel.getHttpTransaction().getResponseBody().getBytes()))
                    .addHeader("content-type", customModel.getHttpTransaction().getResponseContentType())
                    .build();
            HttpCacheInterceptorUtil.showResponseLog(customModel.getHttpTransaction(), true, logger, startNwResponse);
            return response;

        } else {
            Response response;
            try {
                startNwResponse = System.nanoTime();
                response = chain.proceed(request); //Requesting to Server
                int retryCount = 0;                //Retry Policy
                while (null == response || !response.isSuccessful()) {
                    if (maxRetryCount <= retryCount) {
                        transaction.setError(response.message());
                        break;
                    }
                    retryCount++;
                    logger.log("Retry " + retryCount);
                    response = chain.proceed(request);
                }

            } catch (Exception e) {
                transaction.setError(e.toString());
                //update(transaction, transactionUri);
                throw e;
            }
            HttpCacheInterceptorUtil.getResponseTransaction(mContext, transaction, response);
            HttpCacheInterceptorUtil.showResponseLog(transaction, false, logger, startNwResponse);
            if (isCacheEnable && (transaction.getResponseCode() == HttpCacheInterceptorUtil.SUCCESS_STATUS_CODE
                    || transaction.getResponseCode() == HttpCacheInterceptorUtil.SUCCESS_STATUS_CODE_)) {
                insertIntoDb(mContext, transaction);
            }
            // update(transaction, transactionUri);
            return response;
        }


    }

    /**
     * @param mContext
     * @param transaction
     */
    private void insertIntoDb(Context mContext, HttpTransaction transaction) {
        ContentValues values = LocalCupboard.getInstance()
                .withEntity(HttpTransaction.class)
                .toContentValues(transaction);
        Uri uri = mContext.getContentResolver().insert(HttpCacheContentProvider.TRANSACTION_URI, values);
        if (null != uri && !TextUtils.isEmpty(uri.getLastPathSegment())) {
            transaction.setId(Long.valueOf(uri.getLastPathSegment()));
        }
        if (showNotification && BuildConfig.DEBUG) {
            notificationHelper.show(transaction);
        }
        retentionManager.doMaintenance();
    }

    /**
     * @param mContext
     * @param transaction
     * @param uri
     * @return
     */
    private int update(Context mContext, HttpTransaction transaction, Uri uri) {
        ContentValues values = LocalCupboard.getInstance().withEntity(HttpTransaction.class).toContentValues(transaction);
        int updated = mContext.getContentResolver().update(uri, values, null, null);
        if (showNotification && updated > 0) {
            notificationHelper.show(transaction);
        }
        return updated;
    }


    /**
     * The interface Logger.
     */
    public interface Logger {
        /**
         */
        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                CommonFrameworkUtil.showLog(TAG, message);
            }
        };

        /**
         * Log.
         *
         * @param message the message
         */
        void log(String message);
    }
}
