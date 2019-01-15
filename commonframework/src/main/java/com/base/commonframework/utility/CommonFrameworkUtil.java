package com.base.commonframework.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.base.commonframework.BuildConfig;
import com.base.commonframework.R;
import com.base.commonframework.activity.BaseActivity;
import com.base.commonframework.application.BaseApplication;
import com.base.commonframework.baselibrary.utility.ToastUtility;
import com.base.commonframework.network.communication.beans.BaseStatusModel;
import com.base.commonframework.preference.PreferenceManager;
import com.girnarsoft.tracking.event.EventInfo;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * CommonFrameworkUtil Class :
 *
 * @author Roshan Kumar
 * @version 3.0
 * @since 19/12/18
 */
public class CommonFrameworkUtil {
    public static final String TAG = CommonFrameworkUtil.class.getSimpleName();
    public static final String APP_PREF_FILE = "vahan_gyan.xml";
    public static final int APP_PREF_VER = 1;
    private static boolean isShowLog = BuildConfig.DEBUG;

    private CommonFrameworkUtil() {
        //not called
    }


    public static void shareThroughNative(Context context, String text, String subject, String shareVia) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            shareIntent.putExtra(Intent.EXTRA_TEXT, URLDecoder.decode(text, "utf-8"));
            context.startActivity(Intent.createChooser(shareIntent, shareVia));
        } catch (Exception e) {
            showException(e);
        }
    }


    public static boolean isActivityRunning(Context mContext, Class activityClass) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName().equalsIgnoreCase(activityClass.getPackage().getName() + "." + activityClass.getSimpleName());

    }

    public static boolean isActivityRunning(Context mContext, Intent intentClass) {
        boolean status = false;
        try {
            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            status = componentInfo.getClassName().equalsIgnoreCase(intentClass.getComponent().getClassName());
        } catch (Exception e) {
            showException(e);
        } finally {
            return status;
        }
    }

    /**
     * @param mParamsData
     * @return
     */
    public static Map<String, Map> getRequestParamMap(@NonNull Map<String, Object> mParamsData) {
        Map<String, Map> mParamsRequest = new HashMap<>();
        mParamsRequest.put("info", getInfoRequestParamMap(""));
        mParamsRequest.put("data", mParamsData);
        return mParamsRequest;
    }


    public static Map<String, Object> getInfoRequestParamMap(String eCode) {
        HashMap<String, Object> mParamsInfo = new HashMap<>();
        PreferenceManager preferenceManager = BaseApplication.getPreferenceManager();
        mParamsInfo.put("deviceId", preferenceManager.getDeviceId());
        mParamsInfo.put("deviceManufacturer", preferenceManager.getDeviceManufacturer());
        mParamsInfo.put("deviceModel", preferenceManager.getDeviceModel());
        mParamsInfo.put("osVersion", preferenceManager.getOsVersion());
        mParamsInfo.put("platform", preferenceManager.getPlatform());
        mParamsInfo.put("appVersion", preferenceManager.getAppVersionName());
        mParamsInfo.put("appVersionCode", preferenceManager.getAppVersionCode());
        return mParamsInfo;
    }


    public static boolean isSuccessResponse(int status) {
        return status == CommonFrameworkConstant.StatusCode.ShowSeries.SUCCESS
                || status == CommonFrameworkConstant.StatusCode.NotShowSeries.SUCCESS;
    }


    public static Drawable getVectorDrawable(Context context, int drawableResId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            drawable = context.getResources().getDrawable(drawableResId, context.getTheme());
        } else {
            drawable = VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
        }
        return drawable;
    }


    public static void showStatusMessage(Context context, BaseStatusModel status) {
        if (null != status) {
            if (!(status.getStatusCode() == CommonFrameworkConstant.StatusCode.NotShowSeries.SUCCESS
                    || status.getStatusCode() == CommonFrameworkConstant.StatusCode.NotShowSeries.FAILURE)) {
                String statusMessage = status.getStatusMessage();
                if (TextUtils.isEmpty(statusMessage)) {
                    statusMessage = context.getResources().getString(R.string.error_msg);
                }
                ToastUtility.showToastMessage(context, statusMessage);
            } else {
                showLog("NotShowSeries", "Message:" + status.getStatusMessage() + "  Code" + status.getStatusCode());
            }
        }
    }

    public static void showDefaultMessage(Context context, String toastMessage) {
        if (TextUtils.isEmpty(toastMessage)) {
            toastMessage = context.getResources().getString(R.string.error_msg);
        }
        ToastUtility.showToastMessage(context, toastMessage);
    }

    /**
     * Show log. You can disable Logs by setting "isShowLog" value to False
     *
     * @param tagName the tag name
     * @param message the message
     */
    public static void showLog(String tagName, String message) {
        if (isShowLog && !TextUtils.isEmpty(message)) {
            int maxLogSize = 1000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.v(tagName, message.substring(start, end));
            }
        }

    }



    public static void shareImage(Context context, @NonNull String imagePath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Uri photoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(imagePath));
        share.putExtra(Intent.EXTRA_STREAM, photoUri);
        context.startActivity(Intent.createChooser(share, "Share Image"));
    }


    /**
     * Show log. You can disable Logs by setting "isShowLog" value to False
     *
     * @param message the message
     */
    public static void showLog(String message) {
        if (isShowLog && !TextUtils.isEmpty(message)) {
            int maxLogSize = 1000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.v("", message.substring(start, end));
            }
        }

    }

    @Nullable
    public static String getClassifiedScreenName(String screenName) {
        if (!TextUtils.isEmpty(screenName) && (screenName.contains("Activity") || screenName.contains("Fragment"))) {
            screenName = screenName.replaceAll("Activity", "Screen");
            screenName = screenName.replaceAll("Fragment", "Screen");
        }
        return screenName;
    }

    /**
     * Show exception.You can disable Logs by setting "isShowLog" value to False
     * <p>
     * * @param tagName     the tag name
     *
     * @param tagName the tag name
     * @param t       the t
     */
    public static void showException(String tagName, Throwable t) {
        if (isShowLog) {
            Log.e(tagName, Log.getStackTraceString(t));
        }
    }

    /**
     * Show exception.You can disable Logs by setting "isShowLog" value to False
     * <p>
     * * @param tagName     the tag name
     *
     * @param t the t
     */
    public static void showException(Throwable t) {
        if (isShowLog) {
            Log.e("", Log.getStackTraceString(t));
        }
    }

    /**
     * Gets active fragment.
     *
     * @param abstractAppCompactActivity the abstract base activity
     * @return active fragment
     */
    public static Fragment getActiveFragment(@NonNull BaseActivity abstractAppCompactActivity) {
        if (abstractAppCompactActivity.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = abstractAppCompactActivity.getSupportFragmentManager().getBackStackEntryAt(abstractAppCompactActivity.getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return abstractAppCompactActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }


    /**
     * Open play store or browser.
     *
     * @param mContext the m context
     * @param Url      the url
     */
    public static void openPlayStoreOrBrowser(Context mContext, String Url) {
        String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Url)));//"market://details?id=" + appPackageName)
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Url)));//"http://play.google.com/store/apps/details?id=" + appPackageName
        }
    }

    /**
     * Sets dialog height width.
     *
     * @param context the context
     * @param dialog  the dialog
     */
    public static void setDialogHeightWidth(@NonNull Context context, @NonNull Dialog dialog) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //   int height = metrics.heightPixels;
        if (null != dialog.getWindow()) {
            dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * Sets recycler basic properties.
     *
     * @param context      the context
     * @param recyclerView the recycler view
     */
    public static void setRecyclerBasicProperties(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

    }

    public static void setRecyclerGridProperties(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

    }


    public static EventInfo.Builder getEventTrackEventInfo() {
        return new EventInfo.Builder()
                .withPlatform(BaseApplication.getPreferenceManager().getPlatform())
                .withCountryCode(BaseApplication.getPreferenceManager().getCountryCode())
                .withLanguageCode(BaseApplication.getPreferenceManager().getLanguageCode());
    }


    /**
     * Gets camel caps name.
     *
     * @param name the name
     * @return camel caps name
     */
    public static String getCamelCapsName(@NonNull String name) {
        StringBuilder camelCapsName = new StringBuilder();
        String[] nameArray = name.split(" ");
        for (int i = 0; i < nameArray.length; i++) {
            camelCapsName.append(getModifiedName(nameArray[i])).append(" ");
        }
        return camelCapsName.toString().trim();
    }


    /**
     * Sets full screen dialog.
     *
     * @param context the context
     * @param dialog  the dialog
     */
    public static void setFullScreenDialog(Context context, Dialog dialog) {
        WindowManager manager = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
        int width, height;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y;
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        dialog.getWindow().setAttributes(lp);

    }


    /**
     * Gets modified name.
     *
     * @param name the name
     * @return modified name
     */
    private static String getModifiedName(@NonNull String name) {
        if (!TextUtils.isEmpty(name)) {
            String firstLetterWord = name.substring(0, 1);
            String afterFirstLetterWord = name.substring(1);
            StringBuilder modifiedName = new StringBuilder();
            modifiedName.append(firstLetterWord.toUpperCase()).append(afterFirstLetterWord.toLowerCase());
            return modifiedName.toString();
        }
        return name;
    }


}
