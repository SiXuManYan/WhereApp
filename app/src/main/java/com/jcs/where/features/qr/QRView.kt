package com.jcs.where.features.qr

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.qr.QrResponse

/**
 * Created by Wangsw  2021/5/8 15:00.
 *
 */
interface QRView : BaseMvpView {
    fun bindDetail(it: QrResponse)


}