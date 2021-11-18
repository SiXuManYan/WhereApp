package com.jcs.where.api.response.footprint

/**
 * Created by Wangsw  2021/11/18 16:45.
 *
 */
class Footprint {

    companion object {

        var TYPE_HOTEL  = 1
        var TYPE_TRAVEL  = 2
        var TYPE_NEWS  = 3
        var TYPE_GENERAL  = 4
        var TYPE_RESTAURANT  = 5
        var TYPE_STORE  = 6

    }

    var id = 0

    /** 类型（1-酒店，2-旅游景点，3-新闻，4-综合服务，5-餐厅，6-eStore） */
    var type = 0
    var created_at = ""
    var module_data: FootprintModule? = null
}

class FootprintModule {
    var id = 0
    var title = ""
    var images: ArrayList<String> = ArrayList()
}