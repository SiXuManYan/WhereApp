package com.jiechengsheng.city.features.mall.sku.other

import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/7/26 15:46.
 *
 */

interface SkuView :BaseMvpView {

}


class SkuPresenter(private var view: SkuView) : BaseMvpPresenter(view) {

}