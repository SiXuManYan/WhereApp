package com.jcs.where.features.travel.home

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import java.util.*

/**
 * Created by Wangsw  2021/9/13 11:15.
 *
 */

interface TravelHomeView : BaseMvpView {
    fun bindTopBannerData(bannerUrls: ArrayList<String>, response: List<BannerResponse>)
    fun bindPlateData(toMutableList: MutableList<Category>)
    fun bindRecommendData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean)

}

class TravelHomePresenter(private var view: TravelHomeView) : BaseMvpPresenter(view) {


    /**
     * 获取顶部轮播图
     */
    fun getTopBanner() {
        requestApi(mRetrofit.getBanners(2), object : BaseMvpObserver<List<BannerResponse>>(view) {
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
     * 功能区
     */
    fun getCategories(categories: ArrayList<Int>) {

        val toJson = Gson().toJson(categories)

        requestApi(mRetrofit.getCategoriesList(2, toJson, null), object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {

                val default = Category().apply {
                    nativeIsWebType = true
                }
                response.add(response.size,default)
                view.bindPlateData(response.toMutableList())
            }
        })
    }


    /**
     * 推荐列表
     */
    fun getRecommendList(page: Int) {


        requestApi(
            mRetrofit.getTravelRecommends(page),
            object : BaseMvpObserver<PageResponse<HomeRecommendResponse>>(view) {
                override fun onSuccess(response: PageResponse<HomeRecommendResponse>) {
                    val isLastPage = response.lastPage == page
                    val data = response.data.toMutableList()
                    view.bindRecommendData(data, isLastPage)
                }
            })
    }

}