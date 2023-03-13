package com.jiechengsheng.city.features.hotel.comment.child

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
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.hotel.HotelComment
import com.jiechengsheng.city.features.store.detail.comment.chiild.StoreCommentImageAdapter
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.widget.comment.StarView
import com.jiechengsheng.city.widget.list.DividerDecoration
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Wangsw  2021/8/17 14:05.
 *  酒店评价
 */
class HotelCommentAdapter : BaseQuickAdapter<HotelComment, BaseViewHolder>(R.layout.item_comment_hotel), LoadMoreModule {


    var singleLineImage = true
    var isDiamond = false


    override fun convert(holder: BaseViewHolder, item: HotelComment) {
        val avatar_civ = holder.getView<CircleImageView>(R.id.avatar_civ)
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val comment_sv = holder.getView<StarView>(R.id.comment_sv)
        val comment_time_tv = holder.getView<TextView>(R.id.comment_time_tv)
        val content_tv = holder.getView<ExpandableTextView>(R.id.content_etv)
        val image_rv = holder.getView<RecyclerView>(R.id.image_rv)

        val merchant_ll = holder.getView<LinearLayout>(R.id.merchant_ll)
        val merchant_reply_tv = holder.getView<TextView>(R.id.merchant_reply_tv)
        val merchant_iv = holder.getView<ImageView>(R.id.merchant_iv)

        GlideUtil.load(context, item.avatar, avatar_civ)
        name_tv.text = item.username

        comment_sv.apply {
            if (isDiamond) {
                setCheckStarDrawable(R.mipmap.ic_comment_diamond_yellow)
                setStarDrawable(R.mipmap.ic_comment_diamond_gray)
            }
            checkStarCount = if (item.star > 0.0f) {
                item.star.toInt()
            } else {
                item.star_level.toInt()
            }
            refreshView()
        }

        comment_time_tv.text = item.created_at
        content_tv.text = item.content

        // image
        val imageAdapter = StoreCommentImageAdapter()
        imageAdapter.singleLineImage = singleLineImage
        imageAdapter.setNewInstance(item.images)

        val liner = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
        }

        val grid = object : GridLayoutManager(context, 3, RecyclerView.VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }

        image_rv.apply {
            if (singleLineImage) {
                layoutManager = liner
            } else {
                layoutManager = grid
                if (itemDecorationCount == 0) {
                    addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0))
                }
            }
            adapter = imageAdapter
        }

        // 商家回复
        if (!item.merchant_review.isNullOrBlank()) {
            merchant_ll.visibility = View.VISIBLE
            merchant_iv.visibility = View.VISIBLE
            merchant_reply_tv.text = item.merchant_review
        } else {
            merchant_ll.visibility = View.GONE
            merchant_iv.visibility = View.GONE
        }

    }
}