package com.jcs.where.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Outline;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.adapter.ModulesAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.HomeNewsResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.bean.BusinessBean;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HomeBannerBean;
import com.jcs.where.bean.HomeNewsBean;
import com.jcs.where.convenience.activity.ConvenienceServiceActivity;
import com.jcs.where.government.activity.GovernmentMapActivity;
import com.jcs.where.home.activity.TravelStayActivity;
import com.jcs.where.home.adapter.HomeYouLikeAdapter;
import com.jcs.where.home.decoration.HomeModulesItemDecoration;
import com.jcs.where.home.model.HomeModel;
import com.jcs.where.hotel.activity.CityPickerActivity;
import com.jcs.where.hotel.activity.HotelDetailActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.GlideRoundTransform;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.yellow_page.activity.YellowPageActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.annotations.NonNull;
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
    private RelativeLayout mMessageLayout;
    private LinearLayout mSearchLayout;

    @Override
    protected void initView(View view) {
        mModel = new HomeModel();
        bannerLl = view.findViewById(R.id.ll_banner);
        mMessageLayout = view.findViewById(R.id.rl_message);
        mSearchLayout = view.findViewById(R.id.searchLayout);
        ViewGroup.LayoutParams lp;
        lp = bannerLl.getLayoutParams();
        lp.height = getScreenWidth() * 100 / 345;
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
                getBannerData();
                getNewsData();
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
    }

    private void getInitHomeData() {
        showLoading();
        mModel.getInitHomeData(new BaseObserver<HomeModel.HomeZipResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NotNull HomeModel.HomeZipResponse homeZipResponse) {
                stopLoading();

                // 金刚区
                injectDataToModule(homeZipResponse.getModulesResponses());

                // 猜你喜欢
                injectDataToYouLike(homeZipResponse.getYouLikeResponses());

                // banner
                initBanner(homeZipResponse.getBannerResponses());

                // 滚动新闻
                initNews(homeZipResponse.getHomeNewsResponses());
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
        mMessageLayout.setOnClickListener(this::onMessageLayoutClicked);
        mSearchLayout.setOnClickListener(this::onSearchLayoutClicked);
    }

    private void onSearchLayoutClicked(View view) {
        showComing();
    }

    private void onMessageLayoutClicked(View view) {
        showComing();
    }

    private void onYouLickItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        JcsCalendarDialog dialog = new JcsCalendarDialog();
        dialog.initCalendar(getContext());
        HotelDetailActivity.goTo(getContext(), mHomeYouLikeAdapter.getItem(position).getId(), dialog.getStartBean(), dialog.getEndBean(), 1, "", "", 1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    /**
     * 获得金刚圈的信息
     */
    private void getModules() {
        mModel.getModules(new BaseObserver<List<ModulesResponse>>() {
            @Override
            public void onNext(@NonNull List<ModulesResponse> modulesResponses) {
                mModulesAdapter.getData().clear();
                mModulesAdapter.addData(modulesResponses);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }
        });
    }

    private void getBannerData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "commonapi/v1/banners", null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    ptrFrame.refreshComplete();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<HomeBannerBean>>() {
                    }.getType();
                    List<HomeBannerBean> list = gson.fromJson(result, type);
                    refreshBanner = refreshBanner + 1;
//                    initBanner(list);
                } else {
                    ptrFrame.refreshComplete();
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                showToast(e.getMessage());
            }
        });

    }

    private void getNewsData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "newsapi/v1/news/notices?notice_num=10", null, "", TokenManager.get().getToken(getContext()), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    ptrFrame.refreshComplete();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<HomeNewsBean>>() {
                    }.getType();
                    List<HomeNewsBean> list = gson.fromJson(result, type);
//                    initNews(list);
                } else {
                    ptrFrame.refreshComplete();
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                showToast(e.getMessage());
            }
        });

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
            banner3.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    float radius = Resources.getSystem().getDisplayMetrics().density * 10;
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                }
            });
            banner3.setClipToOutline(true);
        }

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
                // TODO 新闻未做
                showComing();
