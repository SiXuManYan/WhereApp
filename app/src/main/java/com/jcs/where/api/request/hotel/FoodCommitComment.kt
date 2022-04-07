package com.jcs.where.api.request.hotel

/**
 * Created by Wangsw  2021/8/17
 * 提交美食评价
 */
class FoodCommitComment {


    /**
     * "["ssssss","xxxxx"]"
     */
    var images: String? = null
    var content: String? = null
    var order_id: Int = 0
    var restaurant_id: Int = 0

    var star: Int = 5

    /**
     * 评价类型（1：堂食菜品，2：外卖）
     */
    var type: Int = 0



}