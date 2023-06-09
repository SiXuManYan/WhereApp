package com.jiechengsheng.city.features.mall.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.MtjDuration
import com.jiechengsheng.city.api.response.mall.MallAttribute
import com.jiechengsheng.city.api.response.mall.MallGoodDetail
import com.jiechengsheng.city.api.response.mall.MallSpecs
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.hotel.comment.child.HotelCommentAdapter
import com.jiechengsheng.city.features.hotel.detail.media.DetailMediaAdapter
import com.jiechengsheng.city.features.hotel.detail.media.MediaData
import com.jiechengsheng.city.features.mall.buy.MallOrderCommitActivity
import com.jiechengsheng.city.features.mall.cart.MallCartActivity
import com.jiechengsheng.city.features.mall.complex.CommentComplexActivity
import com.jiechengsheng.city.features.mall.shop.home.MallShopHomeActivity
import com.jiechengsheng.city.features.mall.sku.bean.Sku
import com.jiechengsheng.city.features.mall.sku.other.Product
import com.jiechengsheng.city.features.mall.sku.other.SkuFragment
import com.jiechengsheng.city.features.message.custom.CustomMessage
import com.jiechengsheng.city.frames.common.Html5Url
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.MobUtil
import com.jiechengsheng.city.view.empty.EmptyView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_mall_good_detail.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by Wangsw  2021/12/10 14:57.
 * 商品详情
 */
class MallDetailActivity : BaseMvpActivity<MallDetailPresenter>(), MallDetailView, SkuFragment.Callback {


    private var goodId = 0
    private var shopId = 0
    private var shopName: String? = ""
    private var goodNumber = 1
    private var mData: MallGoodDetail? = null
    private var mallSpecs: MallSpecs? = null

    /** 0只展示 1加入购物车 2直接购买 */
    private var dialogHandle = 0


    /** 收藏状态（0：未收藏，1：已收藏） */
    private var collect_status = 0

    private lateinit var mMediaAdapter: DetailMediaAdapter

    private lateinit var skuDialog: SkuFragment


    /** 评价 */
    private lateinit var mCommentAdapter: HotelCommentAdapter

    private lateinit var emptyView: EmptyView


    private var duration = 0L

    private var isToolbarDark = false

    companion object {

        fun navigation(context: Context, goodId: Int, couponId: Int? = 0) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, goodId)
                couponId?.let {
                    putInt(Constant.PARAM_COUPON_ID, couponId)
                }
            }
            val intent = Intent(context, MallDetailActivity::class.java)
                .putExtras(bundle)
