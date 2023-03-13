package com.jiechengsheng.city.features.gourmet.refund.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.mall.FoodRefundInfo
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_info_food.*

/**
 *
 * Created by Wangsw  2022/5/12 15:39.
 *  美食外卖退款信息详情
 */
class FoodRefundInfoActivity : BaseMvpActivity<FoodRefundInfoPresenter>(), FoodRefundInfoView {


    private var orderId = 0

    /** 1 美食 2外卖 3酒店 4账单*/
    private var type = 0


    companion object {

        fun navigation(context: Context, orderId: Int, type: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, orderId)
                putInt(Constant.PARAM_TYPE, type)
            }
            val intent = Intent(context, FoodRefundInfoActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_info_food

    override fun initView() {
        initExtra()
    }

    private fun initExtra() {
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
            type = it.getInt(Constant.PARAM_TYPE)
        }
    }

    override fun initData() {
        presenter = FoodRefundInfoPresenter(this)
        presenter.getData(orderId, type)
    }

    override fun bindListener() {

    }

    override fun bindData(response: FoodRefundInfo) {
        // 退款方式
        val refundMethod = response.remit_info
        refundMethod?.let {
            refund_name_tv.text = refundMethod.name
            refund_user_name_tv.text = refundMethod.user_name
            refund_account_tv.text = refundMethod.account
        }
        refund_price_tv.text = StringUtils.getString(R.string.price_unit_format, response.price)
        total_price_tv.text = StringUtils.getString(R.string.price_unit_format, response.total_price)
        time_tv.text = response.cancel_time
    }


}


interface FoodRefundInfoView : BaseMvpView {
    fun bindData(response: FoodRefundInfo)

}


class FoodRefundInfoPresenter(var view: FoodRefundInfoView) : BaseMvpPresenter(view) {


    companion object {

        var TYPE_FOOD = 1
        var TYPE_TAKEAWAY = 2
        var TYPE_HOTEL = 3
        var TYPE_BILL = 4
    }

    fun getData(orderId: Int, type: Int) {
        when (type) {
            TYPE_FOOD,
            TYPE_TAKEAWAY,
            -> {
                requestApi(mRetrofit.getFoodRefundInfo(orderId, type), object : BaseMvpObserver<FoodRefundInfo>(view) {
                    override fun onSuccess(response: FoodRefundInfo) {
                        view.bindData(response)
                    }
                })
            }
            TYPE_HOTEL -> {
                requestApi(mRetrofit.getHotelRefundInfo(orderId), object : BaseMvpObserver<FoodRefundInfo>(view) {
                    override fun onSuccess(response: FoodRefundInfo) {
                        view.bindData(response)
                    }
                })
            }
            TYPE_BILL -> {
                requestApi(mRetrofit.getBillRefundInfo(orderId), object : BaseMvpObserver<FoodRefundInfo>(view) {
                    override fun onSuccess(response: FoodRefundInfo) {
                        view.bindData(response)
                    }
                })
            }

            else -> {}
        }


    }

}
