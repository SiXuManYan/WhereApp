package com.jcs.where.hotel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.JsonElement;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.HotelDetailResponse;
import com.jcs.where.api.response.HotelRoomDetailResponse;
import com.jcs.where.api.response.HotelRoomListResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.hotel.HotelComment;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.SubscribeBean;
import com.jcs.where.currency.WebViewActivity;
import com.jcs.where.features.hotel.comment.HotelCommentActivity2;
import com.jcs.where.features.hotel.comment.child.HotelCommentAdapter;
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter;
import com.jcs.where.hotel.activity.detail.MediaData;
import com.jcs.where.hotel.helper.HotelSelectDateHelper;
import com.jcs.where.hotel.model.HotelDetailModel;
import com.jcs.where.popupwindow.RoomDetailPopup;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.MobUtil;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.widget.pager.IndicatorView2;
import com.jcs.where.widget.ratingstar.RatingStarView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 页面-酒店详情页
 */
public class HotelDetailActivity extends BaseActivity {

    private static final int CALL_PHONE_CODE = 1000;
    private Toolbar toolbar;
    //    private XBanner banner;
    private TextView nameTv, startTimeTv, starTv, commnetNumberTv;
    private RelativeLayout faceBookRl;
    private TextView faceBookTv;
    private String faceBookLink;
    private View faceLine;
    private TextView checkInTv, checkOutTv, addressTv;
    private String phone;
    private TextView mStartDateTv, mStartWeekTv, mEndDateTv, mEndWeekTv, mTotalDayTv;
    private RecyclerView roomRv, rv_facilities;
    private RoomAdapter roomAdapter;
    private FacilitiesAdapter facilitiesAdapter;
    private TextView policyStartTimeTv, policyEndTimeTv, policyChildrenTv;
    private NestedScrollView scrollView;
    private View useView;
    private LinearLayout ll_comment;
    private TextView seeMoreTv;
    private ImageView likeIv, shareIv;
    private TextView titleTv, desc_tv, view_all_desc_tv, view_all_amenities_tv;
    private int like = 2;
    private int toolbarStatus = 0;
    private RelativeLayout hotelDetailRl, navigationRl;
    private String hotelName, hotelBreakfast;
    private ImageView star1Iv, star2Iv, star3Iv, star4Iv, star5Iv;
    private HotelDetailModel mModel;
    private int mHotelId;
    private JcsCalendarAdapter.CalendarBean mStartDateBean;
    private JcsCalendarAdapter.CalendarBean mEndDateBean;
    private int mRoomNum;
    private int mTotalDay;
    private JcsCalendarDialog mJcsCalendarDialog;
    private ImageView back_iv;
    private RecyclerView media_rv;
    private IndicatorView2 point_view;
    private DetailMediaAdapter mMediaAdapter;
    private RatingStarView star_view;
    private ImageView phone_iv;
    private TextView phone_tv;
    private RelativeLayout rl_phone;
    //

    private String mer_name;
    private String tel;
    private int im_status;
    private String uuid;

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

        initMedia();

        mJcsCalendarDialog = new JcsCalendarDialog();
        mJcsCalendarDialog.initCalendar(this);

        phone_iv = findViewById(R.id.phone_iv);
        phone_tv = findViewById(R.id.phone_tv);
        rl_phone = findViewById(R.id.rl_phone);

        desc_tv = findViewById(R.id.desc_tv);
        view_all_amenities_tv = findViewById(R.id.view_all_amenities_tv);
        view_all_desc_tv = findViewById(R.id.view_all_desc_tv);
        toolbar = findViewById(R.id.toolbar);
        back_iv = findViewById(R.id.back_iv);
        //  setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        useView = findViewById(R.id.useView);
        likeIv = findViewById(R.id.iv_like);
        shareIv = findViewById(R.id.iv_share);
        titleTv = findViewById(R.id.titleTv);
        nameTv = findViewById(R.id.tv_name);
        star_view = findViewById(R.id.star_view);

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
        roomRv.setNestedScrollingEnabled(true);
        roomRv.setAdapter(roomAdapter);


