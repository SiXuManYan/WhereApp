package com.jiechengsheng.city.features.message.cloud

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.MtjClickService
import com.jiechengsheng.city.storage.entity.User

/**
 * Created by Wangsw  2023/1/3 16:53.
 *
 */
interface ConversationView : BaseMvpView {

}

class ConversationPresenter(private var view: ConversationView) :BaseMvpPresenter(view){

    fun mtjSendGood(goodId: Int, shopId: Int) {

        val apply = MtjClickService().apply {
            goods_id = goodId
            shop_id = shopId
            user_id = User.getInstance().id
        }

        requestApi(mRetrofit.mtjClickService(apply),object :BaseMvpObserver<JsonElement>(view){
            override fun onSuccess(response: JsonElement?) {

            }

        })
    }

}