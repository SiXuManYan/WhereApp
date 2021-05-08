package com.jcs.where.home.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.adapter.ModulesCategoryAdapter2;
import com.jcs.where.adapter.TravelStayHotelAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.currency.WebViewActivity;
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.home.decoration.HomeModulesItemDecoration;
import com.jcs.where.hotel.activity.HotelActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.model.TravelStayModel;
import com.jcs.where.travel.TravelMapActivity;
import com.jcs.where.utils.GlideRoundTransform;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.jcs.where.widget.calendar.JcsCalendarDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import pl.droidsonroids.gif.GifImageView;

/**
 * 页面-旅游住宿
 */
public class TravelStayActivity extends BaseActivity {
    public static final String K_CATEGORY_IDS = "categoryIds";

    private XBanner banner3;
    private RecyclerView mModuleRecycler;
    private RecyclerView mHotelRecycler;
    private ModulesCategoryAdapter2 mModulesCategoryAdapter;
    private TravelStayHotelAdapter mTravelStayHotelAdapter;

    private TravelStayModel mModel;

    private final String HOTEL_STAY = "107";

    @Override
    protected void initView() {
        banner3 = findViewById(R.id.banner3);
        ViewGroup.LayoutParams layoutParams = banner3.getLayoutParams();
        layoutParams.height = getScreenWidth() * 161 / 345;
        banner3.setLayoutParams(layoutParams);

        initModuleRecycler();
        initHotelRecycler();

    }

    /***
     * 初始化展示推荐酒店的列表
     */
    private void initHotelRecycler() {
        mHotelRecycler = findViewById(R.id.hotelRecycler);
        mTravelStayHotelAdapter = new TravelStayHotelAdapter(R.layout.item_travel_stay_hotel);
        mHotelRecycler.setLayoutManager(new LinearLayoutManager(this));
        mHotelRecycler.setAdapter(mTravelStayHotelAdapter);
    }

    /**
     * 初始化展示二级分类选项的列表
     */
    private void initModuleRecycler() {
        mModuleRecycler = findViewById(R.id.moduleRecycler);
        mModulesCategoryAdapter = new ModulesCategoryAdapter2();
        mModuleRecycler.addItemDecoration(new HomeModulesItemDecoration());
        mModuleRecycler.setLayoutManager(new GridLayoutManager(this, 5, RecyclerView.VERTICAL, false));
        mModuleRecycler.setAdapter(mModulesCategoryAdapter);
    }

    @Override
    protected void initData() {
        mModel = new TravelStayModel();
        //获取二级分类选项
        ArrayList<Integer> categories = getIntent().getIntegerArrayListExtra(K_CATEGORY_IDS);
        getCategories(2, categories);

        mModel.getYouLike(new BaseObserver<List<HotelResponse>>() {
            @Override
            public void onSuccess(@NonNull List<HotelResponse> hotelResponses) {
                mTravelStayHotelAdapter.getData().clear();
                mTravelStayHotelAdapter.addData(hotelResponses);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }
        });

        mModel.getBanners(2, new BaseObserver<List<BannerResponse>>() {
            @Override
            public void onSuccess(@NonNull List<BannerResponse> bannerResponses) {


                if (bannerResponses.isEmpty()) {
                    return;
                }

                List<String> bannerUrls = new ArrayList<>();
                for (int i = 0; i < bannerResponses.size(); i++) {
                    bannerUrls.add(bannerResponses.get(i).src);
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


            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }
        });

    }

    private void getCategories(int level, ArrayList<Integer> categories) {
        mModel.getCategories(level, categories, new BaseObserver<List<CategoryResponse>>() {
            @Override
            public void onSuccess(@NonNull List<CategoryResponse> categoryResponses) {


                CategoryResponse nativeData = new CategoryResponse();
                nativeData.isNativeWebType = true;
                categoryResponses.add(categoryResponses.size(), nativeData);
                mModulesCategoryAdapter.setNewInstance(categoryResponses);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {

                CategoryResponse nativeData = new CategoryResponse();
                nativeData.isNativeWebType = true;
                List<CategoryResponse> categoryResponses = new ArrayList<>();
                categoryResponses.add(categoryResponses.size(), nativeData);
                mModulesCategoryAdapter.setNewInstance(categoryResponses);

            }
        });
    }

    @Override
    protected void bindListener() {
        mModulesCategoryAdapter.setOnItemClickListener(this::onModulesCategoryItemClicked);
        mTravelStayHotelAdapter.setOnItemClickListener(this::onHotelItemClicked);
    }

    private void onHotelItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        JcsCalendarDialog dialog = new JcsCalendarDialog();
        dialog.initCalendar(this);
        HotelDetailActivity.goTo(this, mTravelStayHotelAdapter.getItem(position).getId(), dialog.getStartBean(), dialog.getEndBean(), 1, "", "", 1);
    }

    private void onModulesCategoryItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        CategoryResponse item = mModulesCategoryAdapter.getItem(position);
        if (item.isNativeWebType) {
//            Uri uri = Uri.parse(Html5Url.TOURISM_MANAGEMENT_URL);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);

            WebViewActivity.goTo(this,Html5Url.TOURISM_MANAGEMENT_URL);

        } else {
            String id = item.getId();
            switch (id) {
                case HOTEL_STAY:
                    toActivity(HotelActivity.class, new IntentEntry(HotelActivity.K_CATEGORY_ID, id));
                    break;
                default:
                    toActivity(TravelMapActivity.class, new IntentEntry(HotelActivity.K_CATEGORY_ID, id));
                    break;
            }
        }


    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_travel_stay;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        banner3.releaseBanner();

    }


}
