package com.jcs.where.features.gourmet.takeaway

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.comment.CommentResponse
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.api.response.gourmet.dish.DishTakeawayResponse
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse

/**
 * Created by Wangsw  2021/4/25 10:04.
 *
 */
interface TakeawayView :BaseMvpView {
    fun bindData(data: TakeawayDetailResponse)
    fun bindDishList(list: MutableList<DishTakeawayResponse>)
    fun collectionSuccess()
    fun unCollectionSuccess()
    fun bindCommentData(data: List<CommentResponse>)
}