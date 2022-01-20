package com.jcs.where.features.mall.shop.home

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/1/19 17:22.
 *
 */
interface MallShopHomeView :BaseMvpView {

}

class MallShopHomePresenter(private var view: MallShopHomeView):BaseMvpPresenter(view){

}