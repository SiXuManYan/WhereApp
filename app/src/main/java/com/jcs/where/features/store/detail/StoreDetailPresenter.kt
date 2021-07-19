package com.jcs.where.features.store.detail

import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.store.StoreDetail
import com.jcs.where.widget.JcsTitle

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

    fun collection(shopId: Int, mJcsTitle: JcsTitle) {
        requestApi(mRetrofit.storeCollects(shopId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_red)
            }
        })
    }

    fun unCollection(shopId: Int, mJcsTitle: JcsTitle) {
        requestApi(mRetrofit.StoreCancelCollects(shopId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_black2)
            }
        })

    }
}