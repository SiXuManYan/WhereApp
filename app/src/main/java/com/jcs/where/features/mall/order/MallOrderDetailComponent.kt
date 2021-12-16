package com.jcs.where.features.mall.order

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/12/16 11:27.
 *
 */
interface  MallOrderDetailView :BaseMvpView{

}

class MallOrderDetailPresenter(private var view:MallOrderDetailView):BaseMvpPresenter(view){

}