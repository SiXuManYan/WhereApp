package com.jcs.where.api.response.mall.request

/**
 * Created by Wangsw  2022/1/24 10:48.
 *
 */
class MallShop {

    var id = 0

    var title =  ""
    var image =  ""
    var logo =  ""
    var desc =  ""
    /** 店铺状态（0-关闭，1-正常） */
    var status = 1

    /** 0 未收藏 1已收藏 */
    var collect_status = 1


}