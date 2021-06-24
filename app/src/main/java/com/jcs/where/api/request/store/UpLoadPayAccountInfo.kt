package com.jcs.where.api.request.store

/**
 * Created by Wangsw  2021/6/24 15:12.
 *  银行卡转账支付（上传支付信息）
 */
class UpLoadPayAccountInfo {

    /**
     * 订单id
     * 参数示例：[1,2]
     */
    var order_ids = ""

    /**
     * 	转账户头
     */
    var bank_card_account = ""

    /**
     * 转账账户
     */
    var bank_card_number = ""


    /**
     * 	收款方转账银行卡Id
     */
    var card_id = 0





}