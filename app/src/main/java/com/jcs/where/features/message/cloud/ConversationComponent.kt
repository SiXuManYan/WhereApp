package com.jcs.where.features.message.cloud

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.MtjClickService
import com.jcs.where.storage.entity.User

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