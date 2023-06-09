package com.jiechengsheng.city.features.home

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.jiechengsheng.city.BuildConfig
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.BannerResponse
import com.jiechengsheng.city.api.response.CityPickerResponse
import com.jiechengsheng.city.api.response.ModulesResponse
import com.jiechengsheng.city.api.response.UnReadMessage
import com.jiechengsheng.city.api.response.home.HomeChild
import com.jiechengsheng.city.api.response.home.HomeNewsResponse
import com.jiechengsheng.city.api.response.job.JobNotice
import com.jiechengsheng.city.api.response.version.VersionResponse
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.SPKey
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient

/**
 * Created by Wangsw  2021/4/12 13:53.
 *
 */
class HomePresenter(val view: HomeView) : BaseMvpPresenter(view) {


    /** 轮播图是否请求失败 */
    var isTopBannerError = false

    /** 金刚区是否请求失败 */
    var isPlateDataError = false

    /** 子列表分类是否请求失败 */
    var isChildError = false

    /**
     * 获取未读消息总数
     */
    fun getMessageCount() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.unreadMessageCount, object : BaseMvpObserver<UnReadMessage>(view,false) {
            override fun onSuccess(response: UnReadMessage) {
                val apiUnreadMessageCount = response.count

                try {
                    // 捕获融云SKD 5.2.3.3 获取消息数量时， 在Android 12 上出现的 UnsatisfiedLinkError 异常
                    RongIMClient.getInstance().getTotalUnreadCount(object : RongIMClient.ResultCallback<Int?>() {
                        override fun onSuccess(rongMessageCount: Int?) {

                            var rongCount = 0
                            rongMessageCount?.let {
                                rongCount = it
                            }

                            val totalCount = apiUnreadMessageCount + rongCount
                            view.setMessageCount(totalCount,apiUnreadMessageCount)

                        }

                        override fun onError(errorCode: RongIMClient.ErrorCode) {
                            view.setMessageCount(apiUnreadMessageCount,apiUnreadMessageCount)
                        }
                    })
                } catch (e: Exception) {
                    view.setMessageCount(apiUnreadMessageCount,apiUnreadMessageCount)
                }


            }

            override fun onError(e: Throwable) {
                view.setMessageCount(0,0)
            }

        })


    }

    /**
     * 获取顶部轮播图
     */
    fun getTopBanner() {
        requestApi(mRetrofit.getBanners(1), object : BaseMvpObserver<ArrayList<BannerResponse>>(view, false) {
            override fun onSuccess(response: ArrayList<BannerResponse>?) {
                isTopBannerError = false
                if (response == null || response.isEmpty()) {
                    return
                }

                val bannerUrls: ArrayList<String> = ArrayList()
                response.forEach {
                    bannerUrls.add(it.src)
                }
                view.bindTopBannerData(bannerUrls, response)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                isTopBannerError = true
                super.onError(errorResponse)

            }
        })
    }

    /**
     * 金刚区数据
     */
    fun getPlateData() {
        requestApi(mRetrofit.modules, object : BaseMvpObserver<List<ModulesResponse>>(view, false) {
            override fun onSuccess(response: List<ModulesResponse>) {
                isPlateDataError = false
                val toMutableList = response.toMutableList()
                view.bindPlateData(toMutableList)

                // 存储企业黄页所有一级分类id
                val allYellowCategoryIds = CacheUtil.getShareDefault().getString(Constant.SP_YELLOW_PAGE_ALL_FIRST_CATEGORY_ID, "")
                if (allYellowCategoryIds.isNullOrBlank()) {
                    response.forEach {
                        if (it.id == 2) {
                            val yellowPageAllFirstCategoryId = Gson().toJson(it.categories)
                            CacheUtil.getShareDefault()
                                .put(Constant.SP_YELLOW_PAGE_ALL_FIRST_CATEGORY_ID, yellowPageAllFirstCategoryId)
                            return@forEach
                        }
                    }
                }
            }

            override fun onError(errorResponse: ErrorResponse?) {
                isPlateDataError = true
                super.onError(errorResponse)
            }
        })

    }


    /**
     * 新闻列表
     */
    fun getNewsList() {
        requestApi(mRetrofit.homeNewsList, object : BaseMvpObserver<List<HomeNewsResponse>>(view, false) {
            override fun onSuccess(response: List<HomeNewsResponse>?) {
                view.bindNewsData(response)
            }

            override fun onError(errorResponse: ErrorResponse) = Unit
        })

    }


    /**
     * 连接融云
     */
    fun connectRongCloud() {

        if (!User.isLogon()) {
            return
        }

        val user = User.getInstance()
        user.rongData

        RongIM.connect(user.rongData.token, object : RongIMClient.ConnectCallback() {


            override fun onError(p0: RongIMClient.ConnectionErrorCode?) = Unit

            override fun onDatabaseOpened(p0: RongIMClient.DatabaseOpenStatus?) = Unit

            override fun onSuccess(p0: String?) = Unit

        })
    }

    fun getHomeChild() {

        requestApi(mRetrofit.homeChild, object : BaseMvpObserver<ArrayList<HomeChild>>(view) {
            override fun onSuccess(response: ArrayList<HomeChild>) {
                isChildError = false
                val titles: ArrayList<String> = ArrayList()
                response.forEach {
                    titles.add(it.name)
                }
                view.bindHomeChild(response, titles)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                isChildError = true
                super.onError(errorResponse)
            }

        })
    }

    /**
     * 无城市信息缓存时，获取默认城市信息
     */
    fun getCityData() {

        val instance = SPUtils.getInstance()
        val cityName = instance.getString(SPKey.SELECT_AREA_NAME, "")
        val areaId = instance.getString(SPKey.SELECT_AREA_ID, "")

        if (!cityName.isNullOrBlank() || !areaId.isNullOrBlank()) {
            return
        }

        requestApi(mRetrofit.getCityPickers("list"), object : BaseMvpObserver<CityPickerResponse>(view, false) {
            override fun onSuccess(response: CityPickerResponse) {

                val defaultCity = response.defaultCity
                instance.put(SPKey.SELECT_AREA_ID, defaultCity.id)
                instance.put(SPKey.SELECT_AREA_NAME, defaultCity.name)
                instance.put(SPKey.SELECT_LAT, defaultCity.lat.toFloat())
                instance.put(SPKey.SELECT_LNG, defaultCity.lng.toFloat())
                view.bindDefaultCity(defaultCity.name)
            }
        })
    }


    /**
     * 检查版本更新
     */
    fun checkAppVersion() {
        requestApi(mRetrofit.checkAppVersion(BuildConfig.VERSION_NAME, "2"), object : BaseMvpObserver<VersionResponse>(view, false) {
            override fun onSuccess(response: VersionResponse) {
                if (!response.status) {
                    checkJobNotice()
                    return
                }
                view.checkAppVersion(response)
            }

            override fun onError(errorResponse: ErrorResponse?){
                checkJobNotice()
            }
        })

    }


    /**
     * 检查简历投递状态更新通知
     */
   private  fun checkJobNotice() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.jobNotification, object : BaseMvpObserver<JobNotice>(view) {
            override fun onSuccess(response: JobNotice) {
                val status = response.status
                if (status) {
                    view.showJobNotice()
                }

            }

        })

    }

}