package com.jcs.where.features.comment.batch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.request.hotel.BatchCommentItem
import com.jcs.where.api.response.mall.MallOrderGood
import com.jcs.where.api.response.order.OrderMallGoods
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.widget.list.DividerDecoration
import com.zhihu.matisse.Matisse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_post_batch.*
import org.greenrobot.eventbus.EventBus
import top.zibin.luban.Luban


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

        fun navigation(context: Context, orderId: Int, goods: MutableList<OrderMallGoods>) {

            // 商品
            val arrayList = ArrayList<BatchCommentItem>()
            goods.forEach {
                if (it.status == 1 || it.status == 7) {
                    val apply = BatchCommentItem().apply {
                        good_id = it.good_id
                        nativeGoodImage = it.good_image as String
                        nativeGoodName = it.good_title
                        nativeGoodNumber = it.good_num
                    }
                    arrayList.add(apply)
                }
            }
            start(context, orderId, arrayList)
        }

        fun navigation2(context: Context, orderId: Int, goods: MutableList<MallOrderGood>) {

            // 商品
            val arrayList = ArrayList<BatchCommentItem>()
            goods.forEach {
                if (it.status == 1 || it.status == 7) {
                    val apply = BatchCommentItem().apply {
                        good_id = it.good_id
                        nativeGoodImage = it.good_image
                        nativeGoodName = it.good_title
                        nativeGoodNumber = it.good_num
                    }
                    arrayList.add(apply)
                }
            }
            start(context, orderId, arrayList)
        }


        private fun start(context: Context, orderId: Int, arrayList: ArrayList<BatchCommentItem>) {
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

        Flowable.just("")

    }

    override fun bindListener() {

        commit_tv.setOnClickListener {
            showLoadingDialog(false)
            commit_tv.isClickable = false
            ToastUtils.showLong(R.string.submitting)
//            presenter.handleComment(mAdapter, orderId)
            presenter.handleComment2(mAdapter, orderId)
        }
    }

    override fun handleMediaSelect(adapterPosition: Int, max: Int) {
        handleImageAddPosition = adapterPosition
        FeaturesUtil.handleMediaSelect(this, Constant.IMG, max)
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    }*/


    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 添加图片
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val elements = Matisse.obtainPathResult(data)

        Flowable.just(elements)
            .observeOn(Schedulers.io())
            .map {
                Luban.with(this@BatchCommentActivity).load(elements)
                    .ignoreBy(100)
                    .get()

            }
            .observeOn(AndroidSchedulers.mainThread()) // .subscribe()
            .doOnError {}
            .subscribe {

                val newImageData = ArrayList<RefundImage>()
                it.forEach { file ->
                    val apply = RefundImage().apply {
                        type = RefundImage.TYPE_EDIT
                        imageSource = file.absolutePath
                    }
                    newImageData.add(apply)
                }

                val batchCommentItem = mAdapter.data[handleImageAddPosition]
                batchCommentItem.nativeImage.addAll(newImageData)
                batchCommentItem.star = batchCommentItem.star

                mAdapter.notifyItemChanged(handleImageAddPosition)
            }
    }


    override fun commentSuccess() {
        dismissLoadingDialog()
        commit_tv.isClickable = true
        ToastUtils.showShort(R.string.comment_success)
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        finish()
    }


    override fun onError(errorResponse: ErrorResponse?) {
        super.onError(errorResponse)
        commit_tv.isClickable = true
        dismissLoadingDialog()
    }

}

