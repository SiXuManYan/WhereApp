package com.jcs.where.features.job.pdf

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2023/2/23 14:28.
 *
 */
interface CvPdfView : BaseMvpView {

}

class CvPdfPresenter(private var view: CvPdfView) : BaseMvpPresenter(view){


}