package com.jcs.where.features.collection

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/11/16 15:49.
 *
 */
interface CollectionView : BaseMvpView {

}


class CollectionPresenter(private var view: CollectionView) : BaseMvpPresenter(view) {

}