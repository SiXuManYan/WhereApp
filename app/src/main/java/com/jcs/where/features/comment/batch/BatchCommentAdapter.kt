package com.jcs.where.features.comment.batch

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.request.hotel.BatchCommentItem
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.features.store.refund.image.StoreRefundAdapter2
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.comment.StarView

/**
 * Created by Wangsw  2022/3/26 14:30.
 * 批量评价
 */
class BatchCommentAdapter : BaseQuickAdapter<BatchCommentItem, BaseViewHolder>(R.layout.item_comment_batch) {

    var onImageAddClickListener: OnImageAddClickListener? = null


    override fun convert(holder: BaseViewHolder, item: BatchCommentItem) {
        resetHeight(holder)
        initDefault(holder, item)
        initEdit(holder, item)
        initMedia(holder, item)
    }

    private fun resetHeight(holder: BaseViewHolder) {
        val container = holder.getView<LinearLayout>(R.id.container_ll)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.height = ScreenUtils.getScreenHeight() - SizeUtils.dp2px(16f)
        container.layoutParams = layoutParams
    }

    private fun initDefault(holder: BaseViewHolder, item: BatchCommentItem) {

        val good_image_iv = holder.getView<ImageView>(R.id.good_image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)
        val count_tv = holder.getView<TextView>(R.id.count_tv)
        val comment_star_view = holder.getView<StarView>(R.id.comment_star_view)
        val comment_value_tv = holder.getView<TextView>(R.id.comment_value_tv)

        GlideUtil.load(context, item.nativeGoodImage, good_image_iv, 4)
        good_name_tv.text = item.nativeGoodName
        count_tv.text = StringUtils.getString(R.string.count_format, item.nativeGoodNumber)

        // 星级
        comment_star_view.setStarCount(item.star)
        comment_star_view.setStoreItemOnClickListener {
            comment_value_tv.text = BusinessUtils.getCommentRatingText(it + 1)
        }


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initEdit(holder: BaseViewHolder, item: BatchCommentItem) {
        val content_et = holder.getView<AppCompatEditText>(R.id.content_et)
        content_et.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_DOWN == event.action) {
                v.parent.requestDisallowInterceptTouchEvent(true);
            } else if (MotionEvent.ACTION_UP == event.action) {
                v.parent.requestDisallowInterceptTouchEvent(false);
            }
            false;
        }

        content_et.setText(item.content)
        content_et.doAfterTextChanged {
            item.content = it.toString().trim()
        }

    }


    private fun initMedia(holder: BaseViewHolder, item: BatchCommentItem) {
        val image_rv = holder.getView<RecyclerView>(R.id.image_rv)


        val mImageAdapter = StoreRefundAdapter2().apply {
            addChildClickViewIds(R.id.delete_iv, R.id.image_iv, R.id.image_add_iv)
        }
        mImageAdapter.setOnItemChildClickListener { adapter, view, position ->

            when (view.id) {
                R.id.image_add_iv -> {
                    // 添加图片
                    val size = mImageAdapter.data.size
                    if (size == 7) {
                        ToastUtils.showShort(R.string.refund_image_hint_6)
                        return@setOnItemChildClickListener
                    }
                    val max = 7 - size
                    onImageAddClickListener?.handleMediaSelect(holder.adapterPosition, max)
                }
                R.id.image_iv -> {
                    // 展示图片
                }
                R.id.delete_iv -> mImageAdapter.removeAt(position)
                else -> {
                }
            }

        }

        // 添加数据
        mImageAdapter.setNewInstance(item.nativeImageData)

        // 添加默认
        val addItem = RefundImage().apply {
            type = RefundImage.TYPE_ADD
        }
        mImageAdapter.addData(0, addItem)

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        }


    }

}


interface OnImageAddClickListener {
    fun handleMediaSelect(adapterPosition: Int, max: Int)

}