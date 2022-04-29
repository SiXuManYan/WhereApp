package com.jcs.where.api.response.order

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * create by zyf on 2020/12/11 7:57 PM
 */
class OrderListResponse : MultiItemEntity {
    /**
     * 订单ID
     */
    var id = 0

    var order_type = 0

    override val itemType: Int
        get() =  order_type



    /**
     * 订单号
     */
    var trade_no: String= ""

    /**
     * 模块ID,如酒店模块，则为hotel_id
     */
    var model_id: Int = 0

    /**
     * 订单标题
     */
    var title: String = ""

    /**
     * 创建时间
     */
    var created_at: String = ""

    /**
     * 订单价格
     */
    var price = BigDecimal.ZERO

    /**
     * 模块数据
     */
    //    public ModelDataDTO model_data;
    var model_data: OrderModelData ? = null

    /**
     * 订单图片
     */
    var image: List<String> = ArrayList()

    var shop_title = ""

    var shop_images = ArrayList<String>()

    /** mall estore商城 商品总数*/
    var num = 0


    var pay_time  = 0L


    companion object {
        /**
         * 订单类型：酒店订单
         */
        const val ORDER_TYPE_HOTEL_1 = 1

        /**
         * 订单类型：餐饮-堂食
         */
        const val ORDER_TYPE_DINE_2 = 2

        /**
         * 订单类型：餐饮-外卖
         */
        const val ORDER_TYPE_TAKEAWAY_3 = 3

        /**
         * 订单类型：estore
         */
        const val ORDER_TYPE_STORE_4 = 4

        /**
         * 订单类型：新版estore
         */
        const val ORDER_TYPE_STORE_MALL_5 = 5
    }
}