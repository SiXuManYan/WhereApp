package com.jcs.where.search.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.utils.Constant;

import java.util.List;

/**
 * create by zyf on 2021/1/31 1:26 下午
 */
public class SearchModel extends BaseModel {
    public void getHotelListByInput(String areaId, String input, BaseObserver<PageResponse<HotelResponse>> observer) {
        getHotelListByInput(areaId, input, Constant.LAT, Constant.LNG, 1, observer);
    }

    public void getHotelListByInput(String areaId, String input,
                                    double lat, double lng, int page, BaseObserver<PageResponse<HotelResponse>> observer) {
        dealResponse(mRetrofit.getHotelListByInputAtSearch(areaId, input, lat, lng, page), observer);
    }

    public void getNewsListByInput(String input, BaseObserver<PageResponse<NewsResponse>> observer) {
        dealResponse(mRetrofit.getNewsListByInputAtSearch(input), observer);
    }

    public void getHotHotelList(BaseObserver<List<String>> observer) {
        dealResponse(mRetrofit.getHotHotelListAtSearch(), observer);
    }

    public void getHotNewsList(BaseObserver<List<String>> observer) {
        dealResponse(mRetrofit.getHotNewsListAtSearch(), observer);
    }
}