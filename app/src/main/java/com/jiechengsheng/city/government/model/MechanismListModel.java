package com.jiechengsheng.city.government.model;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.gms.maps.model.LatLng;
import com.jiechengsheng.city.api.BaseModel;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.response.CategoryResponse;
import com.jiechengsheng.city.api.response.MechanismResponse;
import com.jiechengsheng.city.api.response.PageResponse;
import com.jiechengsheng.city.utils.CacheUtil;
import com.jiechengsheng.city.utils.SPKey;

import java.util.List;

/**
 * create by zyf on 2020/12/28 10:06 PM
 */
public class MechanismListModel extends BaseModel {
    /**
     * 获得综合服务、政府机构列表数据
     */
    public void getMechanismList(int page, String categoryId, Integer recommend, BaseObserver<PageResponse<MechanismResponse>> observer) {

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

        dealResponse(mRetrofit.getMechanismListById3(page, categoryId, "", lat, lng, areaId, recommend), observer);
    }

    /**
     * 获得页面TabLayout展示的三级分类
     */
    public void getChildCategories(int level, String categoryId, BaseObserver<List<CategoryResponse>> observer) {
        // level 3，表示3级分类
        dealResponse(mRetrofit.getCategories(level, String.valueOf(categoryId)), observer);
    }
}
