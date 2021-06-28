package com.jcs.where.features.bills.hydropower.record

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/6/28 16:16.
 *
 */
interface PaymentRecordView : BaseMvpView {

}


class PaymentRecordPresenter(private var view: PaymentRecordView) : BaseMvpPresenter(view) {


}