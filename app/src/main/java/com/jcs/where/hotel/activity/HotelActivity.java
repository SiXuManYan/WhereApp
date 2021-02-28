package com.jcs.where.hotel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.jcs.where.R;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.home.dialog.HotelStarDialog;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.hotel.model.HotelModel;
import com.jcs.where.utils.LocationUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 酒店预订页面
 */
public class HotelActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, HotelStarDialog.HotelStarCallback {

    private static final int REQ_SELECT_CITY = 100;
    public static final String K_CATEGORY_ID = "categoryId";
    private final int READ_CODE = 10;
    private final int READ_LOCATIONCODE = 11;
    private TextView mLocationTv, mStartDateTv, mStartWeekTv, mEndDateTv, mEndWeekTv, mTotalDayTv, mRoomNumTv, mPriceAndStarTv;
    private RelativeLayout mChooseDateRl;
    private ImageView mRoomReduceIv, mRoomAddIv;
    private RecyclerView showRv;
    private GuessYouLikeAdapter mGuessYouLikeAdapter;
    private final int startGroup = -1;
    private final int endGroup = -1;
    private final int startChild = -1;
    private final int endChild = -1;
    private String cityId = "0";
    private String usePrice = null;
    private String useStar = null;
    private String useStartYear, useEndYear;
    private ImageView mClearIv;
    private String transmitPrice, transmitStar;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng lastLatLng, perthLatLng;
    private boolean mAddressRequested;
    private HotelModel mModel;

    private HotelStarDialog mHotelStarDialog;
    private JcsCalendarDialog mJcsCalendarDialog;
    private TextView mChooseLocationTv;
    private TextView mSearchTv;
    private int mTotalDay = 1;

