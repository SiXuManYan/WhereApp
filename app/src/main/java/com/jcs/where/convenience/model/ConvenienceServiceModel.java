package com.jcs.where.convenience.model;

import android.util.Log;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismPageResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;
import com.jcs.where.yellow_page.model.YellowPageModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;

/**
 * create by zyf on 2021/1/5 4:35 下午
 */
public class ConvenienceServiceModel extends BaseModel {

    /**
     * @param categoryId 分类id集合
     * @param search     查询字段
     */
    public void getMechanismList(String categoryId,
                                 String search, BaseObserver<MechanismPageResponse> observer) {

        dealResponse(mRetrofit.getMechanismListById(categoryId, search), observer);

    }

    /**
     * 获得页面TabLayout展示的二级分类
     */
    public void getCategories(String categoryIds, BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(2, categoryIds), observer);
    }

    public void getAreaList(BaseObserver<List<CityResponse>> observer) {
        dealResponse(mRetrofit.getAreaForService(), observer);
    }

    public int getCityResponseIndexById(List<CityResponse> cityResponses){
        String cityId = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
        int size = cityResponses.size();
        for (int i = 0; i < size; i++) {
            CityResponse cityResponse = cityResponses.get(i);
            if (cityResponse.getId().equals(cityId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 是否需要更新城市数据
     *
     * @return 城市数据json字符串，为""表示要更新
     */
    public String needUpdateCity() {
        return CacheUtil.needUpdateBySpKey(SPKey.K_ALL_CITIES);
    }

    public String needUpdateCategory() {
        return CacheUtil.needUpdateBySpKey(SPKey.K_CONVENIENCE_SERVICE_CATEGORIES);
    }

    public void getInitData(String categoryIds, BaseObserver<ConvenienceServiceModel.ConvenienceServiceZipResponse> observer) {

        // 获取二级分类用于tab展示
        Observable<List<CategoryResponse>> categoriesObservable = mRetrofit.getCategories(2, categoryIds);
        Observable<List<CityResponse>> citiesObservable = mRetrofit.getAreaForService();


        Observable<ConvenienceServiceModel.ConvenienceServiceZipResponse> zip = Observable.zip(citiesObservable, categoriesObservable, ConvenienceServiceZipResponse::new);
        dealResponse(zip, observer);
    }

    public void saveCities(List<CityResponse> cityResponses) {
        String cityJsonStr = JsonUtil.getInstance().toJsonStr(cityResponses);
        long currentTime = System.currentTimeMillis();
        String saveJson = cityJsonStr + SPKey.K_DELIMITER + currentTime;
        SPUtil.getInstance().saveString(SPKey.K_ALL_CITIES, saveJson);
    }

    public static class ConvenienceServiceZipResponse {
        List<CityResponse> cities;
        List<CategoryResponse> categories;

        public ConvenienceServiceZipResponse(List<CityResponse> cities, List<CategoryResponse> categories) {
            this.cities = cities;
            this.categories = categories;
        }

        public List<CityResponse> getCities() {
            return cities;
        }

        public List<CategoryResponse> getCategories() {
            return categories;
        }
    }
}
