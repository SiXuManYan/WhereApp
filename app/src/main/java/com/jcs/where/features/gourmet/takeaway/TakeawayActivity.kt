package com.jcs.where.features.gourmet.takeaway

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.dish.DishTakeawayResponse
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.takeaway.submit.OrderSubmitTakeawayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_take_away.*
import kotlinx.android.synthetic.main.item_search.*
import kotlinx.android.synthetic.main.layout_take_away_shopping_cart.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/25 10:04.
 * 外卖详情
 */
class TakeawayActivity : BaseMvpActivity<TakeawayPresenter>(), TakeawayView, TakeawayAdapter.OnSelectCountChange {


    /** 餐厅id */
    private lateinit var restaurant_id: String

    /** 包装费 */
    private var packing_charges = "0"

    /** 配送费 */
    private var delivery_cost = "0"

    /** 商家名称 */
    private var restaurant_name = ""

    private lateinit var mDishAdapter: TakeawayAdapter
    private lateinit var mCartAdapter: TakeawayAdapter

    /** 是否收藏 */
    private var like = 1

    /** IM聊天开启状态（1：开启，2：关闭） */
    private var im_status = 0
    private var toolbarStatus = 0
    private var businessPhone = ""
    private var mer_uuid = ""
    private var mer_name = ""

    override fun getLayoutId() = R.layout.activity_take_away

    override fun isStatusDark() = (toolbarStatus == 1)

    override fun initView() {

        val id = intent.getStringExtra(Constant.PARAM_ID)
        if (id.isNullOrBlank()) {
            finish()
            return
        }
        restaurant_id = id
        initRecyclerView()
        initScroll()
    }

