package com.jcs.where.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.bean.BusinessBean;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HomeBannerBean;
import com.jcs.where.bean.HomeNewsBean;
import com.jcs.where.home.activity.TravelStayActivity;
import com.jcs.where.home.adapter.ModulesAdapter;
import com.jcs.where.home.decoration.HomeModulesItemDecoration;
import com.jcs.where.home.model.HomeModel;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.GlideRoundTransform;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.annotations.NonNull;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_OK;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements com.chad.library.adapter.base.listener.OnItemClickListener {

    private static final int REQ_SELECT_CITY = 100;
    private View view;
    private XBanner banner3;
    private MyPtrClassicFrameLayout ptrFrame;
    private SimpleMarqueeView marqueeView;
    private RecyclerView homeRv;
    private TextView cityNameTv;
    private LinearLayout bannerLl;
    private int refreshBanner = 0;

    private HomeModel mModel;
    private RecyclerView mModuleRecycler;
    private ModulesAdapter mModulesAdapter;

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mModel = new HomeModel();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        bannerLl = V.f(view, R.id.ll_banner);
        ViewGroup.LayoutParams lp;
        lp = bannerLl.getLayoutParams();
        lp.height = getScreenWidth() * 100 / 345;
        bannerLl.setLayoutParams(lp);
        banner3 = V.f(view, R.id.banner3);
        ptrFrame = V.f(view, R.id.ptr_frame);
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
        marqueeView = V.f(view, R.id.simpleMarqueeView);
        homeRv = V.f(view, R.id.rv_home);
        cityNameTv = V.f(view, R.id.tv_cityname);
        cityNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CityPickerActivity.class);
                startActivityForResult(intent, REQ_SELECT_CITY);
            }
        });

        mModuleRecycler = view.findViewById(R.id.moduleRecycler);
        mModulesAdapter = new ModulesAdapter(R.layout.item_home_modules);
        mModuleRecycler.addItemDecoration(new HomeModulesItemDecoration());
        mModuleRecycler.setLayoutManager(new GridLayoutManager(getContext(), 5, RecyclerView.VERTICAL, false));
        mModuleRecycler.setAdapter(mModulesAdapter);
        mModulesAdapter.setOnItemClickListener(this);
        getBannerData();
        getNewsData();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getModules();
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
            public void onError(@NonNull Throwable e) {
                ToastUtils.showLong(getContext(), e.getMessage());
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
                    initBanner(list);
                } else {
                    ptrFrame.refreshComplete();
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(getContext(), errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(getContext(), e.getMessage());
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
                    initNews(list);
                } else {
                    ptrFrame.refreshComplete();
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(getContext(), errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(getContext(), e.getMessage());
            }
        });

    }

    private void initBanner(List<HomeBannerBean> list) {
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

    private void initNews(List<HomeNewsBean> list) {
        final List<String> messageList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            messageList.add(list.get(i).title);
        }
        SimpleMF<String> marqueeFactory = new SimpleMF(getContext());
        marqueeFactory.setData(messageList);
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.startFlipping();
        marqueeView.setVisibility(View.VISIBLE);

        marqueeView.setOnItemClickListener(new OnItemClickListener<TextView, String>() {
            @Override
            public void onItemClickListener(TextView mView, String mData, int mPosition) {
                ToastUtils.showLong(getContext(), messageList.get(mPosition));
            }
        });
    }

    private void initView() {


        List<Integer> typeList = new ArrayList<>();
        typeList.add(1);
        typeList.add(2);
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(getContext(), typeList);
        homeRv.removeAllViews();
        adapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        homeRv.setLayoutManager(linearLayoutManager);
        homeRv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(@androidx.annotation.NonNull com.chad.library.adapter.base.BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull View view, int position) {
        if (adapter == mModulesAdapter) {
            //点击了金刚圈
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
                    ToastUtils.showLong(getContext(), "开发中");
                    break;
            }
        }
    }

    /**
     * 处理金刚圈item点击后的跳转逻辑
     *
     * @param item item
     */
    private void dealModulesById(ModulesResponse item) {
        switch (item.getId()) {
            case 1:
                ToastUtils.showLong(getContext(), "带地图的综合服务页面");
                break;
            case 2:
                ToastUtils.showLong(getContext(), "三级联动筛选的综合服务页面");
                break;
            case 3:
                Intent toTravelStay = new Intent(getContext(), TravelStayActivity.class);
                toTravelStay.putIntegerArrayListExtra("categories", (ArrayList<Integer>) item.getCategories());
                startActivity(toTravelStay);
                break;
            case 4:
                ToastUtils.showLong(getContext(), "横向二级联动筛选的综合服务页面");
                break;
            case 5:
                ToastUtils.showLong(getContext(), "横向二级联动筛选的综合服务页面（注：分类需获取到Finance分类下的三级分类）");
                break;
            case 6:
                ToastUtils.showLong(getContext(), "横向二级联动筛选的综合服务页面");
                break;
            case 7:
                ToastUtils.showLong(getContext(), "横向二级联动筛选的综合服务页面");
                break;
            case 8:
                ToastUtils.showLong(getContext(), "横向二级联动筛选的综合服务页面");
                break;
            case 9:
                ToastUtils.showLong(getContext(), "餐厅列表");
                break;
            case 10:
                ToastUtils.showLong(getContext(), "开发中");
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

    public void getBanner() {

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
                initNearbyVerical((NearbyVericalViewHolder) holder);
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
            NearbyTransverseAdapter nearbyTransverseAdapter = new NearbyTransverseAdapter(getContext());
            holder.businessRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            nearbyTransverseAdapter.setData(list);
            holder.businessRv.setAdapter(nearbyTransverseAdapter);

        }

        private void initNearbyVerical(NearbyVericalViewHolder holder) {

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

            NearbyVericalAdapter nearbyVericalAdapter = new NearbyVericalAdapter(getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            holder.businessRv.setLayoutManager(linearLayoutManager);
            nearbyVericalAdapter.setData(list);
            holder.businessRv.setAdapter(nearbyVericalAdapter);
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

    private class NearbyTransverseAdapter extends BaseQuickAdapter<BusinessBean> {

        public NearbyTransverseAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_homebusinessone;
        }

        @Override
        protected void initViews(QuickHolder holder, BusinessBean data, int position) {
            RoundedImageView roundedImageView = holder.findViewById(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImg())) {
                Picasso.with(getContext()).load(data.getImg()).into(roundedImageView);
            } else {
                roundedImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView scoreTv = holder.findViewById(R.id.tv_score);
            scoreTv.setText(data.getScore());
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(data.getName());
            TextView detailTv = holder.findViewById(R.id.tv_detail);
            detailTv.setText(data.getDetail());
            TextView lableOneTv = holder.findViewById(R.id.tv_lableone);
            TextView lableTwoTv = holder.findViewById(R.id.tv_labletwo);
            if (data.getProject().size() > 1) {
                lableOneTv.setText(data.getProject().get(0));
                lableTwoTv.setText(data.getProject().get(1));
            } else {
                lableOneTv.setText(data.getProject().get(0));
                lableTwoTv.setVisibility(View.GONE);
            }
            TextView priceTv = holder.findViewById(R.id.tv_price);
            priceTv.setText(data.getPrice());
            TextView distanceTv = holder.findViewById(R.id.tv_distance);
            distanceTv.setText("距您" + data.getDistance());

        }
    }

    private class NearbyVericalAdapter extends BaseQuickAdapter<BusinessBean> {

        public NearbyVericalAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_hotellist;
        }

        @Override
        protected void initViews(QuickHolder holder, BusinessBean data, int position) {
            TextView distanceTv = holder.findViewById(R.id.tv_distance);
            distanceTv.setText("<1.5Km");
        }
    }

}
