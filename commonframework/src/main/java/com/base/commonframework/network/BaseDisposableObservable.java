package com.base.commonframework.network;


import io.reactivex.observers.DisposableObserver;

public abstract class BaseDisposableObservable<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        if (null != t) {
            success(t);
        } else {
            failure(new RuntimeException("Unexpected response " + t));
        }
    }

    @Override
    public void onError(Throwable e) {
        failure(e);
    }

    @Override
    public void onComplete() {

    }


    public abstract void success(T response);

    public abstract void failure(Throwable t);


}