package com.jcs.where.home.adapter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.OrderListResponse
import com.jcs.where.features.gourmet.order.detail.FoodOrderDetailActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.gourmet.takeaway.TakeawayActivity
import com.jcs.where.home.activity.ApplyRefundActivity
import com.jcs.where.hotel.activity.HotelCommentActivity
import com.jcs.where.hotel.activity.HotelDetailActivity
import com.jcs.where.hotel.activity.HotelOrderDetailActivity
import com.jcs.where.hotel.activity.HotelPayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.widget.calendar.JcsCalendarDialog

/**
 * Created by Wangsw  2021/5/12 10:00.
 */
class OrderListAdapter2 : BaseMultiItemQuickAdapter<OrderListResponse, BaseViewHolder>(), LoadMoreModule {


    init {
        addItemType(OrderListResponse.ORDER_TYPE_HOTEL_1, R.layout.item_order_list_hotel)
        addItemType(OrderListResponse.ORDER_TYPE_DINE_2, R.layout.item_order_list_food)
        addItemType(OrderListResponse.ORDER_TYPE_TAKEAWAY_3, R.layout.item_order_list_takeaway)
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
            else -> {
            }
        }

    }


    /** 酒店 */
    private fun bindHotelItem(holder: BaseViewHolder, item: OrderListResponse) {

        val modelData = item.model_data

        // 标题
        holder.setText(R.id.name_tv, item.title)

        // 状态
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)
        FeaturesUtil.bindHotelOrderStatus(modelData.order_status, order_status_tv)

        // 内容
        val first_tv = holder.getView<TextView>(R.id.first_tv)
        val second_tv = holder.getView<TextView>(R.id.second_tv)
        val third_tv = holder.getView<TextView>(R.id.third_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)

        if (item.image.isNotEmpty()) {
            val options = RequestOptions.bitmapTransform(
                    GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                    .error(R.mipmap.ic_empty_gray)
                    .placeholder(R.mipmap.ic_empty_gray)
            Glide.with(context).load(item.image[0]).apply(options).into(image_iv)
        }
        first_tv.text = StringUtils.getString(R.string.hotel_content_format, modelData.room_num, modelData.room_type)
        second_tv.text = StringUtils.getString(R.string.hotel_date_format, modelData.start_date, modelData.end_date)
        third_tv.text = StringUtils.getString(R.string.hotel_price_format, modelData.room_price.toPlainString())

        // 底部
        val left_tv = holder.getView<TextView>(R.id.left_tv)
        val right_tv = holder.getView<TextView>(R.id.right_tv)


        when (modelData.order_status) {
            1 -> {
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
                left_tv.text = StringUtils.getString(R.string.cancel_order)
                right_tv.text = StringUtils.getString(R.string.to_pay)
                left_tv.setOnClickListener {
                    // todo 展示取消订单 dialog
                }
                right_tv.setOnClickListener {
                    // 立即支付
                    HotelPayActivity.goTo(context, null)
                }
            }
            2 -> {
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
                left_tv.text = StringUtils.getString(R.string.to_refund)
                right_tv.text = StringUtils.getString(R.string.to_use)
                left_tv.setOnClickListener {
                    // 申请退款
                    startActivity(ApplyRefundActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ID_2, item.id.toString())
                    })
                }
                right_tv.setOnClickListener {
                    // 去使用
                    HotelOrderDetailActivity.goTo(context, item.id.toString())
                }
            }
            3 -> {
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
                left_tv.text = StringUtils.getString(R.string.to_review)
                right_tv.text = StringUtils.getString(R.string.book_again)
                left_tv.setOnClickListener {
                    // 去评价
                    startActivity(HotelCommentActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ID_2, item.id.toString())
                    })
                }
                right_tv.setOnClickListener {
                    // 再次预定
                    val dialog = JcsCalendarDialog().apply {
                        initCalendar(context)
                    }
                    HotelDetailActivity.goTo(context, item.model_id, dialog.startBean, dialog.endBean, 1, "", "", 1)
                }
            }
            4, 5, 6, 7 -> {
                left_tv.visibility = View.GONE
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.book_again)
                right_tv.setOnClickListener {
                    // 再次预定
                    val dialog = JcsCalendarDialog().apply {
                        initCalendar(context)
                    }
                    HotelDetailActivity.goTo(context, item.model_id, dialog.startBean, dialog.endBean, 1, "", "", 1)
                }
            }

            8 -> {
                left_tv.visibility = View.GONE
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.refund_failed)
                right_tv.setOnClickListener {
                    // 申请退款
                    HotelOrderDetailActivity.goTo(context, item.id.toString())
                }

            }
            9, 10 -> {
                left_tv.visibility = View.GONE
                right_tv.visibility = View.GONE
            }
            else -> {
                left_tv.visibility = View.GONE
                right_tv.visibility = View.GONE
            }
        }

    }


    /**
     * 美食
     */
    private fun bindFoodItem(holder: BaseViewHolder, item: OrderListResponse) {

        val modelData = item.model_data

        // 标题
        holder.setText(R.id.name_tv, item.title)

        // 状态
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)
        FeaturesUtil.bindFoodOrderStatus(modelData.order_status, order_status_tv)

        // 内容
        val first_tv = holder.getView<TextView>(R.id.first_tv)
        val second_tv = holder.getView<TextView>(R.id.second_tv)
        val third_tv = holder.getView<TextView>(R.id.third_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)

        val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)
        Glide.with(context).load(modelData.food_image).apply(options).into(image_iv)

        first_tv.text = modelData.food_name
        second_tv.text = StringUtils.getString(R.string.quantity_format, modelData.good_num)
        third_tv.text = StringUtils.getString(R.string.total_price_format, modelData.room_price.toPlainString())

        // 底部
        val left_tv = holder.getView<TextView>(R.id.left_tv)
        val right_tv = holder.getView<TextView>(R.id.right_tv)

        when (modelData.order_status) {
            1 -> {
                // 1：待付款
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
                left_tv.text = StringUtils.getString(R.string.cancel_order)
                right_tv.text = StringUtils.getString(R.string.to_pay)
                left_tv.setOnClickListener {
                    // 取消订单
                    startActivity(FoodOrderDetailActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ORDER_ID, item.id.toString())
                    })
                }
                right_tv.setOnClickListener {
                    // 立即支付
                    startActivity(FoodOrderDetailActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ORDER_ID, item.id.toString())
                    })
                }
            }
            2 -> {
                // 2：已取消
                left_tv.visibility = View.GONE
                right_tv.visibility = View.GONE

            }
            3 -> {
                // 3：待使用
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
                left_tv.text = StringUtils.getString(R.string.to_refund)
                right_tv.text = StringUtils.getString(R.string.coupon_code_view)
                left_tv.setOnClickListener {
                    // 申请退款
                    startActivity(FoodOrderDetailActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ORDER_ID, item.id.toString())
                    })
                }
                right_tv.setOnClickListener {
                    // 查看券码
                    startActivity(FoodOrderDetailActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ORDER_ID, item.id.toString())
                    })
                }
            }

            4 -> {
                // 已完成
                left_tv.visibility = View.GONE
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.buy_again)
                right_tv.setOnClickListener {
                    // 再来一单
                    startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ID, item.model_id.toString())
                    })
                }

            }
            5, 6, 7, 8 -> {
                left_tv.visibility = View.GONE
                right_tv.visibility = View.GONE
            }


            9 -> {
                // 待评价
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
                left_tv.text = StringUtils.getString(R.string.evaluation)
                right_tv.text = StringUtils.getString(R.string.buy_again)
                left_tv.setOnClickListener {
                    // 评价

                }
                right_tv.setOnClickListener {
                    // 再来一单
                    startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ID, item.model_id.toString())
                    })
                }
            }
            else -> {
                left_tv.visibility = View.GONE
                right_tv.visibility = View.GONE
            }
        }
    }


    /**
     * 外卖
     */
    private fun bindTakeawayItem(holder: BaseViewHolder, item: OrderListResponse) {

        val modelData = item.model_data

        // 标题
        holder.setText(R.id.name_tv, item.title)

        // 状态
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)
        FeaturesUtil.bindTakeawayOrderStatus(modelData.order_status, order_status_tv)

        // 内容
        val first_tv = holder.getView<TextView>(R.id.first_tv)
        val third_tv = holder.getView<TextView>(R.id.third_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)

        val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)
        Glide.with(context).load(modelData.food_image).apply(options).into(image_iv)

        first_tv.text = modelData.good_names
        third_tv.text = StringUtils.getString(R.string.total_price_format, modelData.room_price.toPlainString())

        // 底部
        val left_tv = holder.getView<TextView>(R.id.left_tv)
        val right_tv = holder.getView<TextView>(R.id.right_tv)

        when (modelData.order_status) {
            5 -> {
                left_tv.visibility = View.GONE
                right_tv.visibility = View.VISIBLE
                left_tv.text = StringUtils.getString(R.string.to_review)
                right_tv.text = StringUtils.getString(R.string.to_pay)
                left_tv.setOnClickListener {
                    // 评价

                }
                right_tv.setOnClickListener {
                    // 再来一单
                    startActivity(TakeawayActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_ID, item.model_id.toString())
                    })
                }
            }
            else -> {
                left_tv.visibility = View.GONE
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

}