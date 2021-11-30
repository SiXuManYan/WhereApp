package com.jcs.where.features.mall.home.child

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/11/30 17:01.
 *
 */
interface MallHomeChildView : BaseMvpView {

}

class MallHomeChildPresenter(private var view: MallHomeChildView) : BaseMvpPresenter(view) {

}