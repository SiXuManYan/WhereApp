package com.jcs.where.features.store.detail.comment

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/7/16 15:25.
 *
 */

interface StoreCommentView : BaseMvpView {

}


class StoreCommentPresenter(private var view:StoreCommentView):BaseMvpPresenter(view){
    
}