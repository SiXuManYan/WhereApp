package com.jcs.where.api.response.mall.request

/**
 * Created by Wangsw  2021/12/9 14:46.
 *
 */
class MallGoodListRequest {
    var page: Int = 1
    var order: SortEnum? = null
    var title: String? = null
    var categoryId: Int = 0
    var startPrice: String? = null
    var endPrice: String? = null
    var sold: SortEnum? = null
    var shopId: Int? = null
}

enum class SortEnum {
    desc, asc
}

class MallAddCart {
    var good_id = 0
    var good_num = 0
    var specs_id = 0

}