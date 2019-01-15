package com.base.commonframework.network.viewcallback;


/**
 * This callback is used to send the message to the view from network with underlying view model, which is required to show the data on
 * view.
 *
 * @param <T> T must be extends with IViewModel, which is further used by the views to show the content.
 */
public abstract class IViewCallback<T> {
    /**
     * Called when network call get response as a success.
     *
     * @param t view model object for the view
     */
    public abstract void onSuccess(T t);

    /**
     * * Called when network call get response as a failure.
     *
     * @param e throwable object
     */
    public abstract void onFailure(Throwable e);

    public void checkAndUpdate(T t) {
        if (isLive()) {
            onSuccess(t);
        }
    }

    public abstract boolean isLive();
}
