package com.base.commonframework.baselibrary.utility;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.util.Set;


public abstract class AbstractPrefUtility {
    private static final String VERSION = "__prefs_version__";
    private static String APP_PREF_FILE = "";
    private static int APP_PREF_VERSION = 1;
    private final Context mContext;
    private SharedPreferences sharedPreferences;

    protected AbstractPrefUtility(Context context, Builder builder) {
        this.mContext = context.getApplicationContext();
        // APP_PREF_FILE = mContext.getString(R.string.ABS_PREF_FILE);
        setPrefConfig(builder);
        if (null == sharedPreferences) {
            sharedPreferences = context.getSharedPreferences(APP_PREF_FILE, Context.MODE_PRIVATE);
        }

        init(APP_PREF_VERSION);
    }


    public static void setPrefConfig(Builder builder) {
        APP_PREF_FILE = builder.appPrefFile;
        APP_PREF_VERSION = builder.appPrefVersion;
    }


    protected Context getContext() {
        return mContext;
    }

    private void init(int newVersion) {
        int oldVersion = sharedPreferences.getInt(VERSION, -1);
        if (oldVersion != newVersion) {
            onUpgrade();
            setIntPreference(VERSION, newVersion);
        }
    }

    private void onUpgrade() {
        sharedPreferences.edit().clear().apply();
    }


    /**
     * save int
     *
     * @param key   the key
     * @param value the value
     */
    protected void setIntPreference(@NonNull String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * get int
     *
     * @param key the key
     * @return int shared preferences int
     */
    protected int getIntPreference(@NonNull String key) {
        return sharedPreferences.getInt(key, -1);
    }

    /**
     * Gets shared preferences int.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the shared preferences int
     */
    protected int getIntPreference(@NonNull String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * set boolean
     *
     * @param key   the key
     * @param value the value
     */
    protected void setBooleanPreference(@NonNull String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * get boolean
     *
     * @param key the key
     * @return boolean shared preferences boolean
     */
    protected boolean getBooleanPreference(@NonNull String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    protected boolean getBooleanPreference(@NonNull String key, boolean defaultvalue) {
        return sharedPreferences.getBoolean(key, defaultvalue);
    }

    /**
     * set String
     *
     * @param key   the key
     * @param value the value
     */
    protected void setStringPreference(@NonNull String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * get String
     *
     * @param key the key
     * @return string shared preferences string
     */
    protected String getStringPreference(@NonNull String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * get String
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return string shared preferences string
     */
    protected String getStringPreference(@NonNull String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * set long
     *
     * @param key   the key
     * @param value the value
     */
    protected void setLongPreference(@NonNull String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * get long
     *
     * @param key the key
     * @return long shared preferences long
     */
    protected long getLongPreference(@NonNull String key) {
        return sharedPreferences.getLong(key, -1);
    }

    protected long getLongPreference(@NonNull String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * set float
     *
     * @param key   the key
     * @param value the value
     */
    protected void setFloatPreference(@NonNull String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * get float
     *
     * @param key the key
     * @return float shared preferences float
     */
    protected float getFloatPreference(@NonNull String key) {
        return sharedPreferences.getFloat(key, -1);
    }

    /**
     * set String
     *
     * @param key   the key
     * @param value the value
     */
    protected void setStingSetPreference(@NonNull String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * get String
     *
     * @param key the key
     * @return set of string
     */
    protected Set<String> getStringSetPreference(@NonNull String key) {
        return sharedPreferences.getStringSet(key, null);
    }


    /**
     * remove all shared preferences
     */
    public boolean clearPreferences() {
        return sharedPreferences.edit().clear().commit();
    }

    public static class Builder {
        private int appPrefVersion = 1;
        private String appPrefFile = "";

        public Builder setAppPrefFile(String appPrefFile) {
            this.appPrefFile = appPrefFile;
            return this;
        }

        public Builder setAppPrefVersion(int appPrefVersion) {
            this.appPrefVersion = appPrefVersion;
            return this;
        }


        public Builder build() {
            if (TextUtils.isEmpty(appPrefFile)) {
                throw new NullPointerException("Base url empty");
            }
            if (appPrefVersion <= 0) {
                appPrefVersion = 1;
            }
            setPrefConfig(this);
            return this;
        }

    }

}
