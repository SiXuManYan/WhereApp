package com.jcs.where.features.store.comment.detail

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.response.store.comment.StoreCommentDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_store_comment_detail.*


/**
 * Created by Wangsw  2021/7/15 15:55.
 * 商城评价详情
 * 0 旧版商城
 * 1 新版商城
 */
class StoreCommentDetailActivity : BaseMvpActivity<StoreCommentDetailPresenter>(), StoreCommentDetailView {

    private var orderId = 0

    /** 0旧版商城 1新版商城 */
    private var useType = 0

    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun getLayoutId() = R.layout.activity_store_comment_detail

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
            useType = it.getInt(Constant.PARAM_TYPE)
        }


        // 相册
        mImageAdapter = StoreRefundAdapter().apply {
            hideDelete = true
        }

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = object : GridLayoutManager(this@StoreCommentDetailActivity, 4, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

    }

    override fun initData() {
        presenter = StoreCommentDetailPresenter(this)
        presenter.getDetail(orderId,useType)
    }

    override fun bindListener() {

    }

    override fun bingDetail(response: StoreCommentDetail) {

        if (response.shop_images.isNotEmpty()) {
            GlideUtil.load(this, response.shop_images[0], image_iv)
        }

        name_tv.text = response.shop_title
        val rating = response.star
        star_view.rating = rating

        val content = response.content
        if (content.isBlank()) {
            comment_text_ll.visibility = View.GONE
        } else {
            comment_text_ll.visibility = View.VISIBLE
            content_tv.text = content
        }


        val merchantReview = response.merchant_review
        if (merchantReview.isNotEmpty()) {
            merchant_rely_ll.visibility = View.VISIBLE
            merchant_rely_tv.text = merchantReview
        } else {
            merchant_rely_ll.visibility = View.GONE
        }

        val list = response.images
        if (list.isNotEmpty()) {
            comment_image_ll.visibility = View.VISIBLE
            mImageAdapter.setNewInstance(list)
        } else {
            comment_image_ll.visibility = View.GONE
        }
        comment_value_tv.text = BusinessUtils.getCommentRatingText(rating.toInt())

    }
}