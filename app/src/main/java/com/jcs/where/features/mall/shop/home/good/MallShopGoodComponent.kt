package com.jcs.where.features.mall.shop.home.good

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/1/24 10:22.
 *
 */
interface MallShopGoodView : BaseMvpView {

}

class MallShopGoodPresenter(private var view: MallShopGoodView) : BaseMvpPresenter(view) {

}