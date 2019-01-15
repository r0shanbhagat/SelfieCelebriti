package com.selfie.star.network;

import android.content.Context;

import com.base.commonframework.network.locator.IServiceFactory;
import com.selfie.star.activity.service.MainService;

public class VgServiceFactory implements IServiceFactory {

    @Override
    public <T> T getService(Context context, Class<T> serviceClass) {
        String serviceName = serviceClass.getSimpleName();
        Object service = null;
        switch (serviceName) {
            case "IMainService":
                service = new MainService(context);
                break;
        }
        return serviceClass.cast(service);
    }
}