    public static void goTo(Context context) {
        Intent intent = new Intent(context, HotelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("MM月dd日");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    public static String getOldWeek(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    @Override
    protected void initView() {
        mGuessYouLikeAdapter = new GuessYouLikeAdapter();
        mLocationTv = findViewById(R.id.locationTv);
        mLocationTv.setOnClickListener(this);
        mChooseDateRl = findViewById(R.id.rl_choosedate);
        mChooseDateRl.setOnClickListener(this);
        mStartDateTv = findViewById(R.id.startDayTv);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_format_month_day), LocalLanguageUtil.getInstance().getSetLanguageLocale(this));
        Date date = new Date(System.currentTimeMillis());
        mStartDateTv.setText(simpleDateFormat.format(date));
        mStartWeekTv = findViewById(R.id.startWeekTv);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(getString(R.string.date_format_yyyy_mm_dd), LocalLanguageUtil.getInstance().getSetLanguageLocale(this));
        Date date1 = new Date(System.currentTimeMillis());
//        startWeekTv.setText("周" + CalendarUtil.getWeekByFormat(simpleDateFormat1.format(date1)));
        mEndDateTv = findViewById(R.id.endDayTv);
//        mEndDateTv.setText(getOldDate(1));
        mEndWeekTv = findViewById(R.id.endWeekTv);
//        endWeekTv.setText("周" + CalendarUtil.getWeekByFormat(getOldWeek(1)));
        mTotalDayTv = findViewById(R.id.totalDayTv);
        mTotalDayTv.setText(String.format(getString(R.string.total_night), 1));
        mRoomNumTv = findViewById(R.id.tv_roomnum);
        mRoomReduceIv = findViewById(R.id.iv_roomreduce);
        mRoomReduceIv.setOnClickListener(this);
        mRoomAddIv = findViewById(R.id.iv_roomadd);
        mRoomAddIv.setOnClickListener(this);
        mPriceAndStarTv = findViewById(R.id.tv_priceandstar);
        mPriceAndStarTv.setOnClickListener(this);
        showRv = findViewById(R.id.rv_show);
        showRv.setLayoutManager(new LinearLayoutManager(this));
        showRv.setNestedScrollingEnabled(false);
        showRv.setAdapter(mGuessYouLikeAdapter);
        mSearchTv = findViewById(R.id.tv_search);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
        useStartYear = simpleDateFormat2.format(date);
        useEndYear = getOldWeek(1).substring(0, 4);
        mChooseLocationTv = findViewById(R.id.tv_chooselocation);
        mClearIv = findViewById(R.id.clearIv);
        mClearIv.setVisibility(View.GONE);

        checkPermission();
        //  initLoaction();
//        checkIsGooglePlayConn();

        mHotelStarDialog = new HotelStarDialog();
        mHotelStarDialog.setCallback(this);

        mJcsCalendarDialog = new JcsCalendarDialog();
    }

    @Override
    protected void initData() {
        mModel = new HotelModel();
        mJcsCalendarDialog.initCalendar(this);
        deployDateTv(mStartDateTv, mStartWeekTv, mJcsCalendarDialog.getStartBean());
        deployDateTv(mEndDateTv, mEndWeekTv, mJcsCalendarDialog.getEndBean());
        initGoogleApi();
        showLoading();
        mModel.getYouLike(new BaseObserver<List<HotelResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
            }

            @Override
            public void onSuccess(@NotNull List<HotelResponse> hotelResponses) {
                stopLoading();
                mGuessYouLikeAdapter.getData().clear();
                mGuessYouLikeAdapter.addData(hotelResponses);
            }
        });
    }

    private void initGoogleApi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    protected void bindListener() {
        mChooseLocationTv.setOnClickListener(this::onChooseLocationTvClick);
        mSearchTv.setOnClickListener(this::onSearchTvClick);
        mClearIv.setOnClickListener(this::onClearClicked);
        mJcsCalendarDialog.setOnDateSelectedListener(this::onDateSelected);
        mGuessYouLikeAdapter.setOnItemClickListener(this::onYouLikeItemClicked);
    }

    private void onYouLikeItemClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        JcsCalendarDialog dialog = new JcsCalendarDialog();
        dialog.initCalendar(this);
        HotelDetailActivity.goTo(this, mGuessYouLikeAdapter.getItem(position).getId(), dialog.getStartBean(), dialog.getEndBean(), 1, "", "", 1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel;
    }

    @Override
    public void onClick(View view) {
        if (view == mLocationTv) {
            Intent intent = new Intent(HotelActivity.this, CityPickerActivity.class);
            startActivityForResult(intent, REQ_SELECT_CITY);
        }

        if (view == mChooseDateRl) {
            mJcsCalendarDialog.show(getSupportFragmentManager());
        }

        if (view == mRoomReduceIv) {
            int roomNum = Integer.parseInt(mRoomNumTv.getText().toString());
            if (roomNum == 1) {
                showToast(getString(R.string.can_not_reduce));
                return;
            } else {
                roomNum--;
                mRoomNumTv.setText(String.valueOf(roomNum));
            }
        }

        if (view == mRoomAddIv) {
            int roomNum = Integer.parseInt(mRoomNumTv.getText().toString());
            roomNum++;
            mRoomNumTv.setText(String.valueOf(roomNum));
        }

        if (view == mPriceAndStarTv) {
            mHotelStarDialog.show(getSupportFragmentManager());
        }
    }

    public void onDateSelected(JcsCalendarAdapter.CalendarBean startDate, JcsCalendarAdapter.CalendarBean endDate) {
        if (startDate != null) {
            deployDateTv(mStartDateTv, mStartWeekTv, startDate);
        }

        if (endDate != null) {
            deployDateTv(mEndDateTv, mEndWeekTv, endDate);
        }
        mTotalDayTv.setText(mJcsCalendarDialog.getTotalDay());
        mTotalDay  = mJcsCalendarDialog.getTotalDay2();
    }

    public void deployDateTv(TextView dateTv, TextView weekTv, JcsCalendarAdapter.CalendarBean calendarBean) {
        dateTv.setText(calendarBean.getShowMonthDayDate());
        weekTv.setText(calendarBean.getShowWeekday());
    }


    public void onSearchTvClick(View view) {
        HotelListActivity.goTo(HotelActivity.this, mJcsCalendarDialog.getStartBean(), mJcsCalendarDialog.getEndBean(), mTotalDay, mLocationTv.getText().toString(), cityId, usePrice, useStar, Integer.parseInt(mRoomNumTv.getText().toString()), getIntent().getStringExtra(K_CATEGORY_ID));
    }

    public void onClearClicked(View view) {
        mPriceAndStarTv.setText(getString(R.string.prompt_price_star));
        mPriceAndStarTv.setTextColor(ContextCompat.getColor(HotelActivity.this, R.color.grey_999999));
        usePrice = null;
        useStar = null;
        transmitPrice = null;
        transmitStar = null;
        mHotelStarDialog = new HotelStarDialog();
        mClearIv.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SELECT_CITY) {
            mLocationTv.setText(data.getStringExtra(CityPickerActivity.EXTRA_CITY));
            cityId = data.getStringExtra(CityPickerActivity.EXTRA_CITYID);
        }
    }

    public void onChooseLocationTvClick(View view) {
        checkIsGooglePlayConn();
    }

    private void checkIsGooglePlayConn() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HotelActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATIONCODE);
            ActivityCompat.requestPermissions(HotelActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, READ_CODE);
            return;
        }

        LocationUtil.getInstance().setAddressCallback(new LocationUtil.AddressCallback() {
            @Override
            public void onGetAddress(Address address) {
                String countryName = address.getCountryName();//国家
                String adminArea = address.getAdminArea();//省
                String locality = address.getLocality();//市
                String subLocality = address.getSubLocality();//区
                String featureName = address.getFeatureName();//街道
                mLocationTv.setText(locality);
            }

            @Override
            public void onGetLocation(double lat, double lng) {
                Log.e("HotelActivity", "onGetLocation: " + "lat=" + lat + "----lng=" + lng);
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(HotelActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
                    // 不相等 请求授权
                    ActivityCompat.requestPermissions(HotelActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATIONCODE);
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void selectPriceOrStar(String show) {
        mPriceAndStarTv.setText(show);
        mPriceAndStarTv.setTextColor(ContextCompat.getColor(this, R.color.black_333333));
        mClearIv.setVisibility(View.VISIBLE);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    private static class GuessYouLikeAdapter extends BaseQuickAdapter<HotelResponse, BaseViewHolder> {


        public GuessYouLikeAdapter() {
            super(R.layout.item_hotellist);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelResponse data) {
            RoundedImageView photoIv = baseViewHolder.findView(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                GlideUtil.load(getContext(), data.getImages().get(0), photoIv);
            } else {
                photoIv.setImageResource(R.mipmap.ic_glide_default);
            }
            TextView nameTv = baseViewHolder.findView(R.id.tv_name);
            nameTv.setText(data.getName());
            if (data.getFacebook_link() == null) {
                nameTv.setCompoundDrawables(null, null, null, null);
            }
            TextView tagOneTv = baseViewHolder.findView(R.id.tv_tagone);
            TextView tagTwoTv = baseViewHolder.findView(R.id.tv_tagtwo);
            LinearLayout tagLl = baseViewHolder.findView(R.id.ll_tag);
            if (data.getTags().size() == 0) {
                tagLl.setVisibility(View.GONE);
            } else if (data.getTags().size() == 1) {
                tagLl.setVisibility(View.VISIBLE);
                tagOneTv.setText(data.getTags().get(0).getName());
            } else {
                tagLl.setVisibility(View.VISIBLE);
                tagOneTv.setText(data.getTags().get(0).getName());
                tagTwoTv.setText(data.getTags().get(1).getName());
            }
            TextView addressTv = baseViewHolder.findView(R.id.tv_address);
            addressTv.setText(data.getAddress());
            TextView distanceTv = baseViewHolder.findView(R.id.tv_distance);
            distanceTv.setText("<" + data.getDistance() + "Km");
            TextView scoreTv = baseViewHolder.findView(R.id.tv_score);
            scoreTv.setText(data.getGrade() + "");
            TextView commentNumTv = baseViewHolder.findView(R.id.tv_commentnumber);
            String commentNumberText = String.format(getContext().getString(R.string.comment_num_prompt), data.getComment_counts());
            commentNumTv.setText(commentNumberText);
            TextView priceTv = baseViewHolder.findView(R.id.tv_price);
            priceTv.setText(String.format(getContext().getString(R.string.price_above_number), data.getPrice()));
        }
    }
}
