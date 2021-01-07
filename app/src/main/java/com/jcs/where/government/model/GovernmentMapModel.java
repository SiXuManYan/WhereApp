package com.jcs.where.government.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.SearchResponse;

import java.util.List;

/**
 * create by zyf on 2020/12/28 8:50 PM
 */
public class GovernmentMapModel extends BaseModel {
    /**
     * 获得页面TabLayout展示的二级分类
     */
    public void getCategories(BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(2, String.valueOf(1)), observer);
    }

    /**
     * 获取某区域下的所有政府机构
     *
     * @param categoryId 分类id，当前页面展示什么分类数据（政府机构是一个分类，酒店是一个分类）
     */
    public void getMechanismListForMap(String categoryId, double lat, double lng,
                                       BaseObserver<List<MechanismResponse>> observer) {
        getMechanismListForMap(categoryId, 0, "", lat, lng, observer);
    }

    /**
     * @param categoryId 分类id
     * @param areaId     区域id（地理位置，在HomeFragment左上角可以选择）
     * @param search     查询字段
     * @param lat        经度
     * @param lng        纬度
     */
    public void getMechanismListForMap(String categoryId, int areaId,
                                       String search, double lat, double lng, BaseObserver<List<MechanismResponse>> observer) {

        dealResponse(mRetrofit.getMechanismListForMap(categoryId, areaId
                , search, lat, lng
        ), observer);

    }

    /**
     * 搜索
     * @param input 关键字
     */
    public void getSearchByInput(String input, BaseObserver<List<SearchResponse>> observer) {
        dealResponse(mRetrofit.getSearchByInput(input), observer);
    }

    /**
     * 根据机构id获得机构详情（主要用于搜索功能）
     * @param mechanismId 机构详情
     */
    public void getMechanismDetailById(int mechanismId, BaseObserver<MechanismDetailResponse> observer) {
        dealResponse(mRetrofit.getMechanismDetailById(mechanismId), observer);
    }
}
