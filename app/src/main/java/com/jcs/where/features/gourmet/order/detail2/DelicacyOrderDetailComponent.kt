package com.jcs.where.features.gourmet.order.detail2

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/7/23 16:06.
 *
 */
interface DelicacyOrderDetailView : BaseMvpView {

}

class DelicacyOrderDetail(private var view: DelicacyOrderDetailView) : BaseMvpPresenter(view) {

}