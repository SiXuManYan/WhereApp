package com.jcs.where.home.adapter

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.order.OrderListResponse
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.comment.CommentPostActivity
import com.jcs.where.features.gourmet.comment.post.FoodCommentPostActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.store.comment.detail.StoreCommentDetailActivity
import com.jcs.where.features.store.comment.post.StoreCommentPostActivity
import com.jcs.where.features.store.detail.StoreDetailActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.*
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.widget.calendar.JcsCalendarDialog

/**
 * Created by Wangsw  2021/5/12 10:00.
 */
open class OrderListAdapter : BaseMultiItemQuickAdapter<OrderListResponse, BaseViewHolder>(), LoadMoreModule {


    init {
        addItemType(OrderListResponse.ORDER_TYPE_HOTEL_1, R.layout.item_order_list_hotel)
        addItemType(OrderListResponse.ORDER_TYPE_DINE_2, R.layout.item_order_list_food)
        addItemType(OrderListResponse.ORDER_TYPE_TAKEAWAY_3, R.layout.item_order_list_takeaway)
        addItemType(OrderListResponse.ORDER_TYPE_STORE_4, R.layout.item_order_list_store)
    }


    override fun convert(holder: BaseViewHolder, item: OrderListResponse) {

        when (holder.itemViewType) {
            OrderListResponse.ORDER_TYPE_HOTEL_1 -> {
                bindHotelItem(holder, item)
            }

            OrderListResponse.ORDER_TYPE_DINE_2 -> {
                bindFoodItem(holder, item)
            }
            OrderListResponse.ORDER_TYPE_TAKEAWAY_3 -> {
                bindTakeawayItem(holder, item)
            }
            OrderListResponse.ORDER_TYPE_STORE_4 -> {
                bindStoreItem(holder, item)
            }
            else -> {
            }
        }

    }


    /** 酒店 */
    private fun bindHotelItem(holder: BaseViewHolder, item: OrderListResponse) {

        val modelData = item.model_data
        if (modelData == null) {
            return
        }
        // 标题
        holder.setText(R.id.name_tv, item.title)
        val title_rl = holder.getView<RelativeLayout>(R.id.title_rl)
        title_rl.setOnClickListener {
            val dialog = JcsCalendarDialog()
            dialog.initCalendar(context)

            HotelDetailActivity2.navigation(context, item.model_id, dialog.startBean, dialog.endBean)
        }

        // 状态
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)

        // 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功）
        val status = modelData.order_status
        order_status_tv.text = BusinessUtils.getHotelStatusText(status)
        if (status == 1 || status == 5) {
            order_status_tv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
        } else {
            order_status_tv.setTextColor(ColorUtils.getColor(R.color.black_333333))
        }

        // 内容
        val first_tv = holder.getView<TextView>(R.id.first_tv)
        val second_tv = holder.getView<TextView>(R.id.second_tv)
        val third_tv = holder.getView<TextView>(R.id.third_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)

