package com.jcs.where.home.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.JcsResponse;
import com.jcs.where.api.response.BannerResponse;
import com.jcs.where.api.response.HomeNewsResponse;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.bean.CityResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.JsonUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function4;

public class HomeModel extends BaseModel {

    public void getInitHomeData(BaseObserver<HomeZipResponse> observer) {
        // 金刚区
        Observable<JcsResponse<List<ModulesResponse>>> modules = mRetrofit.getModules();

        // 猜你喜欢
        Observable<JcsResponse<List<HotelResponse>>> youLike = mRetrofit.getYouLike();

        // 1 表示获得首页的banner
        Observable<JcsResponse<List<BannerResponse>>> banners = mRetrofit.getBanners(1);

        // 获得首页滚动的新闻提示
        Observable<JcsResponse<List<HomeNewsResponse>>> homeNews = mRetrofit.getHomeNews();

        Observable<JcsResponse<HomeZipResponse>> zip = Observable.zip(modules, youLike, banners, homeNews, new Function4<JcsResponse<List<ModulesResponse>>, JcsResponse<List<HotelResponse>>, JcsResponse<List<BannerResponse>>, JcsResponse<List<HomeNewsResponse>>, JcsResponse<HomeZipResponse>>() {
            @NotNull
            @Override
            public JcsResponse<HomeZipResponse> apply(@NotNull JcsResponse<List<ModulesResponse>> listJcsResponse, @NotNull JcsResponse<List<HotelResponse>> listJcsResponse2, @NotNull JcsResponse<List<BannerResponse>> listJcsResponse3, @NotNull JcsResponse<List<HomeNewsResponse>> listJcsResponse4) throws Exception {
                JcsResponse<HomeZipResponse> jcsResponse = new JcsResponse<>();
                int code1 = listJcsResponse.getCode();
                int code2 = listJcsResponse2.getCode();
                int code3 = listJcsResponse3.getCode();
                int code4 = listJcsResponse4.getCode();

                if (code1 != 200) {
                    jcsResponse.setCode(code1);
                    jcsResponse.setMessage(listJcsResponse.getMessage());
                    jcsResponse.setData(null);
                    return jcsResponse;
                }

                if (code2 != 200) {
                    jcsResponse.setCode(code2);
                    jcsResponse.setMessage(listJcsResponse2.getMessage());
                    jcsResponse.setData(null);
                    return jcsResponse;
                }

                if (code3 != 200) {
                    jcsResponse.setCode(code3);
                    jcsResponse.setMessage(listJcsResponse3.getMessage());
                    jcsResponse.setData(null);
                    return jcsResponse;
                }

                if (code4 != 200) {
                    jcsResponse.setCode(code4);
                    jcsResponse.setMessage(listJcsResponse4.getMessage());
                    jcsResponse.setData(null);
                    return jcsResponse;
                }
                jcsResponse.setCode(200);
                jcsResponse.setMessage("success");
                HomeZipResponse homeZipResponse = new HomeZipResponse(listJcsResponse.getData(), listJcsResponse2.getData(), listJcsResponse3.getData(), listJcsResponse4.getData());
                jcsResponse.setData(homeZipResponse);
                return jcsResponse;
            }
        });
        dealResponse(zip, observer);
    }

    public void getModules(BaseObserver<List<ModulesResponse>> observer) {
        dealResponse(mRetrofit.getModules(), observer);
    }

    public String getCurrentAreaId() {
        return SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID);
    }

    public CityResponse getCurrentCity(String currentCityId) {
        String citiesJson = CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_ALL_CITIES);

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


    public void getUnreadMessageCount( BaseObserver<JsonObject> observer ) {
        dealResponse(mRetrofit.getUnreadMessageCount(), observer);
    }


}
