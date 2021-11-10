package com.jcs.where.features.store.detail.comment.chiild

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/7/16 15:40.
 *
 */
class StoreCommentImageAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_comment_store_image) {

    var singleLineImage = false

    override fun convert(holder: BaseViewHolder, item: String) {
        val child_iv = holder.getView<ImageView>(R.id.child_iv)

        if (singleLineImage) {
            val layoutParams = child_iv.layoutParams as RecyclerView.LayoutParams
            layoutParams.width = SizeUtils.dp2px(124f)
            child_iv.layoutParams = layoutParams
        }
        GlideUtil.load(context, item, child_iv, 5)

    }

}