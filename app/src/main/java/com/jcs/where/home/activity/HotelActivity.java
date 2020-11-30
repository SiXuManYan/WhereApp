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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.GoogleMapBean;
import com.jcs.where.bean.GuessYouLikeHotelBean;
import com.jcs.where.home.dialog.HotelStarDialog;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.hotel.HotelDetailActivity;
import com.jcs.where.hotel.HotelListActivity;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.popupwindow.ChoosePricePop;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;

public class HotelActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        initView();
        guessYouLikeAdapter = new GuessYouLikeAdapter(HotelActivity.this);
    }

    private void initView() {
        locationTv = V.f(this, R.id.tv_location);
        locationTv.setOnClickListener(this);
        chooseDateRl = V.f(this, R.id.rl_choosedate);
        chooseDateRl.setOnClickListener(this);
        startDateTv = V.f(this, R.id.tv_startday);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        startDateTv.setText(simpleDateFormat.format(date));
        startWeekTv = V.f(this, R.id.tv_startweek);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date(System.currentTimeMillis());
//        startWeekTv.setText("周" + CalendarUtil.getWeekByFormat(simpleDateFormat1.format(date1)));
        endDateTv = V.f(this, R.id.tv_endday);
        endDateTv.setText(getOldDate(1));
        endWeekTv = V.f(this, R.id.tv_endweek);
//        endWeekTv.setText("周" + CalendarUtil.getWeekByFormat(getOldWeek(1)));
        allDayTv = V.f(this, R.id.tv_allday);
        roomNumTv = V.f(this, R.id.tv_roomnum);
        reduceIv = V.f(this, R.id.iv_roomreduce);
        reduceIv.setOnClickListener(this);
        addIv = V.f(this, R.id.iv_roomadd);
        addIv.setOnClickListener(this);
        priceAndStarTv = V.f(this, R.id.tv_priceandstar);
        priceAndStarTv.setOnClickListener(this);
        showRv = V.f(this, R.id.rv_show);
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
        V.f(this, R.id.tv_search).setOnClickListener(this);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
        useStartYear = simpleDateFormat2.format(date);
        useEndYear = getOldWeek(1).substring(0, 4);
        V.f(this, R.id.tv_chooselocation).setOnClickListener(this);
        clearIv = V.f(this, R.id.iv_clear);
        clearIv.setVisibility(View.GONE);
        clearIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceAndStarTv.setText("价格/星级");
                priceAndStarTv.setTextColor(getResources().getColor(R.color.grey_999999));
                usePrice = null;
                useStar = null;
                transmitPrice = null;
                transmitStar = null;
                clearIv.setVisibility(View.GONE);
            }
        });
        initData();
        checkPermission();
        //  initLoaction();
        checkIsGooglePlayConn();

        mHotelStarDialog = new HotelStarDialog();
    }

    private void initData() {
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
                    guessYouLikeAdapter.setData(list);
                    showRv.setAdapter(guessYouLikeAdapter);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelActivity.this, e.getMessage());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel;
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                Intent intent = new Intent(HotelActivity.this, CityPickerActivity.class);
                startActivityForResult(intent, REQ_SELECT_CITY);
                break;
            case R.id.rl_choosedate:
                break;
            case R.id.iv_roomreduce:
                int roomNum = Integer.valueOf(roomNumTv.getText().toString());
                if (roomNum == 1) {
                    ToastUtils.showLong(HotelActivity.this, "不能再减了");
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
//                new ChoosePricePop.Builder(HotelActivity.this, view, transmitPrice, transmitStar)
//                        .setPriceOnClickListener(new ChoosePricePop.PriceOnClickListener() {
//                            @Override
//                            public void getDate(String price, String star) {
//                                if (price == null && star == null) {
//                                    clearIv.setVisibility(View.GONE);
//                                    priceAndStarTv.setText("价格/星级");
//                                    priceAndStarTv.setTextColor(getResources().getColor(R.color.grey_999999));
//                                    transmitPrice = null;
//                                    transmitStar = null;
//                                } else if (price == null) {
//                                    priceAndStarTv.setText(star);
//                                    if (star.equals("二星及以下")) {
//                                        useStar = "[1,2]";
//                                    } else if (star.equals("三星")) {
//                                        useStar = "[3]";
//                                    } else if (star.equals("四星")) {
//                                        useStar = "[4]";
//                                    } else if (star.equals("五星")) {
//                                        useStar = "[5]";
//                                    }
//                                    transmitStar = star;
//                                    transmitPrice = null;
//                                    usePrice = null;
//                                    clearIv.setVisibility(View.VISIBLE);
//                                } else if (star == null) {
//                                    priceAndStarTv.setText(price);
//                                    if (price.equals("₱ 100以下")) {
//
//                                        usePrice = "[0,100]";
//                                    } else if (price.equals("₱ 100-200")) {
//                                        usePrice = "[100,200]";
//                                    } else if (price.equals("₱ 200-300")) {
//                                        usePrice = "[200,300]";
//                                    } else if (price.equals("₱ 300-400")) {
//                                        usePrice = "[300,400]";
//                                    } else if (price.equals("₱ 400-500")) {
//                                        usePrice = "[400,500]";
//                                    } else if (price.equals("₱ 500-700")) {
//                                        usePrice = "[500,700]";
//                                    } else if (price.equals("₱ 700-900")) {
//                                        usePrice = "[700,900]";
//                                    } else if (price.equals("₱ 900以上")) {
//                                        usePrice = "[900,100000]";
//                                    }
//                                    transmitStar = null;
//                                    transmitPrice = price;
//                                    useStar = null;
//                                    clearIv.setVisibility(View.VISIBLE);
//                                } else {
//                                    priceAndStarTv.setText(price + "，" + star);
//                                    if (price.equals("₱ 100以下")) {
//                                        usePrice = "[0,100]";
//                                    } else if (price.equals("₱ 100-200")) {
//                                        usePrice = "[100,200]";
//                                    } else if (price.equals("₱ 200-300")) {
//                                        usePrice = "[200,300]";
//                                    } else if (price.equals("₱ 300-400")) {
//                                        usePrice = "[300,400]";
//                                    } else if (price.equals("₱ 400-500")) {
//                                        usePrice = "[400,500]";
//                                    } else if (price.equals("₱ 500-700")) {
//                                        usePrice = "[500,700]";
//                                    } else if (price.equals("₱ 700-900")) {
//                                        usePrice = "[700,900]";
//                                    } else if (price.equals("₱ 900以上")) {
//                                        usePrice = "[900,100000]";
//                                    }
//                                    if (star.equals("二星及以下")) {
//                                        useStar = "[1,2]";
//                                    } else if (star.equals("三星")) {
//                                        useStar = "[3]";
//                                    } else if (star.equals("四星")) {
//                                        useStar = "[4]";
//                                    } else if (star.equals("五星")) {
//                                        useStar = "[5]";
//                                    }
//                                    transmitStar = star;
//                                    transmitPrice = price;
//                                    clearIv.setVisibility(View.VISIBLE);
//                                }
//
//                                priceAndStarTv.setTextColor(getResources().getColor(R.color.black_333333));
//
//
//                            }
//                        }).builder();
                break;
            case R.id.tv_search:
                HotelListActivity.goTo(HotelActivity.this, startDateTv.getText().toString(), endDateTv.getText().toString(), startWeekTv.getText().toString(), endWeekTv.getText().toString(), allDayTv.getText().toString(), locationTv.getText().toString(), cityId, usePrice, useStar, useStartYear, useEndYear, roomNumTv.getText().toString());
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
                    ToastUtils.showLong(HotelActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelActivity.this, e.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    private class GuessYouLikeAdapter extends BaseQuickAdapter<GuessYouLikeHotelBean> {

        public GuessYouLikeAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_hotellist;
        }

        @Override
        protected void initViews(QuickHolder holder, GuessYouLikeHotelBean data, int position) {
            RoundedImageView photoIv = holder.findViewById(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                Picasso.with(HotelActivity.this).load(data.getImages().get(0)).into(photoIv);
            } else {
                photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(data.getName());
            if (data.getFacebook_link() == null) {
                nameTv.setCompoundDrawables(null, null, null, null);
            }
            TextView tagOneTv = holder.findViewById(R.id.tv_tagone);
            TextView tagTwoTv = holder.findViewById(R.id.tv_tagtwo);
            LinearLayout tagLl = holder.findViewById(R.id.ll_tag);
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
            TextView addressTv = holder.findViewById(R.id.tv_address);
            addressTv.setText(data.getAddress());
            TextView distanceTv = holder.findViewById(R.id.tv_distance);
            distanceTv.setText("<" + data.getDistance() + "Km");
            TextView scoreTv = holder.findViewById(R.id.tv_score);
            scoreTv.setText(data.getGrade() + "");
            TextView commentNumTv = holder.findViewById(R.id.tv_commentnumber);
            commentNumTv.setText(data.getComment_counts() + "条评论");
            TextView priceTv = holder.findViewById(R.id.tv_price);
            priceTv.setText("₱" + data.getPrice() + "起");
            holder.findViewById(R.id.ll_hotel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HotelDetailActivity.goTo(mContext, data.getId(), startDateTv.getText().toString(), endDateTv.getText().toString(), startWeekTv.getText().toString(), endWeekTv.getText().toString(), allDayTv.getText().toString(), useStartYear, useEndYear, roomNumTv.getText().toString());
                }
            });
        }
    }
}
