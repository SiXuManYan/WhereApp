package com.jcs.where.utils

import android.widget.EditText
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R

/**
 * Created by Wangsw  2021/7/21 17:10.
 * 业务逻辑统一处理
 */
object BusinessUtils {


    fun getCommentRatingText(rating: Int): String = when (rating) {
        0, 1 -> StringUtils.getString(R.string.comment_rating_1)
        2 -> StringUtils.getString(R.string.comment_rating_2)
        3 -> StringUtils.getString(R.string.comment_rating_3)
        4 -> StringUtils.getString(R.string.comment_rating_4)
        5 -> StringUtils.getString(R.string.comment_rating_5)
        else -> StringUtils.getString(R.string.comment_rating_5)
    }

    /**
     * 美食订单状态文案
     * 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待使用，6-交易成功，7-退款中，8-退款成功）
     */
    fun getDelicacyOrderStatusText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.store_status_1)
        2 -> StringUtils.getString(R.string.store_status_2)
        3 -> StringUtils.getString(R.string.store_status_6)
        4 -> StringUtils.getString(R.string.store_status_7)
        5 -> StringUtils.getString(R.string.status_to_use)
        6 -> StringUtils.getString(R.string.store_status_5)
        7 -> StringUtils.getString(R.string.store_status_8)
        8 -> StringUtils.getString(R.string.store_status_9)
        else -> ""
    }

    /**
     * 外卖订单状态文案
     * 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待接单，6-已接单，7-待收货，8-交易成功，9-退款中，10-退款成功）
     */
    fun getTakeawayStatusText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.store_status_1)
        2 -> StringUtils.getString(R.string.store_status_2)
        3 -> StringUtils.getString(R.string.store_status_6)
        4 -> StringUtils.getString(R.string.store_status_7)
        5 -> StringUtils.getString(R.string.status_pending)
        6 -> StringUtils.getString(R.string.status_pended)
        7 -> StringUtils.getString(R.string.store_status_4_2)
        8 -> StringUtils.getString(R.string.store_status_5)
        9 -> StringUtils.getString(R.string.store_status_8)
        10 -> StringUtils.getString(R.string.store_status_9)
        else -> ""
    }


    /**
     * 酒店订单状态文案
     * 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功）
     *
     */
    fun getHotelStatusText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.store_status_1)
        2 -> StringUtils.getString(R.string.store_status_2)
        3 -> StringUtils.getString(R.string.status_shops_confirming)
        4 -> StringUtils.getString(R.string.status_to_use)
        5 -> StringUtils.getString(R.string.store_status_5)
        6 -> StringUtils.getString(R.string.store_status_6)
        7 -> StringUtils.getString(R.string.store_status_7)
        8 -> StringUtils.getString(R.string.store_status_8)
        9 -> StringUtils.getString(R.string.store_status_9)
        else -> ""
    }

    fun getSafeStock(inventory:String?):Int{
      return  if (inventory.isNullOrBlank()) {
            99
        } else {
            try {
                val value = inventory.toInt()
                if (value < 0) {
                    0
                } else {
                    value
                }

            } catch (e: IllegalArgumentException) {
                0
            }
        }
    }





}