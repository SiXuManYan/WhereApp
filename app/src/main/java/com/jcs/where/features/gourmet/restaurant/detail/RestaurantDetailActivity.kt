package com.jcs.where.features.gourmet.restaurant.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.features.gourmet.cart.ShoppingCartActivity
import com.jcs.where.features.gourmet.comment.FoodCommentActivity
import com.jcs.where.features.gourmet.order.OrderSubmitActivity
import com.jcs.where.features.gourmet.restaurant.packages.SetMealActivity
import com.jcs.where.features.gourmet.takeaway.TakeawayActivity
import com.jcs.where.features.hotel.comment.child.HotelCommentAdapter
import com.jcs.where.frams.common.Html5Url
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter
import com.jcs.where.hotel.activity.detail.MediaData
import com.jcs.where.utils.*
import com.jcs.where.widget.NumberView2
import com.jcs.where.widget.list.DividerDecoration
import com.shuyu.gsyvideoplayer.GSYVideoManager
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_restaurant_detail_2.*
import java.util.*

/**
 * Created by Wangsw  2021/4/1 10:28.
 * 餐厅详情
 */
class RestaurantDetailActivity : BaseMvpActivity<RestaurantDetailPresenter>(), RestaurantDetailView {


    /**
     * 餐厅id
     */
    private var mRestaurantId = 0

    private var goodNumber = 1

    /**
     * 餐厅名称
     */
    private var mRestaurantName = ""
    private var mLat = 0.0
    private var mLng = 0.0

    /**
     * 收藏状态（1：未收藏，2：已收藏）
     */
    private var collect_status = 0

    private var isToolbarDark = false
    private var mPhone: CharSequence = ""

    private var webUrl = ""
    private var facebook = ""

    /**
     * 商家融云id
     */
    private var mMerUuid: String = ""

    /**
     * 商家融云聊天名字
     */
    private var mMerName: String? = null

    private lateinit var mDishAdapter: DishAdapter
    private lateinit var mMediaAdapter: DetailMediaAdapter
    private lateinit var mCommentAdapter: HotelCommentAdapter


    companion object {

        fun navigation(context: Context, id: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, id)
            }
            val intent = Intent(context, RestaurantDetailActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_restaurant_detail_2

    override fun initView() {
        mRestaurantId = intent.getIntExtra(Constant.PARAM_ID, 0)
        initMedia()
        initScroll()
        initComment()
        initDish()
    }


    private fun initMedia() {
        mMediaAdapter = DetailMediaAdapter()
        PagerSnapHelper().attachToRecyclerView(media_rv)
        media_rv.apply {
            layoutManager = LinearLayoutManager(this@RestaurantDetailActivity, LinearLayoutManager.HORIZONTAL, false)
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
                        if (GSYVideoManager.isFullState(this@RestaurantDetailActivity)) {
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

        shopping_cart.apply {
            setMessageImageResource(R.mipmap.ic_shopping_cart)
            changeContainerSize(50f, 50f)
            changeMessageCountSize(16f,16f)
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
            setLikeImage()
            setBackImage()
            setShareImage()

            useView.background.alpha = alpha
            toolbar.background.alpha = alpha

            changeStatusTextColor()
        }

    }


    private fun initComment() {
        mCommentAdapter = HotelCommentAdapter()
        comment_rv.apply {
            isNestedScrollingEnabled = true
            adapter = mCommentAdapter
            layoutManager = object : LinearLayoutManager(this@RestaurantDetailActivity) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

    }

    private fun initDish() {

        mDishAdapter = DishAdapter().apply {
            setEmptyView(R.layout.view_empty_text)
            addChildClickViewIds(R.id.buy_tv)
            setOnItemChildClickListener { _, _, position ->
                // 直接购买
                val dish = mDishAdapter.data[position]
                val price = dish.price
                val product = Products().apply {
                    good_data.id = dish.id
                    good_data.title = dish.title
                    good_data.image = dish.image
                    good_data.price = price
                    good_data.original_price = dish.original_price
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
                    putString(Constant.PARAM_TOTAL_PRICE, price.toPlainString())
                })

            }

            setOnItemClickListener { _, view, position ->

                val dish = mDishAdapter.data[position]
                showCompanyDialog(dish)
            }
        }
        dish_rv.apply {
            isNestedScrollingEnabled = true
            adapter = mDishAdapter
            layoutManager = object : LinearLayoutManager(this@RestaurantDetailActivity) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f), 0, 0))
        }

    }


