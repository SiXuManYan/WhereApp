package com.jcs.where.features.city;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CityPickerResponse;

/**
 * create by zyf on 2021/1/12 10:18 下午
 */
public class CityPickerModel extends BaseModel {

    public void getCityPickers(BaseObserver<CityPickerResponse> observer) {
        dealResponse(mRetrofit.getCityPickers("list"), observer);
    }

}
