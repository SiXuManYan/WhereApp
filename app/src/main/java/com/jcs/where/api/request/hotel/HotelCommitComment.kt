package com.jcs.where.api.request.hotel

import com.jcs.where.features.store.refund.image.RefundImage
import java.io.Serializable

/**
 * Created by Wangsw  2021/8/17
 * 提交酒店评价
 */
class HotelCommitComment {


    /**
     * "["ssssss","xxxxx"]"
     */
    var images: String? = null
    var content: String? = null
    var order_id: Int = 0
    var hotel_id: Int = 0

    var star: Int = 5


    var comment_travel_type_id = ""

}

class BatchComment {

    var order_id = 0
    var goods_comments = ""

    var goods_comments_temp = ArrayList<BatchCommentItem>()
}


class BatchCommentItem : Serializable {

    // 提交
    var good_id = 0
    var star = 5
    var content = ""
    var image = ArrayList<String>()


    // 数据
    var nativeImage = ArrayList<RefundImage>()
    var nativeGoodImage = ""
    var nativeGoodName = ""
    var nativeGoodNumber = 0
}


class BatchCommentGood {
    var nativeGoodId = 0
    var nativeGoodImage = ""
    var nativeGoodName = ""
    var nativeGoodNumber = 0
}

class ComplaintRequest {
    var order_id = 0
    var content = ""
    var image :String? = null



}