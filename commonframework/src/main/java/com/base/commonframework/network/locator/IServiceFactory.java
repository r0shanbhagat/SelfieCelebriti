package com.base.commonframework.network.locator;

import android.content.Context;

public interface IServiceFactory {
    <T> T getService(Context ctx, Class<T> serviceClass);
}
