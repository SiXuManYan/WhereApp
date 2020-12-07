package com.jcs.where.api;

import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelCommentsResponse;
import com.jcs.where.api.response.HotelDetailResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.api.response.SuccessResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {


    /**
     * 获得首页金刚圈的信息
     *
     * @return 金刚圈
     */
    @GET("commonapi/v1/modules")
    Observable<List<ModulesResponse>> getModules();

    /**
     * 获得分类列表
     *
     * @param level      分类级别
     * @param categories parentId 上级分类的id
     * @return 分类列表
     */
    @GET("commonapi/v1/categories")
    Observable<List<CategoryResponse>> getCategories(@Query("level") int level, @Query("pid") int[] categories);

    /**
     * 酒店猜你喜欢的酒店列表
     *
     * @return 酒店列表
     */
    @GET("hotelapi/v1/hotels/recommends")
    Observable<List<HotelResponse>> getYouLike();

    /**
     * 获得轮播图
     *
     * @param type 区分是哪个页面的轮播图
     * @return 轮播图
     */
    @GET("commonapi/v1/banners")
    Observable<List<BannerResponse>> getBanners(@Query("type") int type);

    /**
     * 收藏酒店
     *
     * @param hotelId 酒店id
     */
    @POST("hotelapi/v1/collects")
    Observable<Response<SuccessResponse>> postCollectHotel(@Query("hotel_id") int hotelId);

    /**
     * 取消收藏
     *
     * @param hotelId 酒店id
     */
    @DELETE("hotelapi/v1/collects")
    Observable<Response<SuccessResponse>> delCollectHotel(@Query("hotel_id") int hotelId);


    /**
     * 获取酒店详情
     */
    @GET("hotelapi/v1/hotel/{id}")
    Observable<HotelDetailResponse> getHotelDetail(@Path("id") int hotelId);


    /**
     * 获取酒店评价
     *
     * @param hotelId 酒店id
     */
    @GET("hotelapi/v1/hotel/{hotel_id}/comments")
    Observable<HotelCommentsResponse> getHotelComments(@Path("hotel_id") int hotelId);

    /**
     * 获取酒店评价
     *
     * @param hotelId 酒店id
     * @param type    评价类型：1-晒图 2-低分 3-最新
     */
    @GET("hotelapi/v1/hotel/{hotel_id}/comments")
    Observable<HotelCommentsResponse> getHotelComments(@Path("hotel_id") int hotelId, @Query("type") int type);

    /**
     * 获取酒店评论数量
     *
     * @param hotelId 酒店id
     */
    @GET("hotelapi/v1/hotel/{hotel_id}/comment/nums")
    Observable<List<Integer>> getHotelCommentNum(@Path("hotel_id") int hotelId);
}
