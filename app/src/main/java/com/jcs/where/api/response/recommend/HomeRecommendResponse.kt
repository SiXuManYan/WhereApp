package com.jcs.where.api.response.recommend

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/3/1 17:26.
 * 推荐
 */
class HomeRecommendResponse : MultiItemEntity {

    companion object {
        const val MODULE_TYPE_1_HOTEL = 1
        const val MODULE_TYPE_2_SERVICE = 2
        const val MODULE_TYPE_3_FOOD = 3
        const val MODULE_TYPE_4_TRAVEL = 4
    }

    override val itemType: Int
        get() = module_type

    var id = 0

    /**
     * 模块类型（1：酒店，2：综合服务，3：餐厅，4：旅游景点）
     */
    var module_type: Int = 0






    var images = ArrayList<String>()

    var title = ""

    /**
     * 分数
     */
    var grade = 0f

    /**
     * 评价数量
     */
    var comment_num = ""

    /**
     * 标签
     */
    var tags = ArrayList<String>()
    var address = ""
    var lat = ""
    var lng = ""

    /**
     * 剩余房间数
     */
    var remain_room_num = ""

    /**
     * Facebook链接
     */
    var facebook_link = ""

    /**
     * 星级
     */
    var star_level = ""

    /**
     * 距离
     */
    var distance = ""

    /**
     * 均价
     */
    var per_price = ""

    /**
     * 外卖状态（1：关闭，2：开启）
     */
    var take_out_status = 0

    /**
     * 餐厅类型
     */
    var restaurant_type = ""

    /**
     * 地区名称
     */
    var area_name = ""

    /**
     * 创建时间
     */
    var created_at = ""

    /**
     * 现价
     */
    var price: BigDecimal = BigDecimal.ZERO

    /**
     * 原价
     */
    var original_cost: BigDecimal = BigDecimal.ZERO

}