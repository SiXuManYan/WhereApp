package com.jcs.where.features.gourmet.order

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.api.response.gourmet.order.OrderResponse
import com.jcs.where.bean.OrderSubmitChildRequest
import com.jcs.where.bean.OrderSubmitRequest
import com.jcs.where.utils.FeaturesUtil

/**
 * Created by Wangsw  2021/4/20 16:54.
 *
 */
class OrderSubmitPresenter(val view: OrderSubmitView) : BaseMvpPresenter(view) {


    fun submitOrder(arrayList: MutableList<Products>, phoneStr: String) {

        if (FeaturesUtil.isWrongPhoneNumber("63", phoneStr)) {
            return
        }

        val goodIds = ArrayList<OrderSubmitChildRequest>()

        arrayList.forEach {
            val apply = OrderSubmitChildRequest().apply {
                good_id = it.good_data.id
                good_num = it.good_num
            }
            goodIds.add(apply)
        }

        val request = OrderSubmitRequest().apply {
            goods = Gson().toJson(goodIds)
            phone = phoneStr
        }

        requestApi(mRetrofit.orderSubmit(request), object : BaseMvpObserver<List<OrderResponse>>(view) {


            override fun onSuccess(response: List<OrderResponse>) {
                view.bindData(response)
            }
        })

    }


}