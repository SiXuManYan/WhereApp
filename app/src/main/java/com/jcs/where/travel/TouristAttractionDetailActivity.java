package com.jcs.where.travel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.api.response.CommentResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.TouristAttractionDetailResponse;
import com.jcs.where.hotel.adapter.CommentListAdapter;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.travel.model.TouristAttractionDetailModel;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.ImagePreviewActivity;
import com.jcs.where.view.ObservableScrollView;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;

public class TouristAttractionDetailActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    // 屏幕宽度
    public float Width;
    // 屏幕高度
    public float Height;
    private XBanner banner;
    private ObservableScrollView scrollView;
    private int like = 2;
    private int toolbarStatus = 0;
    private TextView nameTv, startTimeTv, scoreTv, commnetNumberTv, addressTv;
    private RelativeLayout navigationRl;
    private String phone;
    private TextView introduceTv, noticeTv;
    private List<CommentResponse> list;
    private RecyclerView commentRv;
    private CommentListAdapter mCommentAdapter;
    private LinearLayout commentLl;
    private TextView seeMoreTv;
    private TouristAttractionDetailModel mMode;
    private int mId;

    public static void goTo(Context context, int id) {
        Intent intent = new Intent(context, TouristAttractionDetailActivity.class);
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
    protected void initView() {
        // TODO 写评论
//        shareIv.setOnClickListener(view -> TravelWriteCommentActivity.goTo(TravelDetailActivity.this, getIntent().getIntExtra(EXT_ID, 0)));
        banner = findViewById(R.id.banner3);
        scrollView = findViewById(R.id.scrollView);
        scrollView.setScrollViewListener((scrollView, x, y, oldx, oldy) -> {
            float headHeight = banner.getMeasuredHeight() - mJcsTitle.getMeasuredHeight();
            int alpha = (int) (((float) y / headHeight) * 255);
            if (alpha >= 255) {
                alpha = 255;
            }
            if (alpha <= 5) {
                alpha = 0;
            }
            if (alpha > 130) {
                if (like == 1) {
                    mJcsTitle.setSecondRightIcon(R.drawable.ic_hotelwhitelike);
                } else {
                    mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_black);
                }
                mJcsTitle.setFirstRightIcon(R.mipmap.ic_share_black);
                mJcsTitle.setBackIcon(R.mipmap.ic_back_black);
                toolbarStatus = 1;
            } else {
                if (like == 1) {
                    mJcsTitle.setSecondRightIcon(R.drawable.ic_hotelwhitelike);
                } else {
                    mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_white);
                }
                mJcsTitle.setFirstRightIcon(R.mipmap.ic_share_white);
                mJcsTitle.setBackIcon(R.mipmap.ic_back_white);
                toolbarStatus = 0;
            }
            mJcsTitle.getBackground().setAlpha(alpha);
            setStatusBar(Color.argb(alpha, 255, 255, 255));
            if (alpha == 255) {
                changeStatusTextColor(true);
                mJcsTitle.setMiddleTitle(nameTv.getText().toString());
            }
            if (alpha == 0) {
                changeStatusTextColor(false);
                mJcsTitle.setMiddleTitle("");
            }
        });
        mJcsTitle.getBackground().setAlpha(0);//透明
        nameTv = findViewById(R.id.tv_name);
        scoreTv = findViewById(R.id.tv_score);
        commnetNumberTv = findViewById(R.id.tv_commentnumber);
        startTimeTv = findViewById(R.id.tv_starttime);
        addressTv = findViewById(R.id.tv_address);
        navigationRl = findViewById(R.id.rl_navigation);
        findViewById(R.id.rl_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(TouristAttractionDetailActivity.this)
                        .setTitle(R.string.prompt)
                        .setMessage(String.format(getString(R.string.ask_call_merchant_phone), phone))
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callPhone();
                            }
                        })

                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });
        introduceTv = findViewById(R.id.tv_introduce);
        noticeTv = findViewById(R.id.tv_notice);
        commentRv = findViewById(R.id.rv_comment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TouristAttractionDetailActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        commentRv.setLayoutManager(linearLayoutManager);
        mCommentAdapter = new CommentListAdapter();
        commentLl = findViewById(R.id.ll_comment);
        seeMoreTv = findViewById(R.id.tv_seemore);
        seeMoreTv.setOnClickListener(view -> TravelCommentActivity.goTo(TouristAttractionDetailActivity.this, getIntent().getIntExtra(EXT_ID, 0)));
    }

    @Override
    protected void initData() {
        mMode = new TouristAttractionDetailModel();
        showLoading();
        mId = getIntent().getIntExtra(EXT_ID, 0);
        mMode.getTouristAttractionDetail(mId, new BaseObserver<TouristAttractionDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(TouristAttractionDetailResponse response) {
                stopLoading();
                if (response.getImages() != null) {
                    banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                            .setImageUrls(response.getImages())
                            .setImageLoader(new AbstractUrlLoader() {
                                @Override
                                public void loadImages(Context context, String url, ImageView image) {
                                    GlideUtil.load(context, url, image);
                                }

                                @Override
                                public void loadGifs(Context context, String url, GifImageView gifImageView, ImageView.ScaleType scaleType) {
                                    GlideUtil.loadGif(context, url, gifImageView);
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
                nameTv.setText(response.getName());
                scoreTv.setText(response.getGrade() + "");
                String commentNumberText = String.format(getString(R.string.comment_num_prompt), response.getComments_count());
                commnetNumberTv.setText(commentNumberText);
                String businessHour = getString(R.string.business_hour) + response.getStart_time() + "-" + response.getEnd_time();
                startTimeTv.setText(businessHour);
                addressTv.setText(response.getAddress());
                if (response.getIs_collect() == 1) {
                    mJcsTitle.setSecondRightIcon(R.drawable.ic_hotelwhitelike);
                } else {
                    mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_white);
                }
                like = response.getIs_collect();
                phone = response.getPhone();
                introduceTv.setText(response.getContent());
                noticeTv.setText(response.getNotice());
                navigationRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNaviGoogle("38.888025", "121.594476");
                    }
                });
                initComment();
            }
        });
    }

    @Override
    protected void bindListener() {
        Log.e("TravelDetailActivity", "bindListener: " + "");
        mCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Log.e("TravelDetailActivity", "onItemClick: " + "");
            }
        });
        mJcsTitle.setSecondRightIvClickListener(view -> collection(like != 1));
        mJcsTitle.setFirstRightIvClickListener(view -> collection(like != 1));
