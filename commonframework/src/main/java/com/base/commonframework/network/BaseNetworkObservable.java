package com.base.commonframework.network;


import com.base.commonframework.utility.CommonFrameworkUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseNetworkObservable<T> implements Observer<T> {
    private static final String TAG = BaseNetworkObservable.class.getSimpleName();

    @Override
    public void onSubscribe(Disposable d) {
        //  Log.i("TAG", "onSubscribe");
    }

    @Override
    public void onNext(T t) {
        if (null != t) {
            success(t);
        } else {
            failure(new RuntimeException("Unexpected response " + t));
        }
    }

    @Override
    public void onError(Throwable t) {
        failure(t);
        CommonFrameworkUtil.showException(TAG, t);
    }

    @Override
    public void onComplete() {
        //Log.i("TAG", "onComplete");

    }

    public abstract void success(T response);

    public abstract void failure(Throwable t);
}
