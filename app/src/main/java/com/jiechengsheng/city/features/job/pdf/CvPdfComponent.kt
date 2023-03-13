package com.jiechengsheng.city.features.job.pdf

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView

/**
 * Created by Wangsw  2023/2/23 14:28.
 *
 */
interface CvPdfView : BaseMvpView {
    fun generateSuccess()

}

class CvPdfPresenter(private var view: CvPdfView) : BaseMvpPresenter(view){

    fun generatePdf(){
        requestApi(mRetrofit.generatePdf() , object :BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.generateSuccess()
            }

        })
    }

}