package com.jcs.where.government.model;

import com.google.android.gms.maps.model.LatLng;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;

/**
 * create by zyf on 2021/3/11 11:15 下午
 */
public class ConvenienceServiceSearchModel extends BaseModel {

    public void getMechanismList(int page ,String categoryId, String searchInput ,BaseObserver<PageResponse<MechanismResponse>> observer) {
        LatLng latLng = CacheUtil.getSafeSelectLatLng();
        dealResponse(mRetrofit.getMechanismListById2(page,categoryId, searchInput, latLng.latitude, latLng.longitude), observer);
    }
}
