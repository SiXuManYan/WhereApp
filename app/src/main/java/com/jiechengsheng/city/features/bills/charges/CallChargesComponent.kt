package com.jiechengsheng.city.features.bills.charges

import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/6/15 16:55.
 *
 */
interface CallChargesView : BaseMvpView {

}


class CallChargesPresenter(var view: CallChargesView) : BaseMvpPresenter(view) {




}