package com.jcs.where.features.gourmet.takeaway

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_take_away.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/25 10:04.
 * 外卖详情
 */
class TakeawayActivity : BaseMvpActivity<TakeawayPresenter>(), TakeawayView, TakeawayAdapter.OnSelectCountChange {


    /** 餐厅id */
    private lateinit var restaurant_id: String
    private lateinit var mDishAdapter: TakeawayAdapter

    /** 是否收藏 */
    private var like = 1
    private var toolbarStatus = 0
    private var businessPhone = ""

    override fun getLayoutId() = R.layout.activity_take_away

    override fun isStatusDark() = ( toolbarStatus == 1)

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


        comment_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(this@TakeawayActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(10f), 0, 0).apply { setDrawHeaderFooter(true) })
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
        if (like == 1) {
            likeIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hotelwhitelike))
        } else {
            likeIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hotelwhiteunlike))
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
            if (businessPhone.isNotBlank()) {
                val data = Uri.parse("tel:$businessPhone")
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    this.data = data
                }
                startActivity(intent)
            }
        }

        cart_iv.setOnClickListener {
            // 上弹出购物车

        }

        settlement_tv.setOnClickListener {
            val totalSelectCount = presenter.getTotalSelectCount(mDishAdapter)
            if (totalSelectCount <= 0) {
                ToastUtils.showShort(R.string.nothing_selected)
                return@setOnClickListener
            }
            showComing()
        }

    }

    override fun bindData(data: TakeawayDetailResponse) {
        GlideUtil.load(this, data.take_out_image, image_iv)
        like = data.collect_status
        businessPhone = data.tel
        setLikeImage()
        restaurant_tv.text = data.restaurant_name
        time_tv.text = getString(R.string.delivery_time_format, data.delivery_time)

        // 配送费
        val deliveryCost = data.delivery_cost
        if (deliveryCost.compareTo(BigDecimal.ZERO) == 1) {
            delivery_fee_tv.text = getString(R.string.delivery_price_format, deliveryCost.toPlainString())
        } else {
            delivery_fee_tv.text = getString(R.string.free_delivery)
        }

    }

    override fun bindDishList(list: MutableList<DishResponse>) {
        mDishAdapter.setNewInstance(list)
    }

    override fun selectCountChange() {

        val totalPrice = presenter.handlePrice(mDishAdapter)
        val totalCount = presenter.getTotalSelectCount(mDishAdapter)

        total_price_tv.text = getString(R.string.price_unit_format, totalPrice)
        if (totalCount > 0) {
            count_tv.visibility = View.VISIBLE
            count_tv.text = totalCount.toString()
            cart_iv.setImageResource(R.mipmap.ic_cart_blue)
        } else {
            count_tv.visibility = View.GONE
            cart_iv.setImageResource(R.mipmap.ic_cart_gray)
        }
    }

}