package com.jcs.where.features.home

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/12 13:54.
 *
 */
interface HomeView2 : BaseMvpView {


    fun bindDetailData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean)

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

}