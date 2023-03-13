package com.jiechengsheng.city.features.mall.shop.home

import android.widget.Button
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.MallShopCollection
import com.jiechengsheng.city.api.request.MallShopUnCollection
import com.jiechengsheng.city.api.response.mall.request.MallShop

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