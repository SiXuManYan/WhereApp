package com.jiechengsheng.city.features.qr

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.gourmet.qr.QrResponse

/**
 * Created by Wangsw  2021/5/8 15:00.
 *
 */
interface QRView : BaseMvpView {
    fun bindDetail(it: QrResponse)


}