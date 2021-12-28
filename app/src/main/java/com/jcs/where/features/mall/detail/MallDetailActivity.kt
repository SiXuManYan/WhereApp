package com.jcs.where.features.mall.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGoodDetail
import com.jcs.where.api.response.mall.MallSpecs
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.hotel.comment.child.HotelCommentAdapter
import com.jcs.where.features.mall.buy.MallOrderCommitActivity
import com.jcs.where.features.mall.cart.MallCartActivity
import com.jcs.where.features.mall.comment.MallCommentActivity
import com.jcs.where.features.mall.detail.sku.MallSkuFragment
import com.jcs.where.features.mall.detail.sku.MallSkuSelectResult
import com.jcs.where.features.mall.shop.MallShopActivity
import com.jcs.where.frams.common.Html5Url
import com.jcs.where.hotel.activity.detail.DetailMediaAdapter
import com.jcs.where.hotel.activity.detail.MediaData
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.utils.MobUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_mall_good_detail.*

import java.util.*


/**
 * Created by Wangsw  2021/12/10 14:57.
 * 商品详情
 */
class MallDetailActivity : BaseMvpActivity<MallDetailPresenter>(), MallDetailView, MallSkuSelectResult {

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

    private lateinit var mSkuDialog: MallSkuFragment

    /** 评价 */
    private lateinit var mCommentAdapter: HotelCommentAdapter

    companion object {

        fun navigation(context: Context, goodId: Int) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, goodId)
            }
            val intent = Intent(context, MallDetailActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_good_detail

    override fun initView() {
        goodId = intent.getIntExtra(Constant.PARAM_ID, 0)
        mSkuDialog = MallSkuFragment().apply {
            selectResult = this@MallDetailActivity
        }
        initMedia()
        initComment()
    }

    private fun initComment() {
        mCommentAdapter = HotelCommentAdapter()
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
    }


    override fun initData() {
        presenter = MallDetailPresenter(this)
        presenter.getDetail(goodId)
    }

    override fun bindListener() {
        share_iv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_MALL, goodId)
            MobUtil.shareFacebookWebPage(url, this@MallDetailActivity)
        }
        select_attr_tv.setOnClickListener {
            dialogHandle = 0
            mSkuDialog.show(supportFragmentManager, mSkuDialog.tag)
        }
        mall_shop_tv.setOnClickListener {
            MallShopActivity.navigation(this, shopId, shopName)
        }
        like_iv.setOnClickListener {
            if (collect_status == 0) {
                presenter.collection(goodId)
            }else{
                presenter.unCollection(goodId)
            }
        }

        buy_after_tv.setOnClickListener {
            if (mData == null) {
                return@setOnClickListener
            }
//            if (mallSpecs == null) {
//                dialogHandle = 1
//                mSkuDialog.show(supportFragmentManager, mSkuDialog.tag)
//            } else {
//                addCart()
//            }
            dialogHandle = 1
            mSkuDialog.show(supportFragmentManager, mSkuDialog.tag)
        }

        buy_now_tv.setOnClickListener {
            if (mData == null) {
                return@setOnClickListener
            }
//            if (mallSpecs == null) {
//                dialogHandle = 2
//                mSkuDialog.show(supportFragmentManager, mSkuDialog.tag)
//            } else {
//                buyNow()
//            }
            dialogHandle = 2
            mSkuDialog.show(supportFragmentManager, mSkuDialog.tag)
        }

        shopping_cart.setOnClickListener {
            startActivityAfterLogin(MallCartActivity::class.java)
        }

        more_comment_tv.setOnClickListener {
            startActivity(MallCommentActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, goodId)
            })
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
        for (image in response.sub_images) {
            val media = MediaData()
            media.type = MediaData.IMAGE
            media.cover = image
            media.src = image
            mediaList.add(media)
        }
        mMediaAdapter.setNewInstance(mediaList)
        point_view.setPointCount(mediaList.size)

        price_tv.text = getString(R.string.price_unit_format, response.max_price)
        title_tv.text = response.title


        response.desc?.let {
            disPlayHtml(it)
        }

        mSkuDialog.data = response
        shopId = response.shop_id
        shopName = response.shop_name
        collect_status = response.collect_status
        setLikeImage()
        if (response.im_status == 1 && !TextUtils.isEmpty(response.mer_uuid)) {
            mall_service_tv.setOnClickListener { _ ->
                RongIM.getInstance().startConversation(
                    this, Conversation.ConversationType.PRIVATE,
                    response.mer_uuid, response.mer_name, null
                )
            }
        }

        // 评价
        comment_count_tv.text = getString(R.string.comment_format, response.count)
        mCommentAdapter.setNewInstance(response.comments)

    }

    private fun disPlayHtml(html: String) {
        val imageGetter = ImageGetter(this, resources, html_tv)
        val styledText = fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)
        html_tv.text = styledText

        // 图片链接可点击
        html_tv.movementMethod = LinkMovementMethod.getInstance()
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
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    private fun setLikeImage() {

        like_iv.setImageResource(
            if (collect_status == 0) {
                R.mipmap.ic_like_normal_night
            } else {
                R.mipmap.ic_like_red_night
            }
        )
    }

    override fun collectionHandleSuccess(collectionStatus: Boolean) {

        collect_status = if (collectionStatus) {
            1
        } else {
            0
        }
        setLikeImage()
    }

    override fun selectResult(mallSpecs: MallSpecs, goodNum: Int) {

        this.mallSpecs = mallSpecs
        goodNumber = goodNum

        val buff = StringBuffer()
        mallSpecs.specs.values.forEach {
            buff.append("$it, ")
        }
        select_attr_tv.text = buff

        if (!User.isLogon()) {
            startActivity(LoginActivity::class.java)
            return
        }
        when (dialogHandle) {
            1 -> addCart()
            2 -> buyNow()
        }


    }


}