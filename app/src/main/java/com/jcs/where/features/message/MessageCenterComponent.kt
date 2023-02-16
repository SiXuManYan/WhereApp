package com.jcs.where.features.message

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.UnReadMessage
import com.jcs.where.storage.entity.User

/**
 * Created by Wangsw  2023/2/13 15:55.
 *
 */
interface MessageCenterView : BaseMvpView {
    fun bindUnreadMessageCount(apiUnreadMessageCount: Int)
}

class MessageCenterPresenter(private var view: MessageCenterView):BaseMvpPresenter(view){

    fun getUnreadMessageCount() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.unreadMessageCount, object : BaseMvpObserver<UnReadMessage>(view,false) {
            override fun onSuccess(response: UnReadMessage) {
                val apiUnreadMessageCount = response.count
                view.bindUnreadMessageCount(apiUnreadMessageCount)
            }

            override fun onError(e: Throwable) {
                view.bindUnreadMessageCount(0)
            }

        })


    }

}