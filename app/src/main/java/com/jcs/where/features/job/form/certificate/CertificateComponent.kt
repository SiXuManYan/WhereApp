package com.jcs.where.features.job.form.certificate

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2023/2/6 11:00.
 *
 */
interface CertificateView : BaseMvpView {

}


class CertificatePresenter(private var view: CertificateView) : BaseMvpPresenter(view) {

}