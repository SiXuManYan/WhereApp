package com.jcs.where.government.model;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.gms.maps.model.LatLng;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.SPKey;

/**
 * create by zyf on 2021/3/11 11:15 下午
 */
public class ConvenienceServiceSearchModel extends BaseModel {

    public void getMechanismList(int page, String categoryId, String searchInput, BaseObserver<PageResponse<MechanismResponse>> observer) {

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
        dealResponse(mRetrofit.getMechanismListById3(page, categoryId, searchInput, lat, lng, areaId,null), observer);
    }
}
