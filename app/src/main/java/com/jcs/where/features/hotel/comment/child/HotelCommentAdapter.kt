package com.jcs.where.features.hotel.comment.child

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ctetin.expandabletextviewlibrary.ExpandableTextView
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelComment
import com.jcs.where.features.store.detail.comment.chiild.StoreCommentImageAdapter
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.list.DividerDecoration
import com.jcs.where.widget.ratingstar.RatingStarView
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Wangsw  2021/8/17 14:05.
 *  酒店评价
 */
class HotelCommentAdapter : BaseQuickAdapter<HotelComment, BaseViewHolder>(R.layout.item_comment_hotel), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: HotelComment) {
        val avatar_civ = holder.getView<CircleImageView>(R.id.avatar_civ)
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val star_rs = holder.getView<RatingStarView>(R.id.star_rs)
        val comment_time_tv = holder.getView<TextView>(R.id.comment_time_tv)
        val content_tv = holder.getView<ExpandableTextView>(R.id.content_etv)
        val image_rv = holder.getView<RecyclerView>(R.id.image_rv)


        GlideUtil.load(context, item.avatar, avatar_civ)
        name_tv.text = item.username
        star_rs.rating = item.star
        comment_time_tv.text = item.created_at
        content_tv.text = item.content


        val imageAdapter = StoreCommentImageAdapter()
        imageAdapter.singleLineImage = true
        imageAdapter.setNewInstance(item.images)

        val liner = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        image_rv.apply {
            layoutManager = liner
            adapter = imageAdapter
        }

    }
}