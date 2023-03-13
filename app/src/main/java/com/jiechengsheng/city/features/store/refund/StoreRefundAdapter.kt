package com.jiechengsheng.city.features.store.refund

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/7/1 16:40.
 *
 */
class StoreRefundAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_refund_image) {

    var hideDelete = false

    override fun convert(holder: BaseViewHolder, item: String) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val delete_iv = holder.getView<ImageView>(R.id.delete_iv)
        GlideUtil.load(context, item, image_iv, 8)

        if (hideDelete) {
            delete_iv.visibility = View.GONE
        }

    }
}