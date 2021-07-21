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
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_store_comment_detail.*


/**
 * Created by Wangsw  2021/7/15 15:55.
 * 商城评价详情
 */
class StoreCommentDetailActivity : BaseMvpActivity<StoreCommentDetailPresenter>(), StoreCommentDetailView {

    private var orderId = 0

    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun getLayoutId() = R.layout.activity_store_comment_detail

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
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
//            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0).apply {
//                setDrawHeaderFooter(false)
//            })
        }

    }

    override fun initData() {
        presenter = StoreCommentDetailPresenter(this)
        presenter.getDetail(orderId)
    }

    override fun bindListener() {

    }

    override fun bingDetail(response: StoreCommentDetail) {

        if (response.shop_images.isNotEmpty()) {
            GlideUtil.load(this, response.shop_images[0], image_iv)
        }

        name_tv.text = response.shop_title
        star_view.rating = response.star

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

    }
}