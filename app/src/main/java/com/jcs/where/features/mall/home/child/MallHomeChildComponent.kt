package com.jcs.where.features.mall.home.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.category.StoryBannerCategory
import com.jcs.where.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/11/30 17:01.
 *
 */
interface MallHomeChildView : BaseMvpView {
    fun bindBannerData(result: ArrayList<StoryBannerCategory>)
    fun bindRecommend(response:ArrayList<StoreRecommend>)

}

class MallHomeChildPresenter(private var view: MallHomeChildView) : BaseMvpPresenter(view) {

    /**
     * 获取一级分类中的二级分类，用于轮播
     */
    fun handleBanner(targetFirstCategory: Category) {

        val result: ArrayList<StoryBannerCategory> = ArrayList()

        val page0 = StoryBannerCategory()
        val page1 = StoryBannerCategory()
        val page2 = StoryBannerCategory()
        val page3 = StoryBannerCategory()
        val page4 = StoryBannerCategory()

        targetFirstCategory.child_categories.forEachIndexed { index, category ->
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

    fun getRecommend() {

        // 暂时用原有商城推荐替代
        requestApi(mRetrofit.storeRecommends, object : BaseMvpObserver<ArrayList<StoreRecommend>>(view) {
            override fun onSuccess(response: ArrayList<StoreRecommend>) {
                view.bindRecommend(response)
            }
        })
    }

}