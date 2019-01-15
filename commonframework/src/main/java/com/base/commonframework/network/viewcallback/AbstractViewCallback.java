package com.base.commonframework.network.viewcallback;


import com.base.commonframework.utility.CommonFrameworkUtil;

public abstract class AbstractViewCallback<T> extends IViewCallback<T> {

    @Override
    public void onFailure(Throwable e) {
        CommonFrameworkUtil.showException(e);

    }
}
