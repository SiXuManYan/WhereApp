package com.jiechengsheng.city.features.qr

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.gourmet.qr.QrResponse

/**
 * Created by Wangsw  2021/5/8 15:00.
 *
 */
class QRPresenter(val view: QRView) : BaseMvpPresenter(view) {


    fun getQrDetail(orderId: String) {
        requestApi(mRetrofit.getQrDetail(orderId), object : BaseMvpObserver<QrResponse>(view) {
            override fun onSuccess(response: QrResponse?) {
               response?.let {
                   view.bindDetail(it)
               }
            }
        })
    }
}