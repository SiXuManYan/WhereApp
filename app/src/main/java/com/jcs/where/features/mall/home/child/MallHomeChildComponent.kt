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
    fun bindTopBannerData(bannerUrls: ArrayList<String>, response: ArrayList<BannerResponse>)
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
        val page5 = MallBannerCategory()
        val page6 = MallBannerCategory()
        val page7 = MallBannerCategory()
        val page8 = MallBannerCategory()
        val page9 = MallBannerCategory()
        val page10 = MallBannerCategory()
        val page11 = MallBannerCategory()
        val page12 = MallBannerCategory()
        val page13 = MallBannerCategory()
        val page14 = MallBannerCategory()
        val page15 = MallBannerCategory()

        targetFirstCategory.second_level.forEachIndexed { index, category ->
            val i = 3
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

            if (index >= i * 5 && index < i * 6) {
                page5.childItem.add(category)
            }

            if (index >= i * 6 && index < i * 7) {
                page6.childItem.add(category)
            }

            if (index >= i * 7 && index < i * 8) {
                page7.childItem.add(category)
            }

            if (index >= i * 8 && index < i * 9) {
                page8.childItem.add(category)
            }
            if (index >= i * 9 && index < i * 10) {
                page9.childItem.add(category)
            }
            if (index >= i * 10 && index < i * 11) {
                page10.childItem.add(category)
            }
            if (index >= i * 11 && index < i * 12) {
                page11.childItem.add(category)
            }

            if (index >= i * 12 && index < i * 13) {
                page12.childItem.add(category)
            }
            if (index >= i * 13 && index < i * 14) {
                page13.childItem.add(category)
            }
            if (index >= i * 14 && index < i * 15) {
                page14.childItem.add(category)
            }
            if (index >= i * 15 && index < i * 16) {
                page15.childItem.add(category)
            }

        }

        if (page0.childItem.isNotEmpty()) result.add(page0)
        if (page1.childItem.isNotEmpty()) result.add(page1)
        if (page2.childItem.isNotEmpty()) result.add(page2)
        if (page3.childItem.isNotEmpty()) result.add(page3)
        if (page4.childItem.isNotEmpty()) result.add(page4)
        if (page5.childItem.isNotEmpty()) result.add(page5)
        if (page6.childItem.isNotEmpty()) result.add(page6)
        if (page7.childItem.isNotEmpty()) result.add(page7)
        if (page8.childItem.isNotEmpty()) result.add(page8)
        if (page9.childItem.isNotEmpty()) result.add(page9)
        if (page10.childItem.isNotEmpty()) result.add(page10)
        if (page11.childItem.isNotEmpty()) result.add(page11)
        if (page12.childItem.isNotEmpty()) result.add(page12)
        if (page13.childItem.isNotEmpty()) result.add(page13)
        if (page14.childItem.isNotEmpty()) result.add(page14)
        if (page15.childItem.isNotEmpty()) result.add(page15)
        view.bindBannerData(result)


    }

    fun getRecommend(categoryId: Int, page: Int) {

        requestApi(mRetrofit.getMallRecommendGood(1, page, categoryId), object : BaseMvpObserver<PageResponse<MallGood>>(view, page) {
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
        requestApi(mRetrofit.getBanners(4), object : BaseMvpObserver<ArrayList<BannerResponse>>(view) {
            override fun onSuccess(response: ArrayList<BannerResponse>?) {
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