        rv_facilities = findViewById(R.id.rv_facilities);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HotelDetailActivity.this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_facilities.setLayoutManager(gridLayoutManager);
        rv_facilities.setNestedScrollingEnabled(true);
        facilitiesAdapter = new FacilitiesAdapter();
        rv_facilities.setAdapter(facilitiesAdapter);


        policyStartTimeTv = findViewById(R.id.tv_policystarttime);
        policyEndTimeTv = findViewById(R.id.tv_policyendtime);
        policyChildrenTv = findViewById(R.id.tv_policychildren);
        scrollView = findViewById(R.id.scrollView);
        useView.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView scrollView, int x, int y, int oldx, int oldy) {
                float headHeight = media_rv.getMeasuredHeight() - toolbar.getMeasuredHeight();
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
                    back_iv.setImageResource(R.drawable.ic_back_black);


                    toolbarStatus = 1;
                } else {
                    if (like == 1) {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                    } else {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hoteltransparentunlike));
                    }
                    shareIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_share_white));
//                    toolbar.setNavigationIcon(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_back_white));
                    back_iv.setImageResource(R.drawable.ic_back_white);
                    toolbarStatus = 0;
                }
                useView.getBackground().setAlpha(alpha);
                toolbar.getBackground().setAlpha(alpha);
                if (alpha == 255) {
//                    titleTv.setText(nameTv.getText().toString());
                    titleTv.setText("");
                }
                if (alpha == 0) {
                    titleTv.setText("");
                }
                changeStatusTextColor();

            }
        });


        useView.getBackground().setAlpha(0);
        toolbar.getBackground().setAlpha(0);//透明
        ll_comment = findViewById(R.id.ll_comment);

        seeMoreTv = findViewById(R.id.tv_seemore);
        seeMoreTv.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.PARAM_ID, mHotelId);
            startActivity(HotelCommentActivity2.class, bundle);
        });
        findViewById(R.id.tv_commentnumber).setOnClickListener(view -> {
            seeMoreTv.performClick();
        });
        likeIv.setOnClickListener(view -> collection(like != 1));
        hotelDetailRl = findViewById(R.id.rl_hoteldetail);
        navigationRl = findViewById(R.id.rl_navigation);


    }

    private void initMedia() {
        mMediaAdapter = new DetailMediaAdapter();
        media_rv = findViewById(R.id.media_rv);
        point_view = findViewById(R.id.point_view);


        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(media_rv);

        media_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        media_rv.setAdapter(mMediaAdapter);
        media_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@androidx.annotation.NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;

                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();

                    //大于0说明有播放
                    if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                        //当前播放的位置
                        int position = GSYVideoManager.instance().getPlayPosition();

                        // 对应的播放列表TAG
                        if (GSYVideoManager.instance().getPlayTag().equals(DetailMediaAdapter.TAG) && (position < firstItemPosition || position > lastItemPosition)) {
                            if (GSYVideoManager.isFullState(HotelDetailActivity.this)) {
                                return;
                            }
                            //如果滑出去了上面和下面就是否
                            GSYVideoManager.releaseAllVideos();
                            mMediaAdapter.notifyDataSetChanged();
                        }

                    }

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        point_view.onPageSelected(firstItemPosition);
                    }


                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
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
            public void onSuccess(@NonNull HotelDetailResponse data) {
                stopLoading();

                ArrayList<MediaData> mediaList = new ArrayList<>();

                if (!TextUtils.isEmpty(data.video)) {
                    MediaData media = new MediaData();
                    media.setType(MediaData.VIDEO);
                    media.setCover(data.video_image);
                    media.setSrc(data.video);
                    mediaList.add(media);
                }
                for (String image : data.getImages()) {

                    MediaData media = new MediaData();
                    media.setType(MediaData.IMAGE);
                    media.setCover(image);
                    media.setSrc(image);
                    mediaList.add(media);
                }
                mMediaAdapter.setNewInstance(mediaList);
                point_view.setPointCount(mediaList.size());

                nameTv.setText(data.getName());
                double grade = data.getGrade();


                star_view.setRating(data.star_level);

                shareIv.setOnClickListener(view -> {
//                        WriteCommentActivity.goTo(HotelDetailActivity.this, mHotelId, data.getName());
                    onShareClick();
                });
                hotelName = data.getName();
//                hotelBreakfast = data.getPolicy().getBreadfast();
                hotelBreakfast = "";

                String startText = String.format(getString(R.string.open_time), data.getStart_business_time());
                startTimeTv.setText(startText);
                starTv.setText(getString(R.string.star_text_format, String.valueOf(grade), FeaturesUtil.getGradeRetouchString((float) grade)));

                String commentNumberText = String.format(getString(R.string.comment_num_prompt), data.getComment_counts());
                commnetNumberTv.setText(commentNumberText);
                if (TextUtils.isEmpty(data.getFacebook_link())) {
                    faceBookRl.setVisibility(View.GONE);
                    faceLine.setVisibility(View.GONE);
                } else {
                    faceBookRl.setVisibility(View.VISIBLE);
                    faceLine.setVisibility(View.VISIBLE);
                    faceBookLink = data.getFacebook_link();
                }

                try {
                    // 返回数据某些情况会类型不匹配
                    String check_in_time = data.policy.check_in_time;
                    String check_out_time = data.policy.check_out_time;

                    if (TextUtils.isEmpty(check_in_time)) {
                        check_in_time = "";
                    }
                    if (TextUtils.isEmpty(check_out_time)) {
                        check_out_time = "";
                    }

                    checkInTv.setText(String.format(getString(R.string.check_in_time), check_in_time));
                    checkOutTv.setText(String.format(getString(R.string.check_out_time), check_out_time));
                    policyStartTimeTv.setText(String.format(getString(R.string.check_in_time), check_in_time));
                    policyEndTimeTv.setText(String.format(getString(R.string.check_out_time), check_out_time));
                    policyChildrenTv.setText(data.policy.children);

                } catch (Exception ignored) {

                }

                addressTv.setText(data.getAddress());
                phone = data.getTel();
                facilitiesAdapter.setNewInstance(data.getFacilities());

                if (data.getCollect_status() == 1) {
                    likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                } else {
                    likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hoteltransparentunlike));
                }
                like = data.getCollect_status();
                navigationRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNaviGoogle("38.888025", "121.594476");
                    }
                });
                initRoomList();
                initCommentList();
                String desc = data.desc;
                if (TextUtils.isEmpty(desc)) {
                    desc_tv.setText(R.string.empty_hotel_desc);
                } else {
                    desc_tv.setText(desc);
                }


                // im
                im_status = data.im_status;
                mer_name = data.mer_name;
                tel = data.tel;
                uuid = data.uuid;

                if (im_status == 1) {
                    phone_iv.setImageResource(R.mipmap.ic_chat);
                    phone_tv.setText(R.string.chat);
                } else {
                    phone_iv.setImageResource(R.mipmap.ic_phone_blue);
                    phone_tv.setText(R.string.telephone);
                }

            }
        });
    }

    private void onChooseDate(View view) {
        mJcsCalendarDialog.show(getSupportFragmentManager());
    }

    @Override
    protected void bindListener() {
        back_iv.setOnClickListener(this::onBackIconClicked);
        findViewById(R.id.ll_choosedate).setOnClickListener(this::onChooseDate);
        mJcsCalendarDialog.setOnDateSelectedListener(this::onDateSelected);
        view_all_desc_tv.setOnClickListener(v -> {
            if (desc_tv.getMaxLines() == 3) {
                desc_tv.setMaxLines(100);
            } else {
                desc_tv.setMaxLines(3);
            }
        });

        view_all_amenities_tv.setOnClickListener(v -> {


            for (int i = 0; i < facilitiesAdapter.getData().size(); i++) {
                if (i > 5) {
                    View view = facilitiesAdapter.getViewByPosition(i, R.id.facilities_rl);
                    if (view != null) {
                        if (view.getVisibility() == View.GONE) {
                            view.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.GONE);
                        }
                    }

                }
            }
        });

        rl_phone.setOnClickListener(v -> {
            if (im_status == 1) {
                if (!TextUtils.isEmpty(uuid)) {
                    RongIM.getInstance().startConversation(HotelDetailActivity.this, Conversation.ConversationType.PRIVATE, uuid, mer_name, null);
                }
            } else {
                Uri data = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(data);
                startActivity(intent);
            }

        });

    }

    public void onDateSelected(JcsCalendarAdapter.CalendarBean startDate, JcsCalendarAdapter.CalendarBean endDate) {
        if (startDate != null) {
            deployDateTv(mStartDateTv, mStartWeekTv, startDate);
        }

        if (endDate != null) {
            deployDateTv(mEndDateTv, mEndWeekTv, endDate);
        }
        mTotalDayTv.setText(mJcsCalendarDialog.getTotalDay());
        mTotalDay = mJcsCalendarDialog.getTotalDay2();
    }

    public void deployDateTv(TextView dateTv, TextView weekTv, JcsCalendarAdapter.CalendarBean calendarBean) {
        dateTv.setText(calendarBean.getShowMonthDayDate());
        weekTv.setText(calendarBean.getShowWeekday());
    }


    public void onBackIconClicked(View view) {
        finish();
    }

    private void initRoomList() {
        showLoading();
        Log.e("HotelDetailActivity", "initRoomList: " + "mHotelId=" + mHotelId);

        mModel.getHotelRooms(mHotelId, mStartDateBean.getShowYearMonthDayDateWithSplit(), mEndDateBean.getShowYearMonthDayDateWithSplit(), mRoomNum, new BaseObserver<ArrayList<HotelRoomListResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull ArrayList<HotelRoomListResponse> hotelRoomListRespons) {
                stopLoading();
                roomAdapter.setNewInstance(hotelRoomListRespons);
            }
        });
    }


    private RecyclerView comment_rv;
    private HotelCommentAdapter mAdapter = new HotelCommentAdapter();


    private void initCommentList() {

        comment_rv = findViewById(R.id.comment_rv);
        comment_rv.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        comment_rv.setLayoutManager(linearLayoutManager);

        mModel.getHotelCommentList(mHotelId, new BaseObserver<PageResponse<HotelComment>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            public void onSuccess(@NonNull PageResponse<HotelComment> hotelCommentsResponse) {
                mAdapter.setNewInstance(hotelCommentsResponse.getData());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel_detail;
    }


    private void collection(boolean status) {
        showLoading();
        if (status) {
            mModel.postCollectHotel(mHotelId, new BaseObserver<JsonElement>() {

                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                protected void onSuccess(JsonElement response) {
                    stopLoading();
                    like = 1;
                    likeIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_hotelwhitelike));
                }
            });
        } else {
            mModel.delCollectHotel(mHotelId, new BaseObserver<JsonElement>() {

                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                protected void onSuccess(JsonElement response) {
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
                                SubscribeBean bean = new SubscribeBean();
                                bean.hotelName = hotelName;
                                bean.roomId = id;
                                bean.roomName = name;
                                bean.bed = bed;
                                if (breakfast == 1) {
                                    bean.breakfast = getString(R.string.with_breakfast);
                                } else {
                                    bean.breakfast = getString(R.string.no_breakfast);
                                }
                                bean.window = window;
                                bean.wifi = wifi;
                                bean.people = people;
                                bean.cancel = cancel;
                                bean.startDate = mStartDateTv.getText().toString();
                                bean.startWeek = mStartWeekTv.getText().toString();
                                bean.startYMD = mStartDateBean.getShowYearMonthDayDateWithSplit();
                                bean.endYMD = mEndDateBean.getShowYearMonthDayDateWithSplit();
                                bean.endDate = mEndDateTv.getText().toString();
                                bean.endWeek = mEndWeekTv.getText().toString();
                                bean.night = mTotalDayTv.getText().toString();
                                bean.roomNumber = String.valueOf(mRoomNum);
                                bean.roomPrice = response.getPrice();
//                                    bean.startYear = getIntent().getStringExtra(EXT_STARTYEAR);
//                                    bean.endYear = getIntent().getStringExtra(EXT_ENDYEAR);
                                HotelSubscribeActivity.goTo(HotelDetailActivity.this, bean);
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
        protected void convert(@NotNull BaseViewHolder holder, HotelRoomListResponse data) {

            TextView surplus_tv = holder.getView(R.id.surplus_tv);
            surplus_tv.setText(getString(R.string.surplus_format, data.getRemain_room_num()));

            RoundedImageView photoIv = holder.getView(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                GlideUtil.load(getContext(), data.getImages().get(0), photoIv);
            } else {
                photoIv.setImageDrawable(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.ic_test));
            }
            TextView nameTv = holder.getView(R.id.tv_name);

            nameTv.setText(data.getName());
            TextView typeTv = holder.getView(R.id.tv_hotelType);
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
            TextView roomAreaTv = holder.getView(R.id.tv_roomarea);
            roomAreaTv.setText(spannableStringBuilder);
            TextView priceTv = holder.getView(R.id.tv_price);
            priceTv.setText(String.format(getString(R.string.show_price_with_forward_unit), data.getPrice()));
            RecyclerView tagRv = holder.getView(R.id.rv_tag);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this,
                    LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            tagAdapter = new TagAdapter();


            tagRv.setLayoutManager(linearLayoutManager);

            tagAdapter.addData(data.getTags());


            tagRv.setAdapter(tagAdapter);


            TextView subscribeTv = holder.getView(R.id.tv_subscribe);
            LinearLayout tagLl = holder.getView(R.id.ll_tag);
            if (data.getRoom_num() == 0) {
                subscribeTv.setText(R.string.full);
                subscribeTv.setBackground(ContextCompat.getDrawable(HotelDetailActivity.this, R.drawable.bg_noroom));
                subscribeTv.setTextColor(getResources().getColor(R.color.grey_999999));
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
                tagLl.setVisibility(View.VISIBLE);
                priceTv.setTextColor(getResources().getColor(R.color.orange_FF5B1B));
                holder.getView(R.id.rl_room).setOnClickListener(view ->
                        initRoomDetail(data.getId(), data.getBreakfast_type())
                );
            }


        }
    }


    private static class TagAdapter extends BaseQuickAdapter<HotelRoomListResponse.TagsBean, BaseViewHolder> {

        public TagAdapter() {
            super(R.layout.item_roomtag);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelRoomListResponse.TagsBean tagsBean) {
            TextView tagTv = baseViewHolder.getView(R.id.tv_tag);
            tagTv.setText(tagsBean.getZh_cn_name());
        }
    }

    private static class FacilitiesAdapter extends BaseQuickAdapter<HotelDetailResponse.FacilitiesBean, BaseViewHolder> {

        public FacilitiesAdapter() {
            super(R.layout.item_facilities);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, HotelDetailResponse.FacilitiesBean data) {


            RelativeLayout facilities_rl = holder.getView(R.id.facilities_rl);
            ImageView iconIv = holder.getView(R.id.iv_icon);
            if (!TextUtils.isEmpty(data.getIcon())) {
                GlideUtil.load(getContext(), data.getIcon(), iconIv);
            } else {
                iconIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_test));
            }
            TextView nameTv = holder.getView(R.id.tv_name);
            nameTv.setText(data.getName());

            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition > 5) {
                facilities_rl.setVisibility(View.GONE);
            } else {
                facilities_rl.setVisibility(View.VISIBLE);
            }

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

    private boolean checkPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(HotelDetailActivity.this, Manifest.permission.CALL_PHONE) != PermissionChecker.PERMISSION_GRANTED) {
                    // 不相等 请求授权
                    ActivityCompat.requestPermissions(HotelDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_CODE);
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
