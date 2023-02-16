package com.jcs.where.features.home

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.CityPickerResponse
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.home.HomeChild
import com.jcs.where.api.response.home.HomeNewsResponse
import com.jcs.where.api.response.version.VersionResponse

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