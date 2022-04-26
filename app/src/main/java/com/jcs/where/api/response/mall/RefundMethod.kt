package com.jcs.where.api.response.mall

/**
 * Created by Wangsw  2022/4/25 16:35.
 *
 */
class RefundMethod {

    var id = 0
    var user_name = ""
    var channel_name = ""
    var account = ""
    var bank_name = ""
    var bank_all_name = ""

}

class RefundChannel {
    var isSelected = false
    var name = ""
}


class RefundBankSelected {
    var abbr = ""
    var all = ""
    var isSelected = false
}

class RefundBindRequest {

    /** 用户名 */
    var user_name = ""

    /** 渠道名 */
    var channel_name = ""

    /** 账号 */
    var account = ""

    /** 银行缩写 */
    var bank_name: String? = null

    /** 银行全名 */
    var bank_all_name: String? = null
}