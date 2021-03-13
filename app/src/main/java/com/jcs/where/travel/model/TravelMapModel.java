package com.jcs.where.travel.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.SearchResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.MapMarkerUtil;

import java.util.List;

/**
 * create by zyf on 2021/1/28 11:57 上午
 */
public class TravelMapModel extends BaseModel {

    /**
     * 获得页面TabLayout展示的二级分类
     */
    public void getCategories(BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(2, String.valueOf(1)), observer);
    }

    /**
     * 获取某区域下的所有旅游景点
     *
     * @param categoryId 分类id，当前页面展示什么子分类数据
     */
    public void getTouristAttractionListForMap(String categoryId, double lat, double lng,
                                       BaseObserver<List<TouristAttractionResponse>> observer) {
        getTouristAttractionListForMap(categoryId, 0, "", lat, lng, observer);
    }

    /**
     * 搜索某区域下的所有旅游景点
     */
    public void getTouristAttractionListForMap(String searchInput,
                                             BaseObserver<List<TouristAttractionResponse>> observer) {
        getTouristAttractionListForMap("0", 0, searchInput, Constant.LAT, Constant.LNG, observer);
    }

    /**
     * 获得展示在地图上的景点数据
     * @param categoryId 分类id
     * @param areaId     区域id（地理位置，在HomeFragment左上角可以选择）
     * @param search     查询字段
     * @param lat        经度
     * @param lng        纬度
     */
    public void getTouristAttractionListForMap(String categoryId, int areaId,
                                               String search, double lat, double lng, BaseObserver<List<TouristAttractionResponse>> observer) {

        dealResponse(mRetrofit.getTouristAttractionListForMap(categoryId, areaId
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
     * 根据景点id获得旅游景点详情（主要用于搜索功能）
     * @param mechanismId 旅游景点id
     */
    public void getMechanismDetailById(int mechanismId, BaseObserver<MechanismDetailResponse> observer) {
        dealResponse(mRetrofit.getMechanismDetailById(mechanismId), observer);
    }
}
