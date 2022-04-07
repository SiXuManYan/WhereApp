package com.jcs.where.features.store.good

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.request.store.StoreAddCart
import com.jcs.where.api.response.store.StoreGoodDetail

/**
 * Created by Wangsw  2021/6/18 14:23.
 *
 */
class StoreGoodDetailPresenter(private var view: StoreGoodDetailView) : BaseMvpPresenter(view) {


    fun getData(goodId: Int) {
        requestApi(mRetrofit.getStoreGoodDetail(goodId), object : BaseMvpObserver<StoreGoodDetail>(view) {
            override fun onSuccess(response: StoreGoodDetail) {
                view.bindData(response)
            }
        })
    }


    fun addCart(goodId: Int, goodNum: Int, deliveryType: Int) {
        val apply = StoreAddCart().apply {
            good_id = goodId
            good_num = goodNum
            delivery_type = deliveryType
        }
        requestApi(mRetrofit.addToStoreCart(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                ToastUtils.showShort(R.string.successful_operation)
                view.addSuccess()
            }
        })

    }
}