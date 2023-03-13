package com.jiechengsheng.city.features.gourmet.takeaway

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.gourmet.takeaway.TakeawayDetailResponse
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.gourmet.takeaway.submit.OrderSubmitTakeawayActivity
import com.jiechengsheng.city.utils.BigDecimalUtil
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_take_away_2.*
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
    private var packing_charges: BigDecimal = BigDecimal.ZERO

    /** 配送费 */
    private var delivery_cost: BigDecimal = BigDecimal.ZERO

    /** 商家名称 */
    private var restaurant_name = ""

    private lateinit var mDishAdapter: TakeawayAdapter
    private lateinit var mCartAdapter: TakeawayAdapter


    private var isToolbarDark = false

    /** IM聊天开启状态（1：开启，2：关闭） */
    private var im_status = 0
    private var toolbarStatus = 0
    private var businessPhone = ""
    private var mer_uuid = ""
    private var mer_name = ""

    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.activity_take_away_2

    override fun isStatusDark() = isToolbarDark

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
        // 菜品列表
        mDishAdapter = TakeawayAdapter().apply {
            setEmptyView(EmptyView(this@TakeawayActivity).apply {
                showEmptyDefault()
            })
            onSelectCountChange = this@TakeawayActivity
        }

        dish_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(this@TakeawayActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(
                Color.WHITE, SizeUtils.dp2px(10f),
                0, 0
            ).apply { setDrawHeaderFooter(true) })
            adapter = mDishAdapter
        }


        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            setEmptyMessage(R.string.empty_cart)
        }

        // 购物车列表
        mCartAdapter = TakeawayAdapter().apply {
            setEmptyView(emptyView)
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
            addItemDecoration(DividerDecoration(
                Color.WHITE, SizeUtils.dp2px(10f),
                0, 0
            ).apply { setDrawHeaderFooter(true) })
            adapter = mCartAdapter
        }

    }

    private fun initScroll() {

        useView.setBackgroundColor(getColor(R.color.white))
        toolbar.setBackgroundColor(getColor(R.color.white))
        useView.background.alpha = 0
        toolbar.background.alpha = 0
        scrollView.setOnScrollChangeListener { _, _, y, _, _ ->
            val headHeight = media_fl.measuredHeight - toolbar.measuredHeight
            var alpha = (y.toFloat() / headHeight * 255).toInt()
            if (alpha >= 255) {
                alpha = 255
            }
            if (alpha <= 5) {
                alpha = 0
            }
            isToolbarDark = alpha > 130
            setBackImage()

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


    private fun setBackImage() {
        back_iv.setImageResource(
            if (isToolbarDark) {
                R.mipmap.ic_back_black
            } else {
                R.mipmap.ic_back_light
            }
        )
    }

    override fun initData() {
        presenter = TakeawayPresenter(this)
        presenter.getDetailData(restaurant_id)
    }

    override fun bindListener() {

        service_iv.setOnClickListener {
            if (im_status == 1 && mer_uuid.isNotBlank()) {

                BusinessUtils.startRongCloudConversationActivity(this,mer_uuid,restaurant_name)

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
            if (selectedList.isNullOrEmpty()) {
                emptyView.showEmptyContainer()
            }

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
            val totalPrice = presenter.handlePrice(mDishAdapter)
            val selectedList = presenter.getSelectedList(mDishAdapter)
            // 额外加上配送费
            val finalTotalPrice = BigDecimalUtil.add(totalPrice, BigDecimalUtil.add(delivery_cost, packing_charges))

            startActivityAfterLogin(OrderSubmitTakeawayActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_ID, restaurant_id)
                putString(Constant.PARAM_PACKING_CHARGES, packing_charges.toPlainString())
                putString(Constant.PARAM_DELIVERY_COST, delivery_cost.toPlainString())
                putString(Constant.PARAM_NAME, restaurant_name)
                putString(Constant.PARAM_TOTAL_PRICE, finalTotalPrice.toPlainString())
                putSerializable(Constant.PARAM_DATA, selectedList)
            })
        }

        clear_cart_tv.setOnClickListener {
            mCartAdapter.setNewInstance(null)
            emptyView.showEmptyContainer()
            presenter.clearCart(mDishAdapter)
            handleBottom()
        }

    }

    override fun bindData(data: TakeawayDetailResponse) {

        GlideUtil.load(this, data.take_out_image, media_iv)


        businessPhone = data.tel
        mer_uuid = data.mer_uuid
        mer_name = data.mer_name
        val restaurantName = data.restaurant_name
        restaurant_name = restaurantName
        restaurant_tv.text = restaurantName
        time_tv.text = getString(R.string.delivery_time_format, data.delivery_time)

        // 配送费
        delivery_cost = data.delivery_cost
        if (delivery_cost.compareTo(BigDecimal.ZERO) == 1) {
            delivery_fee_tv.text = getString(R.string.delivery_price_format_3, delivery_cost.toPlainString())
        } else {
            delivery_fee_tv.text = getString(R.string.free_delivery)
        }

        // 包装费
        packing_charges = data.packing_charges
        packaging_fee_tv.text = getString(R.string.packaging_fee_format, packing_charges.toPlainString())


        // 聊天
        im_status = data.im_status

        mDishAdapter.setNewInstance(data.goods)

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
            settlement_tv.alpha = 1f
        } else {
            count_tv.visibility = View.GONE
            cart_iv.setImageResource(R.mipmap.ic_cart_gray)
            settlement_tv.alpha = 0.5f

        }

    }


}