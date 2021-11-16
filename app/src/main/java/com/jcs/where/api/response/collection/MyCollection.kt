package com.jcs.where.api.response.collection

/**
 * Created by Wangsw  2021/11/16 16:32.
 *
 */
class MyCollection {

    /** 类型（1-酒店，2-旅游，3-新闻，11-综合服务，12-餐厅，13-eStore） */
    var type = 0

    var hotel: HotelCollection? = null
    var travel: TravelCollection? = null
    var news: NewsCollection? = null
    var general: GeneralCollection? = null
    var restaurant: RestaurantCollection? = null
    var estore: StoreCollection? = null
}

class HotelCollection {
    var id = 0
    var comment_num = 0
    var grade = 0f
    var name = ""
    var address = ""
    var images: ArrayList<String> = ArrayList()
}

class TravelCollection {
    var id = 0
    var comment_num = 0
    var grade = 0f
    var name = ""
    var address = ""
    var images: ArrayList<String> = ArrayList()
}

class NewsCollection {

    var id = 0

    /** 内容类型（1：图文，2：视频） */
    var content_type = 0

    var title = ""
    var video_time = ""
    var video_link = ""
    var created_at = ""
    var cover_images: ArrayList<String> = ArrayList()

    var publisher: Publisher? = null
}

class Publisher {

    /** 发布者id */
    var id = 0

    /** 发布者昵称 */
    var nickname = ""

}

class GeneralCollection{
    var id = 0
    var name = ""
    var address = ""
    var images: ArrayList<String> = ArrayList()
}

class RestaurantCollection{
    var id = 0
    var name = ""
    var address = ""
    var images: ArrayList<String> = ArrayList()
}

class StoreCollection{
    var id = 0
    var name = ""
    var address = ""
    var images: ArrayList<String> = ArrayList()
}




