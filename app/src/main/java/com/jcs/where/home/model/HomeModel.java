package com.jcs.where.home.model;

import com.google.gson.reflect.TypeToken;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.HomeNewsResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import java.util.List;

import io.reactivex.Observable;

public class HomeModel extends BaseModel {

    public void getInitHomeData(BaseObserver<HomeZipResponse> observer) {
        // 金刚区
        Observable<List<ModulesResponse>> modules = mRetrofit.getModules();

        // 猜你喜欢
        Observable<List<HotelResponse>> youLike = mRetrofit.getYouLike();

        // 1 表示获得首页的banner
        Observable<List<BannerResponse>> banners = mRetrofit.getBanners(1);

        // 获得首页滚动的新闻提示
        Observable<List<HomeNewsResponse>> homeNews = mRetrofit.getHomeNews();

        Observable<HomeZipResponse> zip = Observable.zip(modules, youLike, banners, homeNews, HomeZipResponse::new);
        dealResponse(zip, observer);

    }

    public void getModules(BaseObserver<List<ModulesResponse>> observer) {
        dealResponse(mRetrofit.getModules(), observer);
    }

    public String getCurrentAreaId() {
        return SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
    }

    public CityResponse getCurrentCity(String currentCityId) {
        String citiesJson = CacheUtil.needUpdateBySpKey(SPKey.K_ALL_CITIES);

        if (!citiesJson.isEmpty()) {
            List<CityResponse> cityList = JsonUtil.getInstance().fromJsonToList(citiesJson, new TypeToken<List<CityResponse>>() {
            }.getType());
            int size = cityList.size();
            for (int i = 0; i < size; i++) {
                CityResponse cityResponse = cityList.get(i);
                if (cityResponse.getId().equals(currentCityId)) {
                    return cityResponse;
                }
            }
        }
        return null;
    }

    /**
     * 猜你喜欢（酒店推荐）
     */
    public void getYouLike(BaseObserver<List<HotelResponse>> observer) {
        dealResponse(mRetrofit.getYouLike(), observer);
    }

    public static class HomeZipResponse {
        List<ModulesResponse> modulesResponses;
        List<HotelResponse> youLikeResponses;
        List<BannerResponse> bannerResponses;
        List<HomeNewsResponse> homeNewsResponses;

        public HomeZipResponse(List<ModulesResponse> modulesResponses, List<HotelResponse> hotelResponses, List<BannerResponse> bannerResponses, List<HomeNewsResponse> homeNewsResponses) {
            this.modulesResponses = modulesResponses;
            this.youLikeResponses = hotelResponses;
            this.bannerResponses = bannerResponses;
            this.homeNewsResponses = homeNewsResponses;
        }


        public List<ModulesResponse> getModulesResponses() {
            return modulesResponses;
        }

        public void setModulesResponses(List<ModulesResponse> modulesResponses) {
            this.modulesResponses = modulesResponses;
        }

        public List<HotelResponse> getYouLikeResponses() {
            return youLikeResponses;
        }

        public void setYouLikeResponses(List<HotelResponse> youLikeResponses) {
            this.youLikeResponses = youLikeResponses;
        }

        public List<BannerResponse> getBannerResponses() {
            return bannerResponses;
        }

        public void setBannerResponses(List<BannerResponse> bannerResponses) {
            this.bannerResponses = bannerResponses;
        }

        public List<HomeNewsResponse> getHomeNewsResponses() {
            return homeNewsResponses;
        }

        public void setHomeNewsResponses(List<HomeNewsResponse> homeNewsResponses) {
            this.homeNewsResponses = homeNewsResponses;
        }
    }
}
