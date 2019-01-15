/*
 * Copyright (C) 2017 Jeff Gilfelt.
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
package com.base.commonframework.baselibrary.network.httpcache.internal.data;

import android.net.Uri;

import com.base.commonframework.baselibrary.network.httpcache.internal.support.FormatUtils;
import com.base.commonframework.baselibrary.network.httpcache.internal.support.JsonConvertor;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.qbusict.cupboard.annotation.Index;
import okhttp3.Headers;

public class HttpTransaction {

    public static final String[] PARTIAL_PROJECTION = new String[]{
            "_id",
            "requestDate",
            "tookMs",
            "method",
            "host",
            "path",
            "scheme",
            "requestContentLength",
            "responseCode",
            "responseHeaders",
            "error",
            "responseContentLength"
    };
    private static final SimpleDateFormat TIME_ONLY_FMT = new SimpleDateFormat("HH:mm:ss", Locale.US);
    private Long _id;
    @Index
    private Date requestDate;
    private Long tookMs;

    private String protocol;
    private String method;
    private String url;
    private String host;
    private String path;
    private String scheme;

    private String requestHeaders;
    private Long requestContentLength;
    private String requestContentType;
    private String requestBody;
    private Boolean requestBodyIsPlainText = true;

    @Index
    private Date responseDate;
    private Integer responseCode;
    private String responseMessage;
    private String error;
    private Long responseContentLength;
    private String responseContentType;
    private String responseHeaders;
    private String responseBody;
    private Boolean responseBodyIsPlainText = true;
    private Long networkTime;


    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getFormattedRequestBody() {
        return formatBody(requestBody, requestContentType);
    }

    public boolean requestBodyIsPlainText() {
        return requestBodyIsPlainText;
    }

    public void setRequestBodyIsPlainText(boolean requestBodyIsPlainText) {
        this.requestBodyIsPlainText = requestBodyIsPlainText;
    }

    public Long getRequestContentLength() {
        return requestContentLength;
    }

    public void setRequestContentLength(Long requestContentLength) {
        this.requestContentLength = requestContentLength;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getFormattedResponseBody() {
        return formatBody(responseBody, responseContentType);
    }

    public boolean responseBodyIsPlainText() {
        return responseBodyIsPlainText;
    }

    public void setResponseBodyIsPlainText(boolean responseBodyIsPlainText) {
        this.responseBodyIsPlainText = responseBodyIsPlainText;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Long getResponseContentLength() {
        return responseContentLength;
    }

    public void setResponseContentLength(Long responseContentLength) {
        this.responseContentLength = responseContentLength;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Long getTookMs() {
        return tookMs;
    }

    public void setTookMs(Long tookMs) {
        this.tookMs = tookMs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        Uri uri = Uri.parse(url);
        host = uri.getHost();
        path = uri.getPath() + ((uri.getQuery() != null) ? "?" + uri.getQuery() : "");
        scheme = uri.getScheme();
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getScheme() {
        return scheme;
    }

    public void setRequestHeaders(Headers headers) {
        setRequestHeaders(toHttpHeaderList(headers));
    }

    public List<HttpHeader> getRequestHeaders() {
        return JsonConvertor.getInstance().fromJson(requestHeaders,
                new TypeToken<List<HttpHeader>>() {
                }.getType());
    }

    public void setRequestHeaders(List<HttpHeader> headers) {
        requestHeaders = JsonConvertor.getInstance().toJson(headers);
    }

    public String getRequestHeadersString(boolean withMarkup) {
        return FormatUtils.formatHeaders(getRequestHeaders(), withMarkup);
    }

    public void setResponseHeaders(Headers headers) {
        setResponseHeaders(toHttpHeaderList(headers));
    }

    public void setResponseHeaders(List<HttpHeader> headers) {
        responseHeaders = JsonConvertor.getInstance().toJson(headers);
    }

    public List<HttpHeader> getResponseHeaders() {
        return JsonConvertor.getInstance().fromJson(responseHeaders,
                new TypeToken<List<HttpHeader>>() {
                }.getType());
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponseHeadersString(boolean withMarkup) {
        return FormatUtils.formatHeaders(getResponseHeaders(), withMarkup);
    }

    public Status getStatus() {
        if (error != null) {
            return Status.Failed;
        } else if (responseCode == null) {
            return Status.Requested;
        } else {
            return Status.Complete;
        }
    }

    public String getRequestStartTimeString() {
        return (requestDate != null) ? TIME_ONLY_FMT.format(requestDate) : null;
    }

    public String getRequestDateString() {
        return (requestDate != null) ? requestDate.toString() : null;
    }

    public String getResponseDateString() {
        return (responseDate != null) ? responseDate.toString() : null;
    }

    public String getDurationString() {
        return tookMs + " ms";
    }

    public String getRequestSizeString() {
        return formatBytes(requestContentLength);
    }

    public String getResponseSizeString() {
        return formatBytes(responseContentLength);
    }

    public String getTotalSizeString() {
        Long reqBytes = requestContentLength;
        Long resBytes = responseContentLength;
        return formatBytes(reqBytes + resBytes);
    }

    public String getResponseSummaryText() {
        switch (getStatus()) {
            case Failed:
                return error;
            case Requested:
                return null;
            default:
                return String.valueOf(responseCode) + " " + responseMessage;
        }
    }

    public String getNotificationText() {
        switch (getStatus()) {
            case Failed:
                return " ! ! !  " + path;
            case Requested:
                return " . . .  " + path;
            default:
                return String.valueOf(responseCode) + " " + path;
        }
    }

    public boolean isSsl() {
        return scheme.toLowerCase().equals("https");
    }

    private List<HttpHeader> toHttpHeaderList(Headers headers) {
        List<HttpHeader> httpHeaders = new ArrayList<>();
        for (int i = 0, count = headers.size(); i < count; i++) {
            httpHeaders.add(new HttpHeader(headers.name(i), headers.value(i)));
        }
        return httpHeaders;
    }

    private String formatBody(String body, String contentType) {
        if (contentType != null && contentType.toLowerCase().contains("json")) {
            return FormatUtils.formatJson(body);
        } else if (contentType != null && contentType.toLowerCase().contains("xml")) {
            return FormatUtils.formatXml(body);
        } else {
            return body;
        }
    }

    private String formatBytes(Long bytes) {
        return FormatUtils.formatByteCount(bytes, true);
    }

    public Long getNetworkTime() {
        return networkTime;
    }

    public void setNetworkTime(Long networkTime) {
        this.networkTime = networkTime;
    }

    public enum Status {
        Requested,
        Complete,
        Failed
    }
}
