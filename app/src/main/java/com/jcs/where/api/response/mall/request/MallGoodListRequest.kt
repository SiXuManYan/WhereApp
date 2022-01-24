package com.jcs.where.api.response.mall.request

import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/12/9 14:46.
 *
 */
class MallGoodListRequest {
    var page: Int = Constant.DEFAULT_FIRST_PAGE

    /** 价格排序(desc, asc) */
    var order: SortEnum? = null
    var title: String? = null
    var categoryId: Int? = null
    var startPrice: String? = null
    var endPrice: String? = null

    /** 销量排序(desc,asc) */
    var sold: SortEnum? = null
    var shopId: Int? = null
}

enum class SortEnum {

    /** 降序 */
    desc,

    /** 升序 */
    asc
}

class MallAddCart {
    var good_id = 0
    var good_num = 0
    var specs_id = 0

}