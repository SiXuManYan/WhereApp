package com.jiechengsheng.city.features.city;

import com.jiechengsheng.city.api.BaseModel;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.response.CityPickerResponse;

/**
 * create by zyf on 2021/1/12 10:18 下午
 */
public class CityPickerModel extends BaseModel {

    public void getCityPickers(BaseObserver<CityPickerResponse> observer) {
        dealResponse(mRetrofit.getCityPickers("list"), observer);
    }

}
