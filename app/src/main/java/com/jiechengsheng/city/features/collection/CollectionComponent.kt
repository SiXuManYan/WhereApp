package com.jiechengsheng.city.features.collection

import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/11/16 15:49.
 *
 */
interface CollectionView : BaseMvpView {

}


class CollectionPresenter(private var view: CollectionView) : BaseMvpPresenter(view) {

}