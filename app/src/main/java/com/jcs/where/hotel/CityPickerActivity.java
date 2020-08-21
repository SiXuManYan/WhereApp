package com.jcs.where.hotel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.adapter.CityListAdapter;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.AreaBean;
import com.jcs.where.bean.City;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.GoogleMapBean;
import com.jcs.where.bean.LocateState;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.PinyinUtils;
import com.jcs.where.widget.SideLetterBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.dialog.CustomProgressDialog;
import co.tton.android.base.view.ToastUtils;

public class CityPickerActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_CITYID = "cityId";

    private ListView mListView;
    private SideLetterBar mLetterBar;
    private CityListAdapter mCityAdapter;
    public static CustomProgressDialog dialog;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng lastLatLng, perthLatLng;
    private boolean mAddressRequested;
    private final int READ_CODE = 10;
    private final int READ_LOCATIONCODE = 11;

    public static void goTo(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CityPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, requestCode);
    }


//    public static void goTo(Context context) {
//        Intent intent = new Intent(context, CityPickerActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        if (!(context instanceof Activity)) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        context.startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        setStatusBar();
        initView();
        initData();
        checkIsGooglePlayConn();
    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.cp_activity_city_list);
//        setStatusBar();
//        initView();
//        initData();
//        // getLocation();
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.cp_activity_city_list;
    }

    protected void initView() {
        mListView = findViewById(R.id.listview_all_city);
        TextView overlay = findViewById(R.id.tv_letter_overlay);
        mLetterBar = findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });
        mCityAdapter = new CityListAdapter(this);
        mListView.setAdapter(mCityAdapter);
    }

    public void getCityData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "commonapi/v1/areas", null, "", TokenManager.get().getToken(CityPickerActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    AreaBean bean = new Gson().fromJson(result, AreaBean.class);
                    HashSet<City> citys = new HashSet<>();
                    for (int i = 0; i < bean.lists.size(); i++) {
                        for (int j = 0; j < bean.lists.get(i).areas.size(); j++) {
                            String name = bean.lists.get(i).areas.get(j).name.replace("　", "");
                            citys.add(new City(bean.lists.get(i).areas.get(j).id, name, PinyinUtils.getPinYin(name), false));
                        }
                    }
                    //set转换list
                    ArrayList<City> cities = new ArrayList<>(citys);
                    //按照字母排序
                    Collections.sort(cities, new Comparator<City>() {
                        @Override
                        public int compare(City city, City t1) {
                            return city.getPinyin().compareTo(t1.getPinyin());
                        }
                    });
                    mCityAdapter.setData(cities);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(CityPickerActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(CityPickerActivity.this, e.getMessage());
            }
        });

    }

    protected void initData() {
        getCityData();
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name, String id) {//选择城市
                // Toast.makeText(CityPickerActivity.this, name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CITY, name);
                intent.putExtra(EXTRA_CITYID, id);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onLocateClick() {//点击定位按钮
                mCityAdapter.updateLocateState(LocateState.LOCATING, null, null);
                checkIsGooglePlayConn();//重新定位
            }
        });
    }

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new CustomProgressDialog(this, "");
//            dialog.setCancelable(isCancelable);
            dialog.show();
        }
    }

    public void stopLoading() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

        } catch (Exception e) {

        }
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    private void checkIsGooglePlayConn() {
        Log.i("MapsActivity", "checkIsGooglePlayConn-->" + mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            initArea(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
        }
        mAddressRequested = true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CityPickerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATIONCODE);
            ActivityCompat.requestPermissions(CityPickerActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, READ_CODE);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "No geocoder available", Toast.LENGTH_LONG).show();
                return;
            }
            if (mAddressRequested) {
                initArea(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                                mCityAdapter.updateLocateState(LocateState.SUCCESS, usecity, "0" + "");
                            }
                        }
                    }
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(CityPickerActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(CityPickerActivity.this, e.getMessage());
            }
        });

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
