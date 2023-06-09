package com.jiechengsheng.city.features.order

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.api.response.order.OrderListResponse
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.comment.CommentPostActivity
import com.jiechengsheng.city.features.comment.batch.BatchCommentActivity
import com.jiechengsheng.city.features.gourmet.comment.post.FoodCommentPostActivity
import com.jiechengsheng.city.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jiechengsheng.city.features.hotel.detail.HotelDetailActivity2
import com.jiechengsheng.city.features.mall.order.MallOrderDetailActivity
import com.jiechengsheng.city.features.mall.shop.home.MallShopHomeActivity
import com.jiechengsheng.city.features.payment.counter.PayCounterActivity
import com.jiechengsheng.city.features.store.comment.detail.StoreCommentDetailActivity
import com.jiechengsheng.city.features.store.comment.post.StoreCommentPostActivity
import com.jiechengsheng.city.features.store.detail.StoreDetailActivity
import com.jiechengsheng.city.features.web.WebViewActivity
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.FeaturesUtil
import com.jiechengsheng.city.utils.image.GlideRoundedCornersTransform
import com.jiechengsheng.city.widget.calendar.JcsCalendarDialog
import com.jiechengsheng.city.widget.list.DividerDecoration

/**
 * Created by Wangsw  2021/5/12 10:00.
 */
open class OrderListAdapter : BaseMultiItemQuickAdapter<OrderListResponse, BaseViewHolder>(), LoadMoreModule {


    init {
        addItemType(OrderListResponse.ORDER_TYPE_HOTEL_1, R.layout.item_order_list_hotel)
        addItemType(OrderListResponse.ORDER_TYPE_DINE_2, R.layout.item_order_list_food)
        addItemType(OrderListResponse.ORDER_TYPE_TAKEAWAY_3, R.layout.item_order_list_takeaway)
        addItemType(OrderListResponse.ORDER_TYPE_STORE_4, R.layout.item_order_list_store)
        addItemType(OrderListResponse.ORDER_TYPE_STORE_MALL_5, R.layout.item_order_list_mall)
    }

    /** mall 商城确认收货 */
    var confirmReceipt: ConfirmReceipt? = null

    /** 订单超时，刷新列表 */
    var orderTimeOut: OrderTimeOut? = null


