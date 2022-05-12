package com.jcs.where.features.gourmet.refund.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.FoodRefundInfo
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_info_food.*

/**
 *
 * Created by Wangsw  2022/5/12 15:39.
 *  美食外卖退款信息详情
 */
class FoodRefundInfoActivity : BaseMvpActivity<FoodRefundInfoPresenter>(), FoodRefundInfoView {


    private var orderId = 0

    /** 1 美食 2外卖 */
    private var type = 0


    companion object {

        var TYPE_FOOD = 1
        var TYPE_TAKEAWAY = 2


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
            val bankChannel = BusinessUtils.isBankChannel(refundMethod.channel_name)
            if (bankChannel) {
                refund_name_tv.text = refundMethod.bank_all_name
            } else {
                refund_name_tv.text = refundMethod.channel_name
            }
            refund_user_name_tv.text = refundMethod.user_name
            refund_account_tv.text = refundMethod.account
        }
        refund_price_tv.text = StringUtils.getString(R.string.price_unit_format,response.price)
        total_price_tv.text = StringUtils.getString(R.string.price_unit_format,response.total_price)
        time_tv.text = response.cancel_time
    }


}


interface FoodRefundInfoView : BaseMvpView {
    fun bindData(response: FoodRefundInfo)

}


class FoodRefundInfoPresenter(var view: FoodRefundInfoView) : BaseMvpPresenter(view) {


    fun getData(orderId: Int, type: Int) {
        requestApi(mRetrofit.getFoodRefundInfo(orderId, type), object : BaseMvpObserver<FoodRefundInfo>(view) {
            override fun onSuccess(response: FoodRefundInfo) {
                view.bindData(response)
            }
        })

    }

}
