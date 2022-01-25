package com.jcs.where.convenience.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.JcsResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * create by zyf on 2021/1/5 4:35 下午
 */
public class ConvenienceServiceModel extends BaseModel {



    /**
     * 获得页面TabLayout展示的二级分类
     */
    public void getCategories(String categoryIds, BaseObserver<List<CategoryResponse>> observer) {
        dealResponse(mRetrofit.getCategories(2, categoryIds), observer);
    }

    public void getAreaList(BaseObserver<List<CityResponse>> observer) {
        dealResponse(mRetrofit.getAreaForService(), observer);
    }

    /**
     * 是否需要更新城市数据
     *
     * @return 城市数据json字符串，为""表示要更新
     */
    public String needUpdateCity() {
        return CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_ALL_CITIES);
    }

    public String needUpdateCategory(String categoryId) {
        return CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_SERVICE_CATEGORIES + categoryId);
    }

    public void getInitData(String categoryIds, BaseObserver<ConvenienceServiceModel.ConvenienceServiceZipResponse> observer) {

        // 获取二级分类用于tab展示
        Observable<JcsResponse<List<CategoryResponse>>> categoriesObservable = mRetrofit.getCategories(2, categoryIds);
        Observable<JcsResponse<List<CityResponse>>> citiesObservable = mRetrofit.getAreaForService();


        Observable<JcsResponse<ConvenienceServiceZipResponse>> zip = Observable.zip(citiesObservable, categoriesObservable, new BiFunction<JcsResponse<List<CityResponse>>, JcsResponse<List<CategoryResponse>>, JcsResponse<ConvenienceServiceZipResponse>>() {
            @NotNull
            @Override
            public JcsResponse<ConvenienceServiceZipResponse> apply(@NotNull JcsResponse<List<CityResponse>> listJcsResponse, @NotNull JcsResponse<List<CategoryResponse>> listJcsResponse2) throws Exception {
                JcsResponse<ConvenienceServiceZipResponse> jcsResponse = new JcsResponse<>();
                int code = listJcsResponse.getCode();
                int code2 = listJcsResponse2.getCode();
                if (code != 200) {
                    jcsResponse.setCode(code);
                    jcsResponse.setMessage(listJcsResponse.getMessage());
                    return jcsResponse;
                }

                if (code2 != 200) {
                    jcsResponse.setCode(code2);
                    jcsResponse.setMessage(listJcsResponse2.getMessage());
                    return jcsResponse;
                }

                ConvenienceServiceZipResponse zipResponse = new ConvenienceServiceZipResponse(listJcsResponse.getData(),listJcsResponse2.getData());
                jcsResponse.setCode(200);
                jcsResponse.setMessage("success");
                jcsResponse.setData(zipResponse);
                return jcsResponse;
            }
        });
        dealResponse(zip, observer);
    }

    public void saveCities(List<CityResponse> cityResponses) {
        CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_ALL_CITIES, cityResponses);
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
