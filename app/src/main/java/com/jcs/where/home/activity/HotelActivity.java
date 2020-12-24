package com.jcs.where.home.activity;

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

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.adapter.HotelCalendarAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.GuessYouLikeHotelBean;
import com.jcs.where.home.dialog.HotelCalendarDialog;
import com.jcs.where.home.dialog.HotelStarDialog;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.hotel.HotelDetailActivity;
import com.jcs.where.hotel.HotelListActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.model.HotelModel;
import com.jcs.where.utils.LocationUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final int READ_CODE = 10;
    private final int READ_LOCATIONCODE = 11;
    private TextView mLocationTv, mStartDateTv, mStartWeekTv, mEndDateTv, mEndWeekTv, allDayTv, mRoomNumTv, mPriceAndStarTv;
    private RelativeLayout mChooseDateRl;
    private ImageView mRoomReduceIv, mRoomAddIv;
    private RecyclerView showRv;
    private GuessYouLikeAdapter guessYouLikeAdapter;
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
    private HotelCalendarDialog mHotelCalendarDialog;
    private TextView mChooseLocationTv;
    private TextView mSearchTv;
    private HotelCalendarAdapter.HotelCalendarBean mStartDate, mEndDate;


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
        guessYouLikeAdapter = new GuessYouLikeAdapter(R.layout.item_hotellist);
        mLocationTv = findViewById(R.id.locationTv);
        mLocationTv.setOnClickListener(this);
        mChooseDateRl = findViewById(R.id.rl_choosedate);
        mChooseDateRl.setOnClickListener(this);
        mStartDateTv = findViewById(R.id.startDayTv);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        mStartDateTv.setText(simpleDateFormat.format(date));
        mStartWeekTv = findViewById(R.id.startWeekTv);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date(System.currentTimeMillis());
//        startWeekTv.setText("周" + CalendarUtil.getWeekByFormat(simpleDateFormat1.format(date1)));
        mEndDateTv = findViewById(R.id.endDayTv);
//        mEndDateTv.setText(getOldDate(1));
        mEndWeekTv = findViewById(R.id.endWeekTv);
//        endWeekTv.setText("周" + CalendarUtil.getWeekByFormat(getOldWeek(1)));
        allDayTv = findViewById(R.id.tv_allday);
        mRoomNumTv = findViewById(R.id.tv_roomnum);
        mRoomReduceIv = findViewById(R.id.iv_roomreduce);
        mRoomReduceIv.setOnClickListener(this);
        mRoomAddIv = findViewById(R.id.iv_roomadd);
        mRoomAddIv.setOnClickListener(this);
        mPriceAndStarTv = findViewById(R.id.tv_priceandstar);
        mPriceAndStarTv.setOnClickListener(this);
        showRv = findViewById(R.id.rv_show);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelActivity.this,
