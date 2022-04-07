package com.jcs.where.features.bills.hydropower.detail

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.order.bill.BillOrderDetails

/**
 * Created by Wangsw  2021/7/20 10:49.
 *
 */
interface BillsDetailView : BaseMvpView {
    fun bindDetail(response: BillOrderDetails)


}

class BillsDetailPresenter(private var view: BillsDetailView) : BaseMvpPresenter(view) {

    fun getDetail(orderId: Int) {

        requestApi(mRetrofit.billOrderDetail(orderId), object : BaseMvpObserver<BillOrderDetails>(view) {
            override fun onSuccess(response: BillOrderDetails) {
                view.bindDetail(response)
            }

        })

    }

    /**
     * 订单状态（1-支付审核中，2-缴费中，3-缴费成功，4-订单关闭，5-退款中，6-退款成功）
     */
    fun getStatus(status: Int): String {
        return when (status) {
            1 -> {
                StringUtils.getString(R.string.bill_status_1)
            }
            2 -> {
                StringUtils.getString(R.string.bill_status_2)
            }
            3 -> {
                StringUtils.getString(R.string.bill_status_3)
            }
            4 -> {
                StringUtils.getString(R.string.bill_status_4)
            }
            5 -> {
                StringUtils.getString(R.string.bill_status_5)
            }
            6 -> {
                StringUtils.getString(R.string.bill_status_6)
            }
            else -> ""
        }
    }


    /**
     * 订单状态（4-订单关闭，5-退款中，6-退款成功）
     */
    fun getStatusDescText(status_desc_tv: TextView, status: Int) {

        when (status) {
            4 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_13)
                status_desc_tv.visibility = View.VISIBLE
            }
            5 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_8)
                status_desc_tv.visibility = View.VISIBLE
            }
            6 -> {
                status_desc_tv.text = StringUtils.getString(R.string.store_status_desc_9)
                status_desc_tv.visibility = View.VISIBLE
            }
            else -> {
                status_desc_tv.visibility = View.GONE
            }
        }
    }

}

