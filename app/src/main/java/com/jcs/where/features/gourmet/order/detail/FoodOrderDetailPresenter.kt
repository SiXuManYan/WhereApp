package com.jcs.where.features.gourmet.order.detail

import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail
import com.jcs.where.utils.Constant
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Wangsw  2021/5/10 9:47.
 *
 */
class FoodOrderDetailPresenter(val view: FoodOrderDetailView) : BaseMvpPresenter(view) {


    fun getDetail(orderId: String) {
        requestApi(mRetrofit.getFoodOrderDetail(orderId), object : BaseMvpObserver<FoodOrderDetail>(view) {
            override fun onSuccess(response: FoodOrderDetail?) {
                response?.let {
                    view.bindDetail(it)
                }
            }
        })
    }


    /**
     * 倒计时
     *
     * @param countdownView 倒计时显示的view
     * @param defaultStr    默认显示的文字
     */
    fun countdown(countdownView: TextView, defaultStr: String) {
        Flowable.intervalRange(0, Constant.WAIT_DELAYS_PAY, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { aLong: Long ->
                    val string = StringUtils.getString(R.string.count_down_format, Constant.WAIT_DELAYS - aLong)
                    countdownView.text = string
                }
                .doOnComplete {
                    countdownView.text = defaultStr
                    view.countdownEnd()
                }.subscribe()
    }

    /**
     * 取消订单
     */
    fun cancelOrder(orderId: String) {
        requestApi(mRetrofit.cancelFoodOrder(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
             view.cancelSuccess()
            }

        })
    }
}