package com.jcs.where.api.response.mall

import java.io.Serializable

/**
 * Created by Wangsw  2022/1/14 19:16.
 *
 */
class MallExpired  : Serializable {
    var cart_id = 0
    var good_data = MallExpiredGood()
}

class MallExpiredGood : Serializable {
    var id = 0
    var title = ""
    var photo = ""
}