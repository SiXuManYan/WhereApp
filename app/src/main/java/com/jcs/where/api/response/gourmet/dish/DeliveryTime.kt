package com.jcs.where.api.response.gourmet.dish

import java.util.*

/**
 * Created by Wangsw  2021/4/27 17:04.
 */
class DeliveryTime {

    /**
     * 立即配送时间
     */
    var delivery_time = ""

    /**
     * 其他配送时间
     */
    var other_times = ArrayList<String>()
}

class DeliveryTimeRetouch {

    var nativeSelected = false
    var time = ""
}