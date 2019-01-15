package com.selfie.star.application;


import com.base.commonframework.application.BaseApplication;
import com.base.commonframework.network.locator.IServiceLocator;
import com.selfie.star.BuildConfig;
import com.selfie.star.R;
import com.selfie.star.activity.adapter.BannerImageLoadingService;
import com.selfie.star.navigation.IntentHelper;
import com.selfie.star.network.servicelocator.ServiceLocator;

import ss.com.bannerslider.Slider;

public class MainApplication extends BaseApplication {

    private static MainApplication vgApplication;
    private static IntentHelper intentHelper;

    public static MainApplication getInstance() {
        return vgApplication;
    }

    @Override
    protected void onPostCreate() {
        super.onPostCreate();
        vgApplication = this;
        getPreferenceManager().setAppVersionCode(BuildConfig.VERSION_CODE);
        getPreferenceManager().setAppVersionName(BuildConfig.VERSION_NAME);
        getPreferenceManager().setBaseUrl(getResources().getString(R.string.baseUrl));
        Slider.init(new BannerImageLoadingService(getApplicationContext()));

    }

    @Override
    public IServiceLocator getLocator() {
        return new ServiceLocator(this);
    }


    public IntentHelper getIntentHelper() {
        if (null == intentHelper) {
            intentHelper = new IntentHelper();
        }
        return intentHelper;
    }

}