    override fun convert(holder: BaseViewHolder, item: OrderListResponse) {

        when (holder.itemViewType) {
            OrderListResponse.ORDER_TYPE_HOTEL_1 -> bindHotelItem(holder, item)
            OrderListResponse.ORDER_TYPE_DINE_2 -> bindFoodItem(holder, item)
            OrderListResponse.ORDER_TYPE_TAKEAWAY_3 -> bindTakeawayItem(holder, item)
            OrderListResponse.ORDER_TYPE_STORE_4 -> bindStoreItem(holder, item)
            OrderListResponse.ORDER_TYPE_STORE_MALL_5 -> bindMallItem(holder, item)
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
            dialog.initCalendar()

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
        third_tv.text = StringUtils.getString(R.string.total_price_format, modelData.room_price.toPlainString())

        // 底部
        val right_tv = holder.getView<TextView>(R.id.right_tv)

        when (status) {
            1 -> {
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_pay_2)
                right_tv.setOnClickListener {
                    // 立即支付
                    val payTime = item.pay_time * 1000
                    if (System.currentTimeMillis() <= payTime) {
                        val orderIds = ArrayList<Int>()
                        orderIds.add(item.id)
                        /*          startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                                      putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
                                      putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                                      putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_HOTEL)
                                  })*/
//                        WebPayActivity.navigation(context, Constant.PAY_INFO_HOTEL, orderIds)

                        PayCounterActivity.navigation(context, PayUrlGet.HOTEL, orderIds, item.price.toPlainString())
                    } else {
                        ToastUtils.showShort(R.string.order_time_out)
                        orderTimeOut?.timeOutRefresh()
                    }


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
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_pay_2)

                right_tv.setOnClickListener {
                    // 立即支付
                    val payTime = item.pay_time * 1000
                    if (System.currentTimeMillis() <= payTime) {
                        val orderIds = ArrayList<Int>()
                        orderIds.add(item.id)
//                        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
//                            putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
//                            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
//                            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_FOOD)
//                        })

//                        WebPayActivity.navigation(context, Constant.PAY_INFO_FOOD, orderIds)

                        PayCounterActivity.navigation(context, PayUrlGet.RESTAURANT, orderIds, item.price.toPlainString())

                    } else {
                        ToastUtils.showShort(R.string.order_time_out)
                        orderTimeOut?.timeOutRefresh()
                    }

                }
            }

            6 -> {
                // 交易成功待评价
                if (modelData.comment_status == 1) {
                    right_tv.visibility = View.VISIBLE
                    right_tv.text = StringUtils.getString(R.string.to_review)
                    right_tv.setOnClickListener {
                        startActivity(FoodCommentPostActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_ORDER_ID, item.id)
                            putInt(Constant.PARAM_RESTAURANT_ID, item.model_id)
                            putInt(Constant.PARAM_TYPE, 1)
                        })
                    }
                } else {
                    right_tv.visibility = View.GONE
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
        val status = modelData.order_status

        order_status_tv.text = BusinessUtils.getTakeawayStatusText(status)

        if (status == 1 || status == 8) {
            order_status_tv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
        } else {
            order_status_tv.setTextColor(ColorUtils.getColor(R.color.black_333333))
        }

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

        when (status) {

            1 -> {
                right_tv.visibility = View.VISIBLE
                right_tv.text = StringUtils.getString(R.string.to_pay_2)

                right_tv.setOnClickListener {

                    // 立即支付
                    val payTime = item.pay_time * 1000
                    if (System.currentTimeMillis() <= payTime) {
                        val orderIds = ArrayList<Int>()
                        orderIds.add(item.id)
//                        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
//                            putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
//                            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
//                            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_TAKEAWAY)
//                        })
//                        WebPayActivity.navigation(context, Constant.PAY_INFO_TAKEAWAY, orderIds)
                        PayCounterActivity.navigation(context, PayUrlGet.TAKEAWAY, orderIds, item.price.toPlainString())
                    } else {
                        ToastUtils.showShort(R.string.order_time_out)
                        orderTimeOut?.timeOutRefresh()
                    }


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
            val goodImage = goods[0].good_image as ArrayList<String>
            if (goodImage.isNotEmpty()) {
                Glide.with(context).load(goodImage[0]).apply(options).into(image_iv)
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
//                    startActivity(StorePayActivity::class.java, Bundle().apply {
//                        putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
//                        putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
//                        putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_ESTORE)
//                    })

//                    WebPayActivity.navigation(context, Constant.PAY_INFO_ESTORE, orderIds)
                    PayCounterActivity.navigation(context, PayUrlGet.MALL, orderIds, item.price.toPlainString())
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
                                putInt(Constant.PARAM_TYPE, 0)
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


    /** mall 商城 */
    private fun bindMallItem(holder: BaseViewHolder, item: OrderListResponse) {

        val goodAdapter = MallGoodAdapter()

        val modelData = item.model_data
        if (modelData == null) {
            return
        }

        val goods = modelData.goods


        // 标题
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        name_tv.text = item.title
        name_tv.setOnClickListener {
            MallShopHomeActivity.navigation(context, item.model_id)
        }

        // 状态

        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)
        FeaturesUtil.bindStoreOrderStatus(modelData.order_status, modelData.delivery_type, order_status_tv)

        // 内容
        val title_rl = holder.getView<RelativeLayout>(R.id.title_rl)
        title_rl.setOnClickListener {
            startActivity(MallOrderDetailActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, item.id)
            })
        }


        // 底部
        val right_tv = holder.getView<TextView>(R.id.right_tv)
        val left_tv = holder.getView<TextView>(R.id.left_tv)
        /**
         * 订单状态，
         *
         * 订单状态，
         * 自提时：（1：待付款，2：支付审核中，           4：待使用，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货，13交易关闭），
         * 配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货，13交易关闭）
         *
         * */
        when (modelData.order_status) {
            1 -> {
                right_tv.text = context.getString(R.string.to_pay_2)
                right_tv.visibility = View.VISIBLE

                right_tv.setOnClickListener {
                    val payTime = item.pay_time * 1000
                    if (System.currentTimeMillis() <= payTime) {
                        val orderIds = ArrayList<Int>()
                        orderIds.add(item.id)
//                        startActivity(StorePayActivity::class.java, Bundle().apply {
//                            putDouble(Constant.PARAM_TOTAL_PRICE, item.price.toDouble())
//                            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
//                            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_MALL)
//                        })
//                        WebPayActivity.navigation(context, Constant.PAY_INFO_MALL, orderIds)
                        PayCounterActivity.navigation(context, PayUrlGet.MALL, orderIds, item.price.toPlainString())
                    } else {
                        ToastUtils.showShort(R.string.order_time_out)
                        orderTimeOut?.timeOutRefresh()
                    }

                }
            }
            4 -> {

                right_tv.text = context.getString(R.string.confirm_receipt)
                right_tv.visibility = View.VISIBLE
                right_tv.setOnClickListener {
                    confirmReceipt?.onConfirmReceiptClick(item.id)
                }
                if (item.wl_url.isNotBlank()) {
                    left_tv.visibility = View.VISIBLE
                    left_tv.setText(R.string.express_check)
                    left_tv.setOnClickListener {
                        WebViewActivity.navigation(context, item.wl_url, true)
                    }
                } else {
                    left_tv.visibility = View.GONE
                }

            }
            5 -> {
                right_tv.visibility = View.GONE
                val commentStatus = modelData.comment_status
                val orderCommentStatus = modelData.order_comment_status
                if (commentStatus == 1 && orderCommentStatus == 1) {
                    right_tv.visibility = View.VISIBLE
                    right_tv.text = context.getString(R.string.evaluation_go)
                    right_tv.setOnClickListener {
                        // 去评价
                        BatchCommentActivity.navigation(context, item.id, modelData.goods)
                    }
                } else {
                    right_tv.visibility = View.GONE
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

        // 商品相关
        val total_money_tv = holder.getView<TextView>(R.id.total_money_tv)
        val total_count_tv = holder.getView<TextView>(R.id.total_count_tv)
        val child_good_rv = holder.getView<RecyclerView>(R.id.child_good_rv)
        val price_ll = holder.getView<LinearLayout>(R.id.price_ll)

        total_money_tv.text = StringUtils.getString(R.string.price_unit_format, item.price.toPlainString())
        total_count_tv.text = StringUtils.getString(R.string.total_count_format, item.num)
        price_ll.setOnClickListener {
            startActivity(MallOrderDetailActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, item.id)
            })
        }

        goodAdapter.showSku = goods.size <= 1
        goodAdapter.setNewInstance(goods)
        goodAdapter.setOnItemClickListener { _, _, _ ->
            startActivity(MallOrderDetailActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, item.id)
            })
        }

        child_good_rv.apply {
            adapter = goodAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            if (itemDecorationCount == 0) {
                addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(8f), 0, 0))
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
        val token = CacheUtil.getToken()
        if (TextUtils.isEmpty(token)) {
            startActivity(LoginActivity::class.java, null)
        } else {
            startActivity(target, bundle)
        }
    }

}

interface ConfirmReceipt {
    fun onConfirmReceiptClick(orderId: Int)
}

interface OrderTimeOut {
    fun timeOutRefresh()
}


