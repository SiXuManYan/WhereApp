package com.jcs.where.features.store.detail.comment

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ctetin.expandabletextviewlibrary.ExpandableTextView
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.comment.CommentResponse
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.list.DividerDecoration
import com.jcs.where.widget.ratingstar.RatingStarView
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Wangsw  2021/7/16 15:40.
 *
 */
class StoreCommentAdapter : BaseQuickAdapter<CommentResponse, BaseViewHolder>(R.layout.item_comment_store), LoadMoreModule {


    var singleLineImage = false

    override fun convert(holder: BaseViewHolder, item: CommentResponse) {
        val avatar_civ = holder.getView<CircleImageView>(R.id.avatar_civ)
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val star_rs = holder.getView<RatingStarView>(R.id.star_rs)
        val comment_time_tv = holder.getView<TextView>(R.id.comment_time_tv)
        val content_tv = holder.getView<ExpandableTextView>(R.id.content_etv)
        val image_rv = holder.getView<RecyclerView>(R.id.image_rv)
        val merchant_ll = holder.getView<LinearLayout>(R.id.merchant_ll)
        val merchant_reply_tv = holder.getView<TextView>(R.id.merchant_reply_tv)

        GlideUtil.load(context, item.avatar, avatar_civ)
        name_tv.text = item.username
        star_rs.rating = item.star
        comment_time_tv.text = item.created_at
        content_tv.text = item.content

        // image

        val imageAdapter = StoreCommentImageAdapter()
        imageAdapter.setNewInstance(item.images)

        val grid = object : GridLayoutManager(context, 3, RecyclerView.VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        val liner = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        image_rv.apply {
            layoutManager = if (singleLineImage) {
                liner
            } else {
                grid
            }
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(15f), 0, 0).apply {
                setDrawHeaderFooter(false)
            })
            adapter = imageAdapter
        }

        if (item.merchant_review.isNotEmpty()) {
            merchant_ll.visibility = View.VISIBLE
            merchant_reply_tv.text = item.merchant_review
        } else {
            merchant_ll.visibility = View.GONE
        }


    }
}


class StoreCommentImageAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_comment_store_image) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val child_iv = holder.getView<ImageView>(R.id.child_iv)
        GlideUtil.load(context, item, child_iv, 5)
    }

}