package com.jcs.where.features.mall.sku.other

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.features.mall.detail.sku.MallSkuView

/**
 * Created by Wangsw  2022/7/26 15:46.
 *
 */

interface SkuView :BaseMvpView {

}


class SkuPresenter(private var view: SkuView) : BaseMvpPresenter(view) {

}