        if (item.image.isNotEmpty()) {
            val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL)
            )
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)
            Glide.with(context).load(item.image[0]).apply(options).into(image_iv)
        }
        first_tv.text = modelData.room_name
        second_tv.text = StringUtils.getString(R.string.hotel_date_format, modelData.start_date, modelData.end_date)
        third_tv.text = StringUtils.getString(R.string.hotel_price_format, modelData.room_price.toPlainString())

        // 底部
        val right_tv = holder.getView<TextView>(R.id.right_tv)

        when (status) {
            1 -> {
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_pay_2)
                right_tv.setOnClickListener {
                    // 立即支付
                    val orderIds = ArrayList<Int>()
                    orderIds.add(item.id)
                    startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                        putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
                        putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                        putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_HOTEL)
                    })
                }
            }
            5 -> {
                right_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_review)
                right_tv.setOnClickListener {
                    CommentPostActivity.navigation(context, 0, item.model_id, item.id)
                }
            }
            else -> {
                right_tv.visibility = View.GONE
            }
        }

    }


    /** 美食 */
    private fun bindFoodItem(holder: BaseViewHolder, item: OrderListResponse) {

        val modelData = item.model_data
        if (modelData == null) {
            return
        }

        // 标题
        holder.setText(R.id.name_tv, item.title)

        // 状态
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)

        // 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待使用，6-交易成功，7-退款中，8-退款成功）
        val status = modelData.order_status
        if (status == 1 || status == 6) {
            order_status_tv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
        } else {
            order_status_tv.setTextColor(ColorUtils.getColor(R.color.black_333333))
        }
        order_status_tv.text = BusinessUtils.getDelicacyOrderStatusText(status)
        // 内容
        val first_tv = holder.getView<TextView>(R.id.first_tv)
        val second_tv = holder.getView<TextView>(R.id.second_tv)
        val third_tv = holder.getView<TextView>(R.id.third_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_rl = holder.getView<RelativeLayout>(R.id.title_rl)
        title_rl.setOnClickListener {
            startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, item.model_id)
            })
        }

        val options = RequestOptions.bitmapTransform(
            GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL)
        )
            .error(R.mipmap.ic_empty_gray)
            .placeholder(R.mipmap.ic_empty_gray)
        Glide.with(context).load(modelData.food_image).apply(options).into(image_iv)


        first_tv.text = modelData.food_name
        second_tv.text = StringUtils.getString(R.string.quantity_format, modelData.good_num)
        third_tv.text = StringUtils.getString(R.string.total_price_format, item.price.toPlainString())

        // 底部
        val right_tv = holder.getView<TextView>(R.id.right_tv)

        when (status) {
            1 -> {
                // 1：待付款
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_pay_2)

                right_tv.setOnClickListener {
                    // 立即支付
                    val orderIds = ArrayList<Int>()
                    orderIds.add(item.id)
                    startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                        putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
                        putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                        putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_FOOD)
                    })
                }
            }

            6 -> {
                // 交易成功待评价
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_review)
                right_tv.setOnClickListener {
                    startActivity(FoodCommentPostActivity::class.java, Bundle().apply {
                        putInt(Constant.PARAM_ORDER_ID, item.id)
                        putInt(Constant.PARAM_RESTAURANT_ID, item.model_id)
                        putInt(Constant.PARAM_TYPE, 1)
                    })
                }
            }
            else -> {
                right_tv.visibility = View.GONE
            }
        }
    }


    /** 外卖 */
    private fun bindTakeawayItem(holder: BaseViewHolder, item: OrderListResponse) {

        val modelData = item.model_data
        if (modelData == null) {
            return
        }

        // 标题
        holder.setText(R.id.name_tv, item.title)

        // 状态
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)
        order_status_tv.text = BusinessUtils.getTakeawayStatusText(modelData.order_status)

        // 内容
        val first_tv = holder.getView<TextView>(R.id.first_tv)
        val second_tv = holder.getView<TextView>(R.id.second_tv)
        val third_tv = holder.getView<TextView>(R.id.third_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_rl = holder.getView<RelativeLayout>(R.id.title_rl)
        title_rl.setOnClickListener {
            startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, item.model_id)
            })
        }

        if (item.image.isNotEmpty()) {
            val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL)
            )
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)
            Glide.with(context).load(item.image[0]).apply(options).into(image_iv)
        }

        first_tv.text = modelData.food_name
        second_tv.text = StringUtils.getString(R.string.quantity_format, modelData.good_num)
        third_tv.text = StringUtils.getString(R.string.total_price_format, item.price.toPlainString())

        // 底部
        val right_tv = holder.getView<TextView>(R.id.right_tv)

        when (modelData.order_status) {

            1 -> {
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_pay_2)

                right_tv.setOnClickListener {
                    // 立即支付
                    val orderIds = ArrayList<Int>()
                    orderIds.add(item.id)
                    startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                        putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
                        putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                        putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_TAKEAWAY)
                    })
                }
            }

            8 -> {
                // 交易成功。待评价
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_review)
                right_tv.setOnClickListener {
                    startActivity(FoodCommentPostActivity::class.java, Bundle().apply {
                        putInt(Constant.PARAM_ORDER_ID, item.id)
                        putInt(Constant.PARAM_RESTAURANT_ID, item.model_id)
                        putInt(Constant.PARAM_TYPE, 2)
                    })

                }
            }
            else -> {
                right_tv.visibility = View.GONE
            }

        }


    }


    /** 商城 */
    private fun bindStoreItem(holder: BaseViewHolder, item: OrderListResponse) {

        val modelData = item.model_data
        if (modelData == null) {
            return
        }

        val goods = modelData.goods


        // 标题
        holder.setText(R.id.name_tv, item.title)

        // 状态
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)
        FeaturesUtil.bindStoreOrderStatus(modelData.order_status, modelData.delivery_type, order_status_tv)

        // 内容
        val first_tv = holder.getView<TextView>(R.id.first_tv)
        val second_tv = holder.getView<TextView>(R.id.second_tv)
        val third_tv = holder.getView<TextView>(R.id.third_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_rl = holder.getView<RelativeLayout>(R.id.title_rl)
        title_rl.setOnClickListener {
            startActivity(StoreDetailActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, item.model_id)
            })
        }

        val options = RequestOptions.bitmapTransform(
            GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL)
        )
            .error(R.mipmap.ic_empty_gray)
            .placeholder(R.mipmap.ic_empty_gray)


        if (goods.isNotEmpty()) {
            if (goods[0].good_image.isNotEmpty()) {
                Glide.with(context).load(goods[0].good_image[0]).apply(options).into(image_iv)
            }
            first_tv.text = goods[0].good_title
        }
        second_tv.text = StringUtils.getString(R.string.quantity_format, goods.size)
        third_tv.text = StringUtils.getString(R.string.total_price_format, item.price.toPlainString())

        // 底部
        val right_tv = holder.getView<TextView>(R.id.right_tv)
        when (modelData.order_status) {
            1 -> {
                right_tv.text = context.getString(R.string.to_pay_2)
                right_tv.visibility = View.VISIBLE

                right_tv.setOnClickListener {
                    val orderIds = ArrayList<Int>()
                    orderIds.add(item.id)
                    startActivity(StorePayActivity::class.java, Bundle().apply {
                        putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
                        putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                        putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_ESTORE)
                    })
                }
            }
            5 -> {

                val shopName = item.title
                var shopImage = ""
                if (item.image.isNotEmpty()) {
                    shopImage = item.image[0]
                }

                val commentStatus = modelData.comment_status
                if (commentStatus == 3) {
                    right_tv.visibility = View.GONE
                } else {
                    right_tv.visibility = View.VISIBLE

                    if (commentStatus == 1) {
                        right_tv.text = context.getString(R.string.evaluation_go)
                        right_tv.setOnClickListener {
                            // 去评价
                            startActivity(StoreCommentPostActivity::class.java, Bundle().apply {
                                putInt(Constant.PARAM_ORDER_ID, item.id)
                                putString(Constant.PARAM_SHOP_NAME, shopName)
                                putString(Constant.PARAM_SHOP_IMAGE, shopImage)
                            })
                        }
                    }

                    if (commentStatus == 2) {
                        right_tv.text = context.getString(R.string.view_evaluation)
                        right_tv.setOnClickListener {
                            // 查看评价
                            startActivity(StoreCommentDetailActivity::class.java, Bundle().apply {
                                putInt(Constant.PARAM_ORDER_ID, item.id)
                            })
                        }
                    }
                }
            }
            12 -> {
                right_tv.text = context.getString(R.string.cancel_application)
                right_tv.visibility = View.GONE
            }
            else -> {
                right_tv.visibility = View.GONE

            }


        }
    }


    private fun startActivity(target: Class<*>, bundle: Bundle?) {
        if (bundle != null) {
            context.startActivity(Intent(context, target).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            context.startActivity(Intent(context, target).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }


    private fun startActivityAfterLogin(target: Class<*>, bundle: Bundle?) {
        val token = CacheUtil.needUpdateBySpKey(SPKey.K_TOKEN)
        if (TextUtils.isEmpty(token)) {
            startActivity(LoginActivity::class.java, null)
        } else {
            startActivity(target, bundle)
        }
    }

}