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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.jcs.where.R;
import com.jcs.where.bean.BusinessBean;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.hotel.HotelActivity;
import com.jcs.where.utils.GlideRoundTransform;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.jcs.where.view.ptr.MyPtrClassicFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment {

    private static final int REQ_SELECT_CITY = 100;
    private View view;
    private XBanner banner3;
    private MyPtrClassicFrameLayout ptrFrame;
    private SimpleMarqueeView marqueeView;
    private RecyclerView homeRv;
    private TextView cityNameTv;
    private LinearLayout bannerLl;

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        initView();
        return view;
    }

    private void initView() {
//        Subscription subscription = Api.get().getHomeBannerList()
//                .compose(new ApiTransformer<List<HomeBannerBean>>())
//                .subscribe(new Observer<List<HomeBannerBean>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<HomeBannerBean> homeBannerBeans) {
//
//                    }
//                });
//        addSubscription(subscription);
        List<String> bannerUrls = new ArrayList<>();
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536187&di=4d4f79fee6c1203e41f73bfbce99f596&imgtype=0&src=http%3A%2F%2Fwww.cnr.cn%2F2013qcpd%2Fzsgz%2F201403%2FW020140327358771316323.jpg");
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536188&di=858d2f57077426cf18c7d5ba1776d90e&imgtype=0&src=http%3A%2F%2Ftoutiao.image.mucang.cn%2Ftoutiao-image%2F2017%2F04%2F27%2F03%2F5132a5b8cb524cb2a474154ba34fc8f7.jpeg");
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536184&di=1d39dd5866d440260e5405d2326fd4ae&imgtype=0&src=http%3A%2F%2Fwww.cnr.cn%2F2013qcpd%2Fzsgz%2F201403%2FW020140327358762180187.jpg");
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536184&di=72f0fdc0787753689c353dfd2dba3fac&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn15%2F649%2Fw900h549%2F20180611%2F226a-hcufqif9808134.jpg");
        bannerUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594037536183&di=c59bc0cce789bca06fc490838b384495&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180519%2F91307c39741e4a96938cd397bd93ab4f.jpeg");
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
                                .error(R.drawable.ic_home_press) //加载失败图片
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
                                .error(R.drawable.ic_home_press) //加载失败图片
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

        final List<String> messageList = new ArrayList<>();
        messageList.add("中印谈判有突破 但印度还在做火上浇油的事");
        messageList.add("加拿大对香港禁运军事物资 一看金额尴尬了");
        messageList.add("世卫承认中方从未报告疫情暴发?外交部回应");
        messageList.add("一不小心 《纽约时报》把美政府反华阴谋给说漏了");
        messageList.add("党中央决定成立的小组又设一专项组 组长亮相");
        messageList.add("美海军叫板：美国航母不会被中国弹道导弹吓倒");
        messageList.add("江苏两逃犯暴力袭警致两名警员牺牲 现已被抓获");
        messageList.add("1071万高考生今迎人生大考 33城直击高考现场");
        messageList.add("山东民办教师自称被人顶替教师岗位26年 官方回应");
        messageList.add("惨剧！欧洲一家动物园饲养员被老虎咬死 吓坏游客");

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
        V.f(view, R.id.ll_hotel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelActivity.goTo(getContext());
            }
        });
    }

    private class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final Context context;
        //布局标识集合
        private final List<Integer> typeList;
        //1.按钮 2.直播
        //设置常量
        //横向附近
        private static final int TYPE_NEARBY_TRANSVERSE = 1;
        //竖向附件
        private static final int TYPE_NEARBY_VERICAL = 2;


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
            return R.layout.item_homebusinesstwo;
        }

        @Override
        protected void initViews(QuickHolder holder, BusinessBean data, int position) {

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

}
