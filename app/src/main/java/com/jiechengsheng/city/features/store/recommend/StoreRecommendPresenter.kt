package com.jiechengsheng.city.features.store.recommend

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.api.response.category.StoryBannerCategory
import com.jiechengsheng.city.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/6/1 10:33.
 *
 */
class StoreRecommendPresenter(val view: StoreRecommendView) : BaseMvpPresenter(view) {


    fun getBanner() {
        requestApi(mRetrofit.storeCategoryFirst, object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {

                val result: ArrayList<StoryBannerCategory> = ArrayList()

                val page0 = StoryBannerCategory()
                val page1 = StoryBannerCategory()
                val page2 = StoryBannerCategory()
                val page3 = StoryBannerCategory()
                val page4 = StoryBannerCategory()

                response.forEachIndexed { index, category ->
                    if (index < 8) {
                        page0.childItem.add(category)
                    }
                    if (index >= 8 && index < 8 * 2) {
                        page1.childItem.add(category)
                    }
                    if (index >= 8 * 2 && index < 8 * 3) {
                        page2.childItem.add(category)
                    }
                    if (index >= 8 * 3 && index < 8 * 4) {
                        page3.childItem.add(category)
                    }
                    if (index >= 8 * 4 && index < 8 * 5) {
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
        })
    }

    fun getRecommend() {
        requestApi(mRetrofit.storeRecommends, object : BaseMvpObserver<ArrayList<StoreRecommend>>(view) {
            override fun onSuccess(response: ArrayList<StoreRecommend>) {
                view.bindRecommend(response)
            }
        })
    }
}