package com.jcs.where.features.bills.form

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/6/8 17:33.
 *
 */
interface BillsFormView : BaseMvpView {

}

class BillsFormPresenter(private var view: BillsFormView) : BaseMvpPresenter(view) {

}