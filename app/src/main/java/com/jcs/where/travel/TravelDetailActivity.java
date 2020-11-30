package com.jcs.where.travel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.TravelCommentListBean;
import com.jcs.where.bean.TravelDetailBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.mango.ImageSelectListener;
import com.jcs.where.mango.Mango;
import com.jcs.where.mango.MultiplexImage;
import com.jcs.where.utils.ImageLoaders;
import com.jcs.where.view.ObservableScrollView;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class TravelDetailActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    // 屏幕宽度
    public float Width;
    // 屏幕高度
    public float Height;
    private Toolbar toolbar;
    private XBanner banner;
    private View useView;
    private ImageView likeIv, shareIv;
    private ObservableScrollView scrollView;
    private int like = 2;
    private int toolbarStatus = 0;
    private TextView nameTv, startTimeTv, scoreTv, commnetNumberTv, addressTv;
    private RelativeLayout navigationRl;
    private String phone;
    private TextView introduceTv, noticeTv;
    private List<TravelCommentListBean.DataBean> list;
    private RecyclerView commentRv;
    private CommentAdapter commentAdapter;
    private LinearLayout commentLl;
    private TextView seeMoreTv;

    public static void goTo(Context context, int id) {
        Intent intent = new Intent(context, TravelDetailActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
        initView();
    }

    private void initView() {
        toolbar = V.f(this, R.id.toolbar);
        useView = V.f(this, R.id.useView);
        likeIv = V.f(this, R.id.iv_like);
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collection(like != 1);
            }
        });
        shareIv = V.f(this, R.id.iv_share);
        shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravelWriteCommentActivity.goTo(TravelDetailActivity.this, getIntent().getIntExtra(EXT_ID, 0));
            }
        });
        banner = V.f(this, R.id.banner3);
        useView.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        scrollView = V.f(this, R.id.scrollView);
        scrollView.setScrollViewListener(new ObservableScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                float headHeight = banner.getMeasuredHeight() - toolbar.getMeasuredHeight();
                int alpha = (int) (((float) y / headHeight) * 255);
                if (alpha >= 255) {
                    alpha = 255;
                }
                if (alpha <= 5) {
                    alpha = 0;
                }
                if (alpha > 130) {
                    if (like == 1) {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhiteunlike));
                    }
                    shareIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_black));
                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_black));
                    toolbarStatus = 1;
                } else {
                    if (like == 1) {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hoteltransparentunlike));
                    }
                    shareIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_white));
                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
                    toolbarStatus = 0;
                }
                useView.getBackground().setAlpha(alpha);
                toolbar.getBackground().setAlpha(alpha);
            }
        });
        useView.getBackground().setAlpha(0);
        toolbar.getBackground().setAlpha(0);//透明
        nameTv = V.f(this, R.id.tv_name);
        scoreTv = V.f(this, R.id.tv_score);
        commnetNumberTv = V.f(this, R.id.tv_commentnumber);
        startTimeTv = V.f(this, R.id.tv_starttime);
        addressTv = V.f(this, R.id.tv_address);
        navigationRl = V.f(this, R.id.rl_navigation);
        V.f(this, R.id.rl_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(TravelDetailActivity.this)
                        .setTitle("提示")
                        .setMessage("是否拨打商家电话" + "tel:" + phone)
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callPhone();
                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });
        introduceTv = V.f(this, R.id.tv_introduce);
        noticeTv = V.f(this, R.id.tv_notice);
        commentRv = V.f(this, R.id.rv_comment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TravelDetailActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        commentRv.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(TravelDetailActivity.this);
        commentLl = V.f(this, R.id.ll_comment);
        seeMoreTv = V.f(this, R.id.tv_seemore);
        seeMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravelCommentActivity.goTo(TravelDetailActivity.this, getIntent().getIntExtra(EXT_ID, 0));
            }
        });
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "travelapi/v1/travel/" + getIntent().getIntExtra(EXT_ID, 0), null, "", TokenManager.get().getToken(TravelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    TravelDetailBean travelDetailBean = new Gson().fromJson(result, TravelDetailBean.class);
                    if (travelDetailBean.getImages() != null) {
                        banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                                .setImageUrls(travelDetailBean.getImages())
                                .setImageLoader(new AbstractUrlLoader() {
                                    @Override
                                    public void loadImages(Context context, String url, ImageView image) {
                                        Glide.with(context)
                                                .load(url)
                                                .into(image);
                                    }

                                    @Override
                                    public void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType) {
                                        Glide.with(context).asGif().load(url).into(gifImageView);
                                    }
                                })
                                .setTitleHeight(50)
                                .isAutoPlay(true)
                                .setDelay(5000)
                                .setUpIndicators(R.drawable.ic_roomselected, R.drawable.ic_roomunselected)
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
                    } else {

                    }
                    nameTv.setText(travelDetailBean.getName());
                    scoreTv.setText(travelDetailBean.getGrade() + "");
                    commnetNumberTv.setText(travelDetailBean.getComments_count() + "条评论");
                    startTimeTv.setText("营业时间：" + travelDetailBean.getStart_time() + "-" + travelDetailBean.getEnd_time());
                    addressTv.setText(travelDetailBean.getAddress());
                    if (travelDetailBean.getIs_collect() == 1) {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hoteltransparentunlike));
                    }
                    like = travelDetailBean.getIs_collect();
                    phone = travelDetailBean.getPhone();
                    introduceTv.setText(travelDetailBean.getContent());
                    noticeTv.setText(travelDetailBean.getNotice());
                    navigationRl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startNaviGoogle("38.888025", "121.594476");
                        }
                    });
                    initComment();
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(TravelDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(TravelDetailActivity.this, e.getMessage());
            }
        });
    }

    private void initComment() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "travelapi/v1/comment/" + getIntent().getIntExtra(EXT_ID, 0), null, "", TokenManager.get().getToken(TravelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    TravelCommentListBean travelCommentListBean = new Gson().fromJson(result, TravelCommentListBean.class);
                    list = new ArrayList<>();
                    if (travelCommentListBean.getData().size() == 0) {
                        commentLl.setVisibility(View.GONE);
                    } else if (travelCommentListBean.getData().size() == 1) {
                        commentLl.setVisibility(View.VISIBLE);
                        list.add(travelCommentListBean.getData().get(0));
                        commentAdapter.setData(list);
                        commentRv.setAdapter(commentAdapter);
                        seeMoreTv.setVisibility(View.GONE);
                    } else if (travelCommentListBean.getData().size() > 1) {
                        commentLl.setVisibility(View.VISIBLE);
                        list.add(travelCommentListBean.getData().get(0));
                        list.add(travelCommentListBean.getData().get(1));
                        commentAdapter.setData(list);
                        commentRv.setAdapter(commentAdapter);
                        seeMoreTv.setVisibility(View.VISIBLE);
                    }

                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(TravelDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(TravelDetailActivity.this, e.getMessage());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_traveldetail;
    }

    private void collection(boolean status) {
        showLoading();
        if (status == true) {
            Map<String, Integer> params = new HashMap<>();
            params.put("travel_id", Integer.valueOf(getIntent().getIntExtra(EXT_ID, 0)));
            HttpUtils.doHttpintReqeust("POST", "travelapi/v1/collects", params, "", TokenManager.get().getToken(TravelDetailActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        ToastUtils.showLong(TravelDetailActivity.this, "收藏成功");
                        like = 1;
                        if (toolbarStatus == 0) {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                        } else {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                        }
                    } else {
                        ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                        ToastUtils.showLong(TravelDetailActivity.this, errorBean.message);
                    }
                }

                @Override
                public void onFaileure(int code, Exception e) {
                    stopLoading();
                    ToastUtils.showLong(TravelDetailActivity.this, e.getMessage());
                }
            });
        } else {
            HttpUtils.doHttpintReqeust("DELETE", "travelapi/v1/collects/" + getIntent().getIntExtra(EXT_ID, 0), null, "", TokenManager.get().getToken(TravelDetailActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        ToastUtils.showLong(TravelDetailActivity.this, "取消成功");
                        like = 2;
                        if (toolbarStatus == 0) {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hoteltransparentunlike));
                        } else {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhiteunlike));
                        }
                    } else {
                        ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                        ToastUtils.showLong(TravelDetailActivity.this, errorBean.message);
                    }
                }

                @Override
                public void onFaileure(int code, Exception e) {
                    stopLoading();
                    ToastUtils.showLong(TravelDetailActivity.this, e.getMessage());
                }
            });
        }
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    public void startNaviGoogle(String lat, String lng) {
        if (isAvilible(this, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            ToastUtils.showLong(this, "您尚未安装谷歌地图或地图版本过低");
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class CommentAdapter extends BaseQuickAdapter<TravelCommentListBean.DataBean> {

        private final int[] ImagaId = {R.id.img_0, R.id.img_1, R.id.img_2, R.id.img_3, R.id.img_4, R.id.img_5, R.id.img_6, R.id.img_7, R.id.img_8};

        public CommentAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_commentlist;
        }

        @Override
        protected void initViews(QuickHolder holder, TravelCommentListBean.DataBean data, int position) {
            CircleImageView avaterIv = holder.findViewById(R.id.listuserimg);
            if (!TextUtils.isEmpty(data.getAvatar())) {
                Picasso.with(TravelDetailActivity.this).load(data.getAvatar()).into(avaterIv);
            } else {
                avaterIv.setImageDrawable(TravelDetailActivity.this.getResources().getDrawable(R.drawable.ic_noheader));
            }
            TextView nameTv = holder.findViewById(R.id.username);
            nameTv.setText(data.getUsername());
            TextView usercontent = holder.findViewById(R.id.usercontent);
            usercontent.setText(data.getContent());
            TextView timeTv = holder.findViewById(R.id.tv_time);
            timeTv.setText(data.getCreated_at());
            TextView fullText = holder.findViewById(R.id.fullText);
            ImageView star1Iv = holder.findViewById(R.id.iv_star1);
            ImageView star2Iv = holder.findViewById(R.id.iv_star2);
            ImageView star3Iv = holder.findViewById(R.id.iv_star3);
            ImageView star4Iv = holder.findViewById(R.id.iv_star4);
            ImageView star5Iv = holder.findViewById(R.id.iv_star5);
            if (data.getStar_level() == 1) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 2) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 3) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 4) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
            } else if (data.getStar_level() == 5) {
                star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
            }
            GridLayout gridview = (GridLayout) holder.findViewById(R.id.gridview);
            ImageView showimage = (ImageView) holder.findViewById(R.id.showimage);
            RoundedImageView[] imgview = new RoundedImageView[9];
            for (int i = 0; i < 9; i++) {
                imgview[i] = (RoundedImageView) holder.findViewById(ImagaId[i]);
            }
            if (data.getImages().size() == 0) {
                showimage.setVisibility(View.GONE);
                gridview.setVisibility(View.GONE);
            } else if (data.getImages().size() > 0) {
                showimage.setVisibility(View.GONE);
                gridview.setVisibility(View.VISIBLE);
                int a = data.getImages().size() / 4;
                int b = data.getImages().size() % 4;
                if (b > 0) {
                    a++;
                }
                float width = (Width - dip2px(60) - dip2px(2)) / 4;
                gridview.getLayoutParams().height = (int) (a * width);

                for (int i = 0; i < 9; i++) {
                    imgview[i].setVisibility(View.GONE);
                }

                List<MultiplexImage> images = new ArrayList<>();
                images.clear();
                for (int i = 0; i < data.getImages().size(); i++) {
                    imgview[i].setVisibility(View.VISIBLE);
                    imgview[i].getLayoutParams().width = (int) width;
                    imgview[i].getLayoutParams().height = (int) width;
                    ImageLoaders.setsendimg(data.getImages().get(i), imgview[i]);
                    images.add(new MultiplexImage(data.getImages().get(i), MultiplexImage.ImageType.NORMAL));
                    int finalI = i;
                    imgview[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Mango.setImages(images);
                            Mango.setPosition(finalI);
                            Mango.setImageSelectListener(new ImageSelectListener() {
                                @Override
                                public void select(int index) {

                                }
                            });
                            try {
                                Mango.open(TravelDetailActivity.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            setContentLayout(usercontent, fullText);
            if (data.is_select) {
                fullText.setText("收起");
                usercontent.setMaxLines(50);
            } else {
                fullText.setText("全文");
                usercontent.setMaxLines(3);
            }

            fullText.setOnClickListener(new CommentAdapter.fullTextOnclick(usercontent, fullText, position));

        }

        private void setContentLayout(final TextView usercontent,
                                      final TextView fullText) {
            ViewTreeObserver vto = usercontent.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                private boolean isInit;

                @Override
                public boolean onPreDraw() {
                    if (isInit) {
                        return true;
                    }
                    Layout layout = usercontent.getLayout();
                    if (layout != null) {
                        int maxline = layout.getLineCount();
                        if (maxline > 3) {
                            fullText.setVisibility(View.VISIBLE);
                        } else {
                            fullText.setVisibility(View.GONE);
                        }
                        isInit = true;
                    }
                    return true;
                }
            });
        }

        class fullTextOnclick implements View.OnClickListener {

            private final TextView usercontent;
            private final TextView fullText;
            private final int index;

            fullTextOnclick(TextView usercontent, TextView fullText, int index) {
                this.fullText = fullText;
                this.usercontent = usercontent;
                this.index = index;
            }

            @Override
            public void onClick(View v) {
                TravelCommentListBean.DataBean info = list.get(index);
                if (info.is_select) {
                    usercontent.setMaxLines(3);
                    fullText.setText("全文");
                    usercontent.invalidate();
                } else {
                    usercontent.setMaxLines(50);
                    fullText.setText("收起");
                    usercontent.invalidate();
                }
                info.is_select = !info.is_select;
                list.set(index, info);
            }
        }
    }


}
