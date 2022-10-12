package com.jcs.where.features.bills.place

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillsOrderDiscount
import com.jcs.where.api.response.bills.BillsPlaceOrder
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse

/**
 * Created by Wangsw  2022/6/9 14:56.
 *
 */
interface BillsPlaceOrderView : BaseMvpView {
    fun commitSuccess(response: HotelOrderCommitResponse)
    fun bindOrderDiscount(response: BillsOrderDiscount)
}

class BillsPlaceOrderPresenter(private var view: BillsPlaceOrderView) : BaseMvpPresenter(view) {


    /**
     * 获取折扣以及最终支付价格
     * @param module       1-话费，2-水费，3-电费，4-网费
     * @param oldPrice   原价
     * @param payAccount 充值手机号
     * @param couponId 优惠券id
     */
    fun billsOrderDiscount(module: Int, oldPrice: String, payAccount: String, couponId: Int?) {

        requestApi(mRetrofit.billsOrderDiscount(module, oldPrice, payAccount, couponId),
            object : BaseMvpObserver<BillsOrderDiscount>(view) {
                override fun onSuccess(response: BillsOrderDiscount) {

                    view.bindOrderDiscount(response)
                }

            })

    }


    fun placeOrder(billerTag: String, firstField: String, secondField: String, money: String, billType: Int,couponId:Int) {

        if (money.isBlank()) {
            return
        }

        val apply = BillsPlaceOrder().apply {
            biller_tag = billerTag
            first_field = firstField
            second_field = secondField
            amount = money
            bill_type = billType
            coupon_id = couponId
        }

        requestApi(mRetrofit.billsPlaceOrder(apply), object : BaseMvpObserver<HotelOrderCommitResponse>(view) {
            override fun onSuccess(response: HotelOrderCommitResponse) {
                view.commitSuccess(response)
            }

        })

    }
}