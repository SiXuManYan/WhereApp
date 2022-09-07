package com.jcs.where.search.model;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.gms.maps.model.LatLng;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.SPKey;

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
        dealResponse(mRetrofit.getHotelListByInputAtSearch(areaId, input, lat, lng, page, ""), observer);
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

    public void getHotConvenienceServiceListAtSearch(BaseObserver<List<String>> observer) {
        dealResponse(mRetrofit.getHotConvenienceServiceListAtSearch(), observer);
    }

    /**
     * @param categoryId 分类id集合
     * @param search     查询字段
     */
    public void getMechanismList(String categoryId,
                                 String search, BaseObserver<PageResponse<MechanismResponse>> observer) {


        LatLng latLng = CacheUtil.getSafeSelectLatLng();
        String areaId = SPUtils.getInstance().getString(SPKey.SELECT_AREA_ID, "");

        Double lat = latLng.latitude;
        Double lng = latLng.longitude;

        if (TextUtils.isEmpty(areaId)) {
            areaId = null;
        } else {
//            lat = null;
//            lng = null;
        }
        dealResponse(mRetrofit.getMechanismListById3(1, categoryId, search, lat, lng, areaId,null), observer);

    }
}
