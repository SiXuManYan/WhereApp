package com.jcs.where.api.response.order

import java.math.BigDecimal


open class OrderModelData {


    /** 订单状态（1：待付款，2：待使用，3：待评价，4：已完成，5：已取消，6：退款中，7：退款成功，8：退款失败，9：商家已取消，10：待确认） */
    var order_status = 0

    /** 房间数量 */
    var room_num = 0

    /** 房间类型 */
    var room_type = ""

    /** 入住日期 */
    var start_date = ""

    /** 离店日期 */
    var end_date = ""

    /** 房间价格 */
    var room_price: BigDecimal = BigDecimal.ZERO

    // ######## 美食 ########

    /** 食物图片 */
    var food_image = ""

    /** 食物名称 */
    var food_name = ""

    /**  商品数量 */
    var good_num = 0


    // ####### 外卖 #########

    /** 商品名称 */
    var good_names = ""

    // ######### 商城 #######


    /** 配送方式（1:自提，2:商家配送） */
    var delivery_type = 0

    /** 商城商品信息 */
    var goods: ArrayList<OrderStoreGoods> = ArrayList()

}

/**
 * Created by Wangsw  2021/6/25 14:28.
 *  酒店订单
 */
class OrderHotel {

    /** 订单状态（1：待付款，2：待使用，3：待评价，4：已完成，5：已取消，6：退款中，7：退款成功，8：退款失败，9：商家已取消，10：待确认） */
    var order_status = 0

    /** 房间数量 */
    var room_num = 0

    /** 房间类型 */
    var room_type = ""

    /** 入住日期 */
    var start_date = ""

    /** 离店日期 */
    var end_date = ""

    /** 房间价格 */
    var room_price: BigDecimal = BigDecimal.ZERO

}

/**
 * 美食订单
 */
class OrderFood {

    /** 订单状态（1：待付款，2：已取消，3：待使用，4：已完成，5：支付失败，6：退款中，7：已退款，8：退款失败，9：待评价） */
    var order_status = 0

    /** 食物图片 */
    var food_image = ""

    /** 食物名称 */
    var food_name = ""

    /**  商品数量 */
    var good_num = 0
}

/**
 * 外卖订单
 */
class OrderTakeOut {

    /** 订单状态（1：待支付，2：未接单，3：已接单，4：已取消，5：已完成，6：支付失败，7：退款中，8：已退款，9：退款失败，10：待评价） */
    var order_status = 0

    /** 商品名称 */
    var good_names = ""
}

/**
 * 商城订单
 */
class OrderStore {

    /**
     * 订单状态，
     * 自提时：（1：待付款，2：支付审核中，           4：待使用，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货），
     * 配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货） */
    var order_status = 0

    /** 配送方式（1:自提，2:商家配送） */
    var delivery_type = 0

    /** 商城商品信息 */
    var goods: ArrayList<OrderStoreGoods> = ArrayList()

}

/**
 * 商城商品信息
 */
class OrderStoreGoods {

    /** 商品id */
    var id = 0

    /** 商品名称 */
    var title = ""


    var good_image: ArrayList<String> = ArrayList()
    var good_title = ""
    var good_num = 0
    var price: BigDecimal = BigDecimal.ZERO
}
