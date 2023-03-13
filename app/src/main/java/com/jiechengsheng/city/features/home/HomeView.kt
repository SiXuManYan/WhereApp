package com.jiechengsheng.city.features.home

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.BannerResponse
import com.jiechengsheng.city.api.response.ModulesResponse
import com.jiechengsheng.city.api.response.home.HomeChild
import com.jiechengsheng.city.api.response.home.HomeNewsResponse
import com.jiechengsheng.city.api.response.version.VersionResponse

/**
 * Created by Wangsw  2021/4/12 13:54.
 *
 */
interface HomeView : BaseMvpView {


    /**
     * 未读消息数量
     */
    fun setMessageCount(i: Int, systemUnreadMessageCount: Int)


    /**
     * 轮播图数据
     */
    fun bindTopBannerData(bannerUrls: ArrayList<String>, response: ArrayList<BannerResponse>)

    /**
     * 金刚区数据
     */
    fun bindPlateData(toMutableList: MutableList<ModulesResponse>)

    /**
     * 检查版本更新
     */
    fun checkAppVersion(response: VersionResponse)


    /**
     * 新闻数据
     */
    fun bindNewsData(newsData: List<HomeNewsResponse>?)


    fun bindHomeChild(response: ArrayList<HomeChild>, titles: ArrayList<String>)

    fun bindDefaultCity(cityName:String)

    /**
     * 展示简历投递状态更新通知
     */
    fun showJobNotice()

}