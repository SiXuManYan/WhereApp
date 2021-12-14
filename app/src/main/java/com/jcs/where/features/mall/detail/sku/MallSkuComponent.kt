package com.jcs.where.features.mall.detail.sku

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/12/13 14:46.
 *
 */
interface MallSkuView : BaseMvpView {

}

class MallSkuPresenter(private var view: MallSkuView) : BaseMvpPresenter(view) {

}