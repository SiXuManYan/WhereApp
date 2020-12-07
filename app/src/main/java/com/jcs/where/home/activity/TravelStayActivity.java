package com.jcs.where.home.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.bean.HomeBannerBean;
import com.jcs.where.home.adapter.ModulesCategoryAdapter;
import com.jcs.where.home.adapter.TravelStayHotelAdapter;
import com.jcs.where.home.decoration.HomeModulesItemDecoration;
import com.jcs.where.home.model.TravelStayModel;
import com.stx.xhb.androidx.XBanner;
import com.stx.xhb.androidx.entity.BaseBannerInfo;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.view.ToastUtils;

public class TravelStayActivity extends BaseActivity implements OnItemClickListener {
    private XBanner mBanner;
    private RecyclerView mModuleRecycler;
    private RecyclerView mHotelRecycler;
    private ModulesCategoryAdapter mModulesCategoryAdapter;
    private TravelStayHotelAdapter mTravelStayHotelAdapter;

    private TravelStayModel mModel;

    private final int HOTEL_STAY = 107;

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
        mModulesCategoryAdapter = new ModulesCategoryAdapter(R.layout.item_home_modules);
        mModuleRecycler.addItemDecoration(new HomeModulesItemDecoration());
        mModuleRecycler.setLayoutManager(new GridLayoutManager(this, 5, RecyclerView.VERTICAL, false));
        mModuleRecycler.setAdapter(mModulesCategoryAdapter);
    }

    @Override
    protected void initData() {
        mModel = new TravelStayModel();
        //获取二级分类选项
        ArrayList<Integer> categories = getIntent().getIntegerArrayListExtra("categories");
        getCategories(2, categories);

        mModel.getYouLike(new BaseObserver<List<HotelResponse>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<HotelResponse> hotelResponses) {
                mTravelStayHotelAdapter.getData().clear();
                mTravelStayHotelAdapter.addData(hotelResponses);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                ToastUtils.showLong(TravelStayActivity.this, errorResponse.getErrMsg());
            }
        });

        mModel.getBanners(2, new BaseObserver<List<BannerResponse>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<BannerResponse> bannerResponses) {
                mBanner.setBannerData(R.layout.banner_travel_stay, bannerResponses);
                mBanner.loadImage((banner, model, view, position) -> Glide.with(TravelStayActivity.this).load(((BaseBannerInfo) model).getXBannerUrl()).into((ImageView) view));
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                ToastUtils.showLong(TravelStayActivity.this, errorResponse.getErrMsg());
            }
        });

    }

    private void getCategories(int level, ArrayList<Integer> categories) {
        int s = categories.size();
        int[] c = new int[s];
        for (int i = 0; i < s; i++) {
            c[i] = categories.get(i);
            Log.e("TravelStayActivity", "----getCategories---c[i]=" + c[i]);
        }
        mModel.getCategories(level, c, new BaseObserver<List<CategoryResponse>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<CategoryResponse> categoryResponses) {
                Log.e("TravelStayActivity", "----onNext---");
                for (int i = 0; i < categoryResponses.size(); i++) {
                    Log.e("TravelStayActivity", "----onNext---" + categoryResponses.get(i).getName());
                }
                mModulesCategoryAdapter.getData().clear();
                mModulesCategoryAdapter.addData(categoryResponses);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                ToastUtils.showLong(TravelStayActivity.this, errorResponse.getErrMsg());
            }
        });
    }

    @Override
    protected void bindListener() {
        mModulesCategoryAdapter.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (adapter == mModulesCategoryAdapter) {
            CategoryResponse item = mModulesCategoryAdapter.getItem(position);
            int id = item.getId();
            switch (id) {
                case HOTEL_STAY:
                    toActivity(HotelActivity.class, new IntentEntry("categoryId", String.valueOf(id)));
                    break;
            }
        }
    }
}
