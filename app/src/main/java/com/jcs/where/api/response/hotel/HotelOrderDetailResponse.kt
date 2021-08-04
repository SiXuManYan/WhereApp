package com.jcs.where.api.response.hotel

import com.jcs.where.api.response.gourmet.order.PaymentData
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/8/4 15:12.
 *
 */
class HotelOrderDetail {

    /**
     * 订单数据
     */
    var order_data: HotelOrderData? = null

    /**
     * 支付数据
     */
    var payment_data: PaymentData? = null

    /**
     * 酒店数据
     */
    var hotel_data: HotelData? = null

    /**
     * 房间数据
     */
    var room_data: HotelRoomData? = null


}

class HotelOrderData {

    var id = 0
    var trade_no = ""
    var price :BigDecimal = BigDecimal.ZERO

    /**
     * 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功）
     */
    var order_status = 0

    var created_at = ""
    var remark = ""

    /** 是否允许退款（true/false） */
    var is_cancel = false

    /** 评论状态（1-未评论，2-已评论，3-已超时） */
    var comment_status = 0

}


class HotelData {

    var id = 0
    var name = ""
    var images :ArrayList<String> = ArrayList()
    var address = ""
    var mer_uuid = ""
    var mer_name = ""
    var tel = ""
    var lat  = 0f
    var lng  = 0f

    /** IM开启状态（1：开启，2：关闭） */
    var im_status  = 0

}

class HotelRoomData {

    var name = ""
    var room_num = 0

    /** 早餐类型（1：有，2：没有） */
    var breakfast_type = 0

    /** wifi类型（1：免费，2：付费） */
    var wifi_type = 0

    /** 房间类型 */
    var room_type = ""

    /** 入住日期 */
    var start_date = ""

    /** 离店日期 */
    var end_date = ""

    /** 入住人 */
    var username = ""

    /** 入住人电话 */
    var phone = ""

    /** 入住天数 */
    var days = 0




}






