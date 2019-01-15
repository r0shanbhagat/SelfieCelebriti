package com.selfie.star.network.servicelocator;

import android.content.Context;

import com.base.commonframework.network.locator.IServiceFactory;
import com.base.commonframework.network.locator.IServiceLocator;
import com.selfie.star.network.VgServiceFactory;


public class ServiceLocator implements IServiceLocator {
    private Context mContext;
    private IServiceFactory serviceFactory;


    public ServiceLocator(Context ctx) {
        mContext = ctx;
        serviceFactory = new VgServiceFactory();
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return serviceFactory.getService(mContext, serviceClass);
    }
}
