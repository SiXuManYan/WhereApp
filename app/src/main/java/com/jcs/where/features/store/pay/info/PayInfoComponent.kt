package com.jcs.where.features.store.pay.info

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.bills.UpLoadBillsPayAccountInfo
import com.jcs.where.api.request.store.UpLoadPayAccountInfo
import java.util.*

/**
 * Created by Wangsw  2021/6/23 17:11.
 *  支付信息
 */
interface PayInfoView : BaseMvpView {
    fun paySuccess()
}

class PayInfoPresenter(private var view: PayInfoView) : BaseMvpPresenter(view) {


    /**
     * 商城支付
     */
    fun upLoadPayAccountInfo(orderIds: ArrayList<Int>, accountName: String, accountNumber: String, id: Int) {

        val apply = UpLoadPayAccountInfo().apply {
            order_ids = Gson().toJson(orderIds)
            bank_card_account = accountName
            bank_card_number = accountNumber
            card_id = id
        }
        requestApi(mRetrofit.upLoadPayAccountInfo(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.paySuccess()
            }
        })

    }


    /**
     * 水电支付
     */
    fun upLoadBillsPayAccountInfo(orderIds: ArrayList<Int>, accountName: String, accountNumber: String, id: Int) {

        if (orderIds.isEmpty()) {
            return
        }

        val apply = UpLoadBillsPayAccountInfo().apply {
            order_id =  orderIds[0]
            bank_card_account = accountName
            bank_card_number = accountNumber
            card_id = id
        }
        requestApi(mRetrofit.upLoadBillsPayAccountInfo(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.paySuccess()
            }
        })

    }


    /**
     * 美食支付
     */
    fun upLoadFoodPayAccountInfo(orderIds: ArrayList<Int>, accountName: String, accountNumber: String, id: Int) {

        val apply = UpLoadPayAccountInfo().apply {
            order_ids = Gson().toJson(orderIds)
            bank_card_account = accountName
            bank_card_number = accountNumber
            card_id = id
        }
        requestApi(mRetrofit.upLoadFoodPayAccountInfo(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.paySuccess()
            }
        })

    }

    /**
     * 外卖支付
     */
    fun upLoadTakeawayPayAccountInfo(orderIds: ArrayList<Int>, accountName: String, accountNumber: String, id: Int) {

        if (orderIds.isEmpty()) {
            return
        }

        val apply = UpLoadBillsPayAccountInfo().apply {
            order_id =  orderIds[0]
            bank_card_account = accountName
            bank_card_number = accountNumber
            card_id = id
        }
        requestApi(mRetrofit.upLoadTakeawayPayAccountInfo(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.paySuccess()
            }
        })

    }

    /**
     * 酒店支付
     */
    fun upLoadHotelPayAccountInfo(orderIds: ArrayList<Int>, accountName: String, accountNumber: String, id: Int) {

        if (orderIds.isEmpty()) {
            return
        }

        val apply = UpLoadBillsPayAccountInfo().apply {
            order_id =  orderIds[0]
            bank_card_account = accountName
            bank_card_number = accountNumber
            card_id = id
        }
        requestApi(mRetrofit.upLoadHotelPayAccountInfo(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.paySuccess()
            }
        })

    }







}

