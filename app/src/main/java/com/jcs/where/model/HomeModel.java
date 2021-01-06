package com.jcs.where.model;

import com.google.gson.reflect.TypeToken;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeModel extends BaseModel {

    public void getModules(BaseObserver<List<ModulesResponse>> observer) {
        mRetrofit.getModules().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer);
    }

    public String getCurrentAreaId() {
        return SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
    }

    public CityResponse getCurrentCity(String currentCityId) {
        String citiesJson = CacheUtil.needUpdateBySpKey(SPKey.K_ALL_CITIES);

        if (!citiesJson.isEmpty()){
            List<CityResponse> cityList = JsonUtil.getInstance().fromJsonToList(citiesJson, new TypeToken<List<CityResponse>>() {
            }.getType());
            int size = cityList.size();
            for (int i = 0; i < size; i++) {
                CityResponse cityResponse = cityList.get(i);
                if (cityResponse.getId().equals(currentCityId)) {
                    return cityResponse;
                }
            }
        }
        return null;
    }
}
