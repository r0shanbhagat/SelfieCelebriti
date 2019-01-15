package com.base.commonframework.baselibrary.network.httpcache;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.base.commonframework.R;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpCacheContentProvider;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpHeader;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpTransaction;
import com.base.commonframework.baselibrary.network.httpcache.statusmodel.Status;
import com.base.commonframework.baselibrary.utility.NetworkUtility;
import com.base.commonframework.baselibrary.utility.ParseUtility;
import com.base.commonframework.utility.CommonFrameworkUtil;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by roshan on 18/4/17.
 */
public class HttpCacheInterceptorUtil {

    /**
     * The constant UTF8.
     */
    public static final Charset UTF8 = Charset.forName("UTF-8");
    /**
     * The constant COL_ID.
     */
    public static final String COL_ID = "_id";
    /**
     * The constant COL_REQUEST_HEADER.
     */
    public static final String COL_REQUEST_HEADER = "requestHeaders";
    /**
     * The constant COL_REQUEST_BODY.
     */
    public static final String COL_REQUEST_BODY = "requestBody";
    /**
     * The constant COL_METHOD.
     */
    public static final String COL_METHOD = "method";
    /**
     * The constant COL_URL.
     */
    public static final String COL_URL = "url";
    /**
     * The constant COL_RESPONSE_CODE.
     */
    public static final String COL_RESPONSE_CODE = "responseCode";
    /**
     * The constant COL_RESPONSE_BODY.
     */
    public static final String COL_RESPONSE_BODY = "responseBody";
    /**
     * The constant COL_RESPONSE_CONTENT_TYPE.
     */
    public static final String COL_RESPONSE_CONTENT_TYPE = "responseContentType";
    /**
     * The constant COL_RESPONSE_MESSAGE.
     */
    public static final String COL_RESPONSE_MESSAGE = "responseMessage";
    /**
     * The constant COL_RESPONSE_DATE.
     */
    public static final String COL_RESPONSE_DATE = "responseDate";

    /**
     * The constant COL_RESPONSE_HEADER.
     */
    public static final String COL_RESPONSE_HEADER = "responseHeaders";

    /**
     * The constant COL_PROTOCOL.
     */
    public static final String COL_PROTOCOL = "protocol";
    /**
     * The constant TAG.
     */
    public static String TAG = HttpCacheInterceptorUtil.class.getSimpleName();
    /**
     * The constant SUCCESS_STATUS_CODE.
     */
    public static int SUCCESS_STATUS_CODE = 200;

    public static int SUCCESS_STATUS_CODE_ = 201;

    /**
     * The constant maxContentLength.
     */
    public static long maxContentLength = 2500000L;

