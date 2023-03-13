package com.jiechengsheng.city.api.response.footprint

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/11/18 16:45.
 *
 */
class Footprint : MultiItemEntity {

    companion object {

        var TYPE_HOTEL  = 1
        var TYPE_TRAVEL  = 2
        var TYPE_NEWS  = 3
        var TYPE_GENERAL  = 4
        var TYPE_RESTAURANT  = 5
        var TYPE_STORE  = 6
        var TYPE_GOOD  = 7
        var TYPE_TITLE = 1000

    }

    var id = 0

    /** 类型（1-酒店，2-旅游景点，3-新闻，4-综合服务，5-餐厅，6-eStore） */
    var type = 0

    var created_at = ""
    var date = ""
    var module_data: FootprintModule? = null

    override val itemType: Int
        get() = type


}

class FootprintModule {
    var id = 0
    var title = ""
    var images: ArrayList<String> = ArrayList()
    var price  = BigDecimal.ZERO
    var original_cost  = BigDecimal.ZERO

}


