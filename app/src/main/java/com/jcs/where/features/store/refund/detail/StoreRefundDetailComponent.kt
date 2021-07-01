package com.jcs.where.features.store.refund.detail

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/7/1 17:54.
 *
 */

interface StoreRefundDetailView :BaseMvpView{

}

class StoreRefundDetailPresenter(private var view:StoreRefundDetailView):BaseMvpPresenter(view){

}