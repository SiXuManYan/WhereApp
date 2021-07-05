package com.jcs.where.features.store.order.detail

import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.order.store.StoreOrderDetail

/**
 * Created by Wangsw  2021/6/26 11:16.
 * 商城订单详情
 */
interface StoreOrderDetailView : BaseMvpView {
    fun bindData(response: StoreOrderDetail)
}


/**
 * 商城订单详情
 */
class StoreOrderDetailPresenter(private var view: StoreOrderDetailView) : BaseMvpPresenter(view) {


    fun getOrderDetail(orderId: Int) {

        requestApi(mRetrofit.getStoreOrderDetail(orderId), object : BaseMvpObserver<StoreOrderDetail>(view) {
            override fun onSuccess(response: StoreOrderDetail) {
                view.bindData(response)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
            }
        })
    }


    /**
     * 订单状态，
     *
     * 订单状态，
     * 自提时：（1：待付款，2：支付审核中，           4：待使用，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货），
     * 配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货）
     *
     * */

    fun getStatusText(deliveryType: Int, orderStatus: Int): String {


        return when (orderStatus) {
            1 -> {
                StringUtils.getString(R.string.store_status_1)
            }
            2 -> {
                StringUtils.getString(R.string.store_status_2)
            }
            3 -> {
                if (deliveryType == 2) {
                    StringUtils.getString(R.string.store_status_3)
                } else {
                    ""
                }
            }
            4 -> {
                if (deliveryType == 2) {
                    StringUtils.getString(R.string.store_status_4_2)
                } else {
                    StringUtils.getString(R.string.store_status_4_1)
                }
            }
            5 -> {
                StringUtils.getString(R.string.store_status_5)
            }
            6 -> {
                StringUtils.getString(R.string.store_status_6)
            }
            7 -> {
                StringUtils.getString(R.string.store_status_7)
            }
            8 -> {
                StringUtils.getString(R.string.store_status_8)
            }
            9 -> {
                StringUtils.getString(R.string.store_status_9)
            }
            10 -> {
                StringUtils.getString(R.string.store_status_10)
            }
            11 -> {
                StringUtils.getString(R.string.store_status_11)
            }
            12 -> {
                StringUtils.getString(R.string.store_status_12)
            }

            else -> ""
        }


    }

    fun getStatusDescText(status_desc_tv: TextView, orderStatus: Int): String {
        var text = ""
        when (orderStatus) {
            8 -> {
                text = StringUtils.getString(R.string.store_status_desc_8)
            }
            9 -> {
                text = StringUtils.getString(R.string.store_status_desc_9)
            }
            10 -> {
                text = StringUtils.getString(R.string.store_status_desc_10)
            }
            11 -> {
                text = StringUtils.getString(R.string.store_status_desc_11)
            }
        }

        return text


    }
}