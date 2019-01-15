package com.base.commonframework.baselibrary.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings.Secure;
import androidx.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public final class DeviceInfoUtility {

    private DeviceInfoUtility() {

    }

    /**
     * Gets device manufacturer.
     *
     * @return the device manufacturer
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER.replaceAll(" ", "");
    }

    /**
     * Gets device model.
     *
     * @return the device model
     */
    public static String getDeviceModel() {
        return Build.MODEL.replaceAll(" ", "");
    }

    /**
     * Gets device os.
     *
     * @return the device os
     */
    public static String getDeviceOS() {
        return String.valueOf("" + Build.VERSION.SDK_INT);
    }

    /**
     * Gets android device id.
     *
     * @param context the context
     * @return the android device id
     */
    public static String getAndroidDeviceID(@NonNull Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * Gets device platform.
     *
     * @param context the context
     * @return the device platform
     */
    public static String getDevicePlatform(@NonNull Context context) {
        return "Android";
    }

    /**
     * Gets device version.
     *
     * @param context the context
     * @return the device version
     * @throws Exception the name not found exception
     */
    public static String getDeviceVersion(@NonNull Context context) throws Exception {
        PackageInfo pInfo = null;
        String version = "";
        pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        version = pInfo.versionName;
        version = version.split(".r")[0];
        return version;
    }

    /**
     * Gets device id.
     *
     * @param context the context
     * @return the device id
     */
    @SuppressLint("HardwareIds")
    @SuppressWarnings("MissingPermission")
    public static String getDeviceID(@NonNull Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String result = "";
        boolean hasReadPhoneStatePermission = context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        if (hasReadPhoneStatePermission) {
            result = tm.getDeviceId();
        }
        if (TextUtils.isEmpty(result)) {
            result = getAndroidDeviceID(context);
        }
        return result;
    }



}
