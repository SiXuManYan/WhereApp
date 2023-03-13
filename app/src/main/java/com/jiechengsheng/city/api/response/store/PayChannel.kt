package com.jiechengsheng.city.api.response.store

import java.io.Serializable

/**
 * Created by Wangsw  2021/6/23 15:23.
 * 支付渠道
 */
class PayChannel : Serializable {

    /**
     * 银行卡id
     */
    var id = 0
    var title = ""

    /**
     * 	收款户头
     */
    var card_account = ""


    /**
     * 收款账户
     */
    var card_number = ""


    var nativeSelected = false

}