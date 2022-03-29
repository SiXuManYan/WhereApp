package com.jcs.where.features.comment.batch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.request.hotel.BatchCommentItem
import com.jcs.where.api.response.order.OrderMallGoods
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.widget.list.DividerDecoration
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_comment_post_batch.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/3/26 14:01.
 *
 */
class BatchCommentActivity : BaseMvpActivity<BatchCommentPresenter>(), BatchCommentView, OnImageAddClickListener {

    /** 订单id */
    private var orderId = 0

    /** 操作添加图片 的item坐标*/
    private var handleImageAddPosition = 0

    override fun isStatusDark() = true

    private lateinit var mAdapter: BatchCommentAdapter

    companion object {

        /**
         * @param orderId 订单id(如需要)
         *
         */
        fun navigation(context: Context, orderId: Int, goods: ArrayList<OrderMallGoods>) {

            // 商品
            val arrayList = ArrayList<BatchCommentItem>()
            goods.forEach {
                val apply = BatchCommentItem().apply {
                    good_id = it.id
                    nativeGoodImage = it.good_image as String
                    nativeGoodName = it.good_title
                    nativeGoodNumber = it.good_num
                }
                arrayList.add(apply)
            }

            // 订单id
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, orderId)
                putSerializable(Constant.PARAM_DATA, arrayList)
            }
            val intent = Intent(context, BatchCommentActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.activity_comment_post_batch

    override fun initView() {
        initContent()
        initExtra()
    }

    private fun initExtra() {
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
            val arrayList = it.getSerializable(Constant.PARAM_DATA) as ArrayList<BatchCommentItem>
            mAdapter.setNewInstance(arrayList)
        }

    }

    private fun initContent() {
        mAdapter = BatchCommentAdapter().apply {
            onImageAddClickListener = this@BatchCommentActivity
        }
        batch_comment_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(getColor(R.color.grey_F5F5F5), SizeUtils.dp2px(16f), 0, 0))

        }

//        val helper = PagerSnapHelper()
//        helper.attachToRecyclerView(batch_comment_rv)

    }

    override fun initData() {
        presenter = BatchCommentPresenter(this)

    }

    override fun bindListener() {

        commit_tv.setOnClickListener {
            commit_tv.isClickable = false
            presenter.handleComment(mAdapter, orderId)


        }
    }

    override fun handleMediaSelect(adapterPosition: Int, max: Int) {
        handleImageAddPosition = adapterPosition
        FeaturesUtil.handleMediaSelect(this, Constant.IMG, max)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 添加图片
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val elements = Matisse.obtainPathResult(data)

        val newImageData = ArrayList<RefundImage>()
        elements.forEach {
            val apply = RefundImage().apply {
                type = RefundImage.TYPE_EDIT
                imageSource = it
            }
            newImageData.add(apply)
        }
        val batchCommentItem = mAdapter.data[handleImageAddPosition]
        batchCommentItem.nativeImage.addAll(newImageData)
        batchCommentItem.star = batchCommentItem.star

        mAdapter.notifyItemChanged(handleImageAddPosition)

    }

    override fun commentSuccess() {
        commit_tv.isClickable = true
        ToastUtils.showShort(R.string.comment_success)
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        finish()
    }

}

