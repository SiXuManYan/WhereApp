package com.jcs.where.government.model;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.gms.maps.model.LatLng;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.SPKey;

import java.util.List;

/**
 * create by zyf on 2020/12/28 10:06 PM
 */
public class MechanismListModel extends BaseModel {
    /**
     * 获得综合服务、政府机构列表数据
     */
    public void getMechanismList(int page ,String categoryId, BaseObserver<PageResponse<MechanismResponse>> observer) {
        LatLng latLng = CacheUtil.getSafeSelectLatLng();
        String areaId = SPUtils.getInstance().getString(SPKey.SELECT_AREA_ID, "0");
        dealResponse(mRetrofit.getMechanismListById3(page ,categoryId, "", latLng.latitude, latLng.longitude,areaId), observer);
    }

    /**
     * 获得页面TabLayout展示的三级分类
     */
    public void getChildCategories(int level, String categoryId, BaseObserver<List<CategoryResponse>> observer) {
        // level 3，表示3级分类
        dealResponse(mRetrofit.getCategories(level, String.valueOf(categoryId)), observer);
    }
}
