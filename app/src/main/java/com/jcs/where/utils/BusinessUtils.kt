package com.jcs.where.utils

import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R

/**
 * Created by Wangsw  2021/7/21 17:10.
 *
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
    fun getDelicacyOrderStatusText(status: Int) = when (status) {

        1 -> {
            StringUtils.getString(R.string.store_status_1)
        }
        2 -> {
            StringUtils.getString(R.string.store_status_2)
        }
        3 -> {
            StringUtils.getString(R.string.store_status_6)
        }
        4 -> {
            StringUtils.getString(R.string.store_status_7)
        }
        5 -> {
            StringUtils.getString(R.string.status_to_use)
        }
        6 -> {
            StringUtils.getString(R.string.store_status_5)
        }
        7 -> {
            StringUtils.getString(R.string.store_status_8)
        }
        8 -> {
            StringUtils.getString(R.string.store_status_9)
        }
        else -> {
            ""
        }


    }
}