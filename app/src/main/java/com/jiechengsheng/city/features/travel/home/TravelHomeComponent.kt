package com.jiechengsheng.city.features.travel.home

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.BannerResponse
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.api.response.recommend.HomeRecommendResponse
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.SPKey
import java.util.*

/**
 * Created by Wangsw  2021/9/13 11:15.
 *
 */

interface TravelHomeView : BaseMvpView {
    fun bindTopBannerData(bannerUrls: ArrayList<String>, response: ArrayList<BannerResponse>)
    fun bindPlateData(toMutableList: MutableList<Category>)
    fun bindRecommendData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean)

}

class TravelHomePresenter(private var view: TravelHomeView) : BaseMvpPresenter(view) {


    /**
     * 获取顶部轮播图
     */
    fun getTopBanner() {
        requestApi(mRetrofit.getBanners(2), object : BaseMvpObserver<ArrayList<BannerResponse>>(view) {
            override fun onSuccess(response: ArrayList<BannerResponse>?) {
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
    fun getRecommendList() {

        val latLng = CacheUtil.getSafeSelectLatLng()
        val areaId = SPUtils.getInstance().getString(SPKey.SELECT_AREA_ID, "")


        requestApi(
            mRetrofit.getTravelRecommends(areaId,latLng.latitude.toString(),latLng.longitude.toString()),
            object : BaseMvpObserver<PageResponse<HomeRecommendResponse>>(view) {
                override fun onSuccess(response: PageResponse<HomeRecommendResponse>) {
//                    val isLastPage = response.lastPage == page
                    val data = response.data.toMutableList()
                    view.bindRecommendData(data, true)
                }
            })
    }

}