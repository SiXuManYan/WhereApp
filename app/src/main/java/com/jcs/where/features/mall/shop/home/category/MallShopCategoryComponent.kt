package com.jcs.where.features.mall.shop.home.category

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/1/24 10:31.
 *
 */
interface MallShopCategoryView : BaseMvpView {

}

class MallShopCategoryPresenter(private var view: MallShopCategoryView) : BaseMvpPresenter(view) {

}