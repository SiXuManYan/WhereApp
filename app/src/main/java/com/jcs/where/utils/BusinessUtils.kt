package com.jcs.where.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.BuildConfig
import com.jcs.where.R
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.home.tag.HomeTagAdapter
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.hotel.detail.media.MediaData
import com.jcs.where.features.job.detail.JobDetailActivity
import com.jcs.where.features.job.main.JobMainActivity
import com.jcs.where.features.job.time.WorkTimeAdapter
import com.jcs.where.features.job.time.WorkTimeUtil
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.features.mall.shop.home.MallShopHomeActivity
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.features.store.refund.image.StoreRefundAdapter2
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.features.web.WebViewActivity
import com.jcs.where.frames.common.Html5Url
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.view.MyLayoutManager
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import io.rong.imkit.RongIM
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.model.Conversation
import me.shaohui.bottomdialog.BottomDialog
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.text.DecimalFormat


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
     * 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功, 10-商家审核中, 11-商家拒绝售后, 12-退款失败）
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
        10 -> StringUtils.getString(R.string.store_status_10)
        11 -> StringUtils.getString(R.string.refuses_to_return)
        12 -> StringUtils.getString(R.string.refund_failed)
        else -> ""
    }


    /**
     * 酒店售后状态描述
     * 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功, 10-商家审核中, 11-商家拒绝售后, 12-退款失败）
     */
    fun getHotelStatusDescText(status: Int): String = when (status) {
        1 -> StringUtils.getString(R.string.hotel_order_status_1)
        3 -> StringUtils.getString(R.string.hotel_order_status_3)
        4 -> StringUtils.getString(R.string.hotel_order_status_4)
        5 -> StringUtils.getString(R.string.hotel_order_status_5)
        6 -> StringUtils.getString(R.string.hotel_order_status_6)
        7 -> StringUtils.getString(R.string.store_status_desc_13)
        8 -> StringUtils.getString(R.string.store_status_desc_8)
        9 -> StringUtils.getString(R.string.store_status_desc_9)
        else -> StringUtils.getString(R.string.line_split)
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


    fun getSafeBigDecimalString(price: BigDecimal?): String {

        return if (price == null) {
            "0"
        } else {
            price.toPlainString()
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

        try {

            // 断开融云连接
            RongIM.getInstance().logout()
            User.clearAllUser()
            CacheUtil.saveToken("")

            // 登出友盟
//            MobclickAgent.onProfileSignOff()

            // 删除极光推送别名
            val sequence = SPUtils.getInstance().getInt(Constant.SP_PUSH_SEQUENCE, 0)
            JPushInterface.deleteAlias(Utils.getApp().applicationContext, sequence)

            EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_SIGN_OUT))
        } catch (e: Exception) {

        }

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
        2 -> StringUtils.getString(R.string.merchant_under_review)
        3 -> StringUtils.getString(R.string.shops_to_receive_desc_2)
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


    fun setNowPriceAndOldPrice(
        price: BigDecimal, oldPrice: BigDecimal,
        priceTv: TextView, oldPriceTv: TextView,
    ) {

        price.setScale(2, BigDecimal.ROUND_HALF_UP)
        oldPrice.setScale(2, BigDecimal.ROUND_HALF_UP)
        val decimalFormat = DecimalFormat("0.00#")

        val nowPriceStr = decimalFormat.format(price)
        val oldPriceStr = decimalFormat.format(oldPrice)


        // 现价
        SpanUtils.with(priceTv)
            .append(StringUtils.getString(R.string.price_unit))
            .setFontSize(12, true)
            .append(nowPriceStr)
            .setFontSize(14, true)
            .create()

        // 原价
        if (price != oldPrice) {
            oldPriceTv.visibility = View.VISIBLE

            SpanUtils.with(oldPriceTv)
                .append(StringUtils.getString(R.string.price_unit))
                .setFontSize(10, true)
                .append(oldPriceStr)
                .setFontSize(11, true)
                .setStrikethrough()
                .create()
        } else {
            oldPriceTv.visibility = View.GONE
        }
    }

    /**
     * 支付账单订单状态
     */
    fun getBillsStatusText(orderStatus: Int): String {

        return when (orderStatus) {
            0 -> StringUtils.getString(R.string.bills_status_0)
            1 -> StringUtils.getString(R.string.bills_status_1)
            2 -> StringUtils.getString(R.string.bills_status_2)
            3 -> StringUtils.getString(R.string.bills_status_3)
            4 -> StringUtils.getString(R.string.bills_status_4)
            5 -> StringUtils.getString(R.string.bills_status_5)
            6 -> StringUtils.getString(R.string.bills_status_6)
            7 -> StringUtils.getString(R.string.bills_status_7)
            8 -> StringUtils.getString(R.string.bills_status_8)
            9 -> StringUtils.getString(R.string.bills_status_9)
            else -> ""
        }

    }


    /**
     * 支付账单订单描述
     *
     * 订单状态（0-待支付，1-缴费中，2-缴费成功，3-缴费失败，4-订单关闭，5-退款审核中，6-拒绝退款，7-退款中，8-退款成功，9-退款失败）
     */
    fun getBillsStatusDesc(orderStatus: Int): String {

        return when (orderStatus) {
            1 -> StringUtils.getString(R.string.bills_desc_1)
            3 -> StringUtils.getString(R.string.bills_desc_3)
            5 -> StringUtils.getString(R.string.bills_desc_5)
            6 -> StringUtils.getString(R.string.bills_desc_6)

            7 -> StringUtils.getString(R.string.store_status_desc_8)
            8 -> StringUtils.getString(R.string.bills_desc_8)
            9 -> StringUtils.getString(R.string.bills_desc_9)
            else -> StringUtils.getString(R.string.line_split)
        }

    }


    fun formatPrice(price: BigDecimal): String {
        price.setScale(2, BigDecimal.ROUND_HALF_UP)
        val decimalFormat = DecimalFormat("0.00#")
        return decimalFormat.format(price)
    }


    fun initTag(tags: ArrayList<String>, tagRv: RecyclerView) {


        val tagAdapter = HomeTagAdapter()
        tagAdapter.setNewInstance(tags)

        val manager = object : MyLayoutManager() {
            override fun canScrollVertically() = false
        }

        val params = tagRv.layoutParams
        if (tags.isNullOrEmpty()) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            params.height = SizeUtils.dp2px(20f)
        }

        tagRv.apply {
            adapter = tagAdapter
            layoutManager = manager
            layoutParams = params
        }

    }


    /**
     * 获取友盟app渠道
     */
    fun getUmengAppChannel(): String {
        val channel =
            if (BuildConfig.FLAVOR == "dev" || BuildConfig.VERSION_NAME.contains("beta") || BuildConfig.VERSION_NAME.contains("alpha")) {
                BuildConfig.UMENG_APP_CHANNEL_DEV
            } else {
                BuildConfig.UMENG_APP_CHANNEL_FORMAL
            }

        return channel
    }

    private var sequence = 1

    /**
     *  【友盟+】在统计用户时以设备为标准，此处切换统计标准为自身账号
     *  @param platformName 账号来源。
     *                      如果用户通过第三方账号登陆，则为具体平台名称，如Facebook googlePlus+
     *                      app本身登录 可传空
     *
     */
    fun umengOnProfileSignIn(platformName: String? = null, userId: Long) {

        try {
            // 友盟登录
//            if (platformName.isNullOrBlank()) {
//                MobclickAgent.onProfileSignIn(userId.toString())
//            } else {
//                MobclickAgent.onProfileSignIn(platformName, userId.toString())
//            }

            // ## 极光推送相关 ##

            // 极光设置别名
            // https://docs.jiguang.cn/jpush/client/Android/android_api#%E5%88%AB%E5%90%8D%E4%B8%8E%E6%A0%87%E7%AD%BE-api
            val alias = StringBuffer()
            if (BuildConfig.FLAVOR == "dev") {
                alias.append("develop_")
            } else {
                alias.append("produce_")
            }
            val append = alias.append(userId.toString())

            val sequence = Math.abs(System.currentTimeMillis().toInt())

            JPushInterface.setAlias(Utils.getApp(), sequence, append.toString())

            // 保存 sequence
            SPUtils.getInstance().put(Constant.SP_PUSH_SEQUENCE, sequence)

            // 根据开发环境设置tag
            val tag = HashSet<String>()
            if (BuildConfig.FLAVOR === "formal") {
                tag.add("formal")
            }
            if (BuildConfig.FLAVOR === "dev") {
                tag.add("develop")
            }
            JPushInterface.setTags(Utils.getApp(), sequence, tag)

        } catch (e: Exception) {

        }

    }


    fun getDeepLinksTargetIntent(module: String?, moduleId: String?, context: Context): Intent? {

        var facebookIntent: Intent? = null

        if (module.isNullOrBlank() || moduleId.isNullOrBlank()) {
            return facebookIntent
        }
        val bundle = Bundle()

        when (module) {
            Html5Url.MODEL_HOTEL -> {
                val dialog = JcsCalendarDialog()
                dialog.initCalendar()
                bundle.apply {
                    putInt(Constant.PARAM_HOTEL_ID, moduleId.toInt())
                    putSerializable(Constant.PARAM_START_DATE, dialog.startBean)
                    putSerializable(Constant.PARAM_END_DATE, dialog.endBean)
                }
                facebookIntent = Intent(context, HotelDetailActivity2::class.java).putExtras(bundle)
            }
            Html5Url.MODEL_NEWS -> {
                bundle.apply {
                    putString(Constant.PARAM_NEWS_ID, moduleId)
                }
                facebookIntent = Intent(context, NewsDetailActivity::class.java).putExtras(bundle)
            }
            Html5Url.MODEL_TRAVEL -> {
                bundle.apply {
                    putInt(Constant.PARAM_ID, moduleId.toInt())
                }
                facebookIntent = Intent(context, TravelDetailActivity::class.java).putExtras(bundle)
            }

            Html5Url.MODEL_GENERAL -> {
                bundle.apply {
                    putInt(Constant.PARAM_ID, moduleId.toInt())
                }
                facebookIntent = Intent(context, MechanismActivity::class.java).putExtras(bundle)
            }

            Html5Url.MODEL_RESTAURANT -> {
                bundle.apply {
                    putInt(Constant.PARAM_ID, moduleId.toInt())
                }
                facebookIntent = Intent(context, RestaurantDetailActivity::class.java).putExtras(bundle)
            }

            Html5Url.MODEL_MALL -> {
                bundle.apply {
                    putInt(Constant.PARAM_ID, moduleId.toInt())
                }
                facebookIntent = Intent(context, MallDetailActivity::class.java).putExtras(bundle)
            }

            Html5Url.MODEL_MALL_SHOP -> {
                bundle.apply {
                    putInt(Constant.PARAM_SHOP_ID, moduleId.toInt())
                }
                facebookIntent = Intent(context, MallShopHomeActivity::class.java).putExtras(bundle)
            }

            Html5Url.MODEL_JOB -> {
                bundle.apply {
                    putInt(Constant.PARAM_ID, moduleId.toInt())
                }
                facebookIntent = Intent(context, JobDetailActivity::class.java).putExtras(bundle)
            }
            Html5Url.MODEL_JOB_APPLY_STATUS -> {
                if (User.isLogon()) {
                    bundle.apply {
                        putBoolean(Constant.PARAM_FROM_NOTICE, true)
                    }
                    facebookIntent = Intent(context, JobMainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtras(bundle)
                }
            }
            else -> {}
        }


        facebookIntent?.let {
            if (context !is Activity) {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        return facebookIntent
    }

    fun getAllImage(data: MutableList<MediaData>): ArrayList<MediaData> {
        val source = ArrayList<MediaData>()
        data.forEach {
            if (it.type == MediaData.IMAGE) {
                source.add(it)
            }
        }
        return source
    }

    fun getFirstVideo(data: MutableList<MediaData>): ArrayList<MediaData> {
        val source = ArrayList<MediaData>()
        data.forEach {
            if (it.type == MediaData.VIDEO) {
                source.add(it)
                return@forEach
            }
        }
        return source
    }


    /**
     * 轮播图点击处理
     */
    fun handleBannerClick(context: Context?, data: BannerResponse?) {
        if (data == null || context == null) {
            return
        }
        if (data.redirect_type == 0) {
            return
        }
        if (data.redirect_type == 1 && data.h5_link.isNotBlank()) {
            WebViewActivity.navigation(context, data.h5_link)
            return
        }

        if (data.redirect_type == 2) {

            when (data.target_type) {
                1 -> {
                    val dialog = JcsCalendarDialog()
                    dialog.initCalendar()
                    HotelDetailActivity2.navigation(context, data.target_id, dialog.startBean, dialog.endBean)
                }
                2 -> TravelDetailActivity.navigation(context, data.target_id)
                3 -> {
                    val intent = Intent(context, NewsDetailActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtras(Bundle().apply {
                            putString(Constant.PARAM_NEWS_ID, data.target_id.toString())
                        })

                    context.startActivity(intent)
                }
                4 -> MechanismActivity.navigation(context, data.target_id)
                5 -> RestaurantDetailActivity.navigation(context, data.target_id)
                6 -> MallDetailActivity.navigation(context, data.target_id)
                //  7 -> startActivityAfterLogin(CouponCenterActivity::class.java)
                else -> {

                }
            }
            return
        }
    }


    fun createBottomDialog(
        context: Context,
        oldIndex: Int? = 0,
        array: Array<String>,
        onCountryCodeSelectListener: OnBottomSelectedIndex,
    ) {

        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_job_prefix, null)
        dialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (ignored: java.lang.Exception) {

        }

        val first = view.findViewById<CheckedTextView>(R.id.first_tv)
        first.text = array[0]
        first.setOnClickListener { v: View? ->
            onCountryCodeSelectListener.onIndexSelect(0)
            dialog.dismiss()
        }

        val second = view.findViewById<CheckedTextView>(R.id.second_tv)
        second.text = array[1]
        second.setOnClickListener { v13: View? ->
            onCountryCodeSelectListener.onIndexSelect(1)
            dialog.dismiss()
        }

        when (oldIndex) {
            0 -> {
                first.isChecked = true
            }
            1 -> {
                second.isChecked = true
            }
        }

        view.findViewById<View>(R.id.cancel_tv).setOnClickListener { v1: View? ->
            dialog.dismiss()
        }
        dialog.show()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun showWorkDialog(context: FragmentActivity, listener: OnWorkTimeSelected?): BottomDialog? {

        var month = ""
        var year = ""


        val monthAdapter = WorkTimeAdapter()
        val yearAdapter = WorkTimeAdapter()

        monthAdapter.setOnItemClickListener { _, _, position ->
            monthAdapter.data.forEachIndexed { index, workTime ->
                workTime.isSelected = index == position
            }
            monthAdapter.notifyDataSetChanged()
            val monthIndex = monthAdapter.data[position].monthIndex + 1
            month = if (monthIndex < 10) {
                "0$monthIndex"
            } else {
                monthIndex.toString()
            }

        }

        yearAdapter.setOnItemClickListener { _, _, position ->

            yearAdapter.data.forEachIndexed { index, workTime ->
                workTime.isSelected = index == position
            }
            yearAdapter.notifyDataSetChanged()
            year = yearAdapter.data[position].name
        }


        val dialog = BottomDialog.create(context.supportFragmentManager)

        dialog.setLayoutRes(R.layout.layout_select_year_month)
            .setViewListener {

                val monthRv = it.findViewById<RecyclerView>(R.id.month_rv)
                monthRv.apply {
                    adapter = monthAdapter
                    addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5), 1, SizeUtils.dp2px(40f), 0))
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }


                val yearRv = it.findViewById<RecyclerView>(R.id.year_rv)
                yearRv.apply {
                    adapter = yearAdapter
                    addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5), 1, 0, SizeUtils.dp2px(40f)))
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }


                yearAdapter.setNewInstance(WorkTimeUtil.getAllYear())
                monthAdapter.setNewInstance(WorkTimeUtil.getAllMonth())
                month = "1"
                year = yearAdapter.data[0].name


                it.findViewById<TextView>(R.id.confirm_tv).setOnClickListener {

                    val result = "$year.$month"
                    listener?.onWorkTimeSelected(result)
                    dialog.dismiss()


                }
                it.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
                    dialog.dismiss()
                }
            }

        return dialog
    }

    fun checkDateLegal(startStr: String, endStr: String, format: String): Boolean {
        var legal = false

        val start = startStr.replace(".", "-")
        val end = endStr.replace(".", "-")

        val startDate = TimeUtils.string2Date(start, format)
        val endDate = TimeUtils.string2Date(end, format)

        if (startDate <= endDate) {
            legal = true
        }
        return legal
    }


    @SuppressLint("NotifyDataSetChanged")
    fun showYearDialog(context: FragmentActivity, listener: OnWorkTimeSelected?): BottomDialog? {

        var year = ""

        val yearAdapter = WorkTimeAdapter()

        yearAdapter.setOnItemClickListener { _, _, position ->

            yearAdapter.data.forEachIndexed { index, workTime ->
                workTime.isSelected = index == position
            }
            yearAdapter.notifyDataSetChanged()
            year = yearAdapter.data[position].name
        }


        val dialog = BottomDialog.create(context.supportFragmentManager)

        dialog.setLayoutRes(R.layout.layout_select_year)
            .setViewListener {

                val yearRv = it.findViewById<RecyclerView>(R.id.year_rv)

                yearRv.apply {
                    adapter = yearAdapter
                    addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5), 1, 0, SizeUtils.dp2px(40f)))
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
                yearAdapter.setNewInstance(WorkTimeUtil.getAllYear())

                year = yearAdapter.data[0].name

                it.findViewById<TextView>(R.id.confirm_tv).setOnClickListener {
                    val result = year
                    listener?.onWorkTimeSelected(result)
                    dialog.dismiss()
                }
                it.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
                    dialog.dismiss()
                }
            }

        return dialog

    }


    fun checkEditBlank(vararg editView: EditText): Boolean {
        var isBlank = false

        editView.forEach {
            val trim = it.text.toString().trim()
            if (trim.isBlank()) {
                isBlank = true
                return@forEach
            }
        }
        return isBlank
    }

    fun checkEditBlank(editView: ArrayList<AppCompatEditText>): Boolean {
        var isBlank = false

        editView.forEach {
            val trim = it.text.toString().trim()
            if (trim.isBlank()) {
                isBlank = true
                return@forEach
            }
        }
        return isBlank
    }


    fun setViewClickable(clickable: Boolean, view: TextView) {

        if (clickable) {
            view.alpha = 1.0f
        } else {
            view.alpha = 0.5f
        }
        view.isClickable = clickable
    }

    fun setViewAlpha(clickable: Boolean, view: TextView) {

        if (clickable) {
            view.alpha = 1.0f
        } else {
            view.alpha = 0.5f
        }
    }


    fun copyText(context: Context, textCopied: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // When setting the clip board text.
        clipboardManager.setPrimaryClip(ClipData.newPlainText(BuildConfig.APPLICATION_ID, textCopied))
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S)
            Toast.makeText(context, context.getString(R.string.copy_successfully), Toast.LENGTH_SHORT).show()
    }

    fun showBalance(
        context: Context,
        title: String,
        channelName: String,
        balanceTitle: String,
        balance: String,
        onCancelClickListener: View.OnClickListener?,
        onConfirmClickListener: View.OnClickListener?,
    ) {

        val timeDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_balance, null)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val title_tv = view.findViewById<TextView>(R.id.title_tv)
        val pay_channel_tv = view.findViewById<TextView>(R.id.pay_channel_tv)
        val balance_title_tv = view.findViewById<TextView>(R.id.balance_title_tv)
        val balance_tv = view.findViewById<TextView>(R.id.balance_tv)
        val cancel_bt = view.findViewById<Button>(R.id.cancel_bt)
        val confirm_bt = view.findViewById<Button>(R.id.confirm_bt)


        title_tv.text = title
        pay_channel_tv.text = channelName
        balance_title_tv.text = balanceTitle
        balance_tv.text = balance

        if (onCancelClickListener != null) {
            cancel_bt.visibility = View.VISIBLE
            confirm_bt.setText(R.string.confirm_lifted)
            confirm_bt.setBackgroundResource(R.drawable.shape_blue_radius_22)
        } else {
            cancel_bt.visibility = View.GONE
            confirm_bt.setBackgroundResource(R.drawable.stock_blue_radius_22)
        }

        cancel_bt.setOnClickListener {
            timeDialog.dismiss()
            onCancelClickListener?.onClick(it)
        }
        confirm_bt.setOnClickListener {
            timeDialog.dismiss()
            onConfirmClickListener?.onClick(it)
        }

        timeDialog.show()

    }


}

interface OnWorkTimeSelected {
    fun onWorkTimeSelected(string: String)
}


interface OnBottomSelectedIndex {
    fun onIndexSelect(selectedIndex: Int)
}

interface OnBalanceClickListener {

}


