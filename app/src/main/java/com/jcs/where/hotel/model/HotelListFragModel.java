package com.jcs.where.hotel.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.PageResponse;

import java.util.List;

/**
 * create by zyf on 2021/2/28 10:59 上午
 */
public class HotelListFragModel extends BaseModel {
    public void getHotelListByInput(String areaId, String input,
                                    double lat, double lng, int page, String hotelTypeIds, BaseObserver<PageResponse<HotelResponse>> observer) {
        dealResponse(mRetrofit.getHotelListByInputAtSearch(areaId, input, lat, lng, page, hotelTypeIds), observer);
    }
}
