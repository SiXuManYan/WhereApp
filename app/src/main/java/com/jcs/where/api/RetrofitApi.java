package com.jcs.where.api;

import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.ModulesResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {


    @GET("commonapi/v1/modules")
    Observable<List<ModulesResponse>> getModules();

    @GET("commonapi/v1/categories")
    Observable<List<CategoryResponse>> getCategories(@Query("level") int level, @Query("pid") int[] categories);

    @GET("hotelapi/v1/hotels/recommends")
    Observable<List<HotelResponse>> getYouLike();

    @GET("commonapi/v1/banners")
    Observable<List<BannerResponse>> getBanners(@Query("type") int type);
}
