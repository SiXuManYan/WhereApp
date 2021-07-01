package com.jcs.where.api.response.order.store

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/26 10:53.
 * 商城订单详情
 */
class StoreOrderDetail {


    /** 订单ID */
    var id = 0

    /** 订单号 */
    var trade_no = ""

    /** 订单时间 */
    var created_at = ""

    /** 订单备注 */
    var remark = ""

    /** 是否支持退货 */
    var is_cancel = 0

    /**
     * 订单状态，
     * 自提时：（1：待付款，2：支付审核中，           4：待自提，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货），
     * 配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货） */
    var status = 0

    /** 订单总额 */
    var price = BigDecimal.ZERO

    /** 配送费 */
    var delivery_fee = BigDecimal.ZERO

    /** 商品价格 */
    var good_price = BigDecimal.ZERO

    /** 配送方式（1:自提，2:商家配送） */
    var delivery_type = 0

    /** 配送时间 配送时间（配送时）*/
    var delivery_times = ""

    /** 自提时间（自提时） */
    var take_times = ""

    /** 电话（自提时） */
    var phone = ""


    /** 地址信息（配送时） */
    var address: StoreOrderAddress? = null

    /** 商家信息 */
    var shop: StoreOrderShop? = null

    /** 支付渠道 */
    var pay_channel = ""

    /** 支付银行户头 */
    var bank_card_account = ""

    /** 支付银行账号 */
    var bank_card_number = ""


}

/**
 * 地址信息（配送时）
 */
class StoreOrderAddress {
    var sex = 1
    var address = ""
    var contact_name = ""
    var contact_number = ""
}


/**
 * 	商家信息
 */
class StoreOrderShop {

    var id = 0
    var title = ""
    var images: ArrayList<String> = ArrayList()
    var goods: ArrayList<StoreOrderShopGoods> = ArrayList()
}


class StoreOrderShopGoods() : Parcelable {


    /** 商品id */
    var id = 0

    /** 商品名称 */
    var title: String? = ""


    var images: ArrayList<String> = ArrayList()
    var good_num = 0
    var price: Double = 0.0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        good_num = parcel.readInt()
        price = parcel.readDouble()
        //
        images = parcel.readSerializable() as ArrayList<String>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(good_num)
        parcel.writeDouble(price)
        //
        parcel.writeSerializable(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoreOrderShopGoods> {
        override fun createFromParcel(parcel: Parcel): StoreOrderShopGoods {
            return StoreOrderShopGoods(parcel)
        }

        override fun newArray(size: Int): Array<StoreOrderShopGoods?> {
            return arrayOfNulls(size)
        }
    }


}