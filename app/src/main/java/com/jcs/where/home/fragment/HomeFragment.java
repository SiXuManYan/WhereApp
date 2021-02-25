package com.jcs.where.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Outline;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.google.gson.JsonObject;
import com.jcs.where.R;
import com.jcs.where.adapter.ModulesAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.HomeNewsResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.convenience.activity.ConvenienceServiceActivity;
import com.jcs.where.features.message.MessageCenterActivity;
import com.jcs.where.features.search.SearchAllActivity;
import com.jcs.where.government.activity.GovernmentMapActivity;
import com.jcs.where.home.activity.TravelStayActivity;
import com.jcs.where.home.adapter.HomeYouLikeAdapter;
import com.jcs.where.home.decoration.HomeModulesItemDecoration;
import com.jcs.where.home.model.HomeModel;
import com.jcs.where.hotel.activity.CityPickerActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.news.NewsActivity;
import com.jcs.where.utils.GlideRoundTransform;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.jcs.where.widget.MessageView;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.yellow_page.activity.YellowPageActivity;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.rong.imlib.RongIMClient;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_OK;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private static final int REQ_SELECT_CITY = 100;
    private View view;
    private XBanner banner3;
    private MyPtrClassicFrameLayout ptrFrame;
    private SimpleMarqueeView marqueeView;
    private RecyclerView homeRv;
    private HomeYouLikeAdapter mHomeYouLikeAdapter;
    private TextView cityNameTv;
    private LinearLayout bannerLl;
    private int refreshBanner = 0;

    private HomeModel mModel;
    private RecyclerView mModuleRecycler;
    private ModulesAdapter mModulesAdapter;
    private LinearLayout mSearchLayout;
    private MessageView message_view;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.rl_title));
        mModel = new HomeModel();
        bannerLl = view.findViewById(R.id.ll_banner);
        mSearchLayout = view.findViewById(R.id.searchLayout);
        ViewGroup.LayoutParams lp;
        lp = bannerLl.getLayoutParams();
        lp.height = getScreenWidth() * 194 / 345;
        bannerLl.setLayoutParams(lp);
        banner3 = view.findViewById(R.id.banner3);
        ptrFrame = view.findViewById(R.id.ptr_frame);
        ptrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
        ptrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getInitHomeData();
                getMessageCount();
            }
        });
        marqueeView = view.findViewById(R.id.simpleMarqueeView);
        homeRv = view.findViewById(R.id.rv_home);
        cityNameTv = view.findViewById(R.id.tv_cityname);
        cityNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CityPickerActivity.class);
                startActivityForResult(intent, REQ_SELECT_CITY);
            }
        });

        mModuleRecycler = view.findViewById(R.id.moduleRecycler);
        int recyclerWidth = getScreenWidth() - getPxFromDp(30);
        mModulesAdapter = new ModulesAdapter(recyclerWidth / 5, getPxFromDp(70));
        mModuleRecycler.addItemDecoration(new HomeModulesItemDecoration());
        mModuleRecycler.setLayoutManager(new GridLayoutManager(getContext(), 5, RecyclerView.VERTICAL, false));
        mModuleRecycler.setAdapter(mModulesAdapter);

        mHomeYouLikeAdapter = new HomeYouLikeAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        homeRv.setLayoutManager(linearLayoutManager);
        homeRv.setAdapter(mHomeYouLikeAdapter);
        message_view = view.findViewById(R.id.message_view);
    }

    private void onModuleItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        //点击了金刚区
            /*
            金刚区模块跳转说明：
            1：政府机构->带地图的综合服务页面
            2：企业黄页->三级联动筛选的综合服务页面
            3：旅游住宿->旅游住宿二级页
            4：便民服务、教育机构、健康&医疗、家政服务->横向二级联动筛选的综合服务页面
            5：金融服务->横向二级联动筛选的综合服务页面（注：分类需获取到Finance分类下的三级分类）
            6：餐饮美食->餐厅列表
            7：线上商店->Comming soon
             */
        //首先判断状态 1：已上线 2：开发中
        ModulesResponse item = mModulesAdapter.getItem(position);
        switch (item.getDev_status()) {
            case 1:
                //根据id做不同的处理
                dealModulesById(item);

                break;
            case 2:
                showToast(getString(R.string.coming_soon));
                break;
        }
    }

    @Override
    protected void initData() {
        String areaId = mModel.getCurrentAreaId();
        if (areaId.equals("3")) {
            // 默认巴郎牙
            cityNameTv.setText(R.string.default_city_name);
        } else {
            CityResponse currentCity = mModel.getCurrentCity(areaId);
            if (currentCity == null) {
                // 默认巴郎牙
                cityNameTv.setText(R.string.default_city_name);
            } else {
                cityNameTv.setText(currentCity.getName());
            }
        }

        // 获取金刚区，猜你喜欢，banner，滚动新闻并一起返回
        getInitHomeData();
        getMessageCount();
    }

    private void getInitHomeData() {
        showLoading();
        mModel.getInitHomeData(new BaseObserver<HomeModel.HomeZipResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                ptrFrame.refreshComplete();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(HomeModel.HomeZipResponse response) {

                stopLoading();
                ptrFrame.refreshComplete();

                // 金刚区
                injectDataToModule(response.getModulesResponses());

                // 猜你喜欢
                injectDataToYouLike(response.getYouLikeResponses());

                // banner
                initBanner(response.getBannerResponses());

                // 滚动新闻
                initNews(response.getHomeNewsResponses());
            }
        });
    }

    private void injectDataToYouLike(List<HotelResponse> youLikeResponses) {
        mHomeYouLikeAdapter.getData().clear();
        mHomeYouLikeAdapter.addData(youLikeResponses);
    }

    private void injectDataToModule(List<ModulesResponse> modulesResponses) {
        mModulesAdapter.getData().clear();
        mModulesAdapter.addData(modulesResponses);
    }

    @Override
    protected void bindListener() {
        mModulesAdapter.setOnItemClickListener(this::onModuleItemClicked);
        mHomeYouLikeAdapter.setOnItemClickListener(this::onYouLickItemClicked);
        message_view.setOnClickListener(this::onMessageLayoutClicked);
        mSearchLayout.setOnClickListener(this::onSearchLayoutClicked);
    }

    private void onSearchLayoutClicked(View view) {
        startActivityAfterLogin(SearchAllActivity.class);
    }

    private void onMessageLayoutClicked(View view) {
        startActivityAfterLogin(MessageCenterActivity.class);
    }

    private void onYouLickItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        JcsCalendarDialog dialog = new JcsCalendarDialog();
        dialog.initCalendar(getContext());
        HotelDetailActivity.goTo(getContext(), mHomeYouLikeAdapter.getItem(position).getId(), dialog.getStartBean(), dialog.getEndBean(), 1, "", "", 1);
    }


    private void initBanner(List<BannerResponse> list) {
        if (refreshBanner > 1) {
            banner3.releaseBanner();
        }
        List<String> bannerUrls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            bannerUrls.add(list.get(i).src);
        }
        banner3.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                .setImageUrls(bannerUrls)
                .setImageLoader(new AbstractUrlLoader() {
                    @Override
                    public void loadImages(Context context, String url, ImageView image) {
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .error(R.mipmap.ic_glide_default) //加载失败图片
                                .priority(Priority.HIGH) //优先级
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                                .transform(new GlideRoundTransform(10)); //圆角
                        Glide.with(context).load(url).apply(options).into(image);
                    }

                    @Override
                    public void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType) {
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .error(R.mipmap.ic_glide_default) //加载失败图片
                                .priority(Priority.HIGH) //优先级
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                                .transform(new GlideRoundTransform(10)); //圆角
                        Glide.with(context).load(url).apply(options).into(gifImageView);
                    }
                })
                .setTitleHeight(50)
                .isAutoPlay(true)
                .setDelay(5000)
                .setUpIndicators(R.drawable.ic_selected, R.drawable.ic_unselected)
                .setUpIndicatorSize(20, 6)
                .setIndicatorGravity(XBanner.INDICATOR_CENTER)
                .setBannerPageListener(new XBanner.BannerPageListener() {
                    @Override
                    public void onBannerClick(int item) {
                    }

                    @Override
                    public void onBannerDragging(int item) {

                    }

                    @Override
                    public void onBannerIdle(int item) {

                    }
                })
                .start();
        banner3.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                float radius = Resources.getSystem().getDisplayMetrics().density * 10;
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        banner3.setClipToOutline(true);
    }

    private void initNews(List<HomeNewsResponse> list) {
        final List<String> messageList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            messageList.add(list.get(i).getTitle());
        }
        SimpleMF<String> marqueeFactory = new SimpleMF(getContext());
        marqueeFactory.setData(messageList);
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.startFlipping();
        marqueeView.setVisibility(View.VISIBLE);

        marqueeView.setOnItemClickListener(new OnItemClickListener<TextView, String>() {
            @Override
            public void onItemClickListener(TextView mView, String mData, int mPosition) {
                Intent intent = new Intent(getContext(), NewsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 处理金刚圈item点击后的跳转逻辑
     *
     * @param item item
     */
    private void dealModulesById(ModulesResponse item) {
        switch (item.getId()) {
            case 1:
                toActivity(GovernmentMapActivity.class);
                break;
            case 2:
                // 跳转到企业黄页
                Intent toYellowPage = new Intent(getContext(), YellowPageActivity.class);

                // 传递企业黄页一级分类id
                ArrayList<Integer> categories = (ArrayList<Integer>) item.getCategories();
                toYellowPage.putIntegerArrayListExtra(YellowPageActivity.K_CATEGORIES, categories);

                // 传递企业黄页的id
                startActivity(toYellowPage);
                break;
            case 3:
                Intent toTravelStay = new Intent(getContext(), TravelStayActivity.class);
                toTravelStay.putIntegerArrayListExtra(TravelStayActivity.K_CATEGORY_IDS, (ArrayList<Integer>) item.getCategories());
                startActivity(toTravelStay);
                break;
            case 4:// 便民服务
            case 5:// 金融服务
            case 6:// 教育机构
            case 7:// 医疗健康
            case 8:// 家政服务
                String convenienceCategoryId = item.getCategories().toString();
                String convenienceName = item.getName();
                toActivity(ConvenienceServiceActivity.class,
                        new IntentEntry(ConvenienceServiceActivity.K_CATEGORIES, convenienceCategoryId),
                        new IntentEntry(ConvenienceServiceActivity.K_SERVICE_NAME, convenienceName)
                );
                break;
            case 9:// 餐厅列表
            case 10:// 线上商店
                showComing();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        banner3.releaseBanner();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SELECT_CITY) {
            cityNameTv.setText(data.getStringExtra(CityPickerActivity.EXTRA_CITY));
        }
    }

    @Override
    protected boolean needChangeStatusBarStatus() {
        return true;
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.widthPixels;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getMessageCount();
        }
    }

    /**
     * 获取消息数量
     */
    private void getMessageCount() {

        mModel.getUnreadMessageCount(new BaseObserver<JsonObject>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            protected void onSuccess(JsonObject response) {
                int apiUnreadMessageCount = 0;
                if (response.has("count")) {
                    apiUnreadMessageCount = response.get("count").getAsInt();
                }


                int finalApiUnreadMessageCount = apiUnreadMessageCount;
                RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {

                    @Override
                    public void onSuccess(Integer rongMessageCount) {

                        if (rongMessageCount == null) {
                            rongMessageCount = 0;
                        }
                        message_view.setMessageCount(finalApiUnreadMessageCount + rongMessageCount);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });


            }
        });


    }


}
