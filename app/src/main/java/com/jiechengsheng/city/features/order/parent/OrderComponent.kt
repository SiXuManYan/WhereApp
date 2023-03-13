package com.jiechengsheng.city.features.order.parent

import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.order.tab.OrderTabResponse
import com.jiechengsheng.city.storage.entity.User

/**
 * Created by Wangsw  2021/7/30 10:58.
 *
 */
interface OrderView : BaseMvpView {
    fun bindTab(response: ArrayList<OrderTabResponse>, titles: ArrayList<String>, isInit: Boolean)
    fun getTabError()
}

class OrderPresenter(private var view: OrderView) : BaseMvpPresenter(view) {


    fun getTabs(isInit: Boolean) {
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
                view.bindTab(response, titles,isInit)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
                view.getTabError()

            }

        })
    }

}