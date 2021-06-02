package com.jcs.where.features.store.list

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/6/1 10:33.
 *
 */
class StoreRecommendPresenter(val view: StoreRecommendView) : BaseMvpPresenter(view) {


    fun getBanner() {
        requestApi(mRetrofit.storeCategoryFirst, object : BaseMvpObserver<ArrayList<Category>>(view) {
            override fun onSuccess(response: ArrayList<Category>) {


                view.bindBanner(response)
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