package com.jcs.where.hotel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.widget.ListView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.adapter.CityListAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CityPickerResponse;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.hotel.activity.picker.CityPickerPresenter;
import com.jcs.where.hotel.activity.picker.CityPickerView;
import com.jcs.where.hotel.model.CityPickerModel;
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


    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_CITY_ID = "cityId";

    private ListView listview_all_city;
    private SideLetterBar mLetterBar;
    private CityListAdapter mCityAdapter;
    private CityPickerModel mModel;
    private TextView location_tv;
    private TextView get_location_tv;
    private String locality;

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
    protected void initView() {
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
                locality = address.getLocality();
                String subLocality = address.getSubLocality();//区
                String featureName = address.getFeatureName();//街道
                location_tv.setText(locality);
            }

            @Override
            public void onGetLocation(double lat, double lng) {

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
        //选择城市
        mCityAdapter.setOnCityClickListener(this::selectCity);
    }

    private void selectCity(String name, String id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CITY, name);
        intent.putExtra(EXTRA_CITY_ID, id);
        setResult(RESULT_OK, intent);
        SPUtil.getInstance().saveString(SPKey.K_CURRENT_AREA_ID, id);
        finish();
    }

    @Override
    protected void bindListener() {
        get_location_tv.setOnClickListener(v -> initCity());
        location_tv.setOnClickListener(v -> selectCity(locality, "0"));
    }


}
