package com.jiechengsheng.city.api.response.pay

import java.math.BigDecimal

/**
 * Created by Wangsw  2023/3/7 16:36.
 *
 */
class PayCounterChannel {

    var id = 0
    var title = ""

    /**
     * 渠道编码
     */
    var channel_code = ""
    var icon = ""

    /**
     * 是否支持令牌支付
     */
    var is_tokenized_pay = false

    /**
     * 是否认证过支付账号
     */
    var is_auth = false

    var nativeIsSelected = false


}

class PayCounterChannelDetail {
    var channel_code = ""
    var balance: BigDecimal = BigDecimal.ZERO
}

class PayChannelUnbind {

    /**
     * 渠道编码
     */
    var channel_code = ""
}


class PayChannelBindUrl {

    /**
     * 渠道编码
     */
    var auth_h5_url = ""
}