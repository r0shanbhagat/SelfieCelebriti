package com.selfie.star.network;

import com.selfie.star.activity.model.CategoryResponseData;
import com.selfie.star.activity.model.ImageDtoListResponse;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

   /* @POST("/UMS/login")
    Observable<Result<LoginResponse>> sendLoginRequest(@Body Object requestBody);
*/
   @GET("SelfieApp/fetchCategoryList")
   Observable<CategoryResponseData> fetchCategoryList();

   @GET("SelfieApp/fetchImagesForACategory")
   Observable<ImageDtoListResponse> fetchDetailList(@Query("categoryId") String address);
}
