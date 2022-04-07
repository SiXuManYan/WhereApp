package com.jcs.where.api.response.gourmet.qr

/**
 * Created by Wangsw  2021/5/8 15:25.
 *
 */
data class QrResponse(
        /** 餐厅名称 */
        var restaurant_name: String = "",
        /** 商品名称 */
        var good_name: String = "",
        /** 超时日期 */
        var expire_date: String = "",
        /** 券码号 */
        var coupon_no: String = "",
        /** 券码二维码 */
        var coupon_qr_code: String = ""

)
