package com.base.commonframework.baselibrary.network.httpcache.internal.support;

import android.app.IntentService;
import android.content.Intent;

import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpCacheContentProvider;

import androidx.annotation.Nullable;

public class ClearTransactionsService extends IntentService {

    public ClearTransactionsService() {
        super("ClearTransactionsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getContentResolver().delete(HttpCacheContentProvider.TRANSACTION_URI, null, null);
        NotificationHelper.clearBuffer();
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.dismiss();
    }
}