package com.jcs.where.api;

import com.google.gson.JsonElement;
import com.jcs.where.api.request.HotelOrderRequest;
import com.jcs.where.api.request.LoginRequest;
import com.jcs.where.api.request.RegisterRequest;
import com.jcs.where.api.request.ResetPasswordRequest;
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
import com.jcs.where.api.response.IntegralDetailResponse;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.api.response.NewsChannelResponse;
import com.jcs.where.api.response.NewsDetailResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.OrderListResponse;
import com.jcs.where.api.response.OrderNumResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.ParentCategoryResponse;
import com.jcs.where.api.response.SearchResponse;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.bean.TouristAttractionDetailResponse;
import com.jcs.where.api.response.CommentResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {

    /**
     * 获得首页金刚圈的信息
     */
    @GET("commonapi/v2/modules")
    Observable<JcsResponse<List<ModulesResponse>>> getModules();

    /**
     * 首页新闻接口
     */
    @GET("newsapi/v2/news/notices?notice_num=10")
    Observable<JcsResponse<List<HomeNewsResponse>>> getHomeNews();

    /**
     * 获得分类列表
     *
     * @param level      分类级别
     * @param categories parentId 上级分类的id
     * @return 分类列表
     */
    @GET("commonapi/v2/categories")
    Observable<JcsResponse<List<CategoryResponse>>> getCategories(@Query("level") int level, @Query("pid") String categories);

    /**
     * 获得分类列表
     *
     * @param level      分类级别
     * @param categories parentId 上级分类的id
     * @return 分类列表
     */
    @GET("commonapi/v2/categories?type=2")
    Observable<JcsResponse<List<CategoryResponse>>> getAllChildCategories(@Query("level") int level, @Query("pid") String categories);

    /**
     * 根据一个分类id集合，获得对应的分类数据
     * 默认传递一个 level=1 的参数，表示一级分类
     *
     * @param categories 分类id集合转化成的字符串
     * @return 分类列表
     */
    @GET("commonapi/v2/categories?level=1")
    Observable<JcsResponse<List<CategoryResponse>>> getCategories(@Query("pid") String categories);

    /**
     * 获得CategoryFragment页面展示的一级二级分类数据
     */
    @GET("commonapi/v2/categories/list")
    Observable<JcsResponse<List<ParentCategoryResponse>>> getParentCategory();

    /**
     * 酒店猜你喜欢的酒店列表
     *
     * @return 酒店列表
     */
    @GET("hotelapi/v2/hotels/recommends")
    Observable<JcsResponse<List<HotelResponse>>> getYouLike();

    /**
     * 获得轮播图
     *
     * @param type 区分是哪个页面的轮播图
     * @return 轮播图
     */
    @GET("commonapi/v2/banners")
    Observable<JcsResponse<List<BannerResponse>>> getBanners(@Query("type") int type);

    /**
     * 收藏酒店
     *
     * @param hotelId 酒店id
     */
    @POST("hotelapi/v2/collects")
    Observable<JcsResponse<Object>> postCollectHotel(@Query("hotel_id") int hotelId);

    /**
     * 取消收藏
     *
     * @param hotelId 酒店id
     */
    @DELETE("hotelapi/v2/collects")
    Observable<JcsResponse<Object>> delCollectHotel(@Query("hotel_id") int hotelId);


    /**
     * 获取酒店详情
     */
    @GET("hotelapi/v2/hotel/{id}")
    Observable<JcsResponse<HotelDetailResponse>> getHotelDetail(@Path("id") int hotelId);

    /**
     * 获取酒店房间列表
     * hotel/{hotel_id}/rooms?start_date=2020-06-16&end_date=2020-06-17&room_num=1
     */
    @GET("hotelapi/v2/hotel/{hotel_id}/rooms")
    Observable<JcsResponse<List<HotelRoomListResponse>>> getHotelRooms(
            @Path("hotel_id") int hotelId,
            @Query("start_date") String starDate,
            @Query("end_date") String endDate,
            @Query("room_num") int roomNum
    );

    /**
     * 根据房间id获得酒店房间详情
     */
    @GET("hotelapi/v2/hotel/room/{room_id}")
    Observable<JcsResponse<HotelRoomDetailResponse>> getHotelRoomById(
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
    @GET("hotelapi/v2/hotel/{hotel_id}/comments")
    Observable<JcsResponse<HotelCommentsResponse>> getHotelComments(@Path("hotel_id") int hotelId);

    /**
     * 获取酒店评价
     *
     * @param hotelId 酒店id
     * @param type    评价类型：1-晒图 2-低分 3-最新
     */
    @GET("hotelapi/v2/hotel/{hotel_id}/comments")
    Observable<JcsResponse<PageResponse<CommentResponse>>> getHotelComments(@Path("hotel_id") int hotelId, @Query("type") int type);

    /**
     * 获取酒店评论数量
     *
     * @param hotelId 酒店id
     */
    @GET("hotelapi/v2/hotel/{hotel_id}/comment/nums")
    Observable<JcsResponse<List<Integer>>> getHotelCommentNum(@Path("hotel_id") int hotelId);

    /**
     * 酒店下订单
     */
    @POST("hotelapi/v2/orders")
    Observable<JcsResponse<HotelOrderResponse>> postHotelOrder(@Body HotelOrderRequest request);

    /**
     * 酒店订单详情
     */
    @GET("hotelapi/v2/orders/{order_id}")
    Observable<JcsResponse<HotelOrderDetailResponse>> getHotelOrderDetail(@Path("order_id") int orderId);

    /**
     * 获得订单各个类型的数量
     */
    @GET("commonapi/v2/orders/nums")
    Observable<JcsResponse<OrderNumResponse>> getOrderNum();

    /**
     * 获得订单列表
     */
    @GET("commonapi/v2/orders")
    Observable<JcsResponse<PageResponse<OrderListResponse>>> getOrderList(@Query("type") int type, @Query("search_input") String keyword);


    @Headers("baseUrl:google/map")
    @GET("maps/api/geocode/json?key=AIzaSyDjaCnD0cWNtAOPiS_Kbb5FRZ4k4qyhayk")
    Observable<JcsResponse<String>> getLocation(@Query("latlng") String latlng);

    /**
     * 获得机构列表数据
     *
     * @param categoryId 机构分类
     * @param search     查询字段
     * @return
     */
    @GET("generalapi/v2/infos")
    Observable<JcsResponse<PageResponse<MechanismResponse>>> getMechanismListById(
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
    @GET("generalapi/v2/map/infos")
    Observable<JcsResponse<List<MechanismResponse>>> getMechanismListForMap(
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
    @GET("generalapi/v2/infos/{info_id}")
    Observable<JcsResponse<MechanismDetailResponse>> getMechanismDetailById(@Path("info_id") int mechanismId);

    /**
     * 收藏机构
     *
     * @param mechanismId 机构id
     */
    @GET("generalapi/v2/collects")
    Observable<JcsResponse<Object>> postCollectMechanism(@Query("info_id") int mechanismId);

    /**
     * 取消收藏机构
     *
     * @param mechanismId 机构id
     */
    @GET("generalapi/v2/collects")
    Observable<JcsResponse<Object>> delCollectMechanism(@Query("info_id") int mechanismId);

    /**
     * 获取综合服务页面的区域列表
     */
    @GET("generalapi/v2/areas")
    Observable<JcsResponse<List<CityResponse>>> getAreaForService();

    /**
     * 搜索接口
     *
     * @param input 关键字
     */
    @GET("commonapi/v2/searches")
    Observable<JcsResponse<List<SearchResponse>>> getSearchByInput(@Query("search_input") String input);

    /**
     * 登录
     */
    @PATCH("userapi/v2/login")
    Observable<JcsResponse<LoginResponse>> patchLogin(@Body LoginRequest loginRequest);

    /**
     * 发送验证码
     */
    @POST("userapi/v2/mobile/auth/code")
    Observable<JcsResponse<Object>> postSendCode(@Body SendCodeRequest sendCodeRequest);

    /**
     * 获得用户数据
     */
    @GET("userapi/v2/user/info")
    Observable<JcsResponse<UserInfoResponse>> getUserInfo();

    /**
     * 更新UserInfo信息
     */
    @PATCH("userapi/v2/users")
    Observable<JcsResponse<UserInfoResponse>> patchUpdateUserInfo(@Body UpdateUserInfoRequest request);

    /**
     * 获得城市选择列表
     *
     * @return
     */
    @GET("commonapi/v2/areas")
    Observable<JcsResponse<CityPickerResponse>> getCityPickers();

    /**
     * 获得新闻页的标签
     */
    @GET("newsapi/v2/channels")
    Observable<JcsResponse<List<NewsChannelResponse>>> getNewsTabs();

    /**
     * 根据频道id获得对应的新闻列表
     *
     * @param channelId 频道id
     */
    @GET("newsapi/v2/news")
    Observable<JcsResponse<PageResponse<NewsResponse>>> getNews(@Query("channel_id") int channelId, @Query("search_input") String input);

    /**
     * 获取积分明细列表
     */
    @GET("commonapi/v2/integrals")
    Observable<JcsResponse<PageResponse<IntegralDetailResponse>>> getIntegralsDetailList(@Query("page") int page);

    /**
     * 签到列表
     */
    @GET("commonapi/v2/sign")
    Observable<JcsResponse<SignListResponse>> getSignList();


    /**
     * 立即签到
     */
    @POST("commonapi/v2/sign")
    Observable<JcsResponse<JsonElement>> signIn();

    /**
     * 订阅新闻频道
     */
    @POST("newsapi/v2/channels/follows")
    Observable<JcsResponse<Object>> postFollowChannels(@Query("cate_ids") String channelIds);

    /**
     * 取消订阅新闻频道
     */
    @DELETE("newsapi/v2/channels/follows")
    Observable<JcsResponse<Object>> delFollowChannels(@Query("cate_ids") String channelIds);

    /**
     * 新闻详情
     */
    @GET("newsapi/v2/news/{news}")
    Observable<JcsResponse<NewsDetailResponse>> getNewsDetail(@Path("news") String newsId);

    /**
     * 新闻视频详情页-推荐新闻列表
     */
    @GET("newsapi/v2/news/recommends")
    Observable<JcsResponse<List<NewsResponse>>> getRecommendNews();

    /**
     * 关注新闻发布者
     */
    @POST("newsapi/v2/follows")
    Observable<JcsResponse<SuccessResponse>> postFollowNewsPublisher(@Query("publisher_id") int publisherId);

    /**
     * 取消关注新闻发布者
     */
    @DELETE("newsapi/v2/follows")
    Observable<JcsResponse<SuccessResponse>> delFollowNewsPublisher(@Query("publisher_id") int publisherId);


    /**
     * 获得展示在地图上的景点数据
     *
     * @param categoryId 景点分类
     * @param areaId     区域id，非必须（0）
     * @param search     查询字段
     * @param lat        必须
     * @param lng        必须
     * @return
     */
    @GET("travelapi/v2/map/travels")
    Observable<JcsResponse<List<MechanismResponse>>> getTouristAttractionListForMap(
            @Query("cate_id") String categoryId,
            @Query("area_id") int areaId,
            @Query("search_input") String search,
            @Query("lat") double lat,
            @Query("lng") double lng
    );

    /**
     * 获得旅游景点的列表数据
     *
     * @param categoryId 机构分类
     * @param search     查询字段
     */
    @GET("travelapi/v2/travels")
    Observable<JcsResponse<PageResponse<TouristAttractionResponse>>> getTouristAttractionListById(
            @Query("cate_id") String categoryId,
            @Query("search_input") String search,
            @Query("lat") double lat,
            @Query("lng") double lng);

    /**
     * 获得旅游景点详情数据
     * @param touristAttractionId 旅游景点id
     */
    @GET("travelapi/v2/travels/{travel_id}")
    Observable<JcsResponse<TouristAttractionDetailResponse>> getTouristAttractionDetail(@Path("travel_id") int touristAttractionId);

    /**
     * 获得旅游景点评论列表
     * @param touristAttractionId 旅游景点id
     */
    @GET("travelapi/v2/comments/{travel_id}")
    Observable<JcsResponse<PageResponse<CommentResponse>>> getTouristAttractionCommentList(@Path("travel_id") int touristAttractionId);

    /**
     * 登录
     */
    @PATCH("userapi/v2/login")
    Observable<JcsResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

    /**
     * 注册
     */
    @POST("userapi/v2/register")
    Observable<JcsResponse<LoginResponse>> register(@Body RegisterRequest registerRequest);

    /**
     * 获取验证码
     */
    @POST("userapi/v2/mobile/auth/code")
    Observable<JcsResponse<JsonElement>> getVerifyCode(@Body SendCodeRequest sendCodeRequest);

    /**
     * 重置密码
     */
    @PUT("userapi/v2/forget")
    Observable<JcsResponse<JsonElement>> resetPassword(@Body ResetPasswordRequest sendCodeRequest);


}
