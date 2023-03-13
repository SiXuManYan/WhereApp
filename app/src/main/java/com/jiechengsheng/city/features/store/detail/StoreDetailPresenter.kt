package com.jiechengsheng.city.features.store.detail

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.request.MallShopCollection
import com.jiechengsheng.city.api.response.store.StoreDetail

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

    fun collection(shopId: Int) {

        val apply = MallShopCollection().apply {
            shop_id = shopId
        }

        requestApi(mRetrofit.storeCollects(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.changeCollection(true)
            }
        })
    }

    fun unCollection(shopId: Int) {
        val apply = MallShopCollection().apply {
            shop_id = shopId
        }

        requestApi(mRetrofit.StoreCancelCollects(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.changeCollection(false)
            }
        })

    }
}