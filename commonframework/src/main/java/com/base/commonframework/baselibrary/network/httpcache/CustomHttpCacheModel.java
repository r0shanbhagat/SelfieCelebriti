package com.base.commonframework.baselibrary.network.httpcache;

import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpTransaction;

/**
 * Created by roshan on 18/4/17.
 */

public class CustomHttpCacheModel {
    private boolean status;
    private HttpTransaction httpTransaction;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public HttpTransaction getHttpTransaction() {
        return httpTransaction;
    }

    public void setHttpTransaction(HttpTransaction httpTransaction) {
        this.httpTransaction = httpTransaction;
    }
}
