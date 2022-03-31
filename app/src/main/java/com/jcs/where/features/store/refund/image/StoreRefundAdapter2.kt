package com.jcs.where.features.store.refund.image

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.utils.GlideUtil
import java.io.Serializable

/**
 * 图片选择、图片展示
 */
class RefundImage : MultiItemEntity, Serializable {


    companion object {
        var TYPE_EDIT = 0
        var TYPE_SHOW = 1
        var TYPE_ADD = 2
    }

    /**
     * 0 图片编辑(有删除)
     * 1 图片展示(无删除)
     * 2 添加按钮
     */
    var type = 0

    /**
     * 本地图片路径或图片url
     */
    var imageSource = ""

    override val itemType: Int
        get() = type

    var tempParentIndex = 0
    var tempRealImageUrl = ""

}

/**
 * Created by Wangsw  2021/7/1 16:40.
 *
 */
class StoreRefundAdapter2 : BaseMultiItemQuickAdapter<RefundImage, BaseViewHolder>() {

    init {
        addItemType(RefundImage.TYPE_EDIT, R.layout.item_refund_image_show)
        addItemType(RefundImage.TYPE_SHOW, R.layout.item_refund_image_show)
        addItemType(RefundImage.TYPE_ADD, R.layout.item_refund_image_add)
    }

    override fun convert(holder: BaseViewHolder, item: RefundImage) {

        when (holder.itemViewType) {
            RefundImage.TYPE_EDIT, RefundImage.TYPE_SHOW -> {
                bindImage(holder, item)
            }
            RefundImage.TYPE_ADD -> {
                bindAdd(holder, item)
            }
        }

    }


    private fun bindImage(holder: BaseViewHolder, item: RefundImage) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val delete_iv = holder.getView<ImageView>(R.id.delete_iv)
        val image_show_rl = holder.getView<RelativeLayout>(R.id.image_show_rl)
        val layoutParams = image_show_rl.layoutParams as RecyclerView.LayoutParams
        layoutParams.bottomMargin = SizeUtils.dp2px(12f)

        GlideUtil.load(context, item.imageSource, image_iv, 8)

        if (holder.itemViewType == 1) {
            delete_iv.visibility = View.GONE
        } else {
            delete_iv.visibility = View.VISIBLE
        }

    }


    private fun bindAdd(holder: BaseViewHolder, item: RefundImage) {
        val imageAdd = holder.getView<ImageView>(R.id.image_add_iv)
        val layoutParams = imageAdd.layoutParams as RecyclerView.LayoutParams
        layoutParams.bottomMargin = SizeUtils.dp2px(12f)

        imageAdd.setImageResource(R.mipmap.ic_upload_image)
    }
}