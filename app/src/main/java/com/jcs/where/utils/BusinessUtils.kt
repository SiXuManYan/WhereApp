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
}