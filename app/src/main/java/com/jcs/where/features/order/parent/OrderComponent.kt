package com.jcs.where.features.order.parent

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.order.tab.OrderTabResponse
import com.jcs.where.storage.entity.User

/**
 * Created by Wangsw  2021/7/30 10:58.
 *
 */
interface OrderView : BaseMvpView {
    fun bindTab(response: ArrayList<OrderTabResponse>, titles: ArrayList<String>)
}

class OrderPresenter(private var view: OrderView) : BaseMvpPresenter(view) {


    fun getTabs() {
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.orderTabs, object : BaseMvpObserver<ArrayList<OrderTabResponse>>(view) {
            override fun onSuccess(response: ArrayList<OrderTabResponse>) {

                val titles: ArrayList<String> = ArrayList()
                response.forEach {
                    it.title = it.title + "(" + it.count + ")"
                    titles.add( it.title)
                }
                view.bindTab(response, titles)
            }

        })
    }

}