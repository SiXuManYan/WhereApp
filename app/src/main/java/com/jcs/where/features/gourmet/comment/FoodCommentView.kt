package com.jcs.where.features.gourmet.comment

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.comment.CommentResponse

/**
 * Created by Wangsw  2021/5/27 16:04.
 *
 */
interface FoodCommentView : BaseMvpView {
    fun bindCommentData(data: MutableList<CommentResponse>, isLastPage: Boolean)


}