    override fun initData() {
        presenter = RestaurantDetailPresenter(this)
        presenter.getDetail(mRestaurantId)
        presenter.getCartCount()
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

    override fun bindListener() {
        back_iv.setOnClickListener { finish() }
        shopping_cart.setOnClickListener { startActivityAfterLogin(ShoppingCartActivity::class.java) }
        phone_tv.setOnClickListener { tel_iv.performClick() }
        tel_iv.setOnClickListener {
            if (!TextUtils.isEmpty(mPhone)) {
                val intent = Intent(Intent.ACTION_DIAL)
                val data = Uri.parse("tel:$mPhone")
                intent.data = data
                startActivity(intent)
            } else {
                ToastUtils.showShort(R.string.no_contact_information)
            }
        }
        support_takeaway_tv.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.PARAM_ID, mRestaurantId.toString())
            startActivity(TakeawayActivity::class.java, bundle)
        }
        navigation_iv.setOnClickListener {
            if (mLat != 0.0 && mLng != 0.0) {
                FeaturesUtil.startNaviGoogle(this, mLat.toFloat(), mLng.toFloat())
            }
        }

        facebook_tv.setOnClickListener {
            WebViewActivity.goTo(this, facebook)
        }
        share_iv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_RESTAURANT, mRestaurantId)
            MobUtil.shareFacebookWebPage(url, this@RestaurantDetailActivity)
        }
        like_iv.setOnClickListener {
            if (collect_status == 1) {
                presenter.collection(mRestaurantId)
            } else {
                presenter.unCollection(mRestaurantId)
            }
        }
        back_iv.setOnClickListener {
            finish()
        }
        chat_iv.setOnClickListener {
            if (mMerUuid.isNotBlank()) {
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, mMerUuid, mRestaurantName, null)
            }
        }
        findViewById<View>(R.id.more_comment_tv).setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.PARAM_ID, mRestaurantId.toString())
            startActivity(FoodCommentActivity::class.java, bundle)
        }
    }

    override fun bindData(data: RestaurantDetailResponse) {
        val mediaList = ArrayList<MediaData>()
        if (!TextUtils.isEmpty(data.video)) {
            val media = MediaData()
            media.type = MediaData.VIDEO
            media.cover = data.video_image
            media.src = data.video
            mediaList.add(media)
        }
        for (image in data.images) {
            val media = MediaData()
            media.type = MediaData.IMAGE
            media.cover = image
            media.src = image
            mediaList.add(media)
        }
        mMediaAdapter.setNewInstance(mediaList)
        point_view.setPointCount(mediaList.size)
        mRestaurantName = data.title
        name_tv.text = mRestaurantName
        score_tv.text = data.grade.toString()
        star_view.rating = data.grade
        per_price_tv.text = getString(R.string.per_price_format, data.per_price)
        address_name_tv.text = data.address
        time_tv.text = getString(R.string.time_format, data.start_time, data.end_time)
        if (data.take_out_status == 2) {
            support_takeaway_tv.visibility = View.VISIBLE
        } else {
            support_takeaway_tv.visibility = View.GONE
        }
        if (data.im_status == 1) {
            contact_sw.displayedChild = 0
        } else {
            contact_sw.displayedChild = 1
        }
        mLat = data.lat
        mLng = data.lng
        collect_status = data.collect_status
        setLikeImage()

        data.tel.apply {
            if (isNotBlank()) {
                mPhone = this
                phone_tv.text = this
                phone_tv.visibility = View.VISIBLE
            } else {
                phone_tv.visibility = View.GONE
            }
        }

        mMerUuid = data.mer_uuid
        mMerName = data.mer_name

        data.introduction.apply {
            if (isNotBlank()) {
                desc_tv.text = this
            }
        }
        data.email.apply {
            if (isNotBlank()) {
                mail_tv.text = this
                mail_tv.visibility = View.VISIBLE
            } else {
                mail_tv.visibility = View.GONE
            }
        }

        data.website.apply {
            if (isNotBlank()) {
                webUrl = this
                web_tv.text = this
                web_tv.visibility = View.VISIBLE
            } else {
                web_tv.visibility = View.GONE
            }
        }
        data.facebook.apply {
            if (isNotBlank()) {
                facebook = this
                facebook_tv.text = this
                facebook_tv.visibility = View.VISIBLE
            } else {
                facebook_tv.visibility = View.GONE
            }
        }
        mCommentAdapter.setNewInstance(data.comments)
        mDishAdapter.setNewInstance(data.goods)

    }


    override fun collectionHandleSuccess(collectionStatus: Boolean) {
        collect_status = if (collectionStatus) {
            2
        } else {
            1
        }
        setLikeImage()
    }

    private fun setLikeImage() {

        like_iv.setImageResource(
            if (collect_status == 1) {
                if (isToolbarDark) {
                    R.mipmap.ic_like_normal_night
                } else {
                    R.mipmap.ic_like_normal_light
                }
            } else {
                if (isToolbarDark) {
                    R.mipmap.ic_like_red_night
                } else {
                    R.mipmap.ic_like_red_light
                }
            }
        )
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

    private fun setShareImage() {
        share_iv.setImageResource(
            if (isToolbarDark) {
                R.mipmap.ic_share_night
            } else {
                R.mipmap.ic_share_light
            }
        )
    }

    private fun showCompanyDialog(dish: DishResponse) {

        goodNumber = 1

        val addressDialog = BottomSheetDialog(this, R.style.bottom_sheet_edit)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_good_number, null)
        addressDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
        }

        val stockTv = view.findViewById<TextView>(R.id.stock_tv)
        val numberView = view.findViewById<NumberView2>(R.id.number_view)
        view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
            addressDialog.dismiss()
        }

        dish.inventory.apply {
            if (!isNullOrBlank()) {
                stockTv.text = getString(R.string.stock_format_2, this)
                stockTv.visibility = View.VISIBLE
            } else {
                stockTv.visibility = View.GONE
            }
        }

        numberView.apply {
            alwaysEnableCut = true
            MIN_GOOD_NUM = 1
            MAX_GOOD_NUM = BusinessUtils.getSafeStock(dish.inventory)
            cut_iv.setImageResource(R.mipmap.ic_cut_blue)
            add_iv.setImageResource(R.mipmap.ic_add_blue)
            updateNumber(goodNumber)
            cut_iv.visibility = View.VISIBLE
            valueChangeListener = object : NumberView2.OnValueChangeListener {
                override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
                    this@RestaurantDetailActivity.goodNumber = goodNum
                }

            }
        }

        view.findViewById<TextView>(R.id.confirm_tv).setOnClickListener {
            startActivity(SetMealActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, dish.id)
                putInt(Constant.PARAM_RESTAURANT_ID, mRestaurantId)
                putInt(Constant.PARAM_GOOD_NUMBER, this@RestaurantDetailActivity.goodNumber)
                putString(Constant.PARAM_RESTAURANT_NAME, mRestaurantName)
            })

            addressDialog.dismiss()
        }

        addressDialog.show()
    }

    override fun bindCartCount(nums: Int) {
        shopping_cart.setMessageCount(nums)
    }


}