    /**
     * Gets available response.
     *
     * @param mContext    the m context
     * @param cacheMaxAge the cache max age
     * @param transaction the transaction
     * @return the available response
     */
    public static CustomHttpCacheModel getAvailableResponse(Context mContext, long cacheMaxAge, HttpTransaction transaction) {
        CustomHttpCacheModel customModel = null;
        try {
            validateTransActionValue(transaction);
            String selection = /*COL_REQUEST_HEADER + "=? and " +*/ COL_URL + "=? and "
                    + COL_REQUEST_BODY + "=? and  " + COL_METHOD + "=? and " + COL_RESPONSE_CODE + "=? ";
            String selectionArgs[] = {/*transaction.requestHeaders,*/ transaction.getUrl(),
                    transaction.getRequestBody(), transaction.getMethod(), String.valueOf(SUCCESS_STATUS_CODE)};
            Cursor cursor = mContext.getContentResolver().query(HttpCacheContentProvider.TRANSACTION_URI,
                    null, selection, selectionArgs, "");
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    customModel = new CustomHttpCacheModel();
                    long responseDate = cursor.getLong(cursor.getColumnIndex(COL_RESPONSE_DATE));
                    long diff = System.currentTimeMillis() - responseDate;
                    if (diff < cacheMaxAge || !NetworkUtility.isNetworkAvailable(mContext)) {
                        customModel.setStatus(true);
                        customModel.setHttpTransaction(getUpdatedTransaction(cursor, transaction));
                    } else {
                        //Option:Network check
                        long rowId = cursor.getLong(cursor.getColumnIndex(COL_ID));
                        deleteRowIfExist(mContext, String.valueOf(rowId));
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customModel;
    }

    /**
     * @param transaction Setting value for Null Column Data
     */
    private static void validateTransActionValue(HttpTransaction transaction) {
        if (TextUtils.isEmpty(transaction.getRequestBody())) {
            transaction.setRequestBody("");
        }
        if (TextUtils.isEmpty(transaction.getMethod())) {
            transaction.setMethod("");
        }
        if (TextUtils.isEmpty(transaction.getUrl())) {
            transaction.setUrl("");
        }
        if (TextUtils.isEmpty(transaction.getRequestHeadersString(true))) {
            // transaction.setRequestHeaders(new Headers(null));
        }
    }

    /**
     * @param mContext
     * @param rowId
     */
    private static void deleteRowIfExist(Context mContext, String rowId) {
        try {
            String selection = COL_ID + " = ?";
            String[] selectionArgs = {rowId};
            mContext.getContentResolver().delete(HttpCacheContentProvider.TRANSACTION_URI, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param cursor
     * @param transaction
     * @return
     */
    private static HttpTransaction getUpdatedTransaction(Cursor cursor, HttpTransaction transaction) {
        transaction.setResponseCode(cursor.getInt(cursor.getColumnIndex(COL_RESPONSE_CODE)));
        transaction.setResponseBody(cursor.getString(cursor.getColumnIndex(COL_RESPONSE_BODY)));
        transaction.setResponseContentType(cursor.getString(cursor.getColumnIndex(COL_RESPONSE_CONTENT_TYPE)));
        transaction.setResponseMessage(cursor.getString(cursor.getColumnIndex(COL_RESPONSE_MESSAGE)));
        transaction.setProtocol(cursor.getString(cursor.getColumnIndex(COL_PROTOCOL)));
        transaction.setResponseHeaders(cursor.getString(cursor.getColumnIndex(COL_RESPONSE_HEADER)));
        return transaction;
    }


    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    /**
     * @param headers
     * @return
     */
    private static boolean bodyHasUnsupportedEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null &&
                !contentEncoding.equalsIgnoreCase("identity") &&
                !contentEncoding.equalsIgnoreCase("gzip");
    }

    /**
     * @param headers
     * @return
     */
    private static boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    /**
     * @param mContext
     * @param buffer
     * @param charset
     * @return
     */
    private static String readFromBuffer(Context mContext, Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
            body += mContext.getString(R.string.httpCacheBodyUnexpectedEof);
        }
        if (bufferSize > maxContentLength) {
            body += mContext.getString(R.string.httpCacheBodyContentTruncated);
        }
        return body;
    }

    /**
     * @param input
     * @param isGzipped
     * @return
     */
    private static BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }

    /**
     * Gets request transaction.
     *
     * @param mContext the m context
     * @param request  the request
     * @return the request transaction
     */
    public static HttpTransaction getRequestTransaction(Context mContext, Request request) {
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        HttpTransaction transaction = new HttpTransaction();
        transaction.setRequestDate(new Date());
        transaction.setNetworkTime(System.nanoTime());
        transaction.setMethod(request.method());
        transaction.setUrl(request.url().toString());
        transaction.setRequestHeaders(request.headers());
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                transaction.setRequestContentType(requestBody.contentType().toString());
            }
            try {
                if (requestBody.contentLength() != -1) {
                    transaction.setRequestContentLength(requestBody.contentLength());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        transaction.setRequestBodyIsPlainText(!bodyHasUnsupportedEncoding(request.headers()));
        if (hasRequestBody && transaction.requestBodyIsPlainText()) {
            BufferedSource source = getNativeSource(new Buffer(), bodyGzipped(request.headers()));
            Buffer buffer = source.buffer();
            try {
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Charset charset = HttpCacheInterceptorUtil.UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(HttpCacheInterceptorUtil.UTF8);
            }
            if (isPlaintext(buffer)) {
                transaction.setRequestBody(readFromBuffer(mContext, buffer, charset));
            } else {
                transaction.setResponseBodyIsPlainText(false);
            }
        }
        return transaction;
    }

    /**
     * Gets response transaction.
     *
     * @param mContext    the m context
     * @param transaction the transaction
     * @param response    the response
     * @throws IOException the io exception
     */
    public static void getResponseTransaction(Context mContext, HttpTransaction transaction, Response response) throws IOException {
        long tookMs = response.receivedResponseAtMillis() - response.sentRequestAtMillis();

        ResponseBody responseBody = response.body();

        transaction.setRequestHeaders(response.request().headers()); // includes headers added later in the chain
        transaction.setResponseDate(new Date());
        transaction.setTookMs(tookMs);
        transaction.setProtocol(response.protocol().toString());
        transaction.setResponseMessage(response.message());

        transaction.setResponseContentLength(responseBody.contentLength());
        if (responseBody.contentType() != null) {
            transaction.setResponseContentType(responseBody.contentType().toString());
        }
        transaction.setResponseHeaders(response.headers());

        transaction.setResponseBodyIsPlainText(!bodyHasUnsupportedEncoding(response.headers()));
        if (HttpHeaders.hasBody(response) && transaction.responseBodyIsPlainText()) {
            BufferedSource source = getNativeSource(maxContentLength, response);
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = HttpCacheInterceptorUtil.UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(HttpCacheInterceptorUtil.UTF8);
                } catch (UnsupportedCharsetException e) {
                    //update(transaction, transactionUri);
                }
            }
            if (isPlaintext(buffer)) {
                transaction.setResponseBody(readFromBuffer(mContext, buffer.clone(), charset));
            } else {
                transaction.setResponseBodyIsPlainText(false);
            }
            transaction.setResponseCode(getResponseBody(transaction.getResponseBody()));
            transaction.setResponseContentLength(buffer.size());
        }

    }

    private static int getResponseBody(String responseBody) {
        int responseCode = 0;
        try {
            if (!TextUtils.isEmpty(responseBody)) {
                responseCode=200;
                /*Status status = ParseUtility.getObject(responseBody, Status.class);
                if (null != status) {
                    responseCode = status.getStatus().getStatusCode();
                }*/
            }
        } finally {
            return responseCode;
        }
    }

    public static void showRequestLog(HttpTransaction transaction, HttpCacheInterceptor.Logger logger) {
        String requestStartMessage =
                "--> REQUEST START " + String
                        .valueOf(transaction.getUrl()) + ' ' + transaction.getMethod() + ' ' + HttpCacheInterceptor.protocol(transaction.getProtocol());
        requestStartMessage += "\n Request Body: " + transaction.getRequestBody() + "\n (" + transaction.getRequestContentLength() + "-byte body)";

        logger.log(requestStartMessage);
        List<HttpHeader> headers = transaction.getRequestHeaders();
        boolean isEncrypted = false;
        if (null != headers && !headers.isEmpty()) {
            logger.log("Headers START -->");
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.get(i).getName() + ": " + headers.get(i).getValue());
                if (headers.get(i).getValue().equalsIgnoreCase("y")) {
                    isEncrypted = true;
                }
            }
            logger.log("<-- Headers END");
        }
        if (isEncrypted) {
            try {
                //logger.log("Decrypted Request START -->" + AESSecurity.decrypt(transaction.getRequestBody()));
                //logger.log();
                logger.log("<-- Decrypted Request END");
            } catch (Exception e) {
                logger.log(Log.getStackTraceString(e));
            }
        }

