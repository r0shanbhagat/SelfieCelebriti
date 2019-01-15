package com.selfie.star.activity.service;

import android.content.Context;

import com.base.commonframework.network.locator.IService;
import com.base.commonframework.network.viewcallback.IViewCallback;
import com.selfie.star.activity.model.CategoryResponseData;
import com.selfie.star.activity.model.ImageDtoListResponse;


public interface IMainService extends IService {
    void getListData(Context pContext, IViewCallback<CategoryResponseData> viewCallback);
    void getDetailData(Context pContext,long categoryId, IViewCallback<ImageDtoListResponse> viewCallback);
}
