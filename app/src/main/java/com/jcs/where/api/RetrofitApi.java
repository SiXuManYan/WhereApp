package com.jcs.where.api;

import com.jcs.where.api.request.HotelOrderRequest;
import com.jcs.where.api.request.LoginRequest;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.request.UpdateUserInfoRequest;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.CityPickerResponse;
import com.jcs.where.api.response.HomeNewsResponse;
import com.jcs.where.api.response.HotelCommentsResponse;
import com.jcs.where.api.response.HotelDetailResponse;
import com.jcs.where.api.response.HotelOrderDetailResponse;
import com.jcs.where.api.response.HotelOrderResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.HotelRoomDetailResponse;
import com.jcs.where.api.response.HotelRoomListResponse;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.NewsTabResponse;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.api.response.OrderNumResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.ParentCategoryResponse;
import com.jcs.where.api.response.SearchResponse;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.bean.CityResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
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
     * 首页新闻接口
     */
    @GET("newsapi/v1/news/notices?notice_num=10")
    Observable<List<HomeNewsResponse>> getHomeNews();

    /**
     * 获得分类列表
     *
     * @param level      分类级别
     * @param categories parentId 上级分类的id
     * @return 分类列表
     */
    @GET("commonapi/v1/categories")
    Observable<List<CategoryResponse>> getCategories(@Query("level") int level, @Query("pid") String categories);

    /**
     * 获得分类列表
     *
     * @param level      分类级别
     * @param categories parentId 上级分类的id
     * @return 分类列表
     */
    @GET("commonapi/v1/categories?type=2")
    Observable<List<CategoryResponse>> getAllChildCategories(@Query("level") int level, @Query("pid") String categories);

    /**
     * 根据一个分类id集合，获得对应的分类数据
     * 默认传递一个 level=1 的参数，表示一级分类
     *
     * @param categories 分类id集合转化成的字符串
     * @return 分类列表
     */
    @GET("commonapi/v1/categories?level=1")
    Observable<List<CategoryResponse>> getCategories(@Query("pid") String categories);

    /**
     * 获得CategoryFragment页面展示的一级二级分类数据
     */
    @GET("commonapi/v1/categories/list")
    Observable<List<ParentCategoryResponse>> getParentCategory();

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
     * 获取酒店房间列表
     * hotel/{hotel_id}/rooms?start_date=2020-06-16&end_date=2020-06-17&room_num=1
     */
    @GET("hotelapi/v1/hotel/{hotel_id}/rooms")
    Observable<List<HotelRoomListResponse>> getHotelRooms(
            @Path("hotel_id") int hotelId,
            @Query("start_date") String starDate,
            @Query("end_date") String endDate,
            @Query("room_num") int roomNum
    );

    /**
     * 根据房间id获得酒店房间详情
     */
    @GET("hotelapi/v1/hotel/room/{room_id}")
    Observable<HotelRoomDetailResponse> getHotelRoomById(
            @Path("room_id") int roomId,
            @Query("start_date") String starDate,
            @Query("end_date") String endDate,
            @Query("room_num") int roomNum
    );

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

    /**
     * 酒店下订单
     */
    @POST("hotelapi/v1/orders")
    Observable<HotelOrderResponse> postHotelOrder(@Body HotelOrderRequest request);

    /**
     * 酒店订单详情
     */
    @GET("hotelapi/v1/orders/{order_id}")
    Observable<HotelOrderDetailResponse> getHotelOrderDetail(@Path("order_id") int orderId);

    /**
     * 获得订单各个类型的数量
     */
    @GET("commonapi/v1/orders/nums")
    Observable<OrderNumResponse> getOrderNum();

    /**
     * 获得订单列表
     */
    @GET("commonapi/v1/orders")
    Observable<PageResponse<OrderListResponse>> getOrderList(@Query("type") int type, @Query("search_input") String keyword);


    @Headers("baseUrl:google/map")
    @GET("maps/api/geocode/json?key=AIzaSyDjaCnD0cWNtAOPiS_Kbb5FRZ4k4qyhayk")
    Observable<String> getLocation(@Query("latlng") String latlng);

    /**
     * 获得机构列表数据
     *
     * @param categoryId 机构分类
     * @param search     查询字段
     * @return
     */
    @GET("generalapi/v1/infos")
    Observable<MechanismPageResponse> getMechanismListById(
            @Query("cate_id") String categoryId,
            @Query("search_input") String search,
            @Query("lat") double lat,
            @Query("lng") double lng);

    /**
     * 获得展示在地图上的机构数据
     *
     * @param categoryId 机构分类
     * @param areaId     区域id，非必须（0）
     * @param search     查询字段
     * @param lat        必须
     * @param lng        必须
     * @return
     */
    @GET("generalapi/v1/map/infos")
    Observable<List<MechanismResponse>> getMechanismListForMap(
            @Query("cate_id") String categoryId,
            @Query("area_id") int areaId,
            @Query("search_input") String search,
            @Query("lat") double lat,
            @Query("lng") double lng
    );

    /**
     * 根据机构id，获取机构详情
     *
     * @param mechanismId 机构id
     */
    @GET("generalapi/v1/infos/{info_id}")
    Observable<MechanismDetailResponse> getMechanismDetailById(@Path("info_id") int mechanismId);

    /**
     * 收藏机构
     *
     * @param mechanismId 机构id
     */
    @GET("generalapi/v1/collects")
    Observable<Response<SuccessResponse>> postCollectMechanism(@Query("info_id") int mechanismId);

    /**
     * 取消收藏机构
     *
     * @param mechanismId 机构id
     */
    @GET("generalapi/v1/collects")
    Observable<Response<SuccessResponse>> delCollectMechanism(@Query("info_id") int mechanismId);

    /**
     * 获取综合服务页面的区域列表
     */
    @GET("generalapi/v1/areas")
    Observable<List<CityResponse>> getAreaForService();

    /**
     * 搜索接口
     *
     * @param input 关键字
     */
    @GET("commonapi/v1/searches")
    Observable<List<SearchResponse>> getSearchByInput(@Query("search_input") String input);

    /**
     * 登录
     */
    @PATCH("userapi/v1/login")
    Observable<LoginResponse> patchLogin(@Body LoginRequest loginRequest);

    /**
     * 发送验证码
     */
    @POST("userapi/v1/mobile/auth/code")
    Observable<ResponseBody> postSendCode(@Body SendCodeRequest sendCodeRequest);

    /**
     * 获得用户数据
     */
    @GET("userapi/v1/user/info")
    Observable<UserInfoResponse> getUserInfo();

    /**
     * 更新UserInfo信息
     */
    @PATCH("userapi/v1/users")
    Observable<UserInfoResponse> patchUpdateUserInfo(@Body UpdateUserInfoRequest request);

    /**
     * 获得城市选择列表
     *
     * @return
     */
    @GET("commonapi/v1/areas")
    Observable<CityPickerResponse> getCityPickers();

    /**
     * 获得新闻页的标签
     */
    @GET("newsapi/v1/channels")
    Observable<List<NewsTabResponse>> getNewsTabs();

    @GET("newsapi/v1/news")
    Observable<PageResponse<NewsResponse>> getNews(@Query("channel_id") int channelId, @Query("search_input") String input);
}
