package com.jcs.where.features.gourmet.restaurant.packages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SpanUtils
import com.jcs.where.R
import com.jcs.where.api.request.AddCartRequest
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.api.response.gourmet.dish.DishDetailResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.cart.ShoppingCartActivity
import com.jcs.where.features.gourmet.order.OrderSubmitActivity
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter
import com.jcs.where.hotel.activity.detail.MediaData
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_set_meal_2.*
import java.math.BigDecimal
import java.util.*

/**
 * Created by Wangsw  2021/4/6 14:38.
 * 菜品详情
 */
class SetMealActivity : BaseMvpActivity<SetMealPresenter>(), SetMealView {


    private var isToolbarDark = false
    private var mEatInFoodId = 0
    private var mRestaurantId = 0
    private var mRestaurantName = ""
    private var mData: DishDetailResponse? = null
    private lateinit var mMediaAdapter: DetailMediaAdapter

    private var goodNumber = 1
    private var mInventory = 0
    private var singlePrice: BigDecimal = BigDecimal.ZERO
    private var totalPrice: BigDecimal = BigDecimal.ZERO

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_set_meal_2

    override fun initView() {
        initExtra()
        initMedia()
        initScroll()
        number_view.apply {
            alwaysEnableCut = true
            MIN_GOOD_NUM = 1
            MAX_GOOD_NUM = 20
            cut_iv.setImageResource(R.mipmap.ic_cut_blue)
            add_iv.setImageResource(R.mipmap.ic_add_blue)
            cutResIdCommon = R.mipmap.ic_cut_blue
            cutResIdMin = R.mipmap.ic_cut_blue_transparent
            addResIdCommon = R.mipmap.ic_add_blue
            addResIdMax = R.mipmap.ic_add_blue_transparent

            updateNumber(goodNumber)
            cut_iv.visibility = View.VISIBLE
            valueChangeListener = this@SetMealActivity
        }

        shopping_cart.apply {
            setMessageImageResource(R.mipmap.ic_shopping_cart)
            changeContainerSize(50f, 50f)
            changeMessageCountSize(16f,16f)
        }
    }


    private fun initExtra() {
        val bundle = intent.extras
        bundle?.let {
            mEatInFoodId = it.getInt(Constant.PARAM_ID, 0)
            mRestaurantId = it.getInt(Constant.PARAM_RESTAURANT_ID, 0)
            goodNumber = it.getInt(Constant.PARAM_GOOD_NUMBER, 1)
            mRestaurantName = it.getString(Constant.PARAM_RESTAURANT_NAME, "")
        }
    }

    private fun initMedia() {
        mMediaAdapter = DetailMediaAdapter()
        PagerSnapHelper().attachToRecyclerView(media_rv)
        media_rv.apply {
            layoutManager = LinearLayoutManager(this@SetMealActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mMediaAdapter
        }
        media_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastItemPosition = layoutManager.findLastVisibleItemPosition()

                if (GSYVideoManager.instance().playPosition >= 0) {
                    val position = GSYVideoManager.instance().playPosition
                    if (GSYVideoManager.instance().playTag == DetailMediaAdapter.TAG && (position < firstItemPosition || position > lastItemPosition)) {
                        if (GSYVideoManager.isFullState(this@SetMealActivity)) {
                            return
                        }
                        GSYVideoManager.releaseAllVideos()
                        mMediaAdapter.notifyDataSetChanged()
                    }
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    point_view.onPageSelected(firstItemPosition)
                }
            }
        })
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
            useView.background.alpha = alpha
            toolbar.background.alpha = alpha
            changeStatusTextColor()
        }
    }


    override fun initData() {
        presenter = SetMealPresenter(this)
        presenter.getDetail(mEatInFoodId)
        presenter.getCartCount()
    }

    override fun bindListener() {
        shopping_cart.setOnClickListener { startActivityAfterLogin(ShoppingCartActivity::class.java) }
        buy_after_tv.setOnClickListener { onBuyAfter() }
        buy_now_tv.setOnClickListener { onBuyNow() }
    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
        presenter?.getCartCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun bindData(data: DishDetailResponse, inventoryValue: Int) {

        val mediaList = ArrayList<MediaData>()
        val media = MediaData().apply {
            type = MediaData.IMAGE
            cover = data.image
            src = data.image
        }
        mediaList.add(media)
        mMediaAdapter.setNewInstance(mediaList)
        point_view.setPointCount(1)
        mInventory = inventoryValue
        number_view.MAX_GOOD_NUM = inventoryValue
        name_tv.text = data.title

        val price = data.price
        singlePrice = price
        now_price_tv.text = getString(R.string.price_unit_format, price.toPlainString())
        val oldPrice = getString(R.string.price_unit_format, data.original_price)
        val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = builder
        sales_tv.text = getString(R.string.sale_format, data.sale_num)
        set_meal_content_tv.text = data.meals
        rule_tv.text = data.rule
        mData = data
        data.inventory.apply {
            if (!isNullOrBlank()) {
                stock_tv.text = getString(R.string.stock_format_2, this)
            }
        }
        handlePrice()
    }

    override fun bindCartCount(nums: Int) {
        shopping_cart. setMessageCount(nums)
    }

    private fun onBuyNow() {
        val product = Products().apply {
            good_data.id = mData!!.id
            good_data.title = mData!!.title
            good_data.image = mData!!.image
            good_data.price = mData!!.price
            good_data.original_price = mData!!.original_price
            good_num = goodNumber
            nativeIsSelect = true
        }
        val response = ShoppingCartResponse().apply {
            restaurant_id = mRestaurantId.toString()
            nativeIsSelect = true
            restaurant_name = mRestaurantName
            products.add(product)
        }

        val value = ArrayList<ShoppingCartResponse>().apply {
            add(response)
        }
        startActivityAfterLogin(OrderSubmitActivity::class.java, Bundle().apply {
            putSerializable(Constant.PARAM_DATA, value)
            val totalPrice = BigDecimalUtil.mul(mData!!.price, BigDecimal(goodNumber))
            putString(Constant.PARAM_TOTAL_PRICE, totalPrice.toEngineeringString())
        })
    }

    private fun onBuyAfter() {
        val request = AddCartRequest().apply {
            good_id = mData!!.id
            good_num = goodNumber
        }
        presenter.addCart(request)
    }

    override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
        goodNumber = goodNum
        handlePrice()
    }

    private fun handlePrice() {
        totalPrice = BigDecimalUtil.mul(singlePrice, BigDecimal(goodNumber))
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
    }


}