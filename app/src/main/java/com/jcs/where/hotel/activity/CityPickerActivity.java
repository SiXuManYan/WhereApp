package com.jcs.where.hotel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.adapter.CityListAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.api.response.CityPickerResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.CustomProgressDialog;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.GoogleMapBean;
import com.jcs.where.bean.LocateState;
import com.jcs.where.hotel.model.CityPickerModel;
import com.jcs.where.utils.PinyinUtils;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.widget.SideLetterBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

/**
 * 城市选择页面
 */
public class CityPickerActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_CITYID = "cityId";
    public static CustomProgressDialog dialog;
    private final int READ_CODE = 10;
    private final int READ_LOCATIONCODE = 11;
    private ListView mListView;
    private SideLetterBar mLetterBar;
    private CityListAdapter mCityAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng lastLatLng, perthLatLng;
    private boolean mAddressRequested;
    private CityPickerModel mModel;

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    public static void goTo(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CityPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, requestCode);
    }


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
        mGoogleApiClient.connect();
        initView();
        initData();
        checkIsGooglePlayConn();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.cp_activity_city_list;
    }

    @Override
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
        mModel.getCityPickers(new BaseObserver<CityPickerResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull CityPickerResponse response) {
                stopLoading();
                HashSet<CityResponse> cityResponses = new HashSet<>();

                List<CityPickerResponse.Lists> lists = response.getLists();
                int listSize = lists.size();
                for (int i = 0; i < listSize; i++) {
                    List<CityPickerResponse.Lists.Areas> areas = lists.get(i).getAreas();
                    int areaSize = areas.size();
                    for (int j = 0; j < areaSize; j++) {
                        CityPickerResponse.Lists.Areas areasDTO = areas.get(j);

                        String name = areasDTO.getName().replace("　", "");
                        cityResponses.add(new CityResponse(areasDTO.getId(), name, PinyinUtils.getPinYin(name), false));
                    }
                }
                //set转换list
                ArrayList<CityResponse> cities = new ArrayList<>(cityResponses);
                //按照字母排序
                Collections.sort(cities, (cityResponse, t1) -> cityResponse.getPinyin().compareTo(t1.getPinyin()));
                mCityAdapter.setData(cities);

            }
        });
    }

    protected void initData() {
        mModel = new CityPickerModel();
        getCityData();
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name, String id) {//选择城市
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CITY, name);
                intent.putExtra(EXTRA_CITYID, id);
                setResult(RESULT_OK, intent);
                SPUtil.getInstance().saveString(SPKey.K_CURRENT_AREA_ID, id);
                finish();
            }

            @Override
            public void onLocateClick() {//点击定位按钮
                mCityAdapter.updateLocateState(LocateState.LOCATING, null, null);
                checkIsGooglePlayConn();//重新定位
            }
        });
    }

    @Override
    protected void bindListener() {

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

    private void checkIsGooglePlayConn() {
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
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
//                showToast(e.getMessage());
            }
        });

    }


    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }
}
