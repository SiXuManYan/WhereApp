package com.jiechengsheng.city.api;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.jiechengsheng.city.api.request.AddCartRequest;
import com.jiechengsheng.city.api.request.CartDeleteRequest;
import com.jiechengsheng.city.api.request.CollectionRestaurantRequest;
import com.jiechengsheng.city.api.request.HotelCollectionRequest;
import com.jiechengsheng.city.api.request.HotelOrderRequest;
import com.jiechengsheng.city.api.request.IdRequest;
import com.jiechengsheng.city.api.request.IntegralPlaceOrder;
import com.jiechengsheng.city.api.request.MallShopCollection;
import com.jiechengsheng.city.api.request.MallShopUnCollection;
import com.jiechengsheng.city.api.request.MtjClickHomeJob;
import com.jiechengsheng.city.api.request.MtjClickService;
import com.jiechengsheng.city.api.request.MtjDuration;
import com.jiechengsheng.city.api.request.SendCodeRequest;
import com.jiechengsheng.city.api.request.StoreOrderCommit;
import com.jiechengsheng.city.api.request.TravelCollectionRequest;
import com.jiechengsheng.city.api.request.UpdateUserInfoRequest;
import com.jiechengsheng.city.api.request.account.BindPhoneRequest;
import com.jiechengsheng.city.api.request.account.LoginRequest;
import com.jiechengsheng.city.api.request.account.RegisterRequest;
import com.jiechengsheng.city.api.request.account.ResetPasswordRequest;
import com.jiechengsheng.city.api.request.account.ThreePartyLoginRequest;
import com.jiechengsheng.city.api.request.bills.UpLoadBillsPayAccountInfo;
import com.jiechengsheng.city.api.request.bills.UpLoadMallPayAccountInfo;
import com.jiechengsheng.city.api.request.hotel.BatchComment;
import com.jiechengsheng.city.api.request.hotel.ComplaintRequest;
import com.jiechengsheng.city.api.request.hotel.FoodCommitComment;
import com.jiechengsheng.city.api.request.hotel.HotelCommitComment;
import com.jiechengsheng.city.api.request.hotel.TravelCommitComment;
import com.jiechengsheng.city.api.request.merchant.MerchantSettledData;
import com.jiechengsheng.city.api.request.merchant.MerchantSettledPost;
import com.jiechengsheng.city.api.request.message.MessageStatusRequest;
import com.jiechengsheng.city.api.request.modify.ModifyPasswordRequest;
import com.jiechengsheng.city.api.request.modify.ModifyPhoneRequest;
import com.jiechengsheng.city.api.request.payment.PayStatus;
import com.jiechengsheng.city.api.request.payment.PayUrl;
import com.jiechengsheng.city.api.request.payment.PayUrlGet;
import com.jiechengsheng.city.api.request.store.MallRefundModifyRequest;
import com.jiechengsheng.city.api.request.store.MallRefundRequest;
import com.jiechengsheng.city.api.request.store.StoreAddCart;
import com.jiechengsheng.city.api.request.store.StoreCommitComment;
import com.jiechengsheng.city.api.request.store.StoreRefundModifyRequest;
import com.jiechengsheng.city.api.request.store.StoreRefundRequest;
import com.jiechengsheng.city.api.request.store.UpLoadPayAccountInfo;
import com.jiechengsheng.city.api.response.BannerResponse;
import com.jiechengsheng.city.api.response.CategoryResponse;
import com.jiechengsheng.city.api.response.CityPickerResponse;
import com.jiechengsheng.city.api.response.Coupon;
import com.jiechengsheng.city.api.response.FootprintResponse;
import com.jiechengsheng.city.api.response.GeCouponDefault;
import com.jiechengsheng.city.api.response.GetCouponResult;
import com.jiechengsheng.city.api.response.HotelResponse;
import com.jiechengsheng.city.api.response.HotelRoomListResponse;
import com.jiechengsheng.city.api.response.IntegralDetailResponse;
import com.jiechengsheng.city.api.response.LoginResponse;
import com.jiechengsheng.city.api.response.MechanismDetailResponse;
import com.jiechengsheng.city.api.response.MechanismResponse;
import com.jiechengsheng.city.api.response.MerchantSettledInfoResponse;
import com.jiechengsheng.city.api.response.MerchantTypeResponse;
import com.jiechengsheng.city.api.response.ModulesResponse;
import com.jiechengsheng.city.api.response.NewsChannelResponse;
import com.jiechengsheng.city.api.response.NewsDetailResponse;
import com.jiechengsheng.city.api.response.NewsResponse;
import com.jiechengsheng.city.api.response.PageResponse;
import com.jiechengsheng.city.api.response.ParentCategoryResponse;
import com.jiechengsheng.city.api.response.SignListResponse;
import com.jiechengsheng.city.api.response.SuccessResponse;
import com.jiechengsheng.city.api.response.UnReadMessage;
import com.jiechengsheng.city.api.response.UploadFileResponse;
import com.jiechengsheng.city.api.response.UploadFileResponse2;
import com.jiechengsheng.city.api.response.UserCoupon;
import com.jiechengsheng.city.api.response.UserInfoResponse;
import com.jiechengsheng.city.api.response.address.AddressRequest;
import com.jiechengsheng.city.api.response.address.AddressResponse;
import com.jiechengsheng.city.api.response.area.AreaResponse;
import com.jiechengsheng.city.api.response.bills.BillAccount;
import com.jiechengsheng.city.api.response.bills.BillAccountEdit;
import com.jiechengsheng.city.api.response.bills.BillCancelOrder;
import com.jiechengsheng.city.api.response.bills.BillRecommit;
import com.jiechengsheng.city.api.response.bills.BillStatus;
import com.jiechengsheng.city.api.response.bills.BillsChannel;
import com.jiechengsheng.city.api.response.bills.BillsCoupon;
import com.jiechengsheng.city.api.response.bills.BillsOrderDiscount;
import com.jiechengsheng.city.api.response.bills.BillsPlaceOrder;
import com.jiechengsheng.city.api.response.bills.BillsRecord;
import com.jiechengsheng.city.api.response.bills.CallChargeChannel;
import com.jiechengsheng.city.api.response.category.Category;
import com.jiechengsheng.city.api.response.category.UserCategory;
import com.jiechengsheng.city.api.response.collection.MallGoodCollection;
import com.jiechengsheng.city.api.response.collection.MyCollection;
import com.jiechengsheng.city.api.response.feedback.About;
import com.jiechengsheng.city.api.response.feedback.FeedbackCategoryAndQuestion;
import com.jiechengsheng.city.api.response.feedback.FeedbackPost;
import com.jiechengsheng.city.api.response.feedback.FeedbackRecord;
import com.jiechengsheng.city.api.response.footprint.Footprint;
import com.jiechengsheng.city.api.response.gourmet.cart.ShoppingCartResponse;
import com.jiechengsheng.city.api.response.gourmet.dish.DeliveryTime;
import com.jiechengsheng.city.api.response.gourmet.dish.DishDetailResponse;
import com.jiechengsheng.city.api.response.gourmet.order.FoodOrderDetail;
import com.jiechengsheng.city.api.response.gourmet.order.FoodOrderSubmitData;
import com.jiechengsheng.city.api.response.gourmet.order.TakeawayOrderDetail;
import com.jiechengsheng.city.api.response.gourmet.order.TakeawayOrderSubmitData;
import com.jiechengsheng.city.api.response.gourmet.qr.QrResponse;
import com.jiechengsheng.city.api.response.gourmet.restaurant.RestaurantDetailResponse;
import com.jiechengsheng.city.api.response.gourmet.restaurant.RestaurantResponse;
import com.jiechengsheng.city.api.response.gourmet.takeaway.TakeawayDetailResponse;
import com.jiechengsheng.city.api.response.home.HomeChild;
import com.jiechengsheng.city.api.response.hotel.HotelComment;
import com.jiechengsheng.city.api.response.hotel.HotelDetail;
import com.jiechengsheng.city.api.response.hotel.HotelHomeRecommend;
import com.jiechengsheng.city.api.response.hotel.HotelOrderCommitResponse;
import com.jiechengsheng.city.api.response.hotel.HotelOrderDetail;
import com.jiechengsheng.city.api.response.hotel.RoomDetail;
import com.jiechengsheng.city.api.response.integral.IntegralGood;
import com.jiechengsheng.city.api.response.integral.IntegralGoodDetail;
import com.jiechengsheng.city.api.response.integral.IntegralOrderDetail;
import com.jiechengsheng.city.api.response.integral.IntegralPlaceOrderResponse;
import com.jiechengsheng.city.api.response.integral.IntegralRecord;
import com.jiechengsheng.city.api.response.integral.IntegralTag;
import com.jiechengsheng.city.api.response.job.CheckIsNeedUpdatePdf;
import com.jiechengsheng.city.api.response.job.CheckResume;
import com.jiechengsheng.city.api.response.job.CompanyAlbum;
import com.jiechengsheng.city.api.response.job.CompanyCollection;
import com.jiechengsheng.city.api.response.job.CompanyInfo;
import com.jiechengsheng.city.api.response.job.CreateCertificate;
import com.jiechengsheng.city.api.response.job.CreateJobExperience;
import com.jiechengsheng.city.api.response.job.CreateProfileDetail;
import com.jiechengsheng.city.api.response.job.Degree;
import com.jiechengsheng.city.api.response.job.EduDet;
import com.jiechengsheng.city.api.response.job.EduRequest;
import com.jiechengsheng.city.api.response.job.EmployerEmail;
import com.jiechengsheng.city.api.response.job.EmployerRequest;
import com.jiechengsheng.city.api.response.job.Job;
import com.jiechengsheng.city.api.response.job.JobCollection;
import com.jiechengsheng.city.api.response.job.JobDetail;
import com.jiechengsheng.city.api.response.job.JobExperience;
import com.jiechengsheng.city.api.response.job.JobFilter;
import com.jiechengsheng.city.api.response.job.JobNotice;
import com.jiechengsheng.city.api.response.job.JobSendCv;
import com.jiechengsheng.city.api.response.job.ProfileDetail;
import com.jiechengsheng.city.api.response.job.Report;
import com.jiechengsheng.city.api.response.job.ReportRequest;
import com.jiechengsheng.city.api.response.mall.FoodRefundInfo;
import com.jiechengsheng.city.api.response.mall.MallCartGroup;
import com.jiechengsheng.city.api.response.mall.MallCategory;
import com.jiechengsheng.city.api.response.mall.MallCommentCount;
import com.jiechengsheng.city.api.response.mall.MallExpired;
import com.jiechengsheng.city.api.response.mall.MallGood;
import com.jiechengsheng.city.api.response.mall.MallGoodDetail;
import com.jiechengsheng.city.api.response.mall.MallOrderDetail;
import com.jiechengsheng.city.api.response.mall.MallRefundInfo;
import com.jiechengsheng.city.api.response.mall.MallShopCategory;
import com.jiechengsheng.city.api.response.mall.MallShopRecommend;
import com.jiechengsheng.city.api.response.mall.RefundBankSelected;
import com.jiechengsheng.city.api.response.mall.RefundBindRequest;
import com.jiechengsheng.city.api.response.mall.RefundChannel;
import com.jiechengsheng.city.api.response.mall.RefundMethod;
import com.jiechengsheng.city.api.response.mall.RemitId;
import com.jiechengsheng.city.api.response.mall.request.MallAddCart;
import com.jiechengsheng.city.api.response.mall.request.MallCollection;
import com.jiechengsheng.city.api.response.mall.request.MallCommitResponse;
import com.jiechengsheng.city.api.response.mall.request.MallDeliveryRequest;
import com.jiechengsheng.city.api.response.mall.request.MallDeliveryResponse;
import com.jiechengsheng.city.api.response.mall.request.MallOrderCommit;
import com.jiechengsheng.city.api.response.mall.request.MallOrderCoupon;
import com.jiechengsheng.city.api.response.mall.request.MallOrderDefaultCoupon;
import com.jiechengsheng.city.api.response.mall.request.MallShop;
import com.jiechengsheng.city.api.response.mall.request.UnCollection;
import com.jiechengsheng.city.api.response.merchant.MerchantApplyRecord;
import com.jiechengsheng.city.api.response.message.RongCloudUserResponse;
import com.jiechengsheng.city.api.response.message.SystemMessageResponse;
import com.jiechengsheng.city.api.response.order.OrderListResponse;
import com.jiechengsheng.city.api.response.order.RefundOrder;
import com.jiechengsheng.city.api.response.order.bill.BillOrderDetails;
import com.jiechengsheng.city.api.response.order.store.RefundDetail;
import com.jiechengsheng.city.api.response.order.store.RefundDetailMall;
import com.jiechengsheng.city.api.response.order.store.StoreOrderDetail;
import com.jiechengsheng.city.api.response.order.tab.OrderTabResponse;
import com.jiechengsheng.city.api.response.other.CartNumberResponse;
import com.jiechengsheng.city.api.response.pay.PayChannelBindUrl;
import com.jiechengsheng.city.api.response.pay.PayChannelUnbind;
import com.jiechengsheng.city.api.response.pay.PayCounterChannel;
import com.jiechengsheng.city.api.response.pay.PayCounterChannelDetail;
import com.jiechengsheng.city.api.response.recommend.HomeRecommendResponse;
import com.jiechengsheng.city.api.response.search.SearchResultResponse;
import com.jiechengsheng.city.api.response.store.PayChannel;
import com.jiechengsheng.city.api.response.store.StoreDetail;
import com.jiechengsheng.city.api.response.store.StoreGoodDetail;
import com.jiechengsheng.city.api.response.store.StoreGoods;
import com.jiechengsheng.city.api.response.store.StoreOrderInfoResponse;
import com.jiechengsheng.city.api.response.store.StoreRecommend;
import com.jiechengsheng.city.api.response.store.cart.StoreCartResponse;
import com.jiechengsheng.city.api.response.store.comment.StoreCommentCount;
import com.jiechengsheng.city.api.response.store.comment.StoreCommentDetail;
import com.jiechengsheng.city.api.response.travel.TravelChild;
import com.jiechengsheng.city.api.response.travel.TravelDetail;
import com.jiechengsheng.city.api.response.version.VersionResponse;
import com.jiechengsheng.city.bean.CityResponse;
import com.jiechengsheng.city.bean.FollowCategoryRequest;
import com.jiechengsheng.city.bean.OrderSubmitRequest;
import com.jiechengsheng.city.bean.OrderSubmitTakeawayRequest;

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
     * @return 轮播图  1：首页顶部，2：旅游住宿，3：首页 ， 4 mall版 estor商城 7. 水电缴费轮播图
     */
    @GET("commonapi/v2/banners")
    Observable<JcsResponse<ArrayList<BannerResponse>>> getBanners(@Query("type") int type);

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
     * @param recommend  1推荐 0距离最近 不传为默认
     * @return
     */
    @GET("generalapi/v2/infos")
    Observable<JcsResponse<PageResponse<MechanismResponse>>> getMechanismListById3(
            @Query("page") int page,
            @Query("cate_id") String categoryId,
            @Query("search_input") String search,
            @Query("lat") @Nullable Double lat,
            @Query("lng") @Nullable Double lng,
            @Query("area_id") String area_id,
            @Query("recommend") @Nullable Integer recommend

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
     * 发送类型（1：登录，2：注册，3：忘记密码，4：更换手机号，5:其他）
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
            @Query("area_id") String area_id,
            @Query("categoryId") @Nullable Integer categoryId
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
    @GET("commonapi/v2/version")
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
     * 获取分类
     * !--level 3 pid 89 type =1 --
     *
     * @param level 分类级别（一级？二级？三级？）
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
     * @param type 列表类型（0：全部，1：有图，2：最新，3：低分）
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
    Observable<JcsResponse<List<com.jiechengsheng.city.api.response.home.HomeNewsResponse>>> getHomeNewsList();

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
     * 商城评价列表数量
     */
    @GET("generalapi/v2/comments/numbers/{shop_id}")
    Observable<JcsResponse<StoreCommentCount>> getStoreCommentCount(@Path("shop_id") int shop_id);


    /**
     * 美食-外卖取消订单
     */
    @DELETE("restaurantapi/v2/take_out/orders/{order_id}")
    Observable<JcsResponse<JsonElement>> takeawayOrderCancel(@Path("order_id") int order_id);

    /**
     * 美食-外卖退款
     */
    @HTTP(method = "DELETE", path = "restaurantapi/v2/take_out/orders/refund/{order_id}", hasBody = true)
    Observable<JcsResponse<JsonElement>> takeawayOrderRefund(
            @Path("order_id") int order_id,
            @Body RemitId request

    );


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
    @HTTP(method = "DELETE", path = "hotelapi/v2/orders/refund/{order_id}", hasBody = true)
    Observable<JcsResponse<JsonElement>> refundHotelOrder(@Path("order_id") int order_id
//                                                          @Body RemitId request
    );


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
            @Query("lat") @Nullable Double lat,
            @Query("lng") @Nullable Double lng,
            @Query("area_id") @Nullable String area_id

    );


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
    Observable<JcsResponse<PageResponse<MallGood>>> getMallRecommendGood(
            @Query("version") int version,
            @Query("page") int page,
            @Query("categoryId") Integer categoryId);


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
     * 获取商品详情
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
     * @param type 列表类型
     *             1 2 3 4 5
     *             6最新 7有图 8低分 0全部
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
     * 酒店评价数量
     */
    @GET("hotelapi/v2/hotel/{hotel_id}/comment/nums")
    Observable<JcsResponse<MallCommentCount>> hotelCommentCount(
            @Path("hotel_id") int hotel_id,
            @Query("version") String version
    );

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
    Observable<JcsResponse<PageResponse<com.jiechengsheng.city.api.response.collection.MallShopCollection>>> collectionShopList(@Query("page") int page);

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
    Observable<JcsResponse<PageResponse<Coupon>>> couponCenter(@Query("page") int page);


    /**
     * 领取优惠券
     *
     * @param coupon_id 券id
     * @param type      领取类型 1平台券 2商户券
     */
    @POST("estoreapi/v2/coupon")
    Observable<JcsResponse<GetCouponResult>> getCoupon(
            @Query("coupon_id") int coupon_id,
            @Query("type") int type
    );


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
     * 缴费账单选择优惠券
     */
    @GET("billsapi/v2/order_discounts_coupon")
    Observable<JcsResponse<ArrayList<BillsCoupon>>> getBillOrderCoupon(

            @Query("type") int type,
            @Query("module") int module,
            @Query("price") String price,
            @Query("pay_account") String pay_account);


    /**
     * 获取当前店铺优惠券
     */
    @GET("estoreapi/v2/shop_coupon")
    Observable<JcsResponse<ArrayList<Coupon>>> mallShopCoupon(@Query("shop_id") int shop_id);


    /**
     * 商品售后信息
     *
     * @param orderId   单个商品订单id
     * @param refund_id 售后id
     */
    @GET("estoreapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<MallRefundInfo>> mallRefundInfo(
            @Path("orderId") int orderId,
            @Query("refund_id") int refund_id);

    /**
     * 商品售后详情
     *
     * @param orderId   单个商品订单id
     * @param refund_id 售后id
     */
    @GET("estoreapi/v2/order/refunds_info/{orderId}")
    Observable<JcsResponse<MallRefundInfo>> mallRefundOrderDetail(
            @Path("orderId") int orderId,
            @Query("refund_id") int refund_id);

    /**
     * mall商城  取消 申请退款
     */
    @PATCH("estoreapi/v2/order/refunds/{orderId}")
    Observable<JcsResponse<JsonElement>> cancelRefund(
            @Path("orderId") int orderId
    );


    /**
     * 售后订单列表
     */
    @GET("estoreapi/v2/order/refunds_list")
    Observable<JcsResponse<PageResponse<RefundOrder>>> refundOrderList(
            @Query("page") int page);


    /**
     * 批量评价
     */
    @POST("estoreapi/v2/comments_order")
    Observable<JcsResponse<JsonElement>> batchComment(@Body BatchComment batchComment);


    /**
     * estore订单投诉
     */
    @POST("estoreapi/v2/order/complaint")
    Observable<JcsResponse<JsonElement>> mallComplaint(@Body ComplaintRequest batchComment);


    /**
     * 美食外卖订单投诉
     */
    @POST("restaurantapi/v2/order/complaint")
    Observable<JcsResponse<JsonElement>> complaintFood(@Body ComplaintRequest batchComment);


    /**
     * 酒店订单投诉
     */
    @POST("hotelapi/v2/order/complaint")
    Observable<JcsResponse<JsonElement>> complaintHotel(@Body ComplaintRequest batchComment);

    /**
     * 获取 web收银台url
     */
    @POST("commonapi/v2/payment")
    Observable<JcsResponse<PayUrl>> getWebPayUrl(@Body PayUrlGet payUrlGet);


    /**
     * 获取 web 支付状态
     */
    @GET("commonapi/v2/order_status")
    Observable<JcsResponse<PayStatus>> getPayStatus(
            @Query("module") String module,
            @Query("id") int orderId
    );

    /**
     * 用户退款账号列表
     */
    @GET("commonapi/v2/account")
    Observable<JcsResponse<ArrayList<RefundMethod>>> getRefundMethod();


    /**
     * 取消绑定支付方式
     */
    @HTTP(method = "DELETE", path = "commonapi/v2/unbundle", hasBody = true)
    Observable<JcsResponse<JsonElement>> unbindRefundMethod(@Body IdRequest idRequest);


    /**
     * 获取 退款渠道
     */
    @GET("commonapi/v2/x_channel")
    Observable<JcsResponse<ArrayList<RefundChannel>>> getRefundChannel();

    /**
     * 获取 退款银行列表
     */
    @GET("commonapi/v2/bank")
    Observable<JcsResponse<ArrayList<RefundBankSelected>>> getBankList();


    /**
     * 校验验证码
     *
     * @param type              1手机号 2 邮箱
     * @param verification_code 验证码
     * @param phone             手机号
     * @param email             邮箱
     */
    @POST("userapi/v2/verify/code")
    Observable<JcsResponse<JsonElement>> checkVerifyCode(
            @Query("type") int type,
            @Query("verification_code") String verification_code,
            @Query("phone") @Nullable String phone,
            @Query("email") @Nullable String email
    );


    /**
     * 保存用户退款账户信息
     */
    @POST("commonapi/v2/remit_account")
    Observable<JcsResponse<JsonElement>> bindRefundInfo(@Body RefundBindRequest request);


    /**
     * 美食-餐厅退款
     */
    @HTTP(method = "DELETE", path = "restaurantapi/v2/eat_in/orders/refund/{order_id}", hasBody = true)
    Observable<JcsResponse<JsonElement>> delicacyOrderRefund(
            @Path("order_id") int order_id
//            @Body RemitId request
    );


    /**
     * 获取 美食外卖退款信息详情
     *
     * @param type 1美食 2外卖
     */
    @GET("restaurantapi/v2/refunds/{order_id}")
    Observable<JcsResponse<FoodRefundInfo>> getFoodRefundInfo(
            @Path("order_id") int order_id,
            @Query("type") int type
    );


    /**
     * 获取 酒店外卖退款信息详情
     */
    @GET("hotelapi/v2/refunds/{order_id}")
    Observable<JcsResponse<FoodRefundInfo>> getHotelRefundInfo(
            @Path("order_id") int order_id
    );

    /**
     * 缴费渠道列表
     *
     * @param bill_type 账单类型（1-话费，2-水费，3-电费，4-网费）
     */
    @GET("billsapi/v2/bills")
    Observable<JcsResponse<ArrayList<BillsChannel>>> payBillsList(
            @Query("bill_type") int bill_type
    );


    /**
     * 缴费账单统一下单
     */
    @POST("billsapi/v2/bills/orders")
    Observable<JcsResponse<HotelOrderCommitResponse>> billsPlaceOrder(@Body BillsPlaceOrder request);


    /**
     * 缴费记录
     */
    @GET("billsapi/v2/bills/orders")
    Observable<JcsResponse<PageResponse<BillsRecord>>> billsRecord(
            @Query("page") int page);

    /**
     * 缴费取消订单（退款）
     */
    @PUT("billsapi/v2/bills/orders/cancel")
    Observable<JcsResponse<JsonElement>> billsCancelOrder(
            @Body BillCancelOrder request);

    /**
     * 执行账单交易(重新提交)
     */
    @POST("billsapi/v2/bills/transaction")
    Observable<JcsResponse<JsonElement>> billsRecommit(@Body BillRecommit request);

    /**
     * 手机充值执行账单交易(重新提交)
     */
    @POST("billsapi/v2/topups/transaction")
    Observable<JcsResponse<JsonElement>> billsRecommit4Phone(@Body BillRecommit request);


    /**
     * 账单缴费状态
     */
    @GET("billsapi/v2/bills/transaction/status")
    Observable<JcsResponse<BillStatus>> billsStatus(@Query("order_id") int order_id);


    /**
     * 水电订单详情
     */
    @GET("billsapi/v2/bills/orders/{order_id}")
    Observable<JcsResponse<BillOrderDetails>> billOrderDetail(@Path("order_id") int order_id);


    /**
     * 获取 账单退款信息详情
     */
    @GET("billsapi/v2/bills/refund")
    Observable<JcsResponse<FoodRefundInfo>> getBillRefundInfo(
            @Path("order_id") int order_id
    );


    /**
     * 获取 首页推荐数据
     */
    @GET("commonapi/v2/recommends/models")
    Observable<JcsResponse<ArrayList<HomeChild>>> getHomeChild();


    /**
     * 话费充值渠道列表
     */
    @GET("billsapi/v2/topups")
    Observable<JcsResponse<ArrayList<CallChargeChannel>>> callChargesPayBillsList();


    /**
     * 账单缴费获取折扣价格 & 最终支付价格
     *
     * @param module      1-话费，2-水费，3-电费，4-网费
     * @param price       缴费金额
     * @param pay_account 缴费账号
     */
    @GET("billsapi/v2/order_discounts")
    Observable<JcsResponse<BillsOrderDiscount>> billsOrderDiscount(
            @Query("module") int module,
            @Query("price") String price,
            @Query("pay_account") String pay_account,
            @Query("coupon_id") @Nullable Integer coupon_id
    );

    /**
     * 获取水电缴费所有折扣 列表
     *
     * @param module 订单类型（1-话费，2-水费，3-电费，4-网费）
     */
    @GET("billsapi/v2/bills_discounts")
    Observable<JcsResponse<ArrayList<String>>> getBillsDiscountList(
            @Query("module") int module
    );


    /**
     * 活动中心
     *
     * @param type 0全部 1商品 2水 3电 4网 5手机充值
     */
    @GET("integralapi/v2/goods")
    Observable<JcsResponse<PageResponse<IntegralGood>>> getIntegralGood(
            @Query("page") int page,
            @Query("type") int type
    );

    /**
     * 兑换券商品详情
     */
    @GET("integralapi/v2/goods/{id}")
    Observable<JcsResponse<IntegralGoodDetail>> getIntegralGoodDetail(
            @Path("id") int id
    );


    /**
     * 兑换券商品详情
     */
    @POST("integralapi/v2/orders")
    Observable<JcsResponse<IntegralPlaceOrderResponse>> integralPlaceOrder(
            @Body IntegralPlaceOrder request
    );


    /**
     * 兑换券商品详情
     */
    @GET("integralapi/v2/order/{order_id}")
    Observable<JcsResponse<IntegralOrderDetail>> integralOrderDetail(
            @Path("order_id") int order_id
    );


    /**
     * 兑换记录
     *
     * @param page
     * @return
     */
    @GET("integralapi/v2/order")
    Observable<JcsResponse<PageResponse<IntegralRecord>>> integralRecord(
            @Query("page") int page
    );


    /**
     * 职位列表
     */
    @GET("jobapi/v2/jobs")
    Observable<JcsResponse<PageResponse<Job>>> jobList(
            @Query("page") int page,
            @Query("search_input") @Nullable String search_input,

            @Query("salary_type") @Nullable Integer salary_type,
            @Query("min_salary") @Nullable String min_salary,
            @Query("max_salary") @Nullable String max_salary,
            @Query("area") @Nullable String area,
            @Query("company_type") @Nullable String company_type,
            @Query("education_id") @Nullable String education_id,
            @Query("experience") @Nullable String experience,
            @Query("company_id") @Nullable Integer company_id
    );


    /**
     * 职位收藏列表
     */
    @GET("jobapi/v2/resumes/collects")
    Observable<JcsResponse<PageResponse<Job>>> jobCollectionList(
            @Query("page") int page
    );

    /**
     * 公司收藏列表
     */
    @GET("jobapi/v2/resumes/company_collects")
    Observable<JcsResponse<PageResponse<CompanyInfo>>> companyCollectionList(
            @Query("page") int page
    );


    /**
     * 投递和面试列表
     *
     * @param status 0 默认列表 1 面试列表
     */
    @GET("jobapi/v2/send_records")
    Observable<JcsResponse<PageResponse<Job>>> jobCollectionList(
            @Query("page") int page,
            @Query("status") int status
    );


    /**
     * 职位详情
     * /jobapi/v2/jobs/{job_id}
     */
    @GET("jobapi/v2/jobs/{job_id}")
    Observable<JcsResponse<JobDetail>> jobDetail(
            @Path("job_id") int job_id,
            @Query("version") int version
    );


    /**
     * 发送简历
     */
    @POST("jobapi/v2/send/records")
    Observable<JcsResponse<JsonElement>> jobSendCv(
            @Body JobSendCv request
    );


    /**
     * 个人信息详情
     */
    @GET("jobapi/v2/resumes/profile")
    Observable<JcsResponse<ProfileDetail>> profileDetail();

    /**
     * 工作经历
     */
    @GET("jobapi/v2/resumes/experiences")
    Observable<JcsResponse<ArrayList<JobExperience>>> jobExperienceList();

    /**
     * 教育背景
     */
    @GET("jobapi/v2/resumes/educations")
    Observable<JcsResponse<ArrayList<JobExperience>>> eduBackgroundList();


    /**
     * 创建简历个人信息
     */
    @POST("jobapi/v2/resumes/profile")
    Observable<JcsResponse<JsonElement>> createCvProfile(
            @Body CreateProfileDetail request
    );


    /**
     * 修改简历个人信息
     *
     * @param profile_id 个人信息id
     */
    @PUT("jobapi/v2/resumes/profile/{profile_id}")
    Observable<JcsResponse<JsonElement>> modifyCvProfile(
            @Path("profile_id") int profile_id,
            @Body CreateProfileDetail request
    );


    /**
     * 创建工作经历
     */
    @POST("jobapi/v2/resumes/experiences")
    Observable<JcsResponse<JsonElement>> createCvExperiences(
            @Body CreateJobExperience request
    );


    /**
     * 修改工作经历
     *
     * @param experience_id 个人信息id
     */
    @PUT("jobapi/v2/resumes/experiences/{experience_id}")
    Observable<JcsResponse<JsonElement>> modifyCvExperiences(
            @Path("experience_id") int experience_id,
            @Body CreateJobExperience request
    );


    /**
     * 意见反馈分类列表
     */
    @GET("helpapi/v2/help_category")
    Observable<JcsResponse<ArrayList<FeedbackCategoryAndQuestion>>> feedbackCategory();

    /**
     * 意见反馈问题列表
     */
    @GET("helpapi/v2/help_category_question")
    Observable<JcsResponse<ArrayList<FeedbackCategoryAndQuestion>>> feedbackQuestion(
            @Query("category_id") int category_id,
            @Query("question") @Nullable String question
    );


    /**
     * 活动中心标题
     */
    @GET("integralapi/v2/filters")
    Observable<JcsResponse<ArrayList<IntegralTag>>> activityCenterTab();


    /**
     * 发布意见反馈
     */
    @POST("commonapi/v2/feedbacks")
    Observable<JcsResponse<JsonElement>> postFeedback(
            @Body FeedbackPost request
    );


    /**
     * 意见反馈记录
     */
    @GET("commonapi/v2/feedbacks")
    Observable<JcsResponse<PageResponse<FeedbackRecord>>> feedbackRecord(
            @Query("page") int page
    );

    /**
     * 意见反馈记录
     */
    @GET("commonapi/v2/settings")
    Observable<JcsResponse<About>> about();


    /**
     * 收藏职位
     */
    @POST("jobapi/v2/resumes/collects")
    Observable<JcsResponse<JsonElement>> jobCollection(
            @Body JobCollection request
    );



    /**
     * 取消收藏职位
     */
    @HTTP(method = "DELETE", path = "jobapi/v2/resumes/collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> jobDelCollection(
            @Body JobCollection request
    );



    /**
     * 收藏公司
     */
    @POST("jobapi/v2/resumes/company_collects")
    Observable<JcsResponse<JsonElement>> companyCollection(
            @Body CompanyCollection request
    );


    /**
     * 取消收藏公司
     */
    @HTTP(method = "DELETE", path = "jobapi/v2/resumes/company_collects", hasBody = true)
    Observable<JcsResponse<JsonElement>> companyDelCollection(
            @Body CompanyCollection request
    );



    /**
     * 教育背景详情
     */
    @GET("jobapi/v2/resumes/educations/{education_id}")
    Observable<JcsResponse<EduDet>> eduBackgroundDet(
            @Path("education_id") int education_id
    );


    /**
     * 学历列表
     */
    @GET("jobapi/v2/resumes/educations/levels")
    Observable<JcsResponse<ArrayList<Degree>>> degreelList();

    /**
     * 添加教育背景
     */
    @POST("jobapi/v2/resumes/educations")
    Observable<JcsResponse<JsonElement>> addEduBackground(
            @Body EduRequest request
    );


    /**
     * 添加教育背景
     */
    @PUT("jobapi/v2/resumes/educations/{education_id}")
    Observable<JcsResponse<JsonElement>> editEduBackground(
            @Path("education_id") int education_id,
            @Body EduRequest request
    );


    /**
     * 雇主申请
     */
    @POST("jobapi/v2/employers")
    Observable<JcsResponse<JsonElement>> employerApply(
            @Body EmployerRequest request
    );


    /**
     * 举报原因
     */
    @GET("jobapi/v2/jobs/report_titles")
    Observable<JcsResponse<ArrayList<Report>>> reportReason();


    /**
     * 举报职位
     */
    @POST("jobapi/v2/jobs/reports")
    Observable<JcsResponse<JsonElement>> reportJob(@Body ReportRequest request);


    /**
     * 公司详情
     */
    @GET("jobapi/v2/jobs/collect_companies/{company_id}")
    Observable<JcsResponse<CompanyInfo>> companyDetail(@Path("company_id") int company_id);

    /**
     * 公司相册
     */
    @GET("jobapi/v2/jobs/companies/images/{company_id}")
    Observable<JcsResponse<CompanyAlbum>> companyAlbum(@Path("company_id") int company_id);


    /**
     * 删除工作经历
     */
    @DELETE("jobapi/v2/resumes/experiences/{experience_id}")
    Observable<JcsResponse<JsonElement>> deleteExperiences(
            @Path("experience_id") int experience_id
    );

    /**
     * 删除教育背景
     */
    @DELETE("jobapi/v2/resumes/educations/{education_id}")
    Observable<JcsResponse<JsonElement>> deleteEducation(
            @Path("education_id") int education_id
    );


    /**
     * 职位筛选项
     */
    @GET("jobapi/v2/jobs_filter")
    Observable<JcsResponse<JobFilter>> getFilterItem();

    /**
     * 获取水电网缴费默认账号
     *
     * @param module 1 自来水 2电力公司 3互联网
     */
    @GET("billsapi/v2/bills/default_account")
    Observable<JcsResponse<BillAccount>> getBillsDefaultAccount(@Query("module") int module);

    /**
     * 获取水电网历史缴费账号列表
     *
     * @param module 1 自来水 2电力公司 3互联网
     */
    @GET("billsapi/v2/bills/account")
    Observable<JcsResponse<ArrayList<BillAccount>>> getBillsAccountHistory(@Query("module") int module);

    /**
     * 添加水电网历史缴费账号列表
     */
    @POST("billsapi/v2/bills/account")
    Observable<JcsResponse<JsonElement>> addBillsAccount(@Body BillAccountEdit request);

    /**
     * 添加水电网历史缴费账号列表
     */
    @PATCH("billsapi/v2/bills/account/{id}")
    Observable<JcsResponse<JsonElement>> editBillsAccount(
            @Path("id") int id,
            @Body BillAccountEdit request);


    /**
     * 删除水电网历史缴费账号列表
     */
    @DELETE("billsapi/v2/bills/account")
    Observable<JcsResponse<JsonElement>> deleteBillsAccount(@Query("id") int id);


    /**
     * 商品停留时间
     */
    @POST("estoreapi/v2/dwell_time")
    Observable<JcsResponse<JsonElement>> mtjDuration(@Body MtjDuration request);

    /**
     * 统计咨询客服，发送商品
     */
    @POST("estoreapi/v2/consultation")
    Observable<JcsResponse<JsonElement>> mtjClickService(@Body MtjClickService request);

    /**
     * 统计点击首页职位列表进入职位详情
     */
    @POST("jobapi/v2/jobs/collection/daus")
    Observable<JcsResponse<JsonElement>> mtjClickHomeJob(@Body MtjClickHomeJob request);


    /**
     * 获取简历状态更新通知
     */
    @GET("jobapi/v2/jobs/inform")
    Observable<JcsResponse<JobNotice>> getJobNotification();

    /**
     * 简历证书列表
     */
    @GET("jobapi/v2/resumes/certificate")
    Observable<JcsResponse<ArrayList<JobExperience>>> getCvCertificate();


    /**
     * 添加简历证书
     */
    @POST("jobapi/v2/resumes/certificate")
    Observable<JcsResponse<JsonElement>> createCvExperiences(
            @Body CreateCertificate request
    );


    /**
     * 修改简历证书
     *
     * @param certificate_id 个人信息id
     */
    @PUT("jobapi/v2/resumes/certificates/{certificate_id}\n")
    Observable<JcsResponse<JsonElement>> modifyCvCertificate(
            @Path("certificate_id") int certificate_id,
            @Body CreateCertificate request
    );


    /**
     * 删除资格证书
     */
    @DELETE("jobapi/v2/resumes/certificates/{certificate_id}")
    Observable<JcsResponse<JsonElement>> deleteCertificate(
            @Path("certificate_id") int certificate_id
    );


    /**
     * 获取雇主邮箱
     */
    @GET("jobapi/v2/employers")
    Observable<JcsResponse<EmployerEmail>> getEmployersEmail();


    /**
     * 检查简历完整性
     */
    @GET("jobapi/v2/resumes/check_resume")
    Observable<JcsResponse<CheckResume>> checkResume();


    /**
     * 简历生成Pdf
     */
    @POST("jobapi/v2/resumes/pdf")
    Observable<JcsResponse<JsonElement>> generatePdf();

    /**
     * 检查是否需要更新PDF
     */
    @GET("jobapi/v2/resumes/check_pdf")
    Observable<JcsResponse<CheckIsNeedUpdatePdf>> checkIsNeedUpdatePdf();


    /**
     * 收银台支付渠道列表
     */
    @GET("commonapi/v2/payment_channels")
    Observable<JcsResponse<ArrayList<PayCounterChannel>>> getPayCounterChannel();

    /**
     * 支付渠道余额
     */
    @GET("commonapi/v2/payment/account_channel_details")
    Observable<JcsResponse<PayCounterChannelDetail>> getChannelBalance(@Query("channel_code") String channelCode);

    /**
     * 已绑定支付渠道列表
     */
    @GET("commonapi/v2/auth_payment_channels")
    Observable<JcsResponse<ArrayList<PayCounterChannel>>> getBoundChannel();


    /**
     * 取消支付账号绑定
     */
    @POST("commonapi/v2/payment/account_unlinking")
    Observable<JcsResponse<JsonElement>> unBindPayChannel(@Body PayChannelUnbind request);

    /**
     * 已绑定支付渠道列表
     */
    @POST("commonapi/v2/payment/account_linking")
    Observable<JcsResponse<PayChannelBindUrl>> getBindTokenUrl(@Body PayChannelUnbind request);


    /**
     * 支付接口
     */
    @POST("commonapi/v2/payment")
    Observable<JcsResponse<PayUrl>> doWherePay(@Body PayUrlGet request);








}
