package com.jcs.where.home.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Outline;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.HomeBannerBean;
import com.jcs.where.home.adapter.ModulesCategoryAdapter;
import com.jcs.where.home.adapter.TravelStayHotelAdapter;
import com.jcs.where.home.decoration.HomeModulesItemDecoration;
import com.jcs.where.home.model.TravelStayModel;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.GlideRoundTransform;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.jcs.where.widget.JcsTitle;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class TravelStayActivity extends BaseActivity implements OnItemClickListener {
    private XBanner mBanner;
    private RecyclerView mModuleRecycler;
    private RecyclerView mHotelRecycler;
    private ModulesCategoryAdapter mModulesCategoryAdapter;
    private TravelStayHotelAdapter mTravelStayHotelAdapter;

    private JcsTitle mJcsTitle;

    private TravelStayModel mModel;


    @Override
    protected void initView() {
        mBanner = findViewById(R.id.travelStayBanner);
        mJcsTitle = findViewById(R.id.jcsTitle);
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
        getCategories(2,categories);

        mModel.getYouLike(new BaseObserver<List<HotelResponse>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<HotelResponse> hotelResponses) {
                mTravelStayHotelAdapter.getData().clear();
                mTravelStayHotelAdapter.addData(hotelResponses);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });

    }

    private void getCategories(int level, ArrayList<Integer> categories) {
        int s = categories.size();
        int[] c = new int[s];
        for (int i = 0; i < s; i++) {
            c[i] = categories.get(i);
            Log.e("TravelStayActivity","----getCategories---c[i]="+c[i]);
        }
        mModel.getCategories(level, c, new BaseObserver<List<CategoryResponse>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<CategoryResponse> categoryResponses) {
                Log.e("TravelStayActivity","----onNext---");
                for (int i = 0; i < categoryResponses.size(); i++) {
                    Log.e("TravelStayActivity","----onNext---"+categoryResponses.get(i).getName());
                }
                mModulesCategoryAdapter.getData().clear();
                mModulesCategoryAdapter.addData(categoryResponses);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });
    }

    @Override
    protected void bindListener() {
        mModulesCategoryAdapter.setOnItemClickListener(this);
        mJcsTitle.setBackIvClickListener(view -> finish());
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
        mBanner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                .setImageUrls(bannerUrls)
                .setImageLoader(new AbstractUrlLoader() {
                    @Override
                    public void loadImages(Context context, String url, ImageView image) {
//                        Glide.with(context)
//                                .load(url)
//                                .into(image);
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .error(R.drawable.ic_test) //加载失败图片
                                .priority(Priority.HIGH) //优先级
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                                .transform(new GlideRoundTransform(10)); //圆角
                        Glide.with(context).load(url).apply(options).into(image);
                    }

                    @Override
                    public void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType) {
//                        Glide.with(context).asGif().load(url).into(gifImageView);
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .error(R.drawable.ic_test) //加载失败图片
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBanner.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    float radius = Resources.getSystem().getDisplayMetrics().density * 10;
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                }
            });
            mBanner.setClipToOutline(true);
        }

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if(adapter == mModulesCategoryAdapter){
            Log.e("TravelStayActivity","----onItemClick---");
        }
    }
}
