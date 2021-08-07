package com.jcs.where.api;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jcs.where.api.request.AddCartRequest;
import com.jcs.where.api.request.CartDeleteRequest;
import com.jcs.where.api.request.CollectionRequest;
import com.jcs.where.api.request.CollectionRestaurantRequest;
import com.jcs.where.api.request.HotelOrderRequest;
import com.jcs.where.api.request.MerchantSettledRequest;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.request.StoreOrderCommit;
import com.jcs.where.api.request.UpdateUserInfoRequest;
import com.jcs.where.api.request.WriteHotelCommentRequest;
import com.jcs.where.api.request.account.BindPhoneRequest;
import com.jcs.where.api.request.account.LoginRequest;
import com.jcs.where.api.request.account.RegisterRequest;
import com.jcs.where.api.request.account.ResetPasswordRequest;
import com.jcs.where.api.request.account.ThreePartyLoginRequest;
import com.jcs.where.api.request.bills.BillsOrderCommit;
import com.jcs.where.api.request.bills.UpLoadBillsPayAccountInfo;
import com.jcs.where.api.request.message.MessageStatusRequest;
import com.jcs.where.api.request.modify.ModifyPasswordRequest;
import com.jcs.where.api.request.modify.ModifyPhoneRequest;
import com.jcs.where.api.request.store.StoreAddCart;
import com.jcs.where.api.request.store.StoreCommitComment;
import com.jcs.where.api.request.store.StoreRefundModifyRequest;
import com.jcs.where.api.request.store.StoreRefundRequest;
import com.jcs.where.api.request.store.UpLoadPayAccountInfo;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.CityPickerResponse;
import com.jcs.where.api.response.CollectedResponse;
import com.jcs.where.api.response.CommentResponse;
import com.jcs.where.api.response.FootprintResponse;
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
import com.jcs.where.api.response.MerchantSettledInfoResponse;
import com.jcs.where.api.response.MerchantTypeResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.api.response.NewsChannelResponse;
import com.jcs.where.api.response.NewsDetailResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.OrderNumResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.ParentCategoryResponse;
import com.jcs.where.api.response.SearchResponse;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.api.response.UploadFileResponse;
import com.jcs.where.api.response.UploadFileResponse2;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.api.response.address.AddressRequest;
import com.jcs.where.api.response.address.AddressResponse;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.api.response.bills.BillsOrderInfo;
import com.jcs.where.api.response.category.Category;
import com.jcs.where.api.response.category.UserCategory;
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse;
import com.jcs.where.api.response.gourmet.dish.DeliveryTime;
import com.jcs.where.api.response.gourmet.dish.DishDetailResponse;
import com.jcs.where.api.response.gourmet.dish.DishResponse;
import com.jcs.where.api.response.gourmet.dish.DishTakeawayResponse;
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail;
import com.jcs.where.api.response.gourmet.order.FoodOrderSubmitData;
import com.jcs.where.api.response.gourmet.order.TakeawayOrderDetail;
import com.jcs.where.api.response.gourmet.order.TakeawayOrderSubmitData;
import com.jcs.where.api.response.gourmet.qr.QrResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse;
import com.jcs.where.api.response.hotel.HotelListResponse;
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse;
import com.jcs.where.api.response.hotel.HotelOrderDetail;
import com.jcs.where.api.response.hydropower.PaymentRecord;
import com.jcs.where.api.response.message.RongCloudUserResponse;
import com.jcs.where.api.response.message.SystemMessageResponse;
import com.jcs.where.api.response.order.OrderListResponse;
import com.jcs.where.api.response.order.bill.BillOrderDetails;
import com.jcs.where.api.response.order.store.RefundDetail;
import com.jcs.where.api.response.order.store.StoreOrderDetail;
import com.jcs.where.api.response.order.tab.OrderTabResponse;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.api.response.search.SearchResultResponse;
import com.jcs.where.api.response.store.PayChannel;
import com.jcs.where.api.response.store.StoreDetail;
import com.jcs.where.api.response.store.StoreGoodDetail;
import com.jcs.where.api.response.store.StoreGoods;
import com.jcs.where.api.response.store.StoreOrderInfoResponse;
import com.jcs.where.api.response.store.StoreRecommend;
import com.jcs.where.api.response.store.cart.StoreCartResponse;
import com.jcs.where.api.response.store.comment.StoreCommentCount;
import com.jcs.where.api.response.store.comment.StoreCommentDetail;
import com.jcs.where.api.response.version.VersionResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.bean.FollowCategoryRequest;
import com.jcs.where.bean.HotelMapListBean;
import com.jcs.where.bean.OrderSubmitRequest;
import com.jcs.where.bean.OrderSubmitTakeawayRequest;
import com.jcs.where.bean.TouristAttractionDetailResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
     * 上传酒店评价
     */
    @POST("hotelapi/v2/hotel/comment")
    Observable<JcsResponse<SuccessResponse>> postHotelComment(@Body WriteHotelCommentRequest request);

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
    Observable<JcsResponse<PageResponse<OrderListResponse>>> getOrderList(@Query("type") int type,
                                                                          @Query("search_input") @Nullable String keyword,
                                                                          @Query("page") int page);


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
     * 获得机构列表数据
     *
     * @param categoryId 机构分类
     * @param search     查询字段
     * @return
     */
    @GET("generalapi/v2/infos")
    Observable<JcsResponse<PageResponse<MechanismResponse>>> getMechanismListById2(
            @Query("page") int page,
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
    @POST("generalapi/v2/collects")
    Observable<JcsResponse<SuccessResponse>> postCollectMechanism(@Query("info_id") int mechanismId);

    /**
     * 取消收藏机构
     *
     * @param mechanismId 机构id
     */
    @DELETE("generalapi/v2/collects")
    Observable<JcsResponse<SuccessResponse>> delCollectMechanism(@Query("info_id") int mechanismId);

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
    Observable<JcsResponse<PageResponse<NewsResponse>>> getNews(@Query("channel_id") @Nullable Integer channelId,
                                                                @Query("search_input") @Nullable String input,
                                                                @Query("page") int page);

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
     * 收藏新闻
     */
    @POST("newsapi/v2/collects")
    Observable<JcsResponse<SuccessResponse>> postCollectNews(@Query("news_id") String newsId);

    /**
     * 收藏新闻
     */
    @DELETE("newsapi/v2/collects")
    Observable<JcsResponse<SuccessResponse>> delCollectNews(@Query("news_id") String newsId);

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
    Observable<JcsResponse<List<TouristAttractionResponse>>> getTouristAttractionListForMap(
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
     *
     * @param touristAttractionId 旅游景点id
     */
    @GET("travelapi/v2/travels/{travel_id}")
    Observable<JcsResponse<TouristAttractionDetailResponse>> getTouristAttractionDetail(@Path("travel_id") int touristAttractionId);

    /**
     * 收藏旅游景点
     */
    @POST("travelapi/v2/collects/{travel_id}")
    Observable<JcsResponse<SuccessResponse>> postCollectTouristAttraction(@Path("travel_id") int travelId);

    /**
     * 取消收藏旅游景点
     */
    @DELETE("travelapi/v2/collects/{travel_id}")
    Observable<JcsResponse<SuccessResponse>> delCollectTouristAttraction(@Path("travel_id") int travelId);

    /**
     * 获得旅游景点评论列表
     *
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

    /**
     * 搜索页面根据参数搜索酒店
     *
     * @param areaId 区域id（城市id）
     * @param input  搜索关键字
     * @param lat    经度
     * @param lng    纬度
     * @param page   页码
     */
    @GET("hotelapi/v2/hotels")
    Observable<JcsResponse<PageResponse<HotelResponse>>> getHotelListByInputAtSearch(
            @Query("area_id") String areaId,
            @Query("search_input") String input,
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("page") int page,
            @Query("hotel_type_ids") String hotelTypeIds
    );

    /**
     * 获得搜索页面-酒店的热门搜索
     */
    @GET("hotelapi/v2/hot/searches")
    Observable<JcsResponse<List<String>>> getHotHotelListAtSearch();

    @GET("newsapi/v2/news")
    Observable<JcsResponse<PageResponse<NewsResponse>>> getNewsListByInputAtSearch(
            @Query("search_input") String input
    );

    /**
     * 获得搜索页面-新闻的热门搜索
     */
    @GET("newsapi/v2/hot/searches")
    Observable<JcsResponse<List<String>>> getHotNewsListAtSearch();

    /**
     * 获得搜索页面-综合服务的热门搜索
     */
    @GET("generalapi/v2/hot/searches")
    Observable<JcsResponse<List<String>>> getHotConvenienceServiceListAtSearch();

    /**
     * 获得收藏页面-收藏的视频列表
     * type=3 表示 视频
     */
    @GET("commonapi/v2/collects?type=3")
    Observable<JcsResponse<PageResponse<CollectedResponse>>> getCollectionVideo(@Query("page") int page);

    /**
     * 获得收藏页面-收藏的文章列表
     * type=2 表示 文章
     */
    @GET("commonapi/v2/collects?type=2")
    Observable<JcsResponse<PageResponse<CollectedResponse>>> getCollectionArticle(@Query("page") int page);

    /**
     * 获得收藏页面-收藏的同城信息
     * type=2 表示 文章
     * (没有分页)
     */
    @GET("commonapi/v2/collects?type=1")
    Observable<JcsResponse<PageResponse<CollectedResponse>>> getCollectionSameCity(@Query("page") int page);

    /**
     * 第三方登录
     * 404：账号不存在，需要跳转绑定手机号界面
     */
    @POST("userapi/v2/third/login")
    Observable<JcsResponse<LoginResponse>> threePartyLogin(@Body ThreePartyLoginRequest request);

    /**
     * 绑定手机号
     */
    @PATCH("userapi/v2/bind/phone")
    Observable<JcsResponse<LoginResponse>> bindPhone(@Body BindPhoneRequest request);


    /**
     * 获得足迹列表
     */
    @GET("commonapi/v2/histories")
    Observable<JcsResponse<PageResponse<FootprintResponse>>> getFootprintList();

    /**
     * 修改密码
     */
    @PATCH("userapi/v2/users/pwd")
    Observable<JcsResponse<JsonElement>> modifyPassword(@Body ModifyPasswordRequest request);

    /**
     * 修改手机号
     */
    @PUT("userapi/v2/users/phone")
    Observable<JcsResponse<JsonElement>> modifyPhone(@Body ModifyPhoneRequest request);


    /**
     * 校验密码
     * 类型（1：手机号，2：邮箱）
     */
    @POST("userapi/v2/verify/password")
    Observable<JcsResponse<JsonElement>> checkPassword(@Query("type") int type,
                                                       @Query("password") String password,
                                                       @Query("phone") String phone,
                                                       @Query("email") String email);

    /**
     * 文件上传
     */
    @Multipart
    @POST("commonapi/v2/file")
    Observable<JcsResponse<UploadFileResponse>> uploadFile(@Part("type") RequestBody type, @Part MultipartBody.Part file);


    /**
     * 多文件上传
     */
    @Multipart
    @POST("commonapi/v2/file")
    Observable<JcsResponse<UploadFileResponse2>> uploadMultiImages(@Part("type") RequestBody type, @PartMap Map<String, RequestBody> maps);

    /**
     * 用户协议
     *
     * @param lang 语言（zh_cn或en）
     */
    @GET("users/agreement")
    Observable<JcsResponse<JsonElement>> getAgreement(@Query("lang") String lang);


    /**
     * 系统通知消息列表
     */
    @GET("messageapi/v2/getMessageList")
    Observable<JcsResponse<PageResponse<SystemMessageResponse>>> getSystemMessage(@Query("page") int page);


    /**
     * 修改消息状态
     */
    @POST("messageapi/v2/updateMessageStatus")
    Observable<JcsResponse<JsonElement>> setMessageRead(@Body MessageStatusRequest request);

    /**
     * 获取未读消息数量
     */
    @GET("messageapi/v2/count")
    Observable<JcsResponse<JsonObject>> getUnreadMessageCount();

    /**
     * 获取融云用户信息
     */
    @GET("commonapi/v2/rong/users")
    Observable<JcsResponse<RongCloudUserResponse>> getRongCloudUserInfo(@Query("user_id") String user_id);

    /**
     * 搜索接口
     */
    @GET("commonapi/v2/searches")
    Observable<JcsResponse<List<SearchResultResponse>>> getSearchResult(@Query("search_input") String input);

    /**
     * 商家入驻的分类
     */
    @GET("userapi/v2/merchant/types/{level}")
    Observable<JcsResponse<List<MerchantTypeResponse>>> getMerchantSettledType(@Path("level") String level, @Query("pid") int pid);

    /**
     * 提交商家入驻信息
     */
    @POST("userapi/v2/merchants")
    Observable<JcsResponse<SuccessResponse>> postMerchant(@Body MerchantSettledRequest request);

    /**
     * 获得商家入驻信息
     */
    @GET("userapi/v2/merchants/info")
    Observable<JcsResponse<MerchantSettledInfoResponse>> getMerchantSettledInfo();

    /**
     * 推荐列表
     */
    @GET("commonapi/v2/recommends")
    Observable<JcsResponse<PageResponse<HomeRecommendResponse>>> getRecommends(
            @Query("page") int page,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("area_id") String area_id
    );


    /**
     * app 版本检测
     */
    @GET("/commonapi/v2/version")
    Observable<JcsResponse<VersionResponse>> checkAppVersion(
            @Query("current_version") String current_version,
            @Query("device") String device);


    /**
     * 获取酒店地图列表
     */
    @GET("hotelapi/v2/map/hotels")
    Observable<JcsResponse<List<HotelMapListBean>>> getHotelMapList(
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("area_id") String area_id,
            @Query("search_input") @Nullable String search_input,
            @Query("star_level") @Nullable String star_level,
            @Query("price_range") @Nullable String price_range
    );


    /**
     * 酒店列表
     *
     * @param page
     * @param areaId       地区id
     * @param lat          纬度
     * @param lng          经度
     * @param price_range  价格区间（特殊情况，900以上请传递[900,100000]）
     * @param star_level   星级（特殊情况，二星以下请传递[1,2]）
     * @param hotelTypeIds 住宿类型ID（多选）
     * @param input        搜索酒店内容
     * @param grade        酒店分数
     */
    @GET("hotelapi/v2/hotels")
    Observable<JcsResponse<PageResponse<HotelListResponse>>> getHotelList(
            @Query("page") int page,
            @Query("lat") @Nullable String lat,
            @Query("lng") @Nullable String lng,
            @Query("area_id") @Nullable String areaId,
            @Query("price_range") @Nullable String price_range,
            @Query("star_level") @Nullable String star_level,
            @Query("hotel_type_ids") @Nullable String hotelTypeIds,
            @Query("search_input") @Nullable String input,
            @Query("grade") @Nullable String grade

    );


    /**
     * 收货地址列表
     */
    @GET("commonapi/v2/addresses")
    Observable<JcsResponse<List<AddressResponse>>> addressList();

    /**
     * 修改地址
     */
    @PATCH("commonapi/v2/addresses/{address_id}")
    Observable<JcsResponse<JsonElement>> editAddress(
            @Path("address_id") String address_id,
            @Body AddressRequest body);


    /**
     * 添加收货地址
     */
    @POST("commonapi/v2/addresses")
    Observable<JcsResponse<JsonElement>> addAddress(@Body AddressRequest body);


    /**
     * 删除收货地址
     */
    @DELETE("commonapi/v2/addresses/{address_id}")
    Observable<JcsResponse<JsonElement>> deleteAddress(@Path("address_id") String address_id);

    /**
     * 美食商品列表
     *
     * @param trading_area_id 商业区ID
     * @param per_price       人均价格
     * @param service         商家服务（1：支持外卖）
     * @param sort            （必要参数）排序（1：智能排序，2：好评优先，3：销量优先，4：距离优先）
     * @param search_input    搜索内容
     * @param lat             纬度
     * @param lng             经度
     * @param category_id     分类ID
     * @see <a href="https://where.w.eolinker.com/#/home/api_studio/inside/api/detail?apiID=4706784&groupID=1222698&projectHashKey=zyRnJSLb6c6ae86fc0cd75b6afab3f7023c37b6f55614c9&spaceKey=where">接口文档地址</a>
     */
    @GET("restaurantapi/v2/restaurants")
    Observable<JcsResponse<PageResponse<RestaurantResponse>>> getRestaurantList(
            @Query("page") int page,
            @Query("trading_area_id") @Nullable String trading_area_id,
            @Query("per_price") @Nullable String per_price,
            @Query("service") @Nullable String service,
            @Query("sort") int sort,
            @Query("search_input") @Nullable String search_input,
            @Query("lat") @Nullable String lat,
            @Query("lng") @Nullable String lng,
            @Query("category_id") @Nullable String category_id);


    /**
     * 获取分类列表
     * !--level 3 pid 89 type =1 --
     *
     * @param level 分类级别
     * @param type  分类类型（1：只看下一级，2：所有下级）
     * @param pid   上级ID（详细看底部参数说明）
     *
     *              <p>
     *              PID参数说明：
     *              本接口请求分为以下情况：
     *              1、获取下级或所有子集的情况（常规）
     *              pid为上级ID，对应形式可以为”1”或”[1]”两种形式
     *              2、获取一批一级分类（场景：使用金刚区列表中categories字段，获取金刚区分类对应真实一级分类数据）
     *              level必须传”1”
     *              pid为一级分类数组，”[1, 10, 17]”的形式
     *              <p>
     *              特殊PID：
     *              89->餐厅外卖
     * @see <a href="https://where.w.eolinker.com/#/home/api_studio/inside/api/detail?apiID=4657209&groupID=1167151&projectHashKey=zyRnJSLb6c6ae86fc0cd75b6afab3f7023c37b6f55614c9&spaceKey=where">接口文档地址</a>
     */
    @GET("commonapi/v2/categories")
    Observable<JcsResponse<List<Category>>> getCategoriesList(
            @Query("level") int level,
            @Query("pid") int pid,
            @Query("type") int type);


    /**
     * 获取商业区列表
     */
    @GET("restaurantapi/v2/trading/areas")
    Observable<JcsResponse<List<AreaResponse>>> getAreasList();


    /**
     * 餐厅详情
     */
    @GET("restaurantapi/v2/restaurants/{restaurant_id}")
    Observable<JcsResponse<RestaurantDetailResponse>> getRestaurantDetail(
            @Path("restaurant_id") String restaurant_id,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    /**
     * 堂食菜品列表
     */
    @GET("restaurantapi/v2/eat_in_foods")
    Observable<JcsResponse<PageResponse<DishResponse>>> getDishList(
            @Query("page") int page,
            @Query("restaurant_id") String restaurant_id
    );


    /**
     * 餐厅评论列表
     *
     * @param type 列表类型（0：全部，1：有图，2：好评，3：差评）
     */
    @GET("restaurantapi/v2/comments")
    Observable<JcsResponse<PageResponse<com.jcs.where.api.response.gourmet.comment.CommentResponse>>> getCommentList(
            @Query("page") int page,
            @Query("type") int type,
            @Query("restaurant_id") String restaurant_id
    );


    /**
     * 堂食菜品详情
     */
    @GET("restaurantapi/v2/eat_in_foods/{eat_in_food_id}")
    Observable<JcsResponse<DishDetailResponse>> getDishDetail(
            @Path("eat_in_food_id") String eat_in_food_id
    );

    /**
     * 购物车接口
     */
    @GET("restaurantapi/v2/carts")
    Observable<JcsResponse<PageResponse<ShoppingCartResponse>>> getShoppingCartList(
            @Query("page") int page
    );

    /**
     * 首页新闻接口
     */
    @GET("newsapi/v2/news/index")
    Observable<JcsResponse<List<com.jcs.where.api.response.home.HomeNewsResponse>>> getHomeNewsList();

    /**
     * 获得CategoryFragment页面展示的一级二级分类数据
     */
    @GET("commonapi/v2/categories/list")
    Observable<JcsResponse<ArrayList<Category>>> getCategoryList();

    /**
     * 获得可编辑分类列表
     */
    @GET("commonapi/v2/categories/follow/list")
    Observable<JcsResponse<UserCategory>> getEditableCategory(
            @Query("device_id") String device_id
    );


    /**
     * 获得可编辑分类列表
     */
    @POST("commonapi/v2/categories/follow/list")
    Observable<JcsResponse<JsonElement>> followCategory(
            @Body FollowCategoryRequest followCategory
    );

    /**
     * 修改购物车商品数量
     */
    @PATCH("restaurantapi/v2/carts/numbers")
    Observable<JcsResponse<JsonElement>> changeCartNumber(
            @Query("cart_id") int cart_id,
            @Query("number") int number
    );

    /**
     * 删除购物车
     */
//    @DELETE("restaurantapi/v2/carts")
    @HTTP(method = "DELETE", path = "restaurantapi/v2/carts", hasBody = true)
    Observable<JcsResponse<JsonElement>> deleteCart(@Body CartDeleteRequest cart_id);


    /**
     * 美食下单接口
     */
    @POST("restaurantapi/v2/eat_in/orders")
    Observable<JcsResponse<FoodOrderSubmitData>> orderSubmit(
            @Body OrderSubmitRequest request
    );


    /**
     * 外卖详情
     */
    @GET("restaurantapi/v2/take_out/restaurants/{restaurant_id}")
    Observable<JcsResponse<TakeawayDetailResponse>> takeawayDetail(
            @Path("restaurant_id") String restaurant_id
    );

    /**
     * 外卖详情
     */
    @GET("restaurantapi/v2/take_out/goods")
    Observable<JcsResponse<PageResponse<DishTakeawayResponse>>> takeawayGoodList(
            @Query("page") int page,
            @Query("restaurant_id") String restaurant_id
    );


    /**
     * 外卖下单接口
     */
    @POST("restaurantapi/v2/take_out/orders")
    Observable<JcsResponse<TakeawayOrderSubmitData>> takeawayOrderSubmit(
            @Body OrderSubmitTakeawayRequest request
    );


    /**
     * 外卖送达时间
     */
    @GET("restaurantapi/v2/take_out/delivery/times")
    Observable<JcsResponse<DeliveryTime>> timeList(
            @Query("restaurant_id") String restaurant_id
    );


    /**
     * 加入购物车商品数量
     */
    @POST("restaurantapi/v2/carts")
    Observable<JcsResponse<JsonElement>> addCartNumber(
            @Body AddCartRequest request
    );


    /**
     * 收藏餐厅
     */
    @POST("restaurantapi/v2/collects")
    Observable<JcsResponse<JsonElement>> collectsRestaurant(
            @Body CollectionRestaurantRequest request
    );

    /**
     * 收藏餐厅
     */
    @HTTP(method = "DELETE", path = "restaurantapi/v2/collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> unCollectsRestaurant(
            @Body CollectionRestaurantRequest request
    );


    /**
     * 券码详情
     */
    @POST("restaurantapi/v2/eat_in/coupon/{order_id}")
    Observable<JcsResponse<QrResponse>> getQrDetail(
            @Path("order_id") String order_id
    );


    /**
     * 美食订单详情
     */
    @GET("restaurantapi/v2/eat_in/orders/{order_id}")
    Observable<JcsResponse<FoodOrderDetail>> getFoodOrderDetail(
            @Path("order_id") int order_id
    );

    /**
     * 取消美食订单
     */
    @DELETE("restaurantapi/v2/eat_in/orders/{order_id}")
    Observable<JcsResponse<JsonElement>> cancelFoodOrder(
            @Path("order_id") int order_id
    );


    /**
     * 外卖订单详情
     */
    @GET("restaurantapi/v2/take_out/orders/{order_id}")
    Observable<JcsResponse<TakeawayOrderDetail>> getTakeawayOrderDetail(
            @Path("order_id") int order_id
    );


    /**
     * 美食地图商品列表
     *
     * @param search_input 搜索内容
     * @param lat          纬度
     * @param lng          经度
     * @param category_id  分类ID
     */
    @GET("restaurantapi/v2/map/restaurants")
    Observable<JcsResponse<ArrayList<RestaurantResponse>>> getRestaurantMapList(
            @Query("search_input") @Nullable String search_input,
            @Query("lat") @Nullable String lat,
            @Query("lng") @Nullable String lng,
            @Query("category_id") @Nullable String category_id);

    /**
     * 商城推荐
     */
    @GET("generalapi/v2/estore/recommends")
    Observable<JcsResponse<ArrayList<StoreRecommend>>> getStoreRecommends();

    /**
     * 商城一级分类
     */
    @GET("generalapi/v2/categories/first")
    Observable<JcsResponse<ArrayList<Category>>> getStoreCategoryFirst();


    /**
     * 商城列表
     *
     * @param cate_id       分类ID（数据类型为‘1’或‘[1,2,3]’）
     * @param search_input  搜索内容
     * @param delivery_type 商家服务/配送方式（1:自提，2:商家配送)
     * @param sort_type     排序方式(1:距离优先，2:好评优先）
     */
    @GET("generalapi/v2/shops")
    Observable<JcsResponse<PageResponse<StoreRecommend>>> getStoreList(
            @Query("page") int page,
            @Query("lat") Float lat,
            @Query("lng") Float lng,
            @Query("cate_id") @Nullable String cate_id,
            @Query("search_input") @Nullable String search_input,
            @Query("delivery_type") @Nullable Integer delivery_type,
            @Query("sort_type") @Nullable Integer sort_type
    );


    /**
     * 商城二三级级分类
     */
    @GET("generalapi/v2/categories/second_third")
    Observable<JcsResponse<ArrayList<Category>>> getStoreCategoryNext(@Query("pid") int pid);


    /**
     * 商城二三级级分类
     */
    @GET("generalapi/v2/shops/{shop_id}")
    Observable<JcsResponse<StoreDetail>> getStoreDetail(@Path("shop_id") int shop_id);


    /**
     * 商城商品列表
     */
    @GET("generalapi/v2/goods")
    Observable<JcsResponse<PageResponse<StoreGoods>>> getStoreGoodList(
            @Query("page") int page,
            @Query("shop_id") int shop_id
    );


    /**
     * 商城商品列表
     */
    @GET("generalapi/v2/goods/{good_id}")
    Observable<JcsResponse<StoreGoodDetail>> getStoreGoodDetail(
            @Path("good_id") int good_id
    );


    /**
     * 提交订单
     */
    @POST("generalapi/v2/orders")
    Observable<JcsResponse<StoreOrderInfoResponse>> storeOrderCommit(
            @Body StoreOrderCommit request
    );


    /**
     * 支付渠道
     */
    @GET("commonapi/v2/cards")
    Observable<JcsResponse<ArrayList<PayChannel>>> getPayChannel();


    /**
     * 商城银行卡转账支付（上传支付信息）
     */
    @POST("generalapi/v2/bank_card/pay")
    Observable<JcsResponse<JsonElement>> upLoadPayAccountInfo(
            @Body UpLoadPayAccountInfo request
    );


    /**
     * 商城订单详情
     */
    @GET("generalapi/v2/orders/{order_id}")
    Observable<JcsResponse<StoreOrderDetail>> getStoreOrderDetail(
            @Path("order_id") int order_id
    );


    /**
     * 水电缴费记录
     */
    @GET("generalapi/v2/pay_bills/orders")
    Observable<JcsResponse<PageResponse<PaymentRecord>>> getPaymentRecord(@Query("page") int page);


    /**
     * 水电统一下单
     */
    @POST("generalapi/v2/pay_bills/orders")
    Observable<JcsResponse<BillsOrderInfo>> billsCommitOrder(
            @Body BillsOrderCommit request
    );


    /**
     * 水电银行卡转账支付（上传支付信息）
     */
    @POST("generalapi/v2/pay_bills/bank_card/pay")
    Observable<JcsResponse<JsonElement>> upLoadBillsPayAccountInfo(
            @Body UpLoadBillsPayAccountInfo request
    );


    /**
     * 商城申请退款/退货
     */
    @POST("generalapi/v2/order/refunds")
    Observable<JcsResponse<JsonElement>> storeRefund(
            @Body StoreRefundRequest request
    );

    /**
     * 商城退货详情
     */
    @GET("generalapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<RefundDetail>> storeRefundDetail(
            @Path("orderId") int orderId
    );

    /**
     * 商城  取消 申请退款
     */
    @DELETE("generalapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<JsonElement>> cancelStoreRefund(
            @Path("orderId") int orderId
    );


    /**
     * 商城  修改申请退款
     */
    @PATCH("generalapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<JsonElement>> storeRefundModify(
            @Path("orderId") int orderId,
            @Body StoreRefundModifyRequest request
    );


    /**
     * 商城购物车
     */
    @GET("generalapi/v2/carts")
    Observable<JcsResponse<StoreCartResponse>> getStoreCart();


    /**
     * 修改商城购物车商品数量
     */
    @PATCH("generalapi/v2/carts/numbers")
    Observable<JcsResponse<JsonElement>> changeStoreCartNumber(
            @Query("cart_id") int cart_id,
            @Query("number") int number
    );


    /**
     * 删除购物车
     */
    @HTTP(method = "DELETE", path = "generalapi/v2/carts", hasBody = true)
    Observable<JcsResponse<JsonElement>> deleteStoreCart(@Body CartDeleteRequest cart_id);


    /**
     * 清空商城购物车
     */
    @DELETE("generalapi/v2/carts/clears")
    Observable<JcsResponse<JsonElement>> clearStoreCart();


    /**
     * 商城添加商城购物车
     */
    @POST("generalapi/v2/carts")
    Observable<JcsResponse<JsonElement>> addToStoreCart(@Body StoreAddCart request);


    /**
     * 取消订单
     */
    @DELETE("generalapi/v2/orders/{order_id}")
    Observable<JcsResponse<JsonElement>> cancelStoreOrder(@Path("order_id") int order_id);


    /**
     * 商城提交评价
     */
    @POST("generalapi/v2/comments")
    Observable<JcsResponse<JsonElement>> commitStoreComment(@Body StoreCommitComment request);


    /**
     * 商城评价详情
     */
    @GET("generalapi/v2/comments/{order_id}")
    Observable<JcsResponse<StoreCommentDetail>> storeCommentDetail(@Path("order_id") int order_id);


    /**
     * 商城评论列表
     *
     * @param type 类型（1-全部，2-最新，3-有图）
     */
    @GET("generalapi/v2/comments")
    Observable<JcsResponse<PageResponse<com.jcs.where.api.response.gourmet.comment.CommentResponse>>> getStoreCommentList(
            @Query("page") int page,
            @Query("shop_id") String shop_id,
            @Query("type") int type
    );


    /**
     * 商城收藏
     */
    @POST("generalapi/v2/estore/collects")
    Observable<JcsResponse<JsonElement>> storeCollects(
            @Body CollectionRequest request
    );

    /**
     * 商城收藏
     */
    @HTTP(method = "DELETE", path = "generalapi/v2/estore/collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> StoreCancelCollects(
            @Body CollectionRequest request
    );


    /**
     * 水电订单详情
     */
    @GET("generalapi/v2/pay_bills/orders/{order_id}")
    Observable<JcsResponse<BillOrderDetails>> billOrderDetail(@Path("order_id") int order_id);


    /**
     * 商城评价列表数量
     */
    @GET("generalapi/v2/comments/numbers/{shop_id}")
    Observable<JcsResponse<StoreCommentCount>> getStoreCommentCount(@Path("shop_id") int shop_id);


    /**
     * 美食-餐厅退款
     */
    @DELETE("restaurantapi/v2/eat_in/orders/refund/{order_id}")
    Observable<JcsResponse<JsonElement>> delicacyOrderRefund(@Path("order_id") int order_id);

    /**
     * 美食-外卖取消订单
     */
    @DELETE("restaurantapi/v2/take_out/orders/{order_id}")
    Observable<JcsResponse<JsonElement>> takeawayOrderCancel(@Path("order_id") int order_id);

    /**
     * 美食-外卖退款
     */
    @DELETE("restaurantapi/v2/take_out/orders/refund/{order_id}")
    Observable<JcsResponse<JsonElement>> takeawayOrderRefund(@Path("order_id") int order_id);


    /**
     * 美食-商城银行卡转账支付（上传支付信息）
     */
    @POST("restaurantapi/v2/eat_in/bank_card/pay")
    Observable<JcsResponse<JsonElement>> upLoadFoodPayAccountInfo(
            @Body UpLoadPayAccountInfo request
    );

    /**
     * 外卖-转账支付（上传支付信息）
     */
    @POST("restaurantapi/v2/take_out/bank_card/pay")
    Observable<JcsResponse<JsonElement>> upLoadTakeawayPayAccountInfo(
            @Body UpLoadBillsPayAccountInfo request
    );


    /**
     * 商城评价列表数量
     */
    @GET("commonapi/v2/orders/filters")
    Observable<JcsResponse<ArrayList<OrderTabResponse>>> getOrderTabs();


    /**
     * 酒店订单详情
     */
    @GET("hotelapi/v2/orders/{order_id}")
    Observable<JcsResponse<HotelOrderDetail>> hotelOrderDetail(@Path("order_id") int order_id);

    /**
     * 取消酒店订单
     */
    @DELETE("hotelapi/v2/orders/{order_id}")
    Observable<JcsResponse<JsonElement>> cancelHotelOrder(@Path("order_id") int order_id);

    /**
     * 酒店订单申请退款
     */
    @DELETE("hotelapi/v2/orders/refund/{order_id}")
    Observable<JcsResponse<JsonElement>> refundHotelOrder(@Path("order_id") int order_id);


    /**
     * 酒店下订单
     */
    @POST("hotelapi/v2/orders")
    Observable<JcsResponse<HotelOrderCommitResponse>> commitHotelOrder(@Body HotelOrderRequest request);

    /**
     * 酒店-转账支付（上传支付信息）
     */
    @POST("hotelapi/v2/bank_card/pay")
    Observable<JcsResponse<JsonElement>> upLoadHotelPayAccountInfo(
            @Body UpLoadBillsPayAccountInfo request
    );



}
