package com.jiechengsheng.city.features.store.good

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.request.store.StoreAddCart
import com.jiechengsheng.city.api.response.store.StoreGoodDetail

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