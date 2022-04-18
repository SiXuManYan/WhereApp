package com.jcs.where.features.mall.home.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallBannerCategory
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.api.response.mall.MallGood

/**
 * Created by Wangsw  2021/11/30 17:01.
 *
 */
interface MallHomeChildView : BaseMvpView {
    fun bindBannerData(result: ArrayList<MallBannerCategory>)
    fun bindTopBannerData(bannerUrls: ArrayList<String>, response: List<BannerResponse>)
    fun bindRecommend(response: MutableList<MallGood>, isLastPage: Boolean)

}

class MallHomeChildPresenter(private var view: MallHomeChildView) : BaseMvpPresenter(view) {

    /**
     * 获取一级分类中的二级分类，用于轮播
     */
    fun handleBanner(targetFirstCategory: MallCategory) {

        val result: ArrayList<MallBannerCategory> = ArrayList()

        val page0 = MallBannerCategory()
        val page1 = MallBannerCategory()
        val page2 = MallBannerCategory()
        val page3 = MallBannerCategory()
        val page4 = MallBannerCategory()

        targetFirstCategory.second_level.forEachIndexed { index, category ->
            val i = 6
            if (index < i) {
                page0.childItem.add(category)
            }
            if (index >= i && index < i * 2) {
                page1.childItem.add(category)
            }
            if (index >= i * 2 && index < i * 3) {
                page2.childItem.add(category)
            }
            if (index >= i * 3 && index < i * 4) {
                page3.childItem.add(category)
            }
            if (index >= i * 4 && index < i * 5) {
                page4.childItem.add(category)
            }
        }

        if (page0.childItem.isNotEmpty()) {
            result.add(page0)
        }
        if (page1.childItem.isNotEmpty()) {
            result.add(page1)
        }
        if (page2.childItem.isNotEmpty()) {
            result.add(page2)
        }
        if (page3.childItem.isNotEmpty()) {
            result.add(page3)
        }
        if (page4.childItem.isNotEmpty()) {
            result.add(page4)
        }

        view.bindBannerData(result)


    }

    fun getRecommend(categoryId:Int,page:Int) {

        requestApi(mRetrofit.getMallRecommendGood(page,categoryId), object : BaseMvpObserver<PageResponse<MallGood>>(view) {
            override fun onSuccess(response: PageResponse<MallGood>) {

                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindRecommend(data.toMutableList(), isLastPage)


            }
        })
    }


    /**
     * 获取顶部轮播图
     */
    fun getTopBanner() {
        requestApi(mRetrofit.getBanners(4), object : BaseMvpObserver<List<BannerResponse>>(view) {
            override fun onSuccess(response: List<BannerResponse>?) {
                if (response == null || response.isEmpty()) {
                    return
                }

                val bannerUrls: java.util.ArrayList<String> = java.util.ArrayList()
                response.forEach {
                    bannerUrls.add(it.src)
                }
                view.bindTopBannerData(bannerUrls, response)
            }
        })
    }

}