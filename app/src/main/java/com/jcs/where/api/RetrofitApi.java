package com.jcs.where.api;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.jcs.where.api.request.AddCartRequest;
import com.jcs.where.api.request.CartDeleteRequest;
import com.jcs.where.api.request.CollectionRestaurantRequest;
import com.jcs.where.api.request.HotelCollectionRequest;
import com.jcs.where.api.request.HotelOrderRequest;
import com.jcs.where.api.request.MallShopCollection;
import com.jcs.where.api.request.MallShopUnCollection;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.request.StoreOrderCommit;
import com.jcs.where.api.request.TravelCollectionRequest;
import com.jcs.where.api.request.UpdateUserInfoRequest;
import com.jcs.where.api.request.account.BindPhoneRequest;
import com.jcs.where.api.request.account.LoginRequest;
import com.jcs.where.api.request.account.RegisterRequest;
import com.jcs.where.api.request.account.ResetPasswordRequest;
import com.jcs.where.api.request.account.ThreePartyLoginRequest;
import com.jcs.where.api.request.bills.BillsOrderCommit;
import com.jcs.where.api.request.bills.UpLoadBillsPayAccountInfo;
import com.jcs.where.api.request.bills.UpLoadMallPayAccountInfo;
import com.jcs.where.api.request.hotel.FoodCommitComment;
import com.jcs.where.api.request.hotel.HotelCommitComment;
import com.jcs.where.api.request.hotel.TravelCommitComment;
import com.jcs.where.api.request.merchant.MerchantSettledData;
import com.jcs.where.api.request.merchant.MerchantSettledPost;
import com.jcs.where.api.request.message.MessageStatusRequest;
import com.jcs.where.api.request.modify.ModifyPasswordRequest;
import com.jcs.where.api.request.modify.ModifyPhoneRequest;
import com.jcs.where.api.request.store.MallRefundModifyRequest;
import com.jcs.where.api.request.store.MallRefundRequest;
import com.jcs.where.api.request.store.StoreAddCart;
import com.jcs.where.api.request.store.StoreCommitComment;
import com.jcs.where.api.request.store.StoreRefundModifyRequest;
import com.jcs.where.api.request.store.StoreRefundRequest;
import com.jcs.where.api.request.store.UpLoadPayAccountInfo;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.CityPickerResponse;
import com.jcs.where.api.response.Coupon;
import com.jcs.where.api.response.FootprintResponse;
import com.jcs.where.api.response.GeCouponDefault;
import com.jcs.where.api.response.GetCouponResult;
import com.jcs.where.api.response.HotelResponse;
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
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.ParentCategoryResponse;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.UnReadMessage;
import com.jcs.where.api.response.UploadFileResponse;
import com.jcs.where.api.response.UploadFileResponse2;
import com.jcs.where.api.response.UserCoupon;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.api.response.address.AddressRequest;
import com.jcs.where.api.response.address.AddressResponse;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.api.response.bills.BillsOrderInfo;
import com.jcs.where.api.response.category.Category;
import com.jcs.where.api.response.category.UserCategory;
import com.jcs.where.api.response.collection.MallGoodCollection;
import com.jcs.where.api.response.collection.MyCollection;
import com.jcs.where.api.response.footprint.Footprint;
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse;
import com.jcs.where.api.response.gourmet.dish.DeliveryTime;
import com.jcs.where.api.response.gourmet.dish.DishDetailResponse;
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail;
import com.jcs.where.api.response.gourmet.order.FoodOrderSubmitData;
import com.jcs.where.api.response.gourmet.order.TakeawayOrderDetail;
import com.jcs.where.api.response.gourmet.order.TakeawayOrderSubmitData;
import com.jcs.where.api.response.gourmet.qr.QrResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse;
import com.jcs.where.api.response.hotel.HotelComment;
import com.jcs.where.api.response.hotel.HotelDetail;
import com.jcs.where.api.response.hotel.HotelHomeRecommend;
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse;
import com.jcs.where.api.response.hotel.HotelOrderDetail;
import com.jcs.where.api.response.hotel.RoomDetail;
import com.jcs.where.api.response.hydropower.PaymentRecord;
import com.jcs.where.api.response.mall.MallCartGroup;
import com.jcs.where.api.response.mall.MallCategory;
import com.jcs.where.api.response.mall.MallCommentCount;
import com.jcs.where.api.response.mall.MallExpired;
import com.jcs.where.api.response.mall.MallGood;
import com.jcs.where.api.response.mall.MallGoodDetail;
import com.jcs.where.api.response.mall.MallOrderDetail;
import com.jcs.where.api.response.mall.MallShopCategory;
import com.jcs.where.api.response.mall.MallShopRecommend;
import com.jcs.where.api.response.mall.request.MallAddCart;
import com.jcs.where.api.response.mall.request.MallCollection;
import com.jcs.where.api.response.mall.request.MallCommitResponse;
import com.jcs.where.api.response.mall.request.MallDeliveryRequest;
import com.jcs.where.api.response.mall.request.MallDeliveryResponse;
import com.jcs.where.api.response.mall.request.MallOrderCommit;
import com.jcs.where.api.response.mall.request.MallOrderCoupon;
import com.jcs.where.api.response.mall.request.MallOrderDefaultCoupon;
import com.jcs.where.api.response.mall.request.MallShop;
import com.jcs.where.api.response.mall.request.UnCollection;
import com.jcs.where.api.response.merchant.MerchantApplyRecord;
import com.jcs.where.api.response.message.RongCloudUserResponse;
import com.jcs.where.api.response.message.SystemMessageResponse;
import com.jcs.where.api.response.order.OrderListResponse;
import com.jcs.where.api.response.order.bill.BillOrderDetails;
import com.jcs.where.api.response.order.store.RefundDetail;
import com.jcs.where.api.response.order.store.RefundDetailMall;
import com.jcs.where.api.response.order.store.StoreOrderDetail;
import com.jcs.where.api.response.order.tab.OrderTabResponse;
import com.jcs.where.api.response.other.CartNumberResponse;
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
import com.jcs.where.api.response.travel.TravelChild;
import com.jcs.where.api.response.travel.TravelDetail;
import com.jcs.where.api.response.version.VersionResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.bean.FollowCategoryRequest;
import com.jcs.where.bean.OrderSubmitRequest;
import com.jcs.where.bean.OrderSubmitTakeawayRequest;

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
     * @return 轮播图  1：首页顶部，2：旅游住宿，3：首页 ， 4 mall版 estor商城
     */
    @GET("commonapi/v2/banners")
    Observable<JcsResponse<List<BannerResponse>>> getBanners(@Query("type") int type);

    /**
     * 收藏酒店
     */
    @POST("hotelapi/v2/collects")
    Observable<JcsResponse<JsonElement>> postCollectHotel(@Body HotelCollectionRequest request);

    /**
     * 取消收藏
     */
    @HTTP(method = "DELETE", path = "hotelapi/v2/collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> delCollectHotel(@Body HotelCollectionRequest request);


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
    Observable<JcsResponse<PageResponse<MechanismResponse>>> getMechanismListById2(
            @Query("page") int page,
            @Query("cate_id") String categoryId,
            @Query("search_input") String search,
            @Query("lat") double lat,
            @Query("lng") double lng


    );


    /**
     * 获得机构列表数据
     *
     * @param categoryId 机构分类
     * @param search     查询字段
     * @return
     */
    @GET("generalapi/v2/infos")
    Observable<JcsResponse<PageResponse<MechanismResponse>>> getMechanismListById3(
            @Query("page") int page,
            @Query("cate_id") String categoryId,
            @Query("search_input") String search,
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("area_id") String area_id

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
    Observable<JcsResponse<JsonElement>> postCollectMechanism(@Query("info_id") int mechanismId);

    /**
     * 取消收藏机构
     *
     * @param mechanismId 机构id
     */
    @DELETE("generalapi/v2/collects")
    Observable<JcsResponse<JsonElement>> delCollectMechanism(@Query("info_id") int mechanismId);

    /**
     * 获取综合服务页面的区域列表
     */
    @GET("generalapi/v2/areas")
    Observable<JcsResponse<List<CityResponse>>> getAreaForService();

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
     * 列表类型（letter-字母形式，list-列表形式）
     *
     * @return
     */
    @GET("commonapi/v2/areas")
    Observable<JcsResponse<CityPickerResponse>> getCityPickers(@Query("type") @Nullable String type);

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
    Observable<JcsResponse<UnReadMessage>> getUnreadMessageCount();

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
     * 获得商家入驻信息
     */
    @GET("userapi/v2/merchants/info")
    Observable<JcsResponse<MerchantSettledInfoResponse>> getMerchantSettledInfo();

    /**
     * 推荐列表(首页)
     */
    @GET("commonapi/v2/recommends")
    Observable<JcsResponse<PageResponse<HomeRecommendResponse>>> getRecommends(
            @Query("page") int page,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("area_id") String area_id
    );


    /**
     * 推荐列表(旅游)
     */
    @GET("travelapi/v2/recommends")
    Observable<JcsResponse<PageResponse<HomeRecommendResponse>>> getTravelRecommends(
            @Query("area_id") String area_id,
            @Query("lat") String lat,
            @Query("lng") String lng

    );


    /**
     * app 版本检测
     */
    @GET("/commonapi/v2/version")
    Observable<JcsResponse<VersionResponse>> checkAppVersion(
            @Query("current_version") String current_version,
            @Query("device") String device);


    /**
     * 收货地址列表
     */
    @GET("commonapi/v2/addresses")
    Observable<JcsResponse<ArrayList<AddressResponse>>> addressList();

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
            @Query("lat") @Nullable Double lat,
            @Query("lng") @Nullable Double lng,
            @Query("category_id") @Nullable Integer category_id);


    /**
     * 美食地图商品列表
     *
     * @param search_input 搜索内容
     * @param lat          纬度
     * @param lng          经度
     * @param category_id  分类ID
     */
    @GET("restaurantapi/v2/map/restaurants")
    Observable<JcsResponse<ArrayList<RestaurantResponse>>> getRestaurantMapMarker(
            @Query("trading_area_id") @Nullable String trading_area_id,
            @Query("per_price") @Nullable String per_price,
            @Query("service") @Nullable String service,
            @Query("sort") int sort,
            @Query("search_input") @Nullable String search_input,
            @Query("lat") @Nullable Double lat,
            @Query("lng") @Nullable Double lng,
            @Query("category_id") @Nullable Integer category_id);


    /**
     * 获取分类列表
     * !--level 3 pid 89 type =1 --
     *
     * @param level 分类级别
     * @param pid   上级ID（详细看底部参数说明）
     * @param type  分类类型（1：只看下一级，2：所有下级）
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
    Observable<JcsResponse<ArrayList<Category>>> getCategoriesList(
            @Query("level") int level,
            @Query("pid") String pid,
            @Query("type") @Nullable Integer type);


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
            @Path("restaurant_id") int restaurant_id,
            @Query("lat") @Nullable Double lat,
            @Query("lng") @Nullable Double lng
    );


    /**
     * 餐厅评论列表
     *
     * @param type 列表类型（0：全部，1：有图，2：好评，3：差评）
     */
    @GET("restaurantapi/v2/comments")
    Observable<JcsResponse<PageResponse<HotelComment>>> getFoodCommentList(
            @Query("page") int page,
            @Query("type") int type,
            @Query("restaurant_id") String restaurant_id
    );


    /**
     * 堂食菜品详情
     */
    @GET("restaurantapi/v2/eat_in_foods/{eat_in_food_id}")
    Observable<JcsResponse<DishDetailResponse>> getDishDetail(
            @Path("eat_in_food_id") int eat_in_food_id
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
     * 取消收藏餐厅
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
     * mall商城申请退款/退货
     */
    @POST("estoreapi/v2/order/refunds/{order_id}")
    Observable<JcsResponse<JsonElement>> mallRefund(
            @Path("order_id") int orderId,
            @Body MallRefundRequest request
    );


    /**
     * 商城退货详情
     */
    @GET("generalapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<RefundDetail>> storeRefundDetail(
            @Path("orderId") int orderId
    );


    /**
     * mall 商城退货详情
     */
    @GET("estoreapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<RefundDetailMall>> mallRefundDetail(
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
     * mall商城  取消 申请退款
     */
    @DELETE("estoreapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<JsonElement>> cancelMallRefund(
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
     * mall商城  修改申请退款
     */
    @PATCH("estoreapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<JsonElement>> mallRefundModify(
            @Path("orderId") int orderId,
            @Body MallRefundModifyRequest request
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
     * 删除mall购物车
     */
    @HTTP(method = "DELETE", path = "estoreapi/v2/carts", hasBody = true)
    Observable<JcsResponse<JsonElement>> deleteMallCart(@Body CartDeleteRequest cart_id);


    /**
     * 清空商城购物车
     */
    @DELETE("generalapi/v2/carts/clears")
    Observable<JcsResponse<JsonElement>> clearStoreCart();


    /**
     * 清空mall商城购物车
     *
     * @param clear 0清空正常商品 1清空失效商品
     */
    @DELETE("estoreapi/v2/carts/clears")
    Observable<JcsResponse<JsonElement>> clearMallCart(@Query("clear") int clear);


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
    Observable<JcsResponse<PageResponse<HotelComment>>> getStoreCommentList2(
            @Query("page") int page,
            @Query("shop_id") String shop_id,
            @Query("type") int type
    );


    /**
     * 商城收藏
     */
    @POST("generalapi/v2/estore/collects")
    Observable<JcsResponse<JsonElement>> storeCollects(
            @Body MallShopCollection request
    );

    /**
     * 商城收藏
     */
    @HTTP(method = "DELETE", path = "generalapi/v2/estore/collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> StoreCancelCollects(
            @Body MallShopCollection request
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
     * 获取订单tab
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


    /**
     * 酒店评论列表
     *
     * @param type 评价显示类型（1：晒图，2：低分，3：最新 0 全部）
     */
    @GET("hotelapi/v2/hotel/{hotel_id}/comments")
    Observable<JcsResponse<PageResponse<HotelComment>>> getHotelCommentList(
            @Path("hotel_id") int hotel_id,
            @Query("type") int type,
            @Query("page") int page
    );


    /**
     * 酒店提交评价
     */
    @POST("hotelapi/v2/hotel/comment")
    Observable<JcsResponse<JsonElement>> commitHotelComment(@Body HotelCommitComment request);


    /**
     * 旅游提交评价
     */
    @POST("travelapi/v2/comments")
    Observable<JcsResponse<JsonElement>> commitTravelComment(@Body TravelCommitComment request);


    /**
     * 美食提交评价
     */
    @POST("restaurantapi/v2/comments")
    Observable<JcsResponse<JsonElement>> commitFoodComment(@Body FoodCommitComment request);


    /**
     * 获得机构列表数据(带分页)
     *
     * @param categoryId 分类ID（数据类型为‘1’或‘[1,2,3]’）
     * @param search     查询字段
     * @return
     */
    @GET("generalapi/v2/infos")
    Observable<JcsResponse<PageResponse<MechanismResponse>>> getMechanismList(
            @Query("page") int page,
            @Query("cate_id") String categoryId,
            @Query("area_id") @Nullable String area_id,
            @Query("search_input") @Nullable String search,
            @Query("lat") @Nullable Double lat,
            @Query("lng") @Nullable Double lng

    );


    /**
     * 获得展示在地图上的机构数据
     */
    @GET("generalapi/v2/map/infos")
    Observable<JcsResponse<ArrayList<MechanismResponse>>> getMechanismListToMap(
            @Query("cate_id") int categoryId,
            @Query("area_id") @Nullable String area_id,
            @Query("search_input") @Nullable String search,
            @Query("lat") double lat,
            @Query("lng") double lng
    );


    /**
     * 酒店猜你喜欢的酒店列表
     *
     * @return 酒店列表
     */
    @GET("hotelapi/v2/hotels/recommends")
    Observable<JcsResponse<ArrayList<HotelHomeRecommend>>> hotelHomeRecommend();

    /**
     * 酒店地图模式列表
     *
     * @param area_id 区域id 必须传
     * @see <a href="https://where.w.eolinker.com/home/api_studio/inside/api/detail?apiID=3925068&groupID=1000127&projectHashKey=zyRnJSLb6c6ae86fc0cd75b6afab3f7023c37b6f55614c9&spaceKey=where">接口文档</a>
     */
    @GET("hotelapi/v2/hotels")
    Observable<JcsResponse<PageResponse<HotelHomeRecommend>>> hotelChildList(
            @Query("page") int page,
            @Query("area_id") @Nullable String area_id,
            @Query("lat") @Nullable Double lat,
            @Query("lng") @Nullable Double lng,
            @Query("search_input") @Nullable String search_input,
            @Query("star_level") @Nullable String star_level,
            @Query("hotel_type_ids") @Nullable String hotel_type_ids,
            @Query("price_range") @Nullable String price_range,
            @Query("grade") @Nullable String grade

    );


    /**
     * 获取所有酒店地图 Maker 信息
     */
    @GET("hotelapi/v2/map/hotels")
    Observable<JcsResponse<ArrayList<HotelHomeRecommend>>> getHotelMapMaker(
            @Query("area_id") String area_id,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("search_input") @Nullable String search_input,
            @Query("star_level") @Nullable String star_level,
            @Query("price_range") @Nullable String price_range,
            @Query("hotel_type_ids") @Nullable Integer hotelTypeIds
    );


    /**
     * 获取酒店房间列表
     * hotel/{hotel_id}/rooms?start_date=2020-06-16&end_date=2020-06-17&room_num=1
     */
    @GET("hotelapi/v2/hotel/{hotel_id}/rooms")
    Observable<JcsResponse<ArrayList<HotelRoomListResponse>>> getHotelRooms(
            @Path("hotel_id") int hotelId,
            @Query("start_date") String starDate,
            @Query("end_date") String endDate,
            @Query("room_num") int roomNum
    );


    /**
     * 获取酒店详情
     */
    @GET("hotelapi/v2/hotel/{id}")
    Observable<JcsResponse<HotelDetail>> hotelDetail(@Path("id") int hotelId);


    /**
     * 酒店房间详情
     */
    @GET("hotelapi/v2/hotel/room/{room_id}")
    Observable<JcsResponse<RoomDetail>> getHotelRoomDetail(
            @Path("room_id") int roomId,
            @Query("room_num") int roomNum,
            @Query("start_date") String starDate,
            @Query("end_date") String endDate
    );


    /**
     * 旅游景点列表
     */
    @GET("travelapi/v2/travels?version=2")
    Observable<JcsResponse<PageResponse<TravelChild>>> getTravelChildList(
            @Query("page") int page,
            @Query("category_id") int categoryId,
            @Query("search_input") @Nullable String search,
            @Query("lat") double lat,
            @Query("lng") double lng);


    /**
     * 旅游景点地图marker
     */
    @GET("travelapi/v2/map/travels?version=2")
    Observable<JcsResponse<ArrayList<TravelChild>>> getTravelMarker(
            @Query("category_id") int categoryId,
            @Query("search_input") @Nullable String search,
            @Query("lat") double lat,
            @Query("lng") double lng);


    /**
     * 旅游详情
     */
    @GET("travelapi/v2/travels/{travel_id}")
    Observable<JcsResponse<TravelDetail>> getTravelDetail(@Path("travel_id") int travelId);


    /**
     * 收藏旅游景点
     */
    @POST("travelapi/v2/collects")
    Observable<JcsResponse<JsonElement>> travelCollection(@Body TravelCollectionRequest travelCollectionRequest);

    /**
     * 取消收藏旅游景点
     */
    @DELETE("travelapi/v2/collects/{travel_id}")
    Observable<JcsResponse<JsonElement>> travelUnCollection(@Path("travel_id") int travelId);


    /**
     * 旅游评论
     */
    @GET("travelapi/v2/comments/{travel_id}")
    Observable<JcsResponse<PageResponse<HotelComment>>> travelComment(
            @Path("travel_id") int touristAttractionId,
            @Query("page") int page
    );


    /**
     * 收藏
     *
     * @param type 类型（1：同城，2：文章，3：视频）
     */
    @GET("commonapi/v2/collects?type=1")
    Observable<JcsResponse<PageResponse<MyCollection>>> getCollection(
            @Query("page") int page,
            @Query("type") int type
    );


    /**
     * 获得足迹列表
     */
    @GET("commonapi/v2/histories")
    Observable<JcsResponse<PageResponse<Footprint>>> getFootprint(@Query("page") int page);


    /**
     * 提交商家入驻信息
     */
    @POST("userapi/v2/merchants")
    Observable<JcsResponse<JsonElement>> postMerchantSettled(@Body MerchantSettledPost request);

    /**
     * 获取商家入驻信息
     */
    @GET("userapi/v2/merchants/info")
    Observable<JcsResponse<MerchantSettledData>> getMerchantSettled();


    /**
     * 重新提交商家入驻信息
     */
    @PATCH("userapi/v2/merchants/{merchant_id}")
    Observable<JcsResponse<JsonElement>> repostMerchantSettled(@Path("merchant_id") int merchant_id, @Body MerchantSettledPost request);


    /**
     * 获取商城一二级分类
     */
    @GET("estoreapi/v2/categories/one")
    Observable<JcsResponse<ArrayList<MallCategory>>> getMallFirstSecondCategory();

    /**
     * 获取商城首页推荐商品
     */
    @GET("estoreapi/v2/goods/rand")
    Observable<JcsResponse<ArrayList<MallGood>>> getMallRecommendGood(@Query("categoryId") int categoryId);


    /**
     * 获取商城三级级分类
     */
    @GET("estoreapi/v2/categories/three")
    Observable<JcsResponse<ArrayList<MallCategory>>> getMallThirdCategory(@Query("categoryId") int categoryId);


    /**
     * 获取商城商品列表
     *
     * @param order      order 价格排序(降序 desc, 升序 asc)
     * @param title      商品名称
     * @param categoryId 分类id
     * @param startPrice 最小价格 start和price同时进行传递
     * @param endPrice   最大价格 start和price同时进行传递
     * @param sold       销量排序(降序 desc, 升序 asc)
     * @param shopId     店铺id
     * @param recommend  0未推荐 1查询推荐
     * @param coupon_id  优惠券id
     */
    @GET("estoreapi/v2/goods")
    Observable<JcsResponse<PageResponse<MallGood>>> getMallGoodList(
            @Query("page") int page,
            @Query("order") @Nullable String order,
            @Query("title") @Nullable String title,
            @Query("categoryId") @Nullable Integer categoryId,
            @Query("start") @Nullable String startPrice,
            @Query("end") @Nullable String endPrice,
            @Query("sold") @Nullable String sold,
            @Query("shopId") @Nullable Integer shopId,
            @Query("shop_categoryId") @Nullable Integer shop_categoryId,
            @Query("recommend") @Nullable Integer recommend,
            @Query("coupon_id") @Nullable Integer coupon_id


    );

    /**
     * 获取商城首页推荐商品
     */
    @GET("estoreapi/v2/goods/{id}")
    Observable<JcsResponse<MallGoodDetail>> getMallGoodDetail(@Path("id") int goodId);

    /**
     * 购物车
     */
    @GET("estoreapi/v2/carts")
    Observable<JcsResponse<PageResponse<MallCartGroup>>> mallCartList(@Query("page") int page);


    /**
     * 提交订单
     */
    @POST("estoreapi/v2/orders")
    Observable<JcsResponse<MallCommitResponse>> mallOrderCommit(
            @Body MallOrderCommit request
    );


    /**
     * 新版商城支付（上传支付信息）
     */
    @POST("estoreapi/v2/bank_card/pay")
    Observable<JcsResponse<JsonElement>> upLoadMallAccountInfo(
            @Body UpLoadMallPayAccountInfo request
    );


    /**
     * 收藏新版商城店铺
     */
    @POST("estoreapi/v2/collects")
    Observable<JcsResponse<JsonElement>> collectsMallShop(
            @Body MallCollection request
    );

    /**
     * 取消收藏新版商城店铺
     */
    @HTTP(method = "DELETE", path = "estoreapi/v2/collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> unCollectsMallShop(
            @Body UnCollection request
    );


    /**
     * 修改新商城购物车商品数量
     */
    @PATCH("estoreapi/v2/carts/numbers")
    Observable<JcsResponse<JsonElement>> changeMallCartNumber(
            @Query("cart_id") int cart_id,
            @Query("number") int number
    );


    /**
     * 商城加入购物车
     */
    @POST("estoreapi/v2/carts")
    Observable<JcsResponse<JsonElement>> mallAddCart(
            @Body MallAddCart request
    );


    /**
     * 新商城订单详情
     */
    @GET("estoreapi/v2/order/{order_id}")
    Observable<JcsResponse<MallOrderDetail>> mallOrderDetail(
            @Path("order_id") int order_id
    );


    /**
     * 取消订单
     */
    @DELETE("estoreapi/v2/order/{order_id}")
    Observable<JcsResponse<JsonElement>> cancelMallOrder(@Path("order_id") int order_id);


    /**
     * 新版商城评论列表
     *
     * @param type 列表类型（0：全部，1：有图，2：最新，3：低分）
     */
    @GET("estoreapi/v2/comments")
    Observable<JcsResponse<PageResponse<HotelComment>>> getMallCommentList(
            @Query("goods_id") int goods_id,
            @Query("type") int type,
            @Query("page") int page
    );


    /**
     * mall商城提交评价
     */
    @POST("estoreapi/v2/comments")
    Observable<JcsResponse<JsonElement>> commitMallComment(@Body StoreCommitComment request);


    /**
     * mall商城评价数量
     */
    @GET("estoreapi/v2/comments/num")
    Observable<JcsResponse<MallCommentCount>> mallCommentCount(@Query("goods_id") int goods_id);

    /**
     * 商城评价详情
     */
    @GET("estoreapi/v2/comments/{comments_id}")
    Observable<JcsResponse<StoreCommentDetail>> mallCommentDetail(@Path("comments_id") int comments_id);


    /**
     * 商品收藏列表
     */
    @GET("estoreapi/v2/collects")
    Observable<JcsResponse<PageResponse<MallGoodCollection>>> collectionGoodList(
            @Query("page") int page
    );

    /**
     * 店铺配送费
     */
    @POST("estoreapi/v2/delivery")
    Observable<JcsResponse<MallDeliveryResponse>> shopDelivery(@Body MallDeliveryRequest request);

    /**
     * 确认收货
     */
    @POST("estoreapi/v2/order/confirm/{order_id}")
    Observable<JcsResponse<JsonElement>> confirmReceipt(@Path("order_id") int order_id);


    /**
     * 商家入驻申请记录
     */
    @GET("userapi/v2/merchants")
    Observable<JcsResponse<PageResponse<MerchantApplyRecord>>> merchantApplyRecord(
            @Query("page") int page
    );


    /**
     * 修改新商城购物车SKU属性
     */
    @PATCH("estoreapi/v2/carts/cart_specs")
    Observable<JcsResponse<JsonElement>> changeMallSku(
            @Query("cart_id") int cart_id,
            @Query("specs_id") int specs_id,
            @Query("nums") int nums
    );


    /**
     * 失效商品
     */
    @GET("estoreapi/v2/carts_efficacy")
    Observable<JcsResponse<ArrayList<MallExpired>>> expiredGoods();


    /**
     * mall店铺详情
     */
    @GET("estoreapi/v2/shop/{id}")
    Observable<JcsResponse<MallShop>> mallShopDetail(@Path("id") int id);


    /**
     * 收藏 mall 店铺
     */
    @POST("estoreapi/v2/shop_collects")
    Observable<JcsResponse<JsonElement>> mallShopCollection(@Body MallShopCollection collection);


    /**
     * 取消收藏 mall 店铺
     */
    @HTTP(method = "DELETE", path = "estoreapi/v2/shop_collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> mallShopUnCollection(@Body MallShopUnCollection collection);


    /**
     * mall店铺详情
     */
    @GET("estoreapi/v2/shop_category")
    Observable<JcsResponse<ArrayList<MallShopCategory>>> mallShopCategory(@Query("shop_id") int shop_id);


    /**
     * 店铺收藏列表
     */
    @GET("estoreapi/v2/shop_collects")
    Observable<JcsResponse<PageResponse<com.jcs.where.api.response.collection.MallShopCollection>>> collectionShopList(@Query("page") int page);

    /**
     * 获得商品足迹列表
     */
    @GET("commonapi/v2/histories_goods")
    Observable<JcsResponse<PageResponse<Footprint>>> getGoodFootprint(@Query("page") int page);


    /**
     * 商城购物车商品数量
     */
    @GET("estoreapi/v2/carts/cart_goods_nums")
    Observable<JcsResponse<CartNumberResponse>> getMallCartCount();


    /**
     * 餐厅购物车商品数量
     */
    @GET("restaurantapi/v2/carts/cart_goods_nums")
    Observable<JcsResponse<CartNumberResponse>> getFoodCartCount();


    /**
     * 店铺推荐
     */
    @GET("estoreapi/v2/recommend")
    Observable<JcsResponse<ArrayList<ArrayList<MallShopRecommend>>>> recommendMallShop(@Query("shop_id") int shop_id);

    /**
     * 获取券包
     *
     * @param type 1未使用 2 已使用 3已过期
     */
    @GET("estoreapi/v2/coupon_user")
    Observable<JcsResponse<PageResponse<UserCoupon>>> couponUser(
            @Query("page") int page,
            @Query("type") int type);


    /**
     * 领券中心
     */
    @GET("estoreapi/v2/coupon")
    Observable<JcsResponse<ArrayList<Coupon>>> couponCenter(@Query("page") int page);


    /**
     * 领取优惠券
     */
    @POST("estoreapi/v2/coupon")
    Observable<JcsResponse<GetCouponResult>> getCoupon(@Query("coupon_id") int coupon_id);


    /**
     * 提交订单获取默认优惠券
     */
    @POST("estoreapi/v2/default_coupon")
    Observable<JcsResponse<GeCouponDefault>> getDefaultCoupon(@Body MallOrderDefaultCoupon request);

    /**
     * 订单选择优惠券
     */
    @POST("estoreapi/v2/order_coupon")
    Observable<JcsResponse<ArrayList<UserCoupon>>> getOrderCoupon(@Body MallOrderCoupon request);


    /**
     * 店铺优惠券
     */
    @POST("estoreapi/v2/shop_coupon")
    Observable<JcsResponse<ArrayList<Coupon>>> mallShopCoupon(@Query("shop_id") int shop_id);


}
