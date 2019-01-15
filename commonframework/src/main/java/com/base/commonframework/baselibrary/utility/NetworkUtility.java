package com.base.commonframework.baselibrary.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

/**
 * Created by ankit on 28/3/17.
 */
public final class NetworkUtility {

    private NetworkUtility() {

    }

    /**
     * Is network available boolean.
     *
     * @param context the context
     * @return boolean boolean
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }



}