        long tookMsRequest = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - transaction.getNetworkTime());
        String endMessage = "\nRequestTime:" + tookMsRequest + "ms";
        logger.log(endMessage);
        logger.log("--> REQUEST END ");
    }

    /**
     * @param maxContentLength
     * @param response
     * @return
     * @throws IOException
     */
    private static BufferedSource getNativeSource(long maxContentLength, Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(maxContentLength).source();
            if (source.buffer().size() < maxContentLength) {
                return getNativeSource(source, true);
            } else {
                Log.w(TAG, "gzip encoded response was too long");
            }
        }
        return response.body().source();
    }

    public static void clearHttpCacheDb(Context mContext) {
        try {
            mContext.getContentResolver().delete(HttpCacheContentProvider.TRANSACTION_URI, null, null);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
    }

    public static void showResponseLog(HttpTransaction response, boolean isFromCache, HttpCacheInterceptor.Logger logger, long startNwResponse) {
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNwResponse);
        final Charset UTF8 = Charset.forName("UTF-8");
        logger.log("<--" + (isFromCache ? "CACHE" : "") + " RESPONSE START " + response.getProtocol() + ' ' + response.getResponseCode() + ' '
                + response.getResponseBody() + "\n (" + response.getRequestContentLength() + "-byte body) ");

        List<HttpHeader> headers = response.getResponseHeaders();
        if (null != headers && !headers.isEmpty()) {
            logger.log("Headers START -->");
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.get(i).getName() + ": " + headers.get(i).getValue());
            }
            logger.log("<-- Headers END");
        }


        logger.log("ResponseTime:" + tookMs + "ms");
        logger.log("response Code >> " + response.getResponseCode());
        logger.log("response SuccessFul >> " + response.getResponseMessage());
        logger.log("<-- RESPONSE END ");

    }

    /**
     * The enum Period.
     */
    public enum Period {
        /**
         * Retain data for the last hour.
         */
        ONE_HOUR,
        /**
         * Retain data for the last day.
         */
        ONE_DAY,
        /**
         * Retain data for the last week.
         */
        ONE_WEEK,
        /**
         * Retain data forever.
         */
        FOREVER
    }
}
