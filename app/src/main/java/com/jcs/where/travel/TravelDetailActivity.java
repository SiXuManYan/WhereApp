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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.TravelDetailBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.view.ObservableScrollView;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;
import pl.droidsonroids.gif.GifImageView;

public class TravelDetailActivity extends BaseActivity {

    private static final String EXT_ID = "id";
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

    public static void goTo(Context context, int id) {
        Intent intent = new Intent(context, TravelDetailActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
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
        initView();
    }

    private void initView() {
        toolbar = V.f(this, R.id.toolbar);
        useView = V.f(this, R.id.useView);
        likeIv = V.f(this, R.id.iv_like);
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (like == 1) {
                    collection(false);
                } else {
                    collection(true);
                }
            }
        });
        shareIv = V.f(this, R.id.iv_share);
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


}
