package com.base.commonframework.baselibrary.network;

import android.text.TextUtils;

import com.base.commonframework.utility.CommonFrameworkUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Gautam Sharma on 16-01-2016.
 */
public class HttpInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = HttpInterceptor.class.getSimpleName();
    private final Logger logger;
    private final int RETRY_COUNT;
    private Map headerMap;
    private volatile Level level = Level.NONE;

    /**
     * Instantiates a new Http interceptor.
     */
    public HttpInterceptor() {
        this(0);
    }

    /**
     * Instantiates a new Http interceptor.
     *
     * @param retryCount the retry count
     */
    public HttpInterceptor(int retryCount) {
        this(Logger.DEFAULT, retryCount);
    }

    /**
     * Instantiates a new Http interceptor.
     *
     * @param logger     the logger
     * @param retryCount the retry count
     */
    public HttpInterceptor(Logger logger, int retryCount) {
        this.logger = logger;
        this.RETRY_COUNT = retryCount;
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }

    private static String requestPath(HttpUrl url) {
        String path = url.encodedPath();
        String query = url.encodedQuery();
        return query != null ? (path + '?' + query) : path;
    }

    /**
     * Change the level at which this interceptor logs.  @param level the level
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;
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
            request = builder.build();
        }
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        long startNsRequest = System.nanoTime();
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage =
                "--> REQUEST START " + String
                        .valueOf(request.url()) + ' ' + request.method() + ' ' /*+ requestPath(request.httpUrl()) + ' '*/ + protocol(protocol);
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.log(requestStartMessage);

        if (logHeaders) {
            Headers headers = request.headers();
            if (null != headers && headers.size() > 0) {
                logger.log("Headers START -->");
                for (int i = 0, count = headers.size(); i < count; i++) {
                    logger.log(headers.name(i) + ": " + headers.value(i));
                }
                logger.log("<-- Headers END");
            }

            String endMessage = "--> REQUEST END ";//+ request.method();
            if (logBody && hasRequestBody) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    contentType.charset(UTF8);
                }

                logger.log("");
                String headerValue = request.header("encrypt");
                String requestMessage = buffer.readString(charset);
                if (!TextUtils.isEmpty(headerValue) && headerValue.equalsIgnoreCase("y")) {
                    logger.log("Encrypted:" + requestMessage);
                   /* if (!TextUtils.isEmpty(requestMessage)) {
                        try {
                            requestMessage = AESSecurity.decrypt(requestMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/
                }
                logger.log(requestMessage);

                long tookMsRequest = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNsRequest);
                endMessage += " (" + requestBody.contentLength() + "-byte body) (" + tookMsRequest + "ms)";
            }
            logger.log(endMessage);
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        int retryCount = 0;
        while (!response.isSuccessful()) {
            if (RETRY_COUNT <= retryCount)
                break;
            retryCount++;
            logger.log("Retry " + retryCount);
            response = chain.proceed(request);
        }

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        String headerValue = response.header("encrypt");
        logger.log("<-- RESPONSE START " + protocol(response.protocol()) + ' ' + response.code() + ' '
                + response.message() + " (" + tookMs + "ms"
                + (!logHeaders ? ", " + responseBody.contentLength() + "-byte body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            if (null != headers) {
                logger.log("Headers START -->");
                for (int i = 0, count = headers.size(); i < count; i++) {
                    logger.log(headers.name(i) + ": " + headers.value(i));
                }
                logger.log("<-- Headers END");
            }

          /*  if(!TextUtils.isEmpty(headerValue) && headers.equals("y"))
            {
                Response response1= response.newBuilder()
                        .body(ResponseBody.create(
                                requestBody.contentType(),
                                requestBody.contentLength(),
                                Okio.buffer(responseBody.source())
                        ))
                        .build();

            }*/
            String endMessage = "<-- RESPONSE END ";
            if (logBody) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (responseBody.contentLength() != 0) {
                    logger.log("\n");
                    logger.log(buffer.clone().readString(charset));
                }

                logger.log("response Code >> " + response.code());
                logger.log("response SuccessFul >> " + response.isSuccessful());

                endMessage += " (" + buffer.size() + "-byte body)";
            }
            logger.log(endMessage);
        }
        return response;
    }


    public void setNetworkAuthorizationHeader(Map headerMap) {
        this.headerMap = headerMap;
    }


    /**
     * The enum Level.
     */
    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * RegisterRequest:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1 (3-byte body)
         *
         * <-- HTTP/1.1 200 OK (22ms, 6-byte body)
         * }*</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * RegisterRequest:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }*</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * RegisterRequest:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }*</pre>
         */
        BODY
    }

    /**
     * The interface Logger.
     */
    public interface Logger {
        /**
         * A {@link Logger} defaults output appropriate for the current platform.
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
