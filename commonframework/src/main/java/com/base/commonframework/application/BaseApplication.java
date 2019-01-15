package com.base.commonframework.application;

import android.content.Context;
import android.os.Process;

import com.base.commonframework.BuildConfig;
import com.base.commonframework.R;
import com.base.commonframework.baselibrary.utility.AbstractPrefUtility;
import com.base.commonframework.baselibrary.utility.DeviceInfoUtility;
import com.base.commonframework.network.locator.IServiceLocator;
import com.base.commonframework.preference.PreferenceManager;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.girnarsoft.tracking.AnalyticsManager;
import com.girnarsoft.tracking.IAnalyticsManager;
import com.girnarsoft.tracking.config.TrackerConfiguration;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import io.fabric.sdk.android.Fabric;


public abstract class BaseApplication extends MultiDexApplication {
    private static BaseApplication abstractApplication;
    private IAnalyticsManager analyticsManager;
    private static PreferenceManager preferenceManager;

    public static BaseApplication getInstance() {
        return BaseApplication.abstractApplication;
    }

    public static PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        abstractApplication = this;
        onPreCreate();
        super.onCreate();
        // ViewTarget.setTagId(R.id.glide_tag);
        isApplicationInstalled();
        init();
        onPostCreate();
    }

    protected void init() {
        configPreference();
        initAnalytics();
        setBasePreference();
    }

    protected void onPostCreate() {
        //do nothing
    }

    protected void onPreCreate() {
        abstractApplication = this;

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * Checks if application installed properly or not.
     */
    private void isApplicationInstalled() {
        if (getResources() == null) {
            Process.killProcess(Process.myPid());
        }
    }

    private void configPreference() {
        AbstractPrefUtility.setPrefConfig(new AbstractPrefUtility.Builder()
                .setAppPrefFile(CommonFrameworkUtil.APP_PREF_FILE)
                .setAppPrefVersion(CommonFrameworkUtil.APP_PREF_VER).build());
        preferenceManager = PreferenceManager.getInstance(this);

    }
    private void initAnalytics() {
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);
        analyticsManager = new AnalyticsManager(this);
        analyticsManager.configure(getTrackerConfiguration());
    }

    private void setBasePreference() {
        getPreferenceManager().setPlatform(getString(R.string.platform));
        getPreferenceManager().setLanguageCode(getString(R.string.languageCode));
        getPreferenceManager().setCountryCode(getString(R.string.countryCode));
        getPreferenceManager().setDeviceId(DeviceInfoUtility.getAndroidDeviceID(this));
        getPreferenceManager().setDeviceManufacturer(DeviceInfoUtility.getDeviceManufacturer());
        getPreferenceManager().setDeviceModel(DeviceInfoUtility.getDeviceModel());
        getPreferenceManager().setOsVersion(DeviceInfoUtility.getDeviceOS());


        /******SETTER OF KEY FILE****/
        getPreferenceManager().setFlurryApiKey(getString(R.string.FLURRY_API_KEY));

    }



    public IAnalyticsManager getAnalyticsManager() {
        return analyticsManager;
    }

    public TrackerConfiguration getTrackerConfiguration() {
        return new TrackerConfiguration.Builder()
                .withDebugMode(true)
                .withDeviceId(DeviceInfoUtility.getDeviceID(getApplicationContext()))
                .build();
    }


    public abstract IServiceLocator getLocator();
}
