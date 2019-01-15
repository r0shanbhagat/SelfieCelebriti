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
package com.base.commonframework.baselibrary.network.httpcache.internal.support;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.LongSparseArray;

import com.base.commonframework.R;
import com.base.commonframework.baselibrary.network.httpcache.HttpCache;
import com.base.commonframework.baselibrary.network.httpcache.internal.data.HttpTransaction;
import com.base.commonframework.baselibrary.network.httpcache.internal.ui.BaseHttpCacheActivity;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class NotificationHelper {

    private static final int NOTIFICATION_ID = 1138;
    private static final int BUFFER_SIZE = 10;

    private static final LongSparseArray<HttpTransaction> transactionBuffer = new LongSparseArray<>();
    private static int transactionCount;

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static synchronized void clearBuffer() {
        transactionBuffer.clear();
        transactionCount = 0;
    }

    private static synchronized void addToBuffer(HttpTransaction transaction) {
        if (transaction.getStatus() == HttpTransaction.Status.Requested) {
            transactionCount++;
        }
        transactionBuffer.put(transaction.getId(), transaction);
        if (transactionBuffer.size() > BUFFER_SIZE) {
            transactionBuffer.removeAt(0);
        }
    }

    public synchronized void show(HttpTransaction transaction) {
        addToBuffer(transaction);
        if (!BaseHttpCacheActivity.isInForeground()) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setContentIntent(PendingIntent.getActivity(context, 0, HttpCache.getLaunchIntent(context), 0))
                    .setLocalOnly(true)
                    .setSmallIcon(R.drawable.httpcache_ic_notification_white_24dp)
                    .setColor(ContextCompat.getColor(context, R.color.httpCacheColorPrimary))
                    .setContentTitle(context.getString(R.string.httpCacheNotificationTitle));
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            int count = 0;
            for (int i = transactionBuffer.size() - 1; i >= 0; i--) {
                if (count < BUFFER_SIZE) {
                    if (count == 0) {
                        mBuilder.setContentText(transactionBuffer.valueAt(i).getNotificationText());
                    }
                    inboxStyle.addLine(transactionBuffer.valueAt(i).getNotificationText());
                }
                count++;
            }
            mBuilder.setAutoCancel(true);
            mBuilder.setStyle(inboxStyle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBuilder.setSubText(String.valueOf(transactionCount));
            } else {
                mBuilder.setNumber(transactionCount);
            }
            mBuilder.addAction(getClearAction());
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    @NonNull
    private NotificationCompat.Action getClearAction() {
        CharSequence clearTitle = context.getString(R.string.httpCacheClear);
        Intent deleteIntent = new Intent(context, ClearTransactionsService.class);
        PendingIntent intent = PendingIntent.getService(context, 11, deleteIntent, PendingIntent.FLAG_ONE_SHOT);
        return new NotificationCompat.Action(R.drawable.httpcache_ic_delete_white_24dp,
                clearTitle, intent);
    }

    public void dismiss() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