//                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_mall_good_detail

    override fun initView() {
        goodId = intent.getIntExtra(Constant.PARAM_ID, 0)
        skuDialog = SkuFragment().apply {
            callback = this@MallDetailActivity
        }
        initScroll()
        initMedia()
        initComment()
        initWeb()
        duration = System.currentTimeMillis()
    }

    private fun initScroll() {
        // alpha
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
        emptyView = EmptyView(this).apply {
            empty_iv.visibility = View.GONE
            parent_ll.layoutParams.height = SizeUtils.dp2px(92f)
            setEmptyHint(R.string.no_content)
            addEmptyList(this)
        }

        mCommentAdapter = HotelCommentAdapter().apply {
            setEmptyView(emptyView)
        }
        comment_rv.isNestedScrollingEnabled = true
        comment_rv.layoutManager = object : LinearLayoutManager(this, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
        comment_rv.adapter = mCommentAdapter
    }


    private fun initMedia() {
        point_view.apply {
            selectedDrawableResId = R.drawable.shape_point_selected_377bff
            commonDrawableResId = R.drawable.shape_point_selected_d8d8d8
        }
        mMediaAdapter = DetailMediaAdapter()

        PagerSnapHelper().attachToRecyclerView(media_rv)
        media_rv.apply {
            layoutManager = LinearLayoutManager(this@MallDetailActivity, LinearLayoutManager.HORIZONTAL, false)
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
                        if (GSYVideoManager.isFullState(this@MallDetailActivity)) {
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
            changeMessageCountSize(14f, 14f)
        }
    }


    private fun initWeb() {

    }

    override fun initData() {
        presenter = MallDetailPresenter(this)
        presenter.getDetail(goodId)
        presenter?.getCartCount()
    }


    override fun bindListener() {
        share_iv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_MALL, goodId)
            MobUtil.shareFacebookWebPage(url, this@MallDetailActivity)
        }
        sku_rl.setOnClickListener {
            dialogHandle = 0
            skuDialog.selectedSku = lastSelectedSku
            skuDialog.show(supportFragmentManager, skuDialog.tag)


        }
        mall_shop_tv.setOnClickListener {
            MallShopHomeActivity.navigation(this, shopId)
        }
        like_iv.setOnClickListener {
            if (collect_status == 0) {
                presenter.collection(goodId)
            } else {
                presenter.unCollection(goodId)
            }

        }

        buy_after_tv.setOnClickListener {
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
            if (mData == null) {
                return@setOnClickListener
            }
            dialogHandle = 1
            skuDialog.selectedSku = lastSelectedSku
            skuDialog.show(supportFragmentManager, skuDialog.tag)
        }

        buy_now_tv.setOnClickListener {
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
            if (mData == null) {
                return@setOnClickListener
            }
            dialogHandle = 2
            skuDialog.selectedSku = lastSelectedSku
            skuDialog.show(supportFragmentManager, skuDialog.tag)
        }

        shopping_cart.setOnClickListener {
            startActivityAfterLogin(MallCartActivity::class.java)
        }

        more_comment_tv.setOnClickListener {
            CommentComplexActivity.navigation(this, goodId, CommentComplexActivity.USE_TYPE_STORE)
        }
    }

    private fun buyNow() {
        val selectedData = presenter.getSelectedData(mData!!, mallSpecs!!, goodNumber)

        startActivityAfterLogin(MallOrderCommitActivity::class.java, Bundle().apply {
            putSerializable(Constant.PARAM_DATA, selectedData)
        })
    }

    private fun addCart() {
        presenter.addCart(goodId, goodNumber, mallSpecs!!.specs_id)
    }

    override fun bindDetail(response: MallGoodDetail) {
        mData = response
        val mediaList = ArrayList<MediaData>()

        response.video?.let {
            val media = MediaData()
            media.type = MediaData.VIDEO
            if (!response.video_image.isNullOrBlank()) {
                media.cover = response.video_image!!
            }
            media.src = it
            mediaList.add(media)
        }

        for (image in response.sub_images) {
            val media = MediaData()
            media.type = MediaData.IMAGE
            media.cover = image
            media.src = image
            mediaList.add(media)
        }

        mMediaAdapter.setNewInstance(mediaList)
        point_view.setPointCount(mediaList.size)

        val nowPrice = response.price
        val originalPrice = response.original_cost
        BusinessUtils.setNowPriceAndOldPrice(nowPrice, originalPrice, price_tv, original_price_tv)


        title_tv.text = response.title


        response.desc?.let {
            disPlayHtml(it)
        }

//        jsweb.loadUrl(response.website)

        shopId = response.shop_id
        shopName = response.shop_name
        collect_status = response.collect_status
        setLikeImage()
        if (response.im_status == 1 && !TextUtils.isEmpty(response.mer_uuid)) {
            mall_service_tv.setOnClickListener {

                val customMessage = CustomMessage(goodId, response.main_image, response.title, nowPrice.toPlainString())

                BusinessUtils.startRongCloudConversationActivity(this, response.mer_uuid, response.mer_name, null, Bundle().apply {
                    putInt(Constant.PARAM_TYPE, 1)
                    putParcelable(Constant.PARAM_GOOD_DATA, customMessage)
                    putInt(Constant.PARAM_GOOD_ID , goodId)
                    putInt(Constant.PARAM_SHOP_ID , shopId)
                })
            }
        }

        // 评价
        comment_count_tv.text = getString(R.string.comment_format, response.count)

        val list = response.comments
        if (list.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
        mCommentAdapter.setNewInstance(list)

        // 预估配送费
        estimated_shipping_tv.text = getString(R.string.price_unit_format, response.estimated_delivery_fee)
    }


    private fun disPlayHtml(html: String) {

        if (html.isNotBlank()) {

            val imageGetter = ImageGetter2(this, resources, html_tv)
            val styledText = fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)
            html_tv.text = styledText

            // 图片链接可点击
            html_tv.movementMethod = LinkMovementMethod.getInstance()

        } else {
            html_tv.setText(R.string.no_content)
            html_tv.gravity = Gravity.CENTER
        }

    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
//        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
//        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
        GSYVideoManager.onResume()
        presenter?.getCartCount()
    }


    private fun setLikeImage() {

        like_iv.setImageResource(

            if (collect_status == 1) {
                if (isToolbarDark) {
                    R.mipmap.ic_like_red_night
                } else {
                    R.mipmap.ic_like_red_light
                }
            } else {
                if (isToolbarDark) {
                    R.mipmap.ic_like_normal_night
                } else {
                    R.mipmap.ic_like_normal_light
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

    override fun collectionHandleSuccess(collectionStatus: Boolean) {

        if (collectionStatus) {
            collect_status = 1
            ToastUtils.showShort(R.string.collection_success)
        } else {
            collect_status = 0
            ToastUtils.showShort(R.string.cancel_collection_success)
        }
        setLikeImage()

    }

    override fun bindCartCount(nums: Int) {
        shopping_cart.setMessageCount(nums)
    }

    override fun bindSkuProduct(product: Product, attributeList: ArrayList<MallAttribute>) {
        skuDialog.setData(product, attributeList)
    }

    var lastSelectedSku: Sku? = null

    override fun onAdded(sku: Sku?, quantity: Int) {
        lastSelectedSku = sku
        // 数量
        goodNumber = quantity
        // 获取选中sku
        if (mData == null || sku == null) {
            return
        }

        val skuId = sku.id

        mData!!.specs.forEach {
            if (it.id.toString() == skuId) {
                this.mallSpecs = it
                return@forEach
            }
        }
        if (mallSpecs != null) {
            val buff = StringBuffer()
            mallSpecs!!.specs.values.forEach {
                buff.append("$it, ")
            }
            buff.append(" " + getString(R.string.quantity_format, goodNumber))
            select_attr_tv.text = buff
        }

        if (!User.isLogon()) {
            startActivity(LoginActivity::class.java)
            return
        }
        when (dialogHandle) {
            1 -> addCart()
            2 -> buyNow()
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_POSITION -> {
                val position = baseEvent.data as Int
                media_rv.scrollToPosition(position)
                point_view.onPageSelected(position)
            }
            else -> {}
        }

    }

    override fun onDestroy() {


        duration = System.currentTimeMillis() - duration

        if (User.isLogon()) {
            val mtj = MtjDuration().apply {
                goods_id = goodId
                time = duration
                shop_id = shopId
                user_id = User.getInstance().id
            }
            EventBus.getDefault().post(BaseEvent<MtjDuration>(EventCode.EVENT_MTJ_DURATION,mtj))
        }




        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }


}