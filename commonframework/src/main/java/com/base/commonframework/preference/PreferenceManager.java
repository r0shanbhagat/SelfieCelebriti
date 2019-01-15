package com.base.commonframework.preference;

import android.content.Context;

import com.base.commonframework.baselibrary.utility.AbstractPrefUtility;

/**
 * CommonPreferenceManager Class :
 *
 * @author Roshan Kumar
 * @version 3.0
 * @since 1/12/18
 * <p>
 * Kindly put the data carefully in this class as we are not going to clear this preference file even on #Logout.
 */
public class PreferenceManager extends AbstractPrefUtility {
    private static final String APP_PERSISTENT_PREF_FILE = "app_persistent.xml";
    private static final int APP_PREF_VER = 1;

    private static final String PREF_GRADLE_PLATFORM = "gradle_platform";
    private static final String PREF_DEVICE_MANUFACTURER = "pref_device_manufacturer";
    private static final String PREF_DEVICE_MODEL = "pref_device_model";
    private static final String PREF_DEVICE_ID = "pref_device_id";
    private static final String PREF_OS_VERSION = "pref_os_version";
    private static final String PREF_VERSION_CODE = "pref_version_code";
    private static final String PREF_VERSION_NAME = "pref_version_name";
    private static final String LANGUAGE_CODE = "language_code";
    private static final String PREF_GRADLE_BASE_URL = "gradle_base_url";
    private static final String PREF_GRADLE_COUNTRY_CODE = "gradle_country_code";
    private static final String PREF_FlURRY_KEY = "gradle_flurry_key";

    private static PreferenceManager preferenceManager;

    public PreferenceManager(Context ctx, Builder build) {
        super(ctx, build);

    }

    public static synchronized PreferenceManager getInstance(Context ctx) {
        if (null == preferenceManager) {
            preferenceManager = new PreferenceManager(ctx, new AbstractPrefUtility.Builder()
                    .setAppPrefFile(APP_PERSISTENT_PREF_FILE)
                    .setAppPrefVersion(APP_PREF_VER)
                    .build());
        }
        return preferenceManager;
    }


    public String getPlatform() {
        return getStringPreference(PREF_GRADLE_PLATFORM, "android");
    }

    public void setPlatform(String platform) {
        setStringPreference(PREF_GRADLE_PLATFORM, platform);
    }

    public String getDeviceManufacturer() {
        return getStringPreference(PREF_DEVICE_MANUFACTURER, "");
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        setStringPreference(PREF_DEVICE_MANUFACTURER, deviceManufacturer);
    }


    public String getDeviceModel() {
        return getStringPreference(PREF_DEVICE_MODEL, "");

    }

    public void setDeviceModel(String deviceModel) {
        setStringPreference(PREF_DEVICE_MODEL, deviceModel);

    }

    public String getDeviceId() {
        return getStringPreference(PREF_DEVICE_ID, "");

    }

    public void setDeviceId(String deviceId) {
        setStringPreference(PREF_DEVICE_ID, deviceId);

    }

    public String getOsVersion() {
        return getStringPreference(PREF_OS_VERSION, "");

    }

    public void setOsVersion(String osVersion) {
        setStringPreference(PREF_OS_VERSION, osVersion);

    }

    public int getAppVersionCode() {
        return getIntPreference(PREF_VERSION_CODE, 0);
    }

    public void setAppVersionCode(int versionNumber) {
        setIntPreference(PREF_VERSION_CODE, versionNumber);
    }

    public String getAppVersionName() {
        return getStringPreference(PREF_VERSION_NAME, "");
    }

    public void setAppVersionName(String versionName) {
        setStringPreference(PREF_VERSION_NAME, versionName);
    }


    public String getCountryCode() {
        return getStringPreference(PREF_GRADLE_COUNTRY_CODE, "in");
    }

    public void setCountryCode(String countryCode) {
        setStringPreference(PREF_GRADLE_COUNTRY_CODE, countryCode);
    }

    public String getLanguageCode() {
        return getStringPreference(LANGUAGE_CODE, "en");
    }

    public void setLanguageCode(String languageCode) {
        setStringPreference(LANGUAGE_CODE, languageCode);
    }

    public String getBaseUrl() {
        return getStringPreference(PREF_GRADLE_BASE_URL, "");
    }

    public void setBaseUrl(String baseUrl) {
        setStringPreference(PREF_GRADLE_BASE_URL, baseUrl);
    }


    public String getFlurryApiKey() {
        return getStringPreference(PREF_FlURRY_KEY, "");
    }

    public void setFlurryApiKey(String flurryApiKey) {
        setStringPreference(PREF_FlURRY_KEY, flurryApiKey);
    }
}
