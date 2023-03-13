package com.jiechengsheng.city.features.message

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.UnReadMessage
import com.jiechengsheng.city.storage.entity.User

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