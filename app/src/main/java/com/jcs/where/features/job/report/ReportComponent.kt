package com.jcs.where.features.job.report

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/10/27 17:38.
 *
 */
interface ReportView : BaseMvpView {

}

class ReportPresenter(private var view: ReportView) : BaseMvpPresenter(view) {

}