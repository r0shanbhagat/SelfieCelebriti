package com.selfie.star.activity.service;

import android.content.Context;

import com.base.commonframework.application.BaseApplication;
import com.base.commonframework.baselibrary.dialog.LoadingDialog;
import com.base.commonframework.baselibrary.network.ServiceGenerator;
import com.base.commonframework.baselibrary.utility.ToastUtility;
import com.base.commonframework.network.BaseNetworkObservable;
import com.base.commonframework.network.viewcallback.IViewCallback;
import com.selfie.star.R;
import com.selfie.star.activity.model.CategoryResponseData;
import com.selfie.star.activity.model.ImageDtoListResponse;
import com.selfie.star.network.ApiService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainService implements IMainService {
    private ServiceGenerator serviceGenerator;

    public MainService(Context context) {
        serviceGenerator = new ServiceGenerator.Builder(context, BaseApplication.getPreferenceManager().getBaseUrl())
                .setCacheEnable(true)
                .build();
    }

    @Override
    public void getListData(Context pContext, final IViewCallback<CategoryResponseData> pViewCallback) {
        ApiService apiService = serviceGenerator.createService(ApiService.class);
        Observable<CategoryResponseData> observable = apiService
                .fetchCategoryList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        observable.subscribe(new BaseNetworkObservable<CategoryResponseData>() {
            @Override
            public void success(CategoryResponseData homeDataResponse) {
                if (homeDataResponse != null) {
                    pViewCallback.onSuccess(homeDataResponse);
                } else {
                    ToastUtility.showToastMessage(pContext, pContext.getString(R.string.error_msg));

                }
            }

            @Override
            public void failure(Throwable error) {
                LoadingDialog.dismissDialog();
                pViewCallback.onFailure(error);
            }
        });
    }

    @Override
    public void getDetailData(Context pContext, long categoryId, IViewCallback<ImageDtoListResponse> pViewCallback) {
        ApiService apiService = serviceGenerator.createService(ApiService.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("categoryId", String.valueOf(categoryId));
        Observable<ImageDtoListResponse> observable = apiService
                .fetchDetailList(String.valueOf(categoryId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        observable.subscribe(new BaseNetworkObservable<ImageDtoListResponse>() {
            @Override
            public void success(ImageDtoListResponse responseData) {
                if (responseData != null) {
                    pViewCallback.onSuccess(responseData);
                } else {
                    ToastUtility.showToastMessage(pContext, pContext.getString(R.string.error_msg));
                }
            }

            @Override
            public void failure(Throwable error) {
                LoadingDialog.dismissDialog();
                pViewCallback.onFailure(error);
            }
        });
    }
}
