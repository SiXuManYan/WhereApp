package com.jcs.where.features.store.detail

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.store.StoreDetail

/**
 * Created by Wangsw  2021/6/15 10:32.
 *
 */
class StoreDetailPresenter(val view: StoreDetailView) : BaseMvpPresenter(view) {


    fun getDetail(id: Int) {

        requestApi(mRetrofit.getStoreDetail(id), object : BaseMvpObserver<StoreDetail>(view) {

            override fun onSuccess(response: StoreDetail) {
                view.bindDetail(response)
            }

        })
    }
}