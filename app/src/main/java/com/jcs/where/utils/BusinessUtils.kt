package com.jcs.where.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.features.store.refund.image.StoreRefundAdapter2
import com.jcs.where.storage.entity.User
import io.rong.imkit.RongIM
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.model.Conversation
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

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
     * 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待使用，6-交易成功，7-退款中，8-退款成功 9-商家审核中 10-拒绝售后 11-退款失败）
     */
    fun getDelicacyOrderStatusText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.store_status_1)
        2 -> StringUtils.getString(R.string.store_status_2)
        3 -> StringUtils.getString(R.string.store_status_6)
        4 -> StringUtils.getString(R.string.store_status_13)
        5 -> StringUtils.getString(R.string.status_to_use)
        6 -> StringUtils.getString(R.string.store_status_5)
        7 -> StringUtils.getString(R.string.store_status_8)
        8 -> StringUtils.getString(R.string.store_status_9)
        9 -> StringUtils.getString(R.string.under_review)
        10 -> StringUtils.getString(R.string.refuses_to_return)
        11 -> StringUtils.getString(R.string.refund_failed)
        else -> ""
    }

    /**
     * 美食描述文案
     * 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待使用，6-交易成功，7-退款中，8-退款成功 9-商家审核中 10-拒绝售后 11-退款失败）
     */
    fun getDelicacyOrderStatusDesc(status: Int): String = when (status) {
        4 -> StringUtils.getString(R.string.store_status_desc_13)
        7 -> StringUtils.getString(R.string.store_status_desc_8)
        8 -> StringUtils.getString(R.string.store_status_desc_9)
        9 -> StringUtils.getString(R.string.store_status_desc_10)
        10 -> StringUtils.getString(R.string.store_status_desc_12)
        11 -> StringUtils.getString(R.string.refund_fail_desc)
        else -> StringUtils.getString(R.string.line_split)
    }


    /**
     * 外卖订单状态文案
     * 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待接单，6-已接单，7-待收货，8-交易成功，9-退款中，10-退款成功
     *        11-商家审核中 12-拒绝售后 13-退款失败
     * ）
     */
    fun getTakeawayStatusText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.store_status_1)
        2 -> StringUtils.getString(R.string.store_status_2)
        3 -> StringUtils.getString(R.string.store_status_6)
        4 -> StringUtils.getString(R.string.store_status_13)
        5 -> StringUtils.getString(R.string.status_pending)
        6 -> StringUtils.getString(R.string.status_pended)
        7 -> StringUtils.getString(R.string.store_status_4_2)
        8 -> StringUtils.getString(R.string.store_status_5)
        9 -> StringUtils.getString(R.string.store_status_8)
        10 -> StringUtils.getString(R.string.store_status_9)
        11 -> StringUtils.getString(R.string.under_review)
        12 -> StringUtils.getString(R.string.refuses_to_return)
        13 -> StringUtils.getString(R.string.refund_failed)
        else -> ""
    }


    /**
     * 外卖描述文案
     * 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待接单，6-已接单，7-待收货，8-交易成功，9-退款中，10-退款成功 11-商家审核中 12-拒绝售后 13-退款失败
     */
    fun getTakeawayOrderStatusDesc(status: Int): String = when (status) {
        4 -> StringUtils.getString(R.string.store_status_desc_13)
        9 -> StringUtils.getString(R.string.store_status_desc_8)
        10 -> StringUtils.getString(R.string.store_status_desc_9)
        11 -> StringUtils.getString(R.string.store_status_desc_10)
        12 -> StringUtils.getString(R.string.store_status_desc_12)
        13 -> StringUtils.getString(R.string.refund_fail_desc)
        else -> StringUtils.getString(R.string.line_split)
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
        7 -> StringUtils.getString(R.string.store_status_13)
        8 -> StringUtils.getString(R.string.store_status_8)
        9 -> StringUtils.getString(R.string.store_status_9)
        else -> ""
    }

    fun getSafeStock(inventory: String?): Int {
        return if (inventory.isNullOrBlank()) {
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


    fun getSafeInt(string: String?): Int {

        return if (string.isNullOrBlank()) {
            0
        } else {
            try {
                string.toInt()
            } catch (e: IllegalArgumentException) {
                0
            }
        }
    }

    fun getSafeBigDecimal(string: String?): BigDecimal {

        return if (string.isNullOrBlank()) {
            BigDecimal.ZERO
        } else {
            try {
                BigDecimal(string)
            } catch (e: Exception) {
                BigDecimal.ZERO
            }
        }
    }


    /**
     * 启动融云会话页面(使用内置方法)
     * ConversationActivity
     */
    fun startRongCloudConversationActivity(
        context: Context,
        targetId: String,
        title: String? = null,
        phone: String? = null,
        bd: Bundle? = null,
    ) {

        if (!User.isLogon()) {
            context.startActivity(Intent(context, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            return
        }

        if (targetId.isBlank()) {
            return
        }

        val bundle = bd ?: Bundle()
        bundle.apply {
            title?.let {
                putString(Constant.PARAM_TITLE, title)
            }
            phone?.let {
                putString(Constant.PARAM_PHONE, it)
            }
        }
        RouteUtils.routeToConversationActivity(context, Conversation.ConversationType.PRIVATE, targetId, bundle)
    }


    fun showRule(context: Context, rule: String) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_coupon_rule, null, false)
        val dialog = AlertDialog.Builder(context).setView(view).create()
        dialog.setCancelable(false)

        val title_tv = view.findViewById<TextView>(R.id.title_tv)
        val content_tv = view.findViewById<TextView>(R.id.content_tv)
        val contconfirm_tvent_tv = view.findViewById<TextView>(R.id.confirm_tv)

        title_tv.setText(R.string.rules_of_use)
        content_tv.text = rule
        contconfirm_tvent_tv.setText(R.string.sure)
        contconfirm_tvent_tv.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setLayout(ScreenUtils.getScreenWidth() / 10 * 9, LinearLayout.LayoutParams.WRAP_CONTENT)
    }


    /**
     * 设置格式化金额
     *
     * @param textView
     * @param text1       单位
     * @param text2       金额
     * @param text1DpSize 单位字体大小 dp
     * @param text2DpSize 金额字体大小 dp
     */
    fun setFormatText(textView: TextView, text1: String, text2: String, text1DpSize: Int, text2DpSize: Int) {

        val stringBuilder = SpannableStringBuilder(text1 + text2)
        stringBuilder.setSpan(AbsoluteSizeSpan(text1DpSize, true), 0, text1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        stringBuilder.setSpan(
            AbsoluteSizeSpan(text2DpSize, true),
            text1.length,
            (text1 + text2).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = stringBuilder
    }

    fun loginOut() {

        // 断开融云连接
        RongIM.getInstance().logout()
        User.clearAllUser()
        CacheUtil.saveToken("")

        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_SIGN_OUT))
    }


    /**
     * 商城售后文案
     * 1 待售后 2 商家审核中 3商家待收货 4商家拒绝退货 5退款中 6退款成功 7取消售后
     */
    fun getMallGoodRefundButtonText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.apply_refund)
        2 -> StringUtils.getString(R.string.under_review)
        3 -> StringUtils.getString(R.string.shops_to_receive)
        4 -> StringUtils.getString(R.string.refuses_to_return)
        5 -> StringUtils.getString(R.string.refunding)
        6 -> StringUtils.getString(R.string.refunded_success)
        7 -> StringUtils.getString(R.string.apply_refund)
        8 -> StringUtils.getString(R.string.refund_failed)
        else -> ""
    }

    /**
     * 商城商品后状态文案
     * 1 待售后 2 商家审核中 3商家待收货 4商家拒绝退货 5退款中 6退款成功 7取消售后 8 退款失败
     */
    fun getMallGoodRefundStatusText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.waiting_after_sales)
        2 -> StringUtils.getString(R.string.under_review)
        3 -> StringUtils.getString(R.string.shops_to_receive)
        4 -> StringUtils.getString(R.string.refuses_to_return)
        5 -> StringUtils.getString(R.string.refunding)
        6 -> StringUtils.getString(R.string.refunded_success)
        7 -> StringUtils.getString(R.string.cancellation_after_sale)
        8 -> StringUtils.getString(R.string.refund_failed)
        else -> ""
    }

    /**
     * 商品售后状态描述
     * 1 待售后 2 商家审核中 3商家待收货 4商家拒绝退货 5退款中 6退款成功 7取消售后 8 退款失败
     */
    fun getMallGoodRefundStatusDescText(status: Int): String = when (status) {
        2 -> StringUtils.getString(R.string.store_status_desc_10)
        3 -> StringUtils.getString(R.string.shops_to_receive_desc)
        4 -> StringUtils.getString(R.string.store_status_desc_12)
        5 -> StringUtils.getString(R.string.store_status_desc_8)
        6 -> StringUtils.getString(R.string.store_status_desc_9)
        8 -> StringUtils.getString(R.string.refund_fail_desc)
        else -> ""
    }

    /**
     * 获取相册内的图片资源
     */
    fun getImageImageUrls(adapter: StoreRefundAdapter2): ArrayList<String> {
        val imageUrl = ArrayList<String>()
        adapter.data.forEach {
            if (it.type != RefundImage.TYPE_ADD) {
                imageUrl.add(it.imageSource)
            }
        }
        return imageUrl
    }

    fun isBankChannel(channel: String?): Boolean = channel == "BANK"

}