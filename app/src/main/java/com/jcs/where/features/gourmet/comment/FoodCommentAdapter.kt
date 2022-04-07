package com.jcs.where.features.gourmet.comment

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.comment.CommentResponse
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.widget.ratingstar.RatingStarView
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Wangsw  2021/5/27 10:38.
 * 餐厅评论列表
 */
class FoodCommentAdapter : BaseQuickAdapter<CommentResponse, BaseViewHolder>(R.layout.item_comment), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: CommentResponse) {

        val avatar_civ = holder.getView<CircleImageView>(R.id.avatar_civ)
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val star_rs = holder.getView<RatingStarView>(R.id.star_rs)
        val comment_time_tv = holder.getView<TextView>(R.id.comment_time_tv)
        val content_tv = holder.getView<TextView>(R.id.content_tv)
        val image_container_ll = holder.getView<LinearLayout>(R.id.image_container_ll)


        GlideUtil.load(context, item.avatar, avatar_civ)
        name_tv.text = item.username
        star_rs.rating = item.star
        comment_time_tv.text = item.created_at
        content_tv.text = item.content
        val images = item.images

        if (images != null && images.isNotEmpty()) {
            val options = RequestOptions.bitmapTransform(GlideRoundedCornersTransform(2, GlideRoundedCornersTransform.CornerType.ALL))
                    .error(R.mipmap.ic_empty_gray)
                    .placeholder(R.mipmap.ic_empty_gray)

            image_container_ll.removeAllViews()
            for (i in images.indices) {
                if (i > 3) {
                    return
                }
                val params = LinearLayout.LayoutParams(SizeUtils.dp2px(79f), SizeUtils.dp2px(70f)).apply {
                    marginEnd = SizeUtils.dp2px(11f)
                }
                val imageView = ImageView(context).apply {
                    layoutParams = params
                }
                Glide.with(context).load(images[i]).apply(options).into(imageView)
                image_container_ll.addView(imageView)
            }
        }


    }


}