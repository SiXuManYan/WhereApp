package com.jiechengsheng.city.features.payment.counter

import com.google.gson.Gson
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.payment.PayUrl
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.api.response.pay.PayChannelBindUrl
import com.jiechengsheng.city.api.response.pay.PayChannelUnbind
import com.jiechengsheng.city.api.response.pay.PayCounterChannel
import com.jiechengsheng.city.api.response.pay.PayCounterChannelDetail

/**
 * Created by Wangsw  2023/3/7 16:08.
 *
 */
interface PayCounterView : BaseMvpView {
    fun bindPayCounter(response: MutableList<PayCounterChannel>){}
    fun bindChannelBalance(response: PayCounterChannelDetail){}
    fun setBindTokenUrl(authH5Url: String){}

    /**
     * 支付完成
     */
    fun payFinish(redirectUrl: String){}

}

class PayCounterPresenter(private var view: PayCounterView) : BaseMvpPresenter(view) {


    /**
     * 支付渠道列表
     */
    fun getChannel() {
        requestApi(mRetrofit.payCounterChannel, object : BaseMvpObserver<ArrayList<PayCounterChannel>>(view) {
            override fun onSuccess(response: ArrayList<PayCounterChannel>) {
                view.bindPayCounter(response.toMutableList())
            }

        })
    }

    /**
     * 获取支付渠道余额
     */
    fun getChannelBalance(channelCode:String) {
        requestApi(mRetrofit.getChannelBalance(channelCode), object : BaseMvpObserver<PayCounterChannelDetail>(view) {
            override fun onSuccess(response: PayCounterChannelDetail) {
                view.bindChannelBalance(response)
            }
        })
    }

    /**
     * 获取绑定渠道Token 链接
     */
    fun getBindTokenUrl(channelCode:String) {

        val apply = PayChannelUnbind().apply {
            channel_code = channelCode
        }

        requestApi(mRetrofit.getBindTokenUrl(apply), object : BaseMvpObserver<PayChannelBindUrl>(view) {
            override fun onSuccess(response: PayChannelBindUrl) {
                view.setBindTokenUrl(response.auth_h5_url)
            }

        })
    }


    /**
     *
     * @param paymentMethod 支付方式（一次性支付: ONE_TIME_PAYMENT，令牌支付: TOKENIZED_PAYMENT)
     * @param channelCode 支付渠道编码
     */
    fun doWherePay(moduleType: String, orderIds: ArrayList<Int> ,channelCode:String ,  paymentMethod:String ) {

        val apply = PayUrlGet().apply {
            module =   moduleType
            order_ids = Gson().toJson(orderIds)

            payment_method = paymentMethod
            channel_code = channelCode
        }



        requestApi(mRetrofit.doWherePay(apply),object :BaseMvpObserver<PayUrl>(view){
            override fun onSuccess(response: PayUrl) {
                view.payFinish(response.redirectUrl)
            }


        })
    }



}