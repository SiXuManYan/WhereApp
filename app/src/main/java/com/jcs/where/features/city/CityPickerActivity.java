package com.jcs.where.features.city;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CityPickerResponse;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.LocationUtil;
import com.jcs.where.utils.PermissionUtils;
import com.jcs.where.utils.PinyinUtils;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.widget.SideLetterBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 城市选择页面
 */
public class CityPickerActivity extends BaseMvpActivity<CityPickerPresenter> implements CityPickerView {

    private ListView listview_all_city;
    private SideLetterBar mLetterBar;
    private CityListAdapter mCityAdapter;
    private CityPickerModel mModel;
    private TextView location_tv;
    private TextView get_location_tv;
    private String currentAreaName;
    private double currentLat;
    private double currentLng;
    private boolean hindCurrentLocation;

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
    protected int getLayoutId() {
        return R.layout.cp_activity_city_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
    }

    @Override
    protected void initView() {
        hindCurrentLocation = getIntent().getBooleanExtra(Constant.PARAM_HIDE_CURRENT_LOCATION, false);


        location_tv = findViewById(R.id.location_tv);
        get_location_tv = findViewById(R.id.get_location_tv);
        listview_all_city = findViewById(R.id.listview_all_city);
        TextView overlay = findViewById(R.id.tv_letter_overlay);
        mLetterBar = findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(letter -> {
            int position = mCityAdapter.getLetterPosition(letter);
            listview_all_city.setSelection(position);
        });
        mCityAdapter = new CityListAdapter(this);
        listview_all_city.setAdapter(mCityAdapter);
        initCity();
    }

    private void initCity() {
        if (hindCurrentLocation) {
            findViewById(R.id.current_location_rl).setVisibility(View.GONE);
            findViewById(R.id.current_location_tv).setVisibility(View.GONE);
            return;
        }

        PermissionUtils.permissionAny(this, granted -> {
                    if (granted) {
                        getCurrentCity();
                    }
                },
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );

    }

    private void getCurrentCity() {


        LocationUtil.getInstance().setAddressCallback(new LocationUtil.AddressCallback() {
            @Override
            public void onGetAddress(Address address) {
                String countryName = address.getCountryName();//国家
                String adminArea = address.getAdminArea();//省
                //市
                currentAreaName = address.getLocality();
                String subLocality = address.getSubLocality();//区
                String featureName = address.getFeatureName();//街道
                location_tv.setText(currentAreaName);
            }

            @Override
            public void onGetLocation(double lat, double lng) {
                currentLat = lat;
                currentLng = lng;
            }
        });

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

                List<CityPickerResponse.CityChild> lists = response.lists;
                int listSize = lists.size();

                for (int i = 0; i < listSize; i++) {

                    CityPickerResponse.CityChild child = lists.get(i);

                    String name = child.name.replace("　", "");
                    CityResponse city = new CityResponse();
                    city.id = child.id;
                    city.name = name;
                    city.lat = child.lat;
                    city.lng = child.lng;
                    city.pinyin = PinyinUtils.getPinYin(name);
                    cityResponses.add(city);

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
        //选择城市
        mCityAdapter.setOnCityClickListener(this::selectCity);
    }

    private void selectCity(String name, String id, double lat, double lng) {

        // old
        SPUtil.getInstance().saveString(SPKey.SELECT_AREA_ID, id);

        // new
        SPUtils.getInstance().put(SPKey.SELECT_AREA_ID, id);
        SPUtils.getInstance().put(SPKey.SELECT_AREA_NAME, name);
        SPUtils.getInstance().put(SPKey.SELECT_LAT, (float) lat);
        SPUtils.getInstance().put(SPKey.SELECT_LNG, (float) lng);

        Intent intent = new Intent();
        intent.putExtra(Constant.PARAM_SELECT_AREA_NAME, name);
        intent.putExtra(Constant.PARAM_SELECT_AREA_ID, id);
        setResult(RESULT_OK, intent);


        finish();
    }

    @Override
    protected void bindListener() {
        get_location_tv.setOnClickListener(v -> initCity());

        // 存储当前位置
        location_tv.setOnClickListener(v -> {
            if (currentLat != 0) {
                selectCity(currentAreaName, "0", currentLat, currentLng);
            }
        });
    }


}
