package com.jcs.where.features.store.good

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.store.StoreGoodDetail

/**
 * Created by Wangsw  2021/6/18 14:23.
 *
 */
class StoreGoodDetailPresenter(private var view: StoreGoodDetailView) : BaseMvpPresenter(view) {


    fun getData(goodId: Int) {
        requestApi(mRetrofit.getStoreGoodDetail(goodId), object : BaseMvpObserver<StoreGoodDetail>(view) {
            override fun onSuccess(response: StoreGoodDetail) {
                view.bingData(response)
            }
        })
    }
}