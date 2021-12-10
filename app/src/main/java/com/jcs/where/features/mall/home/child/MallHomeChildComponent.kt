package com.jcs.where.features.mall.home.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.category.StoryBannerCategory
import com.jcs.where.api.response.mall.MallBannerCategory
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/11/30 17:01.
 *
 */
interface MallHomeChildView : BaseMvpView {
    fun bindBannerData(result: ArrayList<MallBannerCategory>)
    fun bindRecommend(response:ArrayList<MallGood>)

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

    fun getRecommend(categoryId:Int) {

        // 暂时用原有商城推荐替代
        requestApi(mRetrofit.getMallRecommendGood(categoryId), object : BaseMvpObserver<ArrayList<MallGood>>(view) {
            override fun onSuccess(response: ArrayList<MallGood>) {
                view.bindRecommend(response)
            }
        })
    }

}