//                LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        showRv.setLayoutManager(linearLayoutManager);
        showRv.setNestedScrollingEnabled(false);
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

        mHotelCalendarDialog = new HotelCalendarDialog();
    }

    @Override
    protected void initData() {
        mModel = new HotelModel();
        initGoogleApi();
        showLoading();
        Map<String, String> params = new HashMap<>();
        params.put("lat", "");
        params.put("lng", "");
        params.put("area_id", "1");
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotels/recommends", null, "", TokenManager.get().getToken(HotelActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<GuessYouLikeHotelBean>>() {
                    }.getType();
                    List<GuessYouLikeHotelBean> list = gson.fromJson(result, type);
                    guessYouLikeAdapter.addData(list);
                    showRv.setAdapter(guessYouLikeAdapter);
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
        mHotelCalendarDialog.setOnDateSelectedListener(this::onDateSelected);
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
            mHotelCalendarDialog.show(getSupportFragmentManager());
        }

        if (view == mRoomReduceIv) {
            int roomNum = Integer.parseInt(mRoomNumTv.getText().toString());
            if (roomNum == 1) {
                showToast("不能再减了");
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

    public void onDateSelected(HotelCalendarAdapter.HotelCalendarBean startDate, HotelCalendarAdapter.HotelCalendarBean endDate) {
        if (startDate != null) {
            mStartDate = startDate;
            mStartDateTv.setText(startDate.getShowMonthDayDate());
            mStartWeekTv.setText(startDate.getShowWeekday());
        }

        if (endDate != null) {
            mEndDate = endDate;
            mEndDateTv.setText(endDate.getShowMonthDayDate());
            mEndWeekTv.setText(endDate.getShowWeekday());
        }
    }


    public void onSearchTvClick(View view) {
        HotelListActivity.goTo(HotelActivity.this, mStartDateTv.getText().toString(), mEndDateTv.getText().toString(), mStartWeekTv.getText().toString(), mEndWeekTv.getText().toString(), allDayTv.getText().toString(), mLocationTv.getText().toString(), cityId, usePrice, useStar, useStartYear, useEndYear, mRoomNumTv.getText().toString(), getIntent().getStringExtra("categoryId"));
    }

    public void onClearClicked(View view) {
        mPriceAndStarTv.setText("价格/星级");
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
//        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
//            initArea(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
//        }
//        mAddressRequested = true;
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
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        Log.e("HotelActivity", "checkIsGooglePlayConn: " + "mLastLocation=" + mLastLocation);
//        if (mLastLocation != null) {
//            lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            if (!Geocoder.isPresent()) {
//                Toast.makeText(this, "No geocoder available", Toast.LENGTH_LONG).show();
//                return;
//            }
//            initArea(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
//        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(HotelActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATIONCODE);
//            ActivityCompat.requestPermissions(HotelActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, READ_CODE);
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            if (!Geocoder.isPresent()) {
//                Toast.makeText(this, "No geocoder available", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (mAddressRequested) {
//                initArea(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
//            }
//        }
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

    private void initArea(String lat, String lng) {
        showLoading();
        Log.e("HotelActivity", "initArea: " + "-------lat=" + lat + "----lng=" + lng);
        mModel.getLocation(lat, lng, new BaseObserver<String>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull String s) {
                stopLoading();
                Log.e("HotelActivity", "onNext: " + "s=" + s);
            }
        });
//        HttpUtils.doGoogleMapReqeust("GET", "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=AIzaSyDjaCnD0cWNtAOPiS_Kbb5FRZ4k4qyhayk", null, new HttpUtils.StringCallback() {
//            @Override
//            public void onSuccess(int code, String result) {
//                stopLoading();
//                if (code == 200) {
//                    GoogleMapBean googleMapBean = new Gson().fromJson(result, GoogleMapBean.class);
//                    if (googleMapBean.getResults().get(0) != null) {
//                        if (googleMapBean.getResults().get(0).getAddress_components().get(2) != null) {
//                            String usecity = googleMapBean.getResults().get(0).getAddress_components().get(2).getLong_name();
//                            if (usecity != null) {
//                                if (usecity.contains("City of ")) {
//                                    usecity = usecity.substring(8);
//                                }
//                                locationTv.setText(usecity);
//                                cityId = "0";
//                            }
//                        }
//                    }
//                } else {
//                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
//                    showToast(errorBean.message);
//                }
//            }
//
//            @Override
//            public void onFaileure(int code, Exception e) {
//                stopLoading();
//                showToast(e.getMessage());
//            }
//        });

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

    private class GuessYouLikeAdapter extends BaseQuickAdapter<GuessYouLikeHotelBean, BaseViewHolder> {


        public GuessYouLikeAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, GuessYouLikeHotelBean data) {
            RoundedImageView photoIv = baseViewHolder.findView(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                Glide.with(HotelActivity.this).load(data.getImages().get(0)).into(photoIv);
            } else {
                photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
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
            commentNumTv.setText(data.getComment_counts() + "条评论");
            TextView priceTv = baseViewHolder.findView(R.id.tv_price);
            priceTv.setText("₱" + data.getPrice() + "起");
            baseViewHolder.findView(R.id.ll_hotel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HotelDetailActivity.goTo(getContext(), data.getId(), mStartDateTv.getText().toString(), mEndDateTv.getText().toString(), mStartWeekTv.getText().toString(), mEndWeekTv.getText().toString(), allDayTv.getText().toString(), useStartYear, useEndYear, mRoomNumTv.getText().toString());
                }
            });
        }
    }
}
