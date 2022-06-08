package com.jcs.where.features.mall.shop.home

import android.widget.Button
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.MallShopCollection
import com.jcs.where.api.request.MallShopUnCollection
import com.jcs.where.api.response.mall.request.MallShop

/**
 * Created by Wangsw  2022/1/19 17:22.
 *
 */
interface MallShopHomeView :BaseMvpView {
    fun bindDetail(response: MallShop){}
    fun collectionHandleSuccess(collectionStatus: Boolean)

}

class MallShopHomePresenter(private var view: MallShopHomeView):BaseMvpPresenter(view){

    /**
     * 店铺详情
     */
    fun getDetail(shopId :Int){
        requestApi(mRetrofit.mallShopDetail(shopId),object :BaseMvpObserver<MallShop>(view){
            override fun onSuccess(response: MallShop) {
                view.bindDetail(response)
            }
        })
    }



    fun collection(shopId: Int, follow_bt: Button) {
        follow_bt.isClickable = false
        val request = MallShopCollection().apply {
            shop_id = shopId
        }

        requestApi(mRetrofit.mallShopCollection(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(true)
                follow_bt.isClickable = true
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
                follow_bt.isClickable = true
            }
        })
    }

    fun unCollection(shopId: Int, follow_bt: Button) {
        follow_bt.isClickable = false
        val array = ArrayList<Int>()
        array.add(shopId)
        val request = MallShopUnCollection().apply {
            shop_id = Gson().toJson(array)
        }
        requestApi(mRetrofit.mallShopUnCollection(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(false)
                follow_bt.isClickable = true
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
                follow_bt.isClickable = true
            }
        })
    }
}

class CollectionHandle{
    var fansCount = 0

    /** 收藏状态（0：未收藏，1：已收藏） */
    var status = 0
}