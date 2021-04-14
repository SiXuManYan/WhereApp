package com.jcs.where.features.home

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jcs.where.BuildConfig
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.home.HomeNewsResponse
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.api.response.version.VersionResponse
import com.jcs.where.bean.CityResponse
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.*
import io.rong.imlib.RongIMClient
import java.util.*

/**
 * Created by Wangsw  2021/4/12 13:53.
 *
 */
class HomePresenter2(val view: HomeView2) : BaseMvpPresenter(view) {


    /**
     *  当前区域id
     */
    fun getCurrentAreaId(): String = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID)

    fun getCurrentCity(currentCityId: String): CityResponse? {
        val citiesJson = CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_ALL_CITIES)

        if (!citiesJson.isNullOrBlank()) {
            val cityList = JsonUtil.getInstance().fromJsonToList<CityResponse>(citiesJson, object : TypeToken<List<CityResponse?>?>() {}.type)
            val size = cityList.size
            for (i in 0 until size) {
                val cityResponse = cityList[i]
                if (cityResponse.id == currentCityId) {
                    return cityResponse
                }
            }

        }
        return null
    }


    /**
     * 推荐列表
     */
    fun getRecommendList(page: Int) {
        val areaId = SPUtil.getInstance().getString(SPKey.K_CURRENT_AREA_ID)
        val lat = Constant.LAT.toString() + ""
        val lng = Constant.LNG.toString() + ""

        requestApi(mRetrofit.getRecommends(page, lat, lng, areaId), object : BaseMvpObserver<PageResponse<HomeRecommendResponse>>(view) {
            override fun onSuccess(response: PageResponse<HomeRecommendResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data.toMutableList()

                view.bindRecommendData(data, isLastPage)
            }
        })
    }

    /**
     * 获取未读消息数量
     */
    fun getMessageCount() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.unreadMessageCount, object : BaseMvpObserver<JsonObject>(view) {
            override fun onSuccess(response: JsonObject) {
                var apiUnreadMessageCount = 0
                if (response.has("count")) {
                    apiUnreadMessageCount = response["count"].asInt
                }
                val finalApiUnreadMessageCount = apiUnreadMessageCount
                RongIMClient.getInstance().getTotalUnreadCount(object : RongIMClient.ResultCallback<Int?>() {
                    override fun onSuccess(rongMessageCount: Int?) {
                        var rongMessageCount = rongMessageCount
                        if (rongMessageCount == null) {
                            rongMessageCount = 0
                        }
                        view.setMessageCount(finalApiUnreadMessageCount + rongMessageCount)
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode) {
                        view.setMessageCount(0)
                    }
                })
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
                view.setMessageCount(0)
            }
        })
    }

    /**
     * 获取顶部轮播图
     */
    fun getTopBanner() {
        requestApi(mRetrofit.getBanners(1), object : BaseMvpObserver<List<BannerResponse>>(view) {
            override fun onSuccess(response: List<BannerResponse>?) {

                if (response == null || response.isEmpty()) {
                    return
                }
                val bannerUrls: ArrayList<String> = ArrayList()
                response.forEach {
                    bannerUrls.add(it.src)
                }
                view.bindTopBannerData(bannerUrls)
            }
        })
    }

    /**
     * 金刚区数据
     */
    fun getPlateData() {
        requestApi(mRetrofit.modules, object : BaseMvpObserver<List<ModulesResponse>>(view) {
            override fun onSuccess(response: List<ModulesResponse>) {
                val toMutableList = response.toMutableList()
                view.bindPlateData(toMutableList)
            }
        })

    }

    /**
     * 检查版本更新
     */
    fun checkAppVersion() {
        requestApi(mRetrofit.checkAppVersion(BuildConfig.VERSION_NAME, "Android"), object : BaseMvpObserver<VersionResponse>(view) {
            override fun onSuccess(response: VersionResponse) {
                if (!response.status) {
                    return
                }
                view.checkAppVersion(response)
            }

            override fun onError(errorResponse: ErrorResponse?) = Unit
        })

    }

    /**
     * 新闻列表
     */
    fun getNewsList() {
        requestApi(mRetrofit.homeNewsList, object : BaseMvpObserver<List<HomeNewsResponse>>(view) {
            override fun onSuccess(response: List<HomeNewsResponse>?) {
                view.bindNewsData(response)

            }
        })

    }


}