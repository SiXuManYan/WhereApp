package com.jcs.where.features.home

import com.jcs.where.BuildConfig
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.UnReadMessage
import com.jcs.where.api.response.home.HomeNewsResponse
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.api.response.version.VersionResponse
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.SPKey
import com.jcs.where.utils.SPUtil
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import java.util.*

/**
 * Created by Wangsw  2021/4/12 13:53.
 *
 */
class HomePresenter(val view: HomeView) : BaseMvpPresenter(view) {

    
    /**
     * 推荐列表
     */
    fun getRecommendList(page: Int) {
        val areaId = SPUtil.getInstance().getString(SPKey.SELECT_AREA_ID)

        val selectLatLng = CacheUtil.getSafeSelectLatLng()


        requestApi(
            mRetrofit.getRecommends(page, selectLatLng.latitude.toString(), selectLatLng.longitude.toString(), areaId),
            object : BaseMvpObserver<PageResponse<HomeRecommendResponse>>(view) {
                override fun onSuccess(response: PageResponse<HomeRecommendResponse>) {
                    val isLastPage = response.lastPage == page
                    val data = response.data.toMutableList()

                    view.bindRecommendData(data, isLastPage)
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    super.onError(errorResponse)
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
        requestApi(mRetrofit.unreadMessageCount, object : BaseMvpObserver<UnReadMessage>(view) {
            override fun onSuccess(response: UnReadMessage) {
                val apiUnreadMessageCount = response.count
                RongIMClient.getInstance().getTotalUnreadCount(object : RongIMClient.ResultCallback<Int?>() {
                    override fun onSuccess(rongMessageCount: Int?) {

                        if (rongMessageCount == null) {
                            view.setMessageCount(apiUnreadMessageCount)
                        } else {
                            view.setMessageCount(apiUnreadMessageCount + rongMessageCount)
                        }
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode) {
                        view.setMessageCount(0)
                    }
                })
            }

            override fun onError(errorResponse: ErrorResponse?) {
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
                view.bindTopBannerData(bannerUrls, response)
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


}