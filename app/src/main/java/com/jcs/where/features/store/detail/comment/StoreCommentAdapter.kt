package com.jcs.where.features.store.detail.comment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.comment.CommentResponse

/**
 * Created by Wangsw  2021/7/16 15:40.
 *
 */
class StoreCommentAdapter : BaseQuickAdapter<CommentResponse, BaseViewHolder>(R.layout.item_comment), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: CommentResponse) {

    }
}