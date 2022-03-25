package com.jcs.where.features.mall.order

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallOrderDetail

/**
 * Created by Wangsw  2021/12/16 11:27.
 *
 */
interface MallOrderDetailView : BaseMvpView {
    fun bindData(data: MallOrderDetail)

    /**
     * 订单取消成功
     */
    fun orderCancelSuccess()

    /** 确认收货成功 */
    fun confirmReceipt()

}

class MallOrderDetailPresenter(private var view: MallOrderDetailView) : BaseMvpPresenter(view) {

    fun getOrderDetail(orderId: Int) {
        requestApi(mRetrofit.mallOrderDetail(orderId), object : BaseMvpObserver<MallOrderDetail>(view) {
            override fun onSuccess(response: MallOrderDetail) {
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
     * 配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货）
     *
     * 配送方式（1:自提，2:商家配送）
     * */

    fun getStatusText(orderStatus: Int): String {


        return when (orderStatus) {
            1 -> {
                StringUtils.getString(R.string.store_status_1)
            }
            2 -> {
                StringUtils.getString(R.string.store_status_2)
            }
            3 -> {
                StringUtils.getString(R.string.store_status_3)
            }
            4 -> {
                StringUtils.getString(R.string.store_status_4_2)
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


    /**
     *
     * 订单状态，
     * 配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货）
     *
     * */
    fun getStatusDescText(status_desc_tv: TextView, orderStatus: Int) {
        when (orderStatus) {
            7 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_7)
                status_desc_tv.visibility = View.VISIBLE
            }
            8 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_8)
                status_desc_tv.visibility = View.VISIBLE
            }
            9 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_9)
                status_desc_tv.visibility = View.VISIBLE
            }
            10 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_10)
                status_desc_tv.visibility = View.VISIBLE
            }
            11 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_11)
                status_desc_tv.visibility = View.VISIBLE
            }
            12 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_12)
                status_desc_tv.visibility = View.VISIBLE
            }
            else -> {
                status_desc_tv.visibility = View.GONE
            }
        }

    }

    fun cancelStoreOrder(orderId: Int) {

        requestApi(mRetrofit.cancelMallOrder(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.orderCancelSuccess()
            }


        })


    }

    fun confirmReceipt(orderId: Int) {
        requestApi(mRetrofit.confirmReceipt(orderId),object :BaseMvpObserver<JsonElement>(view){
            override fun onSuccess(response: JsonElement) {
                view.confirmReceipt()
            }

        })
    }



}