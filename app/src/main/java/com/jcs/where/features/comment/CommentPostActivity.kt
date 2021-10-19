package com.jcs.where.features.comment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.ratingstar.OnChangeRatingByClickListener
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_comment_post.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by Wangsw  2021/10/19 15:04.
 * 发表评价
 * 0 酒店
 * 1 旅游
 */
class CommentPostActivity : BaseMvpActivity<CommentPostPresenter>(), CommentView {

    /** 订单id */
    private var orderId = 0

    /** 业务id 如：酒店id  旅游id */
    private var targetId = 0

    /** 评论类型( 0 酒店 , 1 旅游) */
    private var commentType = 0

    /** 名称 */
    private var name = ""

    /** 头像 */
    private var avatar = ""


    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun getLayoutId() = R.layout.activity_comment_post

    override fun isStatusDark(): Boolean = true

    companion object {

        /**
         * @param commentType  评论类型( 0 酒店 , 1 旅游)
         * @param targetId 业务id （酒店、旅游）
         * @param orderId 订单id(如需要)
         * @param name 名字(如需要)
         * @param avatar 名字(如需要)
         *
         */
        fun navigation(
            context: Context,
            commentType: Int,
            targetId: Int,
            orderId: Int? = null,
            name: String? = "",
            avatar: String? = "",
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_TYPE, commentType)
                putInt(Constant.PARAM_ID, targetId)

                orderId?.let {
                    putInt(Constant.PARAM_ORDER_ID, it)
                }

                name?.let {
                    putString(Constant.PARAM_NAME, it)
                }

                avatar?.let {
                    putString(Constant.PARAM_AVATAR, it)
                }

            }
            val intent = Intent(context, CommentPostActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun initView() {
        intent.extras?.let {
            commentType = it.getInt(Constant.PARAM_TYPE, 0)
            targetId = it.getInt(Constant.PARAM_ID, 0)
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
            name = it.getString(Constant.PARAM_NAME, "")
            avatar = it.getString(Constant.PARAM_AVATAR, "")
        }

        // 相册
        mImageAdapter = StoreRefundAdapter().apply {
            addChildClickViewIds(R.id.delete_iv)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.delete_iv -> mImageAdapter.removeAt(position)
                }
            }
        }

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (name.isNotBlank()) {
            target_user_ll.visibility = View.VISIBLE
            name_tv.text = name
            GlideUtil.load(this, avatar, image_iv, 5)
        } else {
            target_user_ll.visibility = View.VISIBLE
        }


    }

    override fun initData() {
        presenter = CommentPostPresenter(this)
        presenter.targetId = targetId
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
                presenter.upLoadImage(commentType, orderId, rating, content, ArrayList(mImageAdapter.data))
                return@setOnClickListener
            }
            when (commentType) {
                0 -> presenter.postHotelComment(orderId, rating, content, null)
                1 -> presenter.postTravelComment(rating, content)

                else -> {
                }
            }


        }

        star_view.onChangeRatingByClickListener = object : OnChangeRatingByClickListener {
            override fun clickRatingResult(rating: Int) {
                comment_value_tv.text = BusinessUtils.getCommentRatingText(rating)
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val elements = Matisse.obtainPathResult(data)
        elements.forEach {
            mImageAdapter.addData(it)
        }
    }


    override fun commitSuccess() {
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        ToastUtils.showShort(R.string.commit_success)
        finish()
    }

}