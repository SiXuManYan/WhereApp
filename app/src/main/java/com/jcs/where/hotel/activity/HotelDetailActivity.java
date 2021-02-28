package com.jcs.where.hotel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.HotelCommentsResponse;
import com.jcs.where.api.response.HotelDetailResponse;
import com.jcs.where.api.response.HotelRoomDetailResponse;
import com.jcs.where.api.response.HotelRoomListResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.RoomListBean;
import com.jcs.where.bean.SubscribeBean;
import com.jcs.where.currency.WebViewActivity;
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.hotel.helper.HotelSelectDateHelper;
import com.jcs.where.hotel.model.HotelDetailModel;
import com.jcs.where.popupwindow.RoomDetailPopup;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.MobUtil;
import com.jcs.where.view.ObservableScrollView;
import com.jcs.where.view.XBanner.AbstractUrlLoader;
import com.jcs.where.view.XBanner.XBanner;
import com.jcs.where.widget.StarView;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import pl.droidsonroids.gif.GifImageView;

/**
 * 页面-酒店详情页
 */
public class HotelDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private XBanner banner;
    private TextView nameTv, startTimeTv, starTv, commnetNumberTv;
    private RelativeLayout faceBookRl;
    private TextView faceBookTv;
    private String faceBookLink;
    private View faceLine;
    private TextView checkInTv, checkOutTv, addressTv;
    private String phone;
    private TextView mStartDateTv, mStartWeekTv, mEndDateTv, mEndWeekTv, mTotalDayTv;
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
    private TextView titleTv;
    private int like = 2;
    private int toolbarStatus = 0;
    private RelativeLayout hotelDetailRl, navigationRl;
    private String hotelName, hotelBreakfast;
    private ImageView star1Iv, star2Iv, star3Iv, star4Iv, star5Iv;
    private StarView mStarView;
    private TextView timeTv;
    private HotelDetailModel mModel;
    private int mHotelId;
    private JcsCalendarAdapter.CalendarBean mStartDateBean;
    private JcsCalendarAdapter.CalendarBean mEndDateBean;
    private int mRoomNum;
    private int mTotalDay;

    public static void goTo(Context context, int id, JcsCalendarAdapter.CalendarBean startDate, JcsCalendarAdapter.CalendarBean endDate, int totalDay, String startYear, String endYear, int roomNumber) {
        Intent intent = new Intent(context, HotelDetailActivity.class);
        intent.putExtra(HotelSelectDateHelper.EXT_ID, id);
        intent.putExtra(HotelSelectDateHelper.EXT_START_DATE_BEAN, startDate);
        intent.putExtra(HotelSelectDateHelper.EXT_END_DATE_BEAN, endDate);
        intent.putExtra(HotelSelectDateHelper.EXT_TOTAL_DAY, totalDay);
        intent.putExtra(HotelSelectDateHelper.EXT_START_YEAR, startYear);
        intent.putExtra(HotelSelectDateHelper.EXT_END_YEAR, endYear);
        intent.putExtra(HotelSelectDateHelper.EXT_ROOM_NUMBER, roomNumber);
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
        toolbar = findViewById(R.id.toolbar);
        //  setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        useView = findViewById(R.id.useView);
        likeIv = findViewById(R.id.iv_like);
        shareIv = findViewById(R.id.iv_share);
        titleTv = findViewById(R.id.titleTv);
        nameTv = findViewById(R.id.tv_name);
        banner = findViewById(R.id.banner3);
        startTimeTv = findViewById(R.id.tv_startTime);
        starTv = findViewById(R.id.tv_star);
        commnetNumberTv = findViewById(R.id.tv_commentnumber);
        faceBookRl = findViewById(R.id.rl_facebook);
        faceBookTv = findViewById(R.id.tv_facebookadress);
        faceBookRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.goTo(HotelDetailActivity.this, faceBookLink.trim());
            }
        });
        faceLine = findViewById(R.id.faceline);
        checkInTv = findViewById(R.id.tv_checkin);
        checkOutTv = findViewById(R.id.tv_checkout);
        addressTv = findViewById(R.id.tv_address);
        findViewById(R.id.rl_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(HotelDetailActivity.this)
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
        mStartDateTv = findViewById(R.id.startDayTv);
        mStartWeekTv = findViewById(R.id.startWeekTv);
        mEndDateTv = findViewById(R.id.endDayTv);
        mEndWeekTv = findViewById(R.id.endWeekTv);
        mTotalDayTv = findViewById(R.id.totalDayTv);
        roomRv = findViewById(R.id.rv_room);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        roomAdapter = new RoomAdapter();
        roomRv.setLayoutManager(linearLayoutManager);
        facilitiesRv = findViewById(R.id.rv_facilities);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HotelDetailActivity.this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        facilitiesRv.setLayoutManager(gridLayoutManager);
        facilitiesAdapter = new FacilitiesAdapter();
        policyStartTimeTv = findViewById(R.id.tv_policystarttime);
        policyEndTimeTv = findViewById(R.id.tv_policyendtime);
        policyChildrenTv = findViewById(R.id.tv_policychildren);
        scrollView = findViewById(R.id.scrollView);
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
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhiteunlike));
                    }
                    shareIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_share_black));
                    toolbar.setNavigationIcon(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_back_black));
                    toolbarStatus = 1;
                } else {
                    if (like == 1) {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hoteltransparentunlike));
                    }
                    shareIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_share_white));
                    toolbar.setNavigationIcon(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_back_white));
                    toolbarStatus = 0;
                }
                useView.getBackground().setAlpha(alpha);
                toolbar.getBackground().setAlpha(alpha);
                if (alpha == 255) {
                    titleTv.setText(nameTv.getText().toString());
                }
                if (alpha == 0) {
                    titleTv.setText("");
                }
                changeStatusTextColor();
            }
        });
        useView.getBackground().setAlpha(0);
        toolbar.getBackground().setAlpha(0);//透明
        commentLl = findViewById(R.id.ll_comment);
        commentAvaterIv = findViewById(R.id.iv_commentavatar);
        commentNameTv = findViewById(R.id.username);
        commentDetailTv = findViewById(R.id.tv_commentdetail);
        seeMoreTv = findViewById(R.id.tv_seemore);
        seeMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelCommentActivity.goTo(HotelDetailActivity.this, mHotelId);
            }
        });
        findViewById(R.id.tv_commentnumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelCommentActivity.goTo(HotelDetailActivity.this, mHotelId);
            }
        });
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collection(like != 1);
            }
        });
        hotelDetailRl = findViewById(R.id.rl_hoteldetail);
        navigationRl = findViewById(R.id.rl_navigation);
        findViewById(R.id.ll_choosedate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mStarView = findViewById(R.id.starView);
        timeTv = findViewById(R.id.tv_time);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mStartDateBean = (JcsCalendarAdapter.CalendarBean) intent.getSerializableExtra(HotelSelectDateHelper.EXT_START_DATE_BEAN);
        mEndDateBean = (JcsCalendarAdapter.CalendarBean) intent.getSerializableExtra(HotelSelectDateHelper.EXT_END_DATE_BEAN);
        mRoomNum = intent.getIntExtra(HotelSelectDateHelper.EXT_ROOM_NUMBER, 0);
        mTotalDay = intent.getIntExtra(HotelSelectDateHelper.EXT_TOTAL_DAY, 0);
        mModel = new HotelDetailModel();
        mHotelId = intent.getIntExtra(HotelSelectDateHelper.EXT_ID, 0);

        mStartDateTv.setText(mStartDateBean.getShowMonthDayDate());
        mStartWeekTv.setText(mStartDateBean.getShowWeekday());
        mEndDateTv.setText(mEndDateBean.getShowMonthDayDate());
        mEndWeekTv.setText(mEndDateBean.getShowWeekday());
        String totalDayStr = mTotalDay + getString(R.string.night);
        mTotalDayTv.setText(totalDayStr);

        showLoading();
        //获取酒店详情
        mModel.getHotelDetail(mHotelId, new BaseObserver<HotelDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull HotelDetailResponse hotelDetailResponse) {
                stopLoading();
                if (hotelDetailResponse.getImages() != null) {
                    banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                            .setImageUrls(hotelDetailResponse.getImages())
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
                }
                nameTv.setText(hotelDetailResponse.getName());

                shareIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        WriteCommentActivity.goTo(HotelDetailActivity.this, mHotelId, hotelDetailResponse.getName());
                        onShareClick();
                    }
                });
                hotelName = hotelDetailResponse.getName();
                hotelBreakfast = hotelDetailResponse.getPolicy().getBreadfast();

                String startText = String.format(getString(R.string.open_time), hotelDetailResponse.getStart_business_time());
                startTimeTv.setText(startText);
                starTv.setText(String.valueOf(hotelDetailResponse.getGrade()));
                String commentNumberText = String.format(getString(R.string.comment_num_prompt), hotelDetailResponse.getComment_counts());
                commnetNumberTv.setText(commentNumberText);
                if (TextUtils.isEmpty(hotelDetailResponse.getFacebook_link())) {
                    faceBookRl.setVisibility(View.GONE);
                    faceLine.setVisibility(View.GONE);
                } else {
                    faceBookRl.setVisibility(View.VISIBLE);
                    faceLine.setVisibility(View.VISIBLE);
                    faceBookLink = hotelDetailResponse.getFacebook_link();
                }
                checkInTv.setText(String.format(getString(R.string.check_in_time), hotelDetailResponse.getPolicy().getCheck_in_time().substring(0, 5)));
                checkOutTv.setText(String.format(getString(R.string.check_out_time), hotelDetailResponse.getPolicy().getCheck_out_time().substring(0, 5)));
                addressTv.setText(hotelDetailResponse.getAddress());
                phone = hotelDetailResponse.getTel();
                facilitiesAdapter.addData(hotelDetailResponse.getFacilities());
                facilitiesRv.setAdapter(facilitiesAdapter);
                policyStartTimeTv.setText(String.format(getString(R.string.check_in_time), hotelDetailResponse.getPolicy().getCheck_in_time()));
                policyEndTimeTv.setText(String.format(getString(R.string.check_out_time), hotelDetailResponse.getPolicy().getCheck_out_time()));

                policyChildrenTv.setText(String.format(getString(R.string.child_and_bed_added), hotelDetailResponse.getPolicy().getChildren()));
                if (hotelDetailResponse.getCollect_status() == 1) {
                    likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                } else {
                    likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hoteltransparentunlike));
                }
                like = hotelDetailResponse.getCollect_status();
                navigationRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNaviGoogle("38.888025", "121.594476");
                    }
                });
                initRoomList();
                initCommentList();
            }
        });
    }

    @Override
    protected void bindListener() {
        toolbar.setNavigationOnClickListener(this::onBackIconClicked);
    }

    public void onBackIconClicked(View view) {
        finish();
    }

    private void initRoomList() {
        showLoading();
        Log.e("HotelDetailActivity", "initRoomList: " + "mHotelId=" + mHotelId);

        mModel.getHotelRooms(mHotelId, mStartDateBean.getShowYearMonthDayDateWithSplit(), mEndDateBean.getShowYearMonthDayDateWithSplit(), mRoomNum, new BaseObserver<List<HotelRoomListResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull List<HotelRoomListResponse> hotelRoomListRespons) {
                stopLoading();
                roomAdapter.addData(hotelRoomListRespons);
                roomRv.setAdapter(roomAdapter);
            }
        });
    }

    private void initCommentList() {
        showLoading();
        mModel.getComments(mHotelId, new BaseObserver<HotelCommentsResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
            }

            @Override
            public void onSuccess(@NonNull HotelCommentsResponse hotelCommentsResponse) {
                stopLoading();
                if (hotelCommentsResponse.getData().size() == 0) {
                    commentLl.setVisibility(View.GONE);
                } else {
                    commentLl.setVisibility(View.VISIBLE);
                    if (hotelCommentsResponse.getData().size() > 1) {
                        seeMoreTv.setVisibility(View.VISIBLE);
                    } else {
                        seeMoreTv.setVisibility(View.GONE);
                    }
                    HotelCommentsResponse.DataBean dataBean = hotelCommentsResponse.getData().get(0);
                    if (!TextUtils.isEmpty(dataBean.getAvatar())) {
                        GlideUtil.load(HotelDetailActivity.this, dataBean.getAvatar(), commentAvaterIv);
                    } else {
                        commentAvaterIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_test));
                    }
                    commentNameTv.setText(dataBean.getUsername());
                    timeTv.setText(dataBean.getCreated_at());
                    mStarView.setStartNum(dataBean.getStar());
                    commentDetailTv.setText(dataBean.getContent());
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel_detail;
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    private void collection(boolean status) {
        showLoading();
        if (status) {
            mModel.postCollectHotel(mHotelId, new BaseObserver<Object>() {

                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                protected void onSuccess(Object response) {
                    stopLoading();
                    like = 1;
                    if (toolbarStatus == 0) {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                    }
                }
            });
        } else {
            mModel.delCollectHotel(mHotelId, new BaseObserver<Object>() {

                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                protected void onSuccess(Object response) {
                    stopLoading();
                    like = 2;
                    if (toolbarStatus == 0) {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hoteltransparentunlike));
                    } else {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhiteunlike));
                    }
                }

            });
        }
    }

    private void initRoomDetail(final int roomId, final int breakfast) {
        showLoading();
        mModel.getHotelRoomById(roomId, mStartDateBean.getShowYearMonthDayDateWithSplit(), mEndDateBean.getShowYearMonthDayDateWithSplit(), mRoomNum, new BaseObserver<HotelRoomDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(HotelRoomDetailResponse response) {
                stopLoading();
                new RoomDetailPopup.Builder(HotelDetailActivity.this, hotelDetailRl, roomId, response)
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
                                    subscribeBean.breakfast = getString(R.string.with_breakfast);
                                } else {
                                    subscribeBean.breakfast = getString(R.string.no_breakfast);
                                }
                                subscribeBean.window = window;
                                subscribeBean.wifi = wifi;
                                subscribeBean.people = people;
                                subscribeBean.cancel = cancel;
                                subscribeBean.startDate = mStartDateTv.getText().toString();
                                subscribeBean.startWeek = mStartWeekTv.getText().toString();
                                subscribeBean.startYMD = mStartDateBean.getShowYearMonthDayDateWithSplit();
                                subscribeBean.endYMD = mEndDateBean.getShowYearMonthDayDateWithSplit();
                                subscribeBean.endDate = mEndDateTv.getText().toString();
                                subscribeBean.endWeek = mEndWeekTv.getText().toString();
                                subscribeBean.night = mTotalDayTv.getText().toString();
                                subscribeBean.roomNumber = String.valueOf(mRoomNum);
                                subscribeBean.roomPrice = response.getPrice();
//                                    subscribeBean.startYear = getIntent().getStringExtra(EXT_STARTYEAR);
//                                    subscribeBean.endYear = getIntent().getStringExtra(EXT_ENDYEAR);
                                HotelSubscribeActivity.goTo(HotelDetailActivity.this, subscribeBean);
                            }
                        }).builder();

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
            showToast(getString(R.string.no_install_google_map_prompt));
        }
    }

    private class RoomAdapter extends BaseQuickAdapter<HotelRoomListResponse, BaseViewHolder> {

        private TagAdapter tagAdapter;

        public RoomAdapter() {
            super(R.layout.item_room);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelRoomListResponse data) {
            RoundedImageView photoIv = baseViewHolder.findView(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                GlideUtil.load(getContext(), data.getImages().get(0), photoIv);
            } else {
                photoIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_test));
            }
            TextView nameTv = baseViewHolder.findView(R.id.tv_name);
            nameTv.setText(data.getName());
            TextView typeTv = baseViewHolder.findView(R.id.tv_hotelType);
            String breakfast = null;
            if (data.getBreakfast_type() == 1) {
                breakfast = getString(R.string.with_breakfast);
            } else {
                breakfast = getString(R.string.no_breakfast);
            }
            String typeText = data.getHotel_room_type() + "  " + breakfast + "  ";
            typeTv.setText(typeText);

            SpannableString m2 = new SpannableString("m2");
            m2.setSpan(new RelativeSizeSpan(0.5f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//一半大小
            m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   //上标
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(data.getRoom_area());
            spannableStringBuilder.append(m2);
            TextView roomAreaTv = baseViewHolder.findView(R.id.tv_roomarea);
            roomAreaTv.setText(spannableStringBuilder);
            TextView priceTv = baseViewHolder.findView(R.id.tv_price);
            priceTv.setText(String.format(getString(R.string.show_price_with_forward_unit), data.getPrice()));
            RecyclerView tagRv = baseViewHolder.findView(R.id.rv_tag);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this,
                    LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            tagAdapter = new TagAdapter();
            tagRv.setLayoutManager(linearLayoutManager);
//            tagAdapter.addData(data.getTags());
//            tagRv.setAdapter(tagAdapter);
            TextView subscribeTv = baseViewHolder.findView(R.id.tv_subscribe);
            LinearLayout tagLl = baseViewHolder.findView(R.id.ll_tag);
            if (data.getRemain_room_num() == 0) {
                subscribeTv.setText("已客满");
                subscribeTv.setBackground(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.bg_noroom));
                subscribeTv.setTextColor(getResources().getColor(R.color.grey_999999));
                subscribeTv.setEnabled(false);
                tagLl.setVisibility(View.GONE);
                priceTv.setTextColor(getResources().getColor(R.color.grey_999999));
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                photoIv.setColorFilter(filter);
            } else {
                subscribeTv.setText(getString(R.string.subscribe_prompt));
                subscribeTv.setBackground(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.bg_roomsubscribe));
                subscribeTv.setTextColor(getResources().getColor(R.color.white));
                subscribeTv.setEnabled(true);
                tagLl.setVisibility(View.VISIBLE);
                priceTv.setTextColor(getResources().getColor(R.color.orange_FF5B1B));
            }
            baseViewHolder.findView(R.id.tv_subscribe).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initRoomDetail(data.getId(), data.getBreakfast_type());
                }
            });
            baseViewHolder.findView(R.id.rl_room).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initRoomDetail(data.getId(), data.getBreakfast_type());
                }
            });

        }
    }

    private static class TagAdapter extends BaseQuickAdapter<RoomListBean.TagsBean, BaseViewHolder> {

        public TagAdapter() {
            super(R.layout.item_roomtag);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, RoomListBean.TagsBean tagsBean) {
            TextView tagTv = baseViewHolder.findView(R.id.tv_tag);
            tagTv.setText(tagsBean.getZh_cn_name());
        }
    }

    private static class FacilitiesAdapter extends BaseQuickAdapter<HotelDetailResponse.FacilitiesBean, BaseViewHolder> {

        public FacilitiesAdapter() {
            super(R.layout.item_facilities);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelDetailResponse.FacilitiesBean data) {

            ImageView iconIv = baseViewHolder.findView(R.id.iv_icon);
            if (!TextUtils.isEmpty(data.getIcon())) {
                GlideUtil.load(getContext(), data.getIcon(), iconIv);
            } else {
                iconIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_test));
            }
            TextView nameTv = baseViewHolder.findView(R.id.tv_name);
            nameTv.setText(data.getName());
        }
    }

    @Override
    protected boolean isStatusDark() {
        return toolbarStatus == 1;
    }

    private void onShareClick() {
        String url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_HOTEL, String.valueOf(mHotelId));
        MobUtil.shareFacebookWebPage(url, this);
    }


}
