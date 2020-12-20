package com.jcs.where.home.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.GoogleMapBean;
import com.jcs.where.bean.GuessYouLikeHotelBean;
import com.jcs.where.home.dialog.HotelCalendarDialog;
import com.jcs.where.home.dialog.HotelStarDialog;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.hotel.HotelDetailActivity;
import com.jcs.where.hotel.HotelListActivity;
import com.jcs.where.manager.TokenManager;
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
    private TextView locationTv, startDateTv, startWeekTv, endDateTv, endWeekTv, allDayTv, roomNumTv, priceAndStarTv;
    private RelativeLayout chooseDateRl;
    private ImageView reduceIv, addIv;
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
    private ImageView clearIv;
    private String transmitPrice, transmitStar;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng lastLatLng, perthLatLng;
    private boolean mAddressRequested;

    private HotelStarDialog mHotelStarDialog;
    private HotelCalendarDialog mHotelCalendarDialog;


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
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        guessYouLikeAdapter = new GuessYouLikeAdapter(R.layout.item_hotellist);

        locationTv = findViewById(R.id.tv_location);
        locationTv.setOnClickListener(this);
        chooseDateRl = findViewById(R.id.rl_choosedate);
        chooseDateRl.setOnClickListener(this);
        startDateTv = findViewById(R.id.startDayTv);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        startDateTv.setText(simpleDateFormat.format(date));
        startWeekTv = findViewById(R.id.tv_startweek);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date(System.currentTimeMillis());
//        startWeekTv.setText("周" + CalendarUtil.getWeekByFormat(simpleDateFormat1.format(date1)));
        endDateTv = findViewById(R.id.endDayTv);
        endDateTv.setText(getOldDate(1));
        endWeekTv = findViewById(R.id.tv_endweek);
//        endWeekTv.setText("周" + CalendarUtil.getWeekByFormat(getOldWeek(1)));
        allDayTv = findViewById(R.id.tv_allday);
        roomNumTv = findViewById(R.id.tv_roomnum);
        reduceIv = findViewById(R.id.iv_roomreduce);
        reduceIv.setOnClickListener(this);
        addIv = findViewById(R.id.iv_roomadd);
        addIv.setOnClickListener(this);
        priceAndStarTv = findViewById(R.id.tv_priceandstar);
        priceAndStarTv.setOnClickListener(this);
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
        findViewById(R.id.tv_search).setOnClickListener(this);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
        useStartYear = simpleDateFormat2.format(date);
        useEndYear = getOldWeek(1).substring(0, 4);
        findViewById(R.id.tv_chooselocation).setOnClickListener(this);
        clearIv = findViewById(R.id.clearIv);
        clearIv.setVisibility(View.GONE);
        clearIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceAndStarTv.setText("价格/星级");
                priceAndStarTv.setTextColor(ContextCompat.getColor(HotelActivity.this, R.color.grey_999999));
                usePrice = null;
                useStar = null;
                transmitPrice = null;
                transmitStar = null;
                mHotelStarDialog = new HotelStarDialog();
                clearIv.setVisibility(View.GONE);
            }
        });
        initData();
        checkPermission();
        //  initLoaction();
        checkIsGooglePlayConn();

        mHotelStarDialog = new HotelStarDialog();
        mHotelStarDialog.setCallback(this);

        mHotelCalendarDialog = new HotelCalendarDialog();
    }

    @Override
    protected void initData() {
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

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                Intent intent = new Intent(HotelActivity.this, CityPickerActivity.class);
                startActivityForResult(intent, REQ_SELECT_CITY);
                break;
            case R.id.rl_choosedate:
                mHotelCalendarDialog.show(getSupportFragmentManager());
                break;
            case R.id.iv_roomreduce:
                int roomNum = Integer.valueOf(roomNumTv.getText().toString());
                if (roomNum == 1) {
                    showToast("不能再减了");
                    return;
                } else {
                    roomNum--;
                    roomNumTv.setText(roomNum + "");
                }
                break;
            case R.id.iv_roomadd:
                int roomNum1 = Integer.valueOf(roomNumTv.getText().toString());
                roomNum1++;
                roomNumTv.setText(roomNum1 + "");
                break;
            case R.id.tv_priceandstar:
                mHotelStarDialog.show(getSupportFragmentManager());
                break;
            case R.id.tv_search:
                HotelListActivity.goTo(HotelActivity.this, startDateTv.getText().toString(), endDateTv.getText().toString(), startWeekTv.getText().toString(), endWeekTv.getText().toString(), allDayTv.getText().toString(), locationTv.getText().toString(), cityId, usePrice, useStar, useStartYear, useEndYear, roomNumTv.getText().toString(), getIntent().getStringExtra("categoryId"));
                break;
            case R.id.tv_chooselocation:
                //   initLoaction();
                checkIsGooglePlayConn();
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SELECT_CITY) {
            locationTv.setText(data.getStringExtra(CityPickerActivity.EXTRA_CITY));
            cityId = data.getStringExtra(CityPickerActivity.EXTRA_CITYID);
        }
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "No geocoder available", Toast.LENGTH_LONG).show();
                return;
            }
            initArea(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
        }
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
        HttpUtils.doGoogleMapReqeust("GET", "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=AIzaSyDjaCnD0cWNtAOPiS_Kbb5FRZ4k4qyhayk", null, new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    GoogleMapBean googleMapBean = new Gson().fromJson(result, GoogleMapBean.class);
                    if (googleMapBean.getResults().get(0) != null) {
                        if (googleMapBean.getResults().get(0).getAddress_components().get(2) != null) {
                            String usecity = googleMapBean.getResults().get(0).getAddress_components().get(2).getLong_name();
                            if (usecity != null) {
                                if (usecity.contains("City of ")) {
                                    usecity = usecity.substring(8);
                                }
                                locationTv.setText(usecity);
                                cityId = "0";
                            }
                        }
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

    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void selectPriceOrStar(String show) {
        priceAndStarTv.setText(show);
        priceAndStarTv.setTextColor(ContextCompat.getColor(this, R.color.black_333333));
        clearIv.setVisibility(View.VISIBLE);
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
                    HotelDetailActivity.goTo(getContext(), data.getId(), startDateTv.getText().toString(), endDateTv.getText().toString(), startWeekTv.getText().toString(), endWeekTv.getText().toString(), allDayTv.getText().toString(), useStartYear, useEndYear, roomNumTv.getText().toString());
                }
            });
        }
    }
}
