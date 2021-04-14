package com.jcs.where.features.home

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.home.HomeNewsResponse
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.api.response.version.VersionResponse
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/12 13:54.
 *
 */
interface HomeView2 : BaseMvpView {


    /**
     * 推荐列表
     */
    fun bindRecommendData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean)

    /**
     * 未读消息数量
     */
    fun setMessageCount(i: Int)


    /**
     * 轮播图数据
     */
    fun bindTopBannerData(bannerUrls: ArrayList<String>)

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

}