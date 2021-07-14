package com.jcs.where.features.store.comment.post

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/7/14 14:43.
 *
 */
interface StoreCommentPostView : BaseMvpView {

}


class StoreCommentPostPresenter(private var view: StoreCommentPostView) : BaseMvpPresenter(view) {

}