//                Intent intent = new Intent(getContext(), NewsActivity.class);
//                startActivity(intent);
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

    private class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        //1.按钮 2.直播
        //设置常量
        //横向附近
        private static final int TYPE_NEARBY_TRANSVERSE = 1;
        //竖向附件
        private static final int TYPE_NEARBY_VERICAL = 2;
        private final Context context;
        //布局标识集合
        private final List<Integer> typeList;


        public HomeRecyclerViewAdapter(Context context, List<Integer> typeList) {
            this.context = context;
            this.typeList = typeList;
        }


        @Override
        public int getItemViewType(int position) {
            if (typeList.get(position) == 1) {
                return TYPE_NEARBY_TRANSVERSE;
            } else if (typeList.get(position) == 2) {
                return TYPE_NEARBY_VERICAL;
            }
            return 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_NEARBY_TRANSVERSE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transversenearby, parent, false);
                NebaryTransverseViewHolder nebaryTransverseViewHolder = new NebaryTransverseViewHolder(view);
                return nebaryTransverseViewHolder;
            } else if (viewType == TYPE_NEARBY_VERICAL) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearbyverical, parent, false);
                NearbyVericalViewHolder nearbyVericalViewHolder = new NearbyVericalViewHolder(view);
                return nearbyVericalViewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NebaryTransverseViewHolder) {
                initNebaryTransverse((NebaryTransverseViewHolder) holder);
            } else if (holder instanceof NearbyVericalViewHolder) {
                initNearbyVertical((NearbyVericalViewHolder) holder);
            }
        }

        private void initNebaryTransverse(NebaryTransverseViewHolder holder) {
            List<BusinessBean> list = new ArrayList<>();
            BusinessBean businessBean = new BusinessBean();
            businessBean.setImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536187&di=4d4f79fee6c1203e41f73bfbce99f596&imgtype=0&src=http%3A%2F%2Fwww.cnr.cn%2F2013qcpd%2Fzsgz%2F201403%2FW020140327358771316323.jpg");
            businessBean.setName("Batanes North");
            businessBean.setScore("4.9分");
            businessBean.setDetail("距离各大景点近,自助");
            List<String> list1 = new ArrayList<>();
            list1.add("商务出行");
            list1.add("供应早餐");
            businessBean.setProject(list1);
            businessBean.setPrice("245");
            businessBean.setDistance("< 2km");
            list.add(businessBean);

            BusinessBean businessBean1 = new BusinessBean();
            businessBean1.setImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536187&di=4d4f79fee6c1203e41f73bfbce99f596&imgtype=0&src=http%3A%2F%2Fwww.cnr.cn%2F2013qcpd%2Fzsgz%2F201403%2FW020140327358771316323.jpg");
            businessBean1.setName("Batanes North");
            businessBean1.setScore("4.9分");
            businessBean1.setDetail("距离各大景点近,自助");
            List<String> list2 = new ArrayList<>();
            list2.add("供应早餐");
            businessBean1.setProject(list2);
            businessBean1.setPrice("245");
            businessBean1.setDistance("< 2km");
            list.add(businessBean1);

            BusinessBean businessBean2 = new BusinessBean();
            businessBean2.setImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536187&di=4d4f79fee6c1203e41f73bfbce99f596&imgtype=0&src=http%3A%2F%2Fwww.cnr.cn%2F2013qcpd%2Fzsgz%2F201403%2FW020140327358771316323.jpg");
            businessBean2.setName("Batanes North");
            businessBean2.setScore("4.9分");
            businessBean2.setDetail("距离各大景点近,自助");
            List<String> list3 = new ArrayList<>();
            list3.add("商务出行");
            list3.add("供应早餐");
            businessBean2.setProject(list2);
            businessBean2.setPrice("245");
            businessBean2.setDistance("< 2km");
            list.add(businessBean2);


            BusinessBean businessBean3 = new BusinessBean();
            businessBean3.setImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536187&di=4d4f79fee6c1203e41f73bfbce99f596&imgtype=0&src=http%3A%2F%2Fwww.cnr.cn%2F2013qcpd%2Fzsgz%2F201403%2FW020140327358771316323.jpg");
            businessBean3.setName("Batanes North");
            businessBean3.setScore("4.9分");
            businessBean3.setDetail("距离各大景点近,自助");
            List<String> list4 = new ArrayList<>();
            list4.add("供应早餐");
            businessBean3.setProject(list2);
            businessBean3.setPrice("245");
            businessBean3.setDistance("< 2km");
            list.add(businessBean3);
            NearbyTransverseAdapter nearbyTransverseAdapter = new NearbyTransverseAdapter(R.layout.item_homebusinessone);
            holder.businessRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            nearbyTransverseAdapter.getData().clear();
            nearbyTransverseAdapter.addData(list);
            holder.businessRv.setAdapter(nearbyTransverseAdapter);

        }

        private void initNearbyVertical(NearbyVericalViewHolder holder) {

            List<BusinessBean> list = new ArrayList<>();
            BusinessBean businessBean = new BusinessBean();
            list.add(businessBean);
            BusinessBean businessBean1 = new BusinessBean();
            list.add(businessBean1);
            BusinessBean businessBean2 = new BusinessBean();
            list.add(businessBean2);
            BusinessBean businessBean3 = new BusinessBean();
            list.add(businessBean3);
            BusinessBean businessBean4 = new BusinessBean();
            list.add(businessBean4);
            BusinessBean businessBean5 = new BusinessBean();
            list.add(businessBean5);

            NearbyVerticalAdapter nearbyVerticalAdapter = new NearbyVerticalAdapter(R.layout.item_hotellist);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            holder.businessRv.setLayoutManager(linearLayoutManager);
            nearbyVerticalAdapter.getData().clear();
            nearbyVerticalAdapter.addData(list);
            holder.businessRv.setAdapter(nearbyVerticalAdapter);
        }

        @Override
        public int getItemCount() {
            return typeList.size();
        }

        public class NebaryTransverseViewHolder extends RecyclerView.ViewHolder {

            public TextView titleTv, seeMoreTv;
            public RecyclerView businessRv;

            public NebaryTransverseViewHolder(View itemView) {
                super(itemView);
                titleTv = (TextView) itemView.findViewById(R.id.tv_title);
                businessRv = (RecyclerView) itemView.findViewById(R.id.rv_business);
                seeMoreTv = (TextView) itemView.findViewById(R.id.tv_seemore);
            }
        }

        public class NearbyVericalViewHolder extends RecyclerView.ViewHolder {

            public TextView titleTv, seeMoreTv;
            public RecyclerView businessRv;

            public NearbyVericalViewHolder(View itemView) {
                super(itemView);
                titleTv = (TextView) itemView.findViewById(R.id.tv_title);
                businessRv = (RecyclerView) itemView.findViewById(R.id.rv_business);
                seeMoreTv = (TextView) itemView.findViewById(R.id.tv_seemore);
            }
        }
    }

    private class NearbyTransverseAdapter extends BaseQuickAdapter<BusinessBean, BaseViewHolder> {


        public NearbyTransverseAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, BusinessBean data) {
            RoundedImageView roundedImageView = baseViewHolder.findView(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImg())) {
                Glide.with(getContext()).load(data.getImg()).into(roundedImageView);
            } else {
                roundedImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView scoreTv = baseViewHolder.findView(R.id.tv_score);
            scoreTv.setText(data.getScore());
            TextView nameTv = baseViewHolder.findView(R.id.tv_name);
            nameTv.setText(data.getName());
            TextView detailTv = baseViewHolder.findView(R.id.tv_detail);
            detailTv.setText(data.getDetail());
            TextView lableOneTv = baseViewHolder.findView(R.id.tv_lableone);
            TextView lableTwoTv = baseViewHolder.findView(R.id.tv_labletwo);
            if (data.getProject().size() > 1) {
                lableOneTv.setText(data.getProject().get(0));
                lableTwoTv.setText(data.getProject().get(1));
            } else {
                lableOneTv.setText(data.getProject().get(0));
                lableTwoTv.setVisibility(View.GONE);
            }
            TextView priceTv = baseViewHolder.findView(R.id.tv_price);
            priceTv.setText(data.getPrice());
            TextView distanceTv = baseViewHolder.findView(R.id.tv_distance);
            distanceTv.setText("距您" + data.getDistance());
        }
    }

    private class NearbyVerticalAdapter extends BaseQuickAdapter<BusinessBean, BaseViewHolder> {


        public NearbyVerticalAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, BusinessBean businessBean) {
            baseViewHolder.setText(R.id.tv_distance, "<1.5Km");
        }
    }

}
