package com.jcs.where.features.hotel.comment.post

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import kotlinx.android.synthetic.main.activity_hotel_comment_post.*


/**
 * Created by Wangsw  2021/8/17 16:12.
 * 发表酒店评价
 */
class HotelCommentPostActivity : BaseMvpActivity<HotelCommentPostPresenter>(), HotelCommentView {


    private var orderId = 0
    private var hotelId = 0

    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun getLayoutId() = R.layout.activity_hotel_comment_post

    override fun isStatusDark(): Boolean = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
            hotelId = it.getInt(Constant.PARAM_HOTEL_ID)
        }

        // 相册
        mImageAdapter = StoreRefundAdapter().apply {
            addChildClickViewIds(R.id.delete_iv)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.delete_iv -> {
                        mImageAdapter.removeAt(position)
                    }
                    else -> {
                    }
                }
            }
        }

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }


    }

    override fun initData() {
        presenter = HotelCommentPostPresenter(this)
        presenter.hotelId = hotelId
    }

    override fun bindListener() {
        select_iv.setOnClickListener {
            val size = mImageAdapter.data.size
            if (size == 6) {
                ToastUtils.showShort(R.string.refund_image_max_6)
                return@setOnClickListener
            }
            val max = 6 - size
            FeaturesUtil.handleMediaSelect(this, Constant.IMG, max)
        }
        commit_tv.setOnClickListener {

            val rating = star_view.rating.toInt()
            val content = content_et.text.toString().trim()

            if (mImageAdapter.data.isNotEmpty()) {
                presenter.upLoadImage(orderId, rating, content, ArrayList(mImageAdapter.data))
            } else {
                presenter.commitStoreComment(orderId, rating, content, null)
            }
        }

    }

    override fun commitSuccess() {
        ToastUtils.showShort(R.string.commit_success)
        finish()
    }

}