    private fun initRecyclerView() {


        mDishAdapter = TakeawayAdapter().apply {
            setEmptyView(EmptyView(this@TakeawayActivity).apply {
                showEmptyDefault()
            })
            onSelectCountChange = this@TakeawayActivity
        }


        // 菜品列表
        dish_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(this@TakeawayActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(10f),
                    0, 0).apply { setDrawHeaderFooter(true) })
            adapter = mDishAdapter
        }


        // 评论列表
        comment_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(this@TakeawayActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(10f), 0, 0).apply { setDrawHeaderFooter(true) })
        }

        // 购物车列表
        mCartAdapter = TakeawayAdapter().apply {
            setEmptyView(EmptyView(this@TakeawayActivity).apply {
                showEmptyDefault()
            })
            onSelectCountChange = object : TakeawayAdapter.OnSelectCountChange {

                override fun selectCountChange(goodNum: Int, id: Int) {

                    // 更新菜品列表数量
                    mDishAdapter.data.forEachIndexed { index, dishResponse ->
                        if (dishResponse.id == id) {
                            dishResponse.nativeSelectCount = goodNum
                            mDishAdapter.notifyItemChanged(index)
                        }
                    }
                    // 更新
                    handleBottom()
                }
            }
        }

        shopping_cart_rv.apply {
            layoutManager = LinearLayoutManager(this@TakeawayActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(10f),
                    0, 0).apply { setDrawHeaderFooter(true) })
            adapter = mCartAdapter
        }

    }

    private fun initScroll() {

        // alpha
        useView.setBackgroundColor(getColor(R.color.white))
        toolbar.setBackgroundColor(getColor(R.color.white))

        useView.background.alpha = 0
        toolbar.background.alpha = 0
        scrollView.setOnScrollChangeListener { _, _, y, _, _ ->
            val headHeight = image_iv.measuredHeight - toolbar.measuredHeight
            var alpha = (y.toFloat() / headHeight * 255).toInt()
            if (alpha >= 255) {
                alpha = 255
            }
            if (alpha <= 5) {
                alpha = 0
            }
            if (alpha > 130) {
                setLikeImage()
                shareIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_share_black))
                back_iv.setImageResource(R.drawable.ic_back_black)
                toolbarStatus = 1
            } else {
                setLikeImage()
                shareIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_share_white))
                back_iv.setImageResource(R.drawable.ic_back_white)
                toolbarStatus = 0
            }
            useView.background.alpha = alpha
            toolbar.background.alpha = alpha
            if (alpha == 255) {
                titleTv.text = restaurant_tv.text
            }
            if (alpha == 0) {
                titleTv.text = ""
            }
            changeStatusTextColor()
        }


    }

    private fun setLikeImage() {
        if (like == 2) {
            likeIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hotelwhitelike))
        } else {
            likeIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hoteltransparentunlike))
        }
    }

    override fun initData() {
        presenter = TakeawayPresenter(this)
        presenter.getDetailData(restaurant_id)
        presenter.getDishList(restaurant_id)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }

        shareIv.setOnClickListener {

        }
        likeIv.setOnClickListener {

        }

        service_iv.setOnClickListener {

            if (im_status == 1 && mer_uuid.isNotBlank()) {
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, mer_uuid, restaurant_name, null)
            } else {
                if (businessPhone.isNotBlank()) {
                    val data = Uri.parse("tel:$businessPhone")
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        this.data = data
                    }
                    startActivity(intent)
                }
            }

        }

        cart_iv.setOnClickListener {

            val selectedList = presenter.getSelectedList(mDishAdapter)
            mCartAdapter.setNewInstance(selectedList)

            if (cart_ll.visibility != View.VISIBLE) {
                cart_ll.visibility = View.VISIBLE
            } else {
                cart_ll.visibility = View.GONE
            }
        }

        close_cart_v.setOnClickListener {
            cart_ll.visibility = View.GONE
        }

        settlement_tv.setOnClickListener {
            val totalSelectCount = presenter.getTotalSelectCount(mDishAdapter)
            if (totalSelectCount <= 0) {
                ToastUtils.showShort(R.string.nothing_selected)
                return@setOnClickListener
            }
            val totalPrice = presenter.handlePrice(mDishAdapter).toPlainString()
            val selectedList = presenter.getSelectedList(mDishAdapter)

            startActivityAfterLogin(OrderSubmitTakeawayActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_ID, restaurant_id)
                putString(Constant.PARAM_PACKING_CHARGES, packing_charges)
                putString(Constant.PARAM_DELIVERY_COST, delivery_cost)
                putString(Constant.PARAM_DELIVERY_COST, delivery_cost)
                putString(Constant.PARAM_NAME, restaurant_name)
                putString(Constant.PARAM_TOTAL_PRICE, totalPrice)
                putSerializable(Constant.PARAM_DATA, selectedList)
            })
        }

    }

    override fun bindData(data: TakeawayDetailResponse) {
        GlideUtil.load(this, data.take_out_image, image_iv)
        like = data.collect_status
        businessPhone = data.tel
        mer_uuid = data.mer_uuid
        mer_name = data.mer_name
        like = data.collect_status
        setLikeImage()
        val restaurantName = data.restaurant_name
        restaurant_name = restaurantName
        restaurant_tv.text = restaurantName
        time_tv.text = getString(R.string.delivery_time_format, data.delivery_time)

        // 配送费
        val deliveryCost = data.delivery_cost
        if (deliveryCost.compareTo(BigDecimal.ZERO) == 1) {
            delivery_fee_tv.text = getString(R.string.delivery_price_format, deliveryCost.toPlainString())
        } else {
            delivery_fee_tv.text = getString(R.string.free_delivery)
        }
        delivery_cost = deliveryCost.toPlainString()

        // 包装费
        val toPlainString = data.packing_charges.toPlainString()
        packaging_fee_tv.text = getString(R.string.price_unit_format, toPlainString)
        packing_charges = toPlainString

        // 聊天
        im_status = data.im_status
    }

    override fun bindDishList(list: MutableList<DishTakeawayResponse>) {
        mDishAdapter.setNewInstance(list)
    }

    override fun selectCountChange(goodNum: Int, id: Int) = handleBottom()

    /**
     * 购物车数量、总价、是否能结算
     */
    private fun handleBottom() {
        val totalPrice = presenter.handlePrice(mDishAdapter)
        val totalCount = presenter.getTotalSelectCount(mDishAdapter)

        // 总价
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice)

        // 购物车数量
        if (totalCount > 0) {
            count_tv.visibility = View.VISIBLE
            count_tv.text = totalCount.toString()
            cart_iv.setImageResource(R.mipmap.ic_cart_blue)
            settlement_tv.setBackgroundResource(R.drawable.shape_gradient_orange)
        } else {
            count_tv.visibility = View.GONE
            cart_iv.setImageResource(R.mipmap.ic_cart_gray)
            settlement_tv.setBackgroundResource(R.drawable.shape_gradient_orange_un_enable)

        }

    }

}