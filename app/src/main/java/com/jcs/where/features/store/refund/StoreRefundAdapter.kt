package com.jcs.where.features.store.refund

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/7/1 16:40.
 *
 */
class StoreRefundAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_refund_image) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        GlideUtil.load(context, item, image_iv)
    }
}