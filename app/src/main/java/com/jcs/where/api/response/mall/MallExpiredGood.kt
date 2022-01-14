package com.jcs.where.api.response.mall

/**
 * Created by Wangsw  2022/1/14 19:16.
 *
 */
class MallExpired {
    var cart_id = 0
    var good_data = MallExpiredGood()
}

class MallExpiredGood {
    var id = 0
    var title = ""
    var photo = ""
}