//        mCommentAdapter.addChildClickViewIds(R.id.fullText, R.id.commentIcon01, R.id.commentIcon02, R.id.commentIcon03, R.id.commentIcon04);
//        mCommentAdapter.setOnItemChildClickListener(this::onCommentItemChildClicked);
    }

    private void onCommentItemChildClicked(BaseQuickAdapter baseQuickAdapter, View view, int position) {
        Log.e("TravelDetailActivity", "onCommentItemChildClicked: " + "");
        int id = view.getId();
        CommentResponse item = mCommentAdapter.getData().get(position);
        if (id == R.id.fullText) {
            item.is_select = !item.is_select;
            mCommentAdapter.notifyItemChanged(position);
        }

        if (view instanceof RoundedImageView) {
            Intent to = new Intent(this, ImagePreviewActivity.class);
            ArrayList<String> images = (ArrayList<String>) item.getImages();
            to.putStringArrayListExtra(ImagePreviewActivity.IMAGES_URL_KEY, images);
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "commentIcon");

            int imgPosition = -1;
            if (id == R.id.commentIcon01) {
                imgPosition = 0;
            }
            if (id == R.id.commentIcon02) {
                imgPosition = 1;
            }
            if (id == R.id.commentIcon03) {
                imgPosition = 2;
            }
            if (id == R.id.commentIcon04) {
                imgPosition = 3;
            }
            to.putExtra(ImagePreviewActivity.IMAGE_POSITION, imgPosition);


            startActivity(to);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
    }

    private void initComment() {
        showLoading();
        mMode.getTouristAttractionCommentList(mId, new BaseObserver<PageResponse<CommentResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(PageResponse<CommentResponse> response) {
                stopLoading();
                list = new ArrayList<>();
                if (response.getData().size() == 0) {
                    commentLl.setVisibility(View.GONE);
                } else if (response.getData().size() == 1) {
                    commentLl.setVisibility(View.VISIBLE);
                    list.add(response.getData().get(0));
                    mCommentAdapter.setNewInstance(list);
                    commentRv.setAdapter(mCommentAdapter);
                    seeMoreTv.setVisibility(View.GONE);
                } else if (response.getData().size() > 1) {
                    commentLl.setVisibility(View.VISIBLE);
                    list.add(response.getData().get(0));
                    list.add(response.getData().get(1));
                    mCommentAdapter.setNewInstance(list);
                    commentRv.setAdapter(mCommentAdapter);
                    seeMoreTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_traveldetail;
    }

    private void collection(boolean status) {
        showLoading();
        if (status) {
            Map<String, Integer> params = new HashMap<>();
            params.put("travel_id", Integer.valueOf(getIntent().getIntExtra(EXT_ID, 0)));
            HttpUtils.doHttpintReqeust("POST", "travelapi/v1/collects", params, "", TokenManager.get().getToken(TouristAttractionDetailActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        like = 1;
                        mJcsTitle.setSecondRightIcon(R.drawable.ic_hotelwhitelike);
                    } else {
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
        } else {
            HttpUtils.doHttpintReqeust("DELETE", "travelapi/v1/collects/" + getIntent().getIntExtra(EXT_ID, 0), null, "", TokenManager.get().getToken(TouristAttractionDetailActivity.this), new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        like = 2;
                        if (toolbarStatus == 0) {
                            mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_white);
                        } else {
                            mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_black);
                        }
                    } else {
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
            showToast("您尚未安装谷歌地图或地图版本过低");
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
