package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelCommentBean;
import com.jcs.where.bean.HotelDetailBean;
import com.jcs.where.bean.RoomDetailBean;
import com.jcs.where.bean.RoomListBean;
import com.jcs.where.bean.SubscribeBean;
import com.jcs.where.currency.WebViewActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.popupwindow.RoomDetailPopup;
import com.jcs.where.view.ObservableScrollView;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
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

public class HotelDetailActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    private static final String EXT_STARTDAY = "startDay";
    private static final String EXT_ENDDAY = "endDay";
    private static final String EXT_STARTWEEK = "startWeek";
    private static final String EXT_ENDWEEK = "endWeek";
    private static final String EXT_ALLDAY = "allDay";
    private static final String EXT_STARTYEAR = "startYear";
    private static final String EXT_ENDYEAR = "endYear";
    private static final String EXT_ROOMNUMBER = "roomNumber";

    private Toolbar toolbar;
    private XBanner banner;
    private TextView nameTv, startTimeTv, starTv, commnetNumberTv;
    private RelativeLayout faceBookRl;
    private TextView faceBookTv;
    private View faceLine;
    private TextView checkInTv, checkOutTv, addressTv;
    private String phone;
    private TextView startDateTv, startWeekTv, endDateTv, endWeekTv, allDayTv;
    private RecyclerView roomRv, facilitiesRv;
    private RoomAdapter roomAdapter;
    private FacilitiesAdapter facilitiesAdapter;
    private TextView policyStartTimeTv, policyEndTimeTv, policyChildrenTv;
    private ObservableScrollView scrollView;
    private View useView;
    private LinearLayout commentLl;
    private CircleImageView commentAvaterIv;
    private TextView commentNameTv, commentDetailTv, seeMoreTv;
    private ImageView likeIv, shareIv;
    private int like = 2;
    private int toolbarStatus = 0;
    private RelativeLayout hotelDetailRl, navigationRl;
    private String hotelName, hotelBreakfast;
    private ImageView star1Iv, star2Iv, star3Iv, star4Iv, star5Iv;
    private TextView timeTv;

    public static void goTo(Context context, int id, String startDate, String endDate, String startWeek, String endWeek, String allDay, String startYear, String endYear, String roomNumber) {
        Intent intent = new Intent(context, HotelDetailActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.putExtra(EXT_STARTDAY, startDate);
        intent.putExtra(EXT_ENDDAY, endDate);
        intent.putExtra(EXT_STARTWEEK, startWeek);
        intent.putExtra(EXT_ENDWEEK, endWeek);
        intent.putExtra(EXT_ALLDAY, allDay);
        intent.putExtra(EXT_STARTYEAR, startYear);
        intent.putExtra(EXT_ENDYEAR, endYear);
        intent.putExtra(EXT_ROOMNUMBER, roomNumber);
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
        initView();
    }

    private void initView() {
        toolbar = V.f(this, R.id.toolbar);
        //  setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        useView = V.f(this, R.id.useView);
        likeIv = V.f(this, R.id.iv_like);
        shareIv = V.f(this, R.id.iv_share);
        nameTv = V.f(this, R.id.tv_name);
        banner = V.f(this, R.id.banner3);
        startTimeTv = V.f(this, R.id.tv_startTime);
        starTv = V.f(this, R.id.tv_star);
        commnetNumberTv = V.f(this, R.id.tv_commentnumber);
        faceBookRl = V.f(this, R.id.rl_facebook);
        faceBookTv = V.f(this, R.id.tv_facebookadress);
        faceBookRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.goTo(HotelDetailActivity.this, faceBookTv.getText().toString().trim());
            }
        });
        faceLine = V.f(this, R.id.faceline);
        checkInTv = V.f(this, R.id.tv_checkin);
        checkOutTv = V.f(this, R.id.tv_checkout);
        addressTv = V.f(this, R.id.tv_address);
        V.f(this, R.id.rl_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showLong(HotelDetailActivity.this, "导航");
            }
        });
        V.f(this, R.id.rl_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(HotelDetailActivity.this)
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
        startDateTv = V.f(this, R.id.tv_startday);
        startDateTv.setText(getIntent().getStringExtra(EXT_STARTDAY));
        startWeekTv = V.f(this, R.id.tv_startweek);
        startWeekTv.setText(getIntent().getStringExtra(EXT_STARTWEEK));
        endDateTv = V.f(this, R.id.tv_endday);
        endDateTv.setText(getIntent().getStringExtra(EXT_ENDDAY));
        endWeekTv = V.f(this, R.id.tv_endweek);
        endWeekTv.setText(getIntent().getStringExtra(EXT_ENDWEEK));
        allDayTv = V.f(this, R.id.tv_allday);
        allDayTv.setText(getIntent().getStringExtra(EXT_ALLDAY));
        roomRv = V.f(this, R.id.rv_room);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        roomAdapter = new RoomAdapter(HotelDetailActivity.this);
        roomRv.setLayoutManager(linearLayoutManager);
        facilitiesRv = V.f(this, R.id.rv_facilities);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HotelDetailActivity.this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        facilitiesRv.setLayoutManager(gridLayoutManager);
        facilitiesAdapter = new FacilitiesAdapter(HotelDetailActivity.this);
        policyStartTimeTv = V.f(this, R.id.tv_policystarttime);
        policyEndTimeTv = V.f(this, R.id.tv_policyendtime);
        policyChildrenTv = V.f(this, R.id.tv_policychildren);
        scrollView = V.f(this, R.id.scrollView);
        useView.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
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
        commentLl = V.f(this, R.id.ll_comment);
        commentAvaterIv = V.f(this, R.id.iv_commentavatar);
        commentNameTv = V.f(this, R.id.username);
        commentDetailTv = V.f(this, R.id.tv_commentdetail);
        seeMoreTv = V.f(this, R.id.tv_seemore);
        seeMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelCommentActivity.goTo(HotelDetailActivity.this, getIntent().getIntExtra(EXT_ID, 0));
            }
        });
        V.f(this, R.id.tv_commentnumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelCommentActivity.goTo(HotelDetailActivity.this, getIntent().getIntExtra(EXT_ID, 0));
            }
        });
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collection(like != 1);
            }
        });
        hotelDetailRl = V.f(this, R.id.rl_hoteldetail);
        navigationRl = V.f(this, R.id.rl_navigation);
        V.f(this, R.id.ll_choosedate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        star1Iv = V.f(this, R.id.iv_star1);
        star2Iv = V.f(this, R.id.iv_star2);
        star3Iv = V.f(this, R.id.iv_star3);
        star4Iv = V.f(this, R.id.iv_star4);
        star5Iv = V.f(this, R.id.iv_star5);
        timeTv = V.f(this, R.id.tv_time);
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotel/" + getIntent().getIntExtra(EXT_ID, 0), null, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    HotelDetailBean hotelDetailBean = new Gson().fromJson(result, HotelDetailBean.class);
                    if (hotelDetailBean.getImages() != null) {
                        banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                                .setImageUrls(hotelDetailBean.getImages())
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
                    nameTv.setText(hotelDetailBean.getName());

                    shareIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WriteCommentActivity.goTo(HotelDetailActivity.this, getIntent().getIntExtra(EXT_ID, 0), hotelDetailBean.getName());
                        }
                    });
                    hotelName = hotelDetailBean.getName();
                    hotelBreakfast = hotelDetailBean.getPolicy().getBreadfast();
                    startTimeTv.setText(hotelDetailBean.getStart_business_time() + "开业");
                    starTv.setText(hotelDetailBean.getGrade() + "");
                    commnetNumberTv.setText(hotelDetailBean.getComment_counts() + "条评论");
                    if (TextUtils.isEmpty(hotelDetailBean.getFacebook_link())) {
                        faceBookRl.setVisibility(View.GONE);
                        faceLine.setVisibility(View.GONE);
                    } else {
                        faceBookRl.setVisibility(View.VISIBLE);
                        faceLine.setVisibility(View.VISIBLE);
                        faceBookTv.setText(hotelDetailBean.getFacebook_link());
                    }
                    checkInTv.setText("入住时间：" + hotelDetailBean.getPolicy().getCheck_in_time().substring(0, 5));
                    checkOutTv.setText("退房时间：" + hotelDetailBean.getPolicy().getCheck_out_time().substring(0, 5));
                    addressTv.setText(hotelDetailBean.getAddress());
                    phone = hotelDetailBean.getTel();
                    facilitiesAdapter.setData(hotelDetailBean.getFacilities());
                    facilitiesRv.setAdapter(facilitiesAdapter);
                    policyStartTimeTv.setText("入住时间：" + hotelDetailBean.getPolicy().getCheck_in_time());
                    policyEndTimeTv.setText("退房时间：" + hotelDetailBean.getPolicy().getCheck_out_time());
                    policyChildrenTv.setText("儿童及加床：" + hotelDetailBean.getPolicy().getChildren());
                    if (hotelDetailBean.getCollect_status() == 1) {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hoteltransparentunlike));
                    }
                    like = hotelDetailBean.getCollect_status();
                    navigationRl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startNaviGoogle("38.888025", "121.594476");
                        }
                    });
                    initRoomList();
                    initCommentList();
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
            }
        });
    }

    private void initRoomList() {
        showLoading();
        String url = "hotelapi/v1/hotel/" + getIntent().getIntExtra(EXT_ID, 0) + "/rooms?start_date=" + getIntent().getStringExtra(EXT_STARTYEAR) + "-" + getIntent().getStringExtra(EXT_STARTDAY).replace("月", "-").replace("日", "") + "&end_date=" + getIntent().getStringExtra(EXT_ENDYEAR) + "-" + getIntent().getStringExtra(EXT_ENDDAY).replace("月", "-").replace("日", "") + "&room_num=" + getIntent().getStringExtra(EXT_ROOMNUMBER);
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<RoomListBean>>() {
                    }.getType();
                    List<RoomListBean> list = gson.fromJson(result, type);
                    roomAdapter.setData(list);
                    roomRv.setAdapter(roomAdapter);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
            }
        });
    }

    private void initCommentList() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotel/" + getIntent().getIntExtra(EXT_ID, 0) + "/comments", null, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    HotelCommentBean hotelCommentBean = new Gson().fromJson(result, HotelCommentBean.class);
                    if (hotelCommentBean.getData().size() == 0) {
                        commentLl.setVisibility(View.GONE);
                    } else {
                        commentLl.setVisibility(View.VISIBLE);
                        if (hotelCommentBean.getData().size() > 1) {
                            seeMoreTv.setVisibility(View.VISIBLE);
                        } else {
                            seeMoreTv.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(hotelCommentBean.getData().get(0).getAvatar())) {
                            Picasso.with(HotelDetailActivity.this).load(hotelCommentBean.getData().get(0).getAvatar()).into(commentAvaterIv);
                        } else {
                            commentAvaterIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
                        }
                        commentNameTv.setText(hotelCommentBean.getData().get(0).getUsername());
                        timeTv.setText(hotelCommentBean.getData().get(0).getCreated_at());
                        if (hotelCommentBean.getData().get(0).getStar() == 1) {
                            star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                            star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                            star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                            star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                        } else if (hotelCommentBean.getData().get(0).getStar() == 2) {
                            star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                            star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                            star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                        } else if (hotelCommentBean.getData().get(0).getStar() == 3) {
                            star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                            star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                        } else if (hotelCommentBean.getData().get(0).getStar() == 4) {
                            star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistgreystar));
                        } else if (hotelCommentBean.getData().get(0).getStar() == 5) {
                            star1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star4Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                            star5Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_commentlistlightstar));
                        }
                        commentDetailTv.setText(hotelCommentBean.getData().get(0).getContent());
                    }

                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hoteldetail;
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    private void collection(boolean status) {
        showLoading();
        if (status == true) {
            Map<String, Integer> params = new HashMap<>();
            params.put("hotel_id", Integer.valueOf(getIntent().getIntExtra(EXT_ID, 0)));
            HttpUtils.doHttpintReqeust("POST", "hotelapi/v1/collects", params, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        ToastUtils.showLong(HotelDetailActivity.this, "收藏成功");
                        like = 1;
                        if (toolbarStatus == 0) {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                        } else {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhitelike));
                        }
                    } else {
                        ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                        ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                    }
                }

                @Override
                public void onFaileure(int code, Exception e) {
                    stopLoading();
                    ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
                }
            });
        } else {
            Map<String, Integer> params = new HashMap<>();
            params.put("hotel_id", Integer.valueOf(getIntent().getIntExtra(EXT_ID, 0)));
            HttpUtils.doHttpintReqeust("DELETE", "hotelapi/v1/collects", params, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        ToastUtils.showLong(HotelDetailActivity.this, "取消成功");
                        like = 2;
                        if (toolbarStatus == 0) {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hoteltransparentunlike));
                        } else {
                            likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_hotelwhiteunlike));
                        }
                    } else {
                        ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                        ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                    }
                }

                @Override
                public void onFaileure(int code, Exception e) {
                    stopLoading();
                    ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
                }
            });
        }
    }

    private void initRoomDetail(final int roomId, final int breakfast) {
        showLoading();
        String url = "hotelapi/v1/hotel/room/" + roomId + "?start_date=" + getIntent().getStringExtra(EXT_STARTYEAR) + "-" + getIntent().getStringExtra(EXT_STARTDAY).replace("月", "-").replace("日", "") + "&end_date=" + getIntent().getStringExtra(EXT_ENDYEAR) + "-" + getIntent().getStringExtra(EXT_ENDDAY).replace("月", "-").replace("日", "") + "&room_num=" + getIntent().getStringExtra(EXT_ROOMNUMBER);
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    RoomDetailBean roomDetailBean = new Gson().fromJson(result, RoomDetailBean.class);
                    new RoomDetailPopup.Builder(HotelDetailActivity.this, hotelDetailRl, roomId, roomDetailBean)
                            .setPriceOnClickListener(new RoomDetailPopup.SubscribeOnClickListener() {
                                @Override
                                public void getDate(int id, String name, String bed, int window, int wifi, int people, int cancel) {
                                    // ToastUtils.showLong(HotelDetailActivity.this, id + "");
                                    SubscribeBean subscribeBean = new SubscribeBean();
                                    subscribeBean.hotelName = hotelName;
                                    subscribeBean.roomId = id;
                                    subscribeBean.roomName = name;
                                    subscribeBean.bed = bed;
                                    if (breakfast == 1) {
                                        subscribeBean.breakfast = "有早餐";
                                    } else {
                                        subscribeBean.breakfast = "无早餐";
                                    }
                                    subscribeBean.window = window;
                                    subscribeBean.wifi = wifi;
                                    subscribeBean.people = people;
                                    subscribeBean.cancel = cancel;
                                    subscribeBean.startDate = startDateTv.getText().toString();
                                    subscribeBean.startWeek = startWeekTv.getText().toString();
                                    subscribeBean.endDate = endDateTv.getText().toString();
                                    subscribeBean.endWeek = endWeekTv.getText().toString();
                                    subscribeBean.night = allDayTv.getText().toString();
                                    subscribeBean.roomNumber = getIntent().getStringExtra(EXT_ROOMNUMBER);
                                    subscribeBean.roomPrice = roomDetailBean.getPrice();
                                    subscribeBean.startYear = getIntent().getStringExtra(EXT_STARTYEAR);
                                    subscribeBean.endYear = getIntent().getStringExtra(EXT_ENDYEAR);
                                    HotelSubscribeActivity.goTo(HotelDetailActivity.this, subscribeBean);
                                }
                            }).builder();
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                }
            }


            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
            }
        });

    }

    private void subRoom(final int roomId, final int breakfast) {
        showLoading();
        String url = "hotelapi/v1/hotel/room/" + roomId + "?start_date=" + getIntent().getStringExtra(EXT_STARTYEAR) + "-" + getIntent().getStringExtra(EXT_STARTDAY).replace("月", "-").replace("日", "") + "&end_date=" + getIntent().getStringExtra(EXT_ENDYEAR) + "-" + getIntent().getStringExtra(EXT_ENDDAY).replace("月", "-").replace("日", "") + "&room_num=" + getIntent().getStringExtra(EXT_ROOMNUMBER);
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    RoomDetailBean roomDetailBean = new Gson().fromJson(result, RoomDetailBean.class);
                    // ToastUtils.showLong(HotelDetailActivity.this, id + "");
                    SubscribeBean subscribeBean = new SubscribeBean();
                    subscribeBean.hotelName = hotelName;
                    subscribeBean.roomId = roomDetailBean.getId();
                    subscribeBean.roomName = roomDetailBean.getName();
                    subscribeBean.bed = roomDetailBean.getHotel_room_type();
                    if (breakfast == 1) {
                        subscribeBean.breakfast = "有早餐";
                    } else {
                        subscribeBean.breakfast = "无早餐";
                    }
                    subscribeBean.window = roomDetailBean.getWindow_type();
                    subscribeBean.wifi = roomDetailBean.getWifi_type();
                    subscribeBean.people = roomDetailBean.getPeople();
                    subscribeBean.cancel = roomDetailBean.getIs_cancel();
                    subscribeBean.startDate = startDateTv.getText().toString();
                    subscribeBean.startWeek = startWeekTv.getText().toString();
                    subscribeBean.endDate = endDateTv.getText().toString();
                    subscribeBean.endWeek = endWeekTv.getText().toString();
                    subscribeBean.night = allDayTv.getText().toString();
                    subscribeBean.roomNumber = getIntent().getStringExtra(EXT_ROOMNUMBER);
                    subscribeBean.roomPrice = roomDetailBean.getPrice();
                    subscribeBean.startYear = getIntent().getStringExtra(EXT_STARTYEAR);
                    subscribeBean.endYear = getIntent().getStringExtra(EXT_ENDYEAR);
                    HotelSubscribeActivity.goTo(HotelDetailActivity.this, subscribeBean);

                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                }
            }


            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
            }
        });

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

    private class RoomAdapter extends BaseQuickAdapter<RoomListBean> {

        private TagAdapter tagAdapter;

        public RoomAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_room;
        }

        @Override
        protected void initViews(QuickHolder holder, final RoomListBean data, int position) {
            RoundedImageView photoIv = holder.findViewById(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                Picasso.with(HotelDetailActivity.this).load(data.getImages().get(0)).into(photoIv);
            } else {
                photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(data.getName());
            TextView typeTv = holder.findViewById(R.id.tv_hotelType);
            String breakfast = null;
            if (data.getBreakfast_type() == 1) {
                breakfast = "有早餐";
            } else {
                breakfast = "无早餐";
            }
            typeTv.setText(data.getHotel_room_type() + "  " + breakfast + "  ");

            SpannableString m2 = new SpannableString("m2");
            m2.setSpan(new RelativeSizeSpan(0.5f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//一半大小
            m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   //上标
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(data.getRoom_area());
            spannableStringBuilder.append(m2);
            TextView roomAreaTv = holder.findViewById(R.id.tv_roomarea);
            roomAreaTv.setText(spannableStringBuilder);
            TextView priceTv = holder.findViewById(R.id.tv_price);
            priceTv.setText("₱" + data.getPrice());
            RecyclerView tagRv = holder.findViewById(R.id.rv_tag);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this,
                    LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            tagAdapter = new TagAdapter(mContext);
            tagRv.setLayoutManager(linearLayoutManager);
            tagAdapter.setData(data.getTags());
            tagRv.setAdapter(tagAdapter);
            TextView subscribeTv = holder.findViewById(R.id.tv_subscribe);
            LinearLayout tagLl = holder.findViewById(R.id.ll_tag);
            if (data.getRemain_room_num() == 0) {
                subscribeTv.setText("已客满");
                subscribeTv.setBackground(getResources().getDrawable(R.drawable.bg_noroom));
                subscribeTv.setTextColor(getResources().getColor(R.color.grey_999999));
                subscribeTv.setEnabled(false);
                tagLl.setVisibility(View.GONE);
                priceTv.setTextColor(getResources().getColor(R.color.grey_999999));
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                photoIv.setColorFilter(filter);
            } else {
                subscribeTv.setText("预订");
                subscribeTv.setBackground(getResources().getDrawable(R.drawable.bg_roomsubscribe));
                subscribeTv.setTextColor(getResources().getColor(R.color.white));
                subscribeTv.setEnabled(true);
                tagLl.setVisibility(View.VISIBLE);
                priceTv.setTextColor(getResources().getColor(R.color.orange_FF5B1B));
            }
            holder.findViewById(R.id.tv_subscribe).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subRoom(data.getId(), data.getBreakfast_type());
                }
            });
            holder.findViewById(R.id.rl_room).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initRoomDetail(data.getId(), data.getBreakfast_type());
                }
            });

        }
    }

    private class TagAdapter extends BaseQuickAdapter<RoomListBean.TagsBean> {

        public TagAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_roomtag;
        }

        @Override
        protected void initViews(QuickHolder holder, RoomListBean.TagsBean data, int position) {
            TextView tagTv = holder.findViewById(R.id.tv_tag);
            tagTv.setText(data.getZh_cn_name());
        }
    }

    private class FacilitiesAdapter extends BaseQuickAdapter<HotelDetailBean.FacilitiesBean> {

        public FacilitiesAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_facilities;
        }

        @Override
        protected void initViews(QuickHolder holder, HotelDetailBean.FacilitiesBean data, int position) {
            ImageView iconIv = holder.findViewById(R.id.iv_icon);
            if (!TextUtils.isEmpty(data.getIcon())) {
                Picasso.with(HotelDetailActivity.this).load(data.getIcon()).into(iconIv);
            } else {
                iconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(data.getName());
        }
    }

}
