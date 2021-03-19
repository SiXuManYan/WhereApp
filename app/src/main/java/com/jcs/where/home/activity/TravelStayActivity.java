package com.jcs.where.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.adapter.ModulesCategoryAdapter;
import com.jcs.where.adapter.TravelStayHotelAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.bean.HomeBannerBean;
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.home.decoration.HomeModulesItemDecoration;
import com.jcs.where.hotel.activity.HotelActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.model.TravelStayModel;
import com.jcs.where.travel.TravelMapActivity;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.stx.xhb.androidx.XBanner;
import com.stx.xhb.androidx.entity.BaseBannerInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 页面-旅游住宿
 */
public class TravelStayActivity extends BaseActivity {
    public static final String K_CATEGORY_IDS = "categoryIds";

    private XBanner mBanner;
    private RecyclerView mModuleRecycler;
    private RecyclerView mHotelRecycler;
    private ModulesCategoryAdapter mModulesCategoryAdapter;
    private TravelStayHotelAdapter mTravelStayHotelAdapter;

    private TravelStayModel mModel;

    private final String HOTEL_STAY = "107";

    @Override
    protected void initView() {
        mBanner = findViewById(R.id.travelStayBanner);
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
        mModulesCategoryAdapter = new ModulesCategoryAdapter();
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
                mBanner.setBannerData(R.layout.banner_travel_stay, bannerResponses);
                mBanner.loadImage((banner, model, view, position) -> GlideUtil.load(TravelStayActivity.this, ((BaseBannerInfo) model).getXBannerUrl().toString(), (ImageView) view));
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
            Uri uri = Uri.parse(Html5Url.TOURISM_MANAGEMENT_URL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else {
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

    private void initBanner(List<HomeBannerBean> list) {

        List<String> bannerUrls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            bannerUrls.add(list.get(i).src);
        }


    }

}
