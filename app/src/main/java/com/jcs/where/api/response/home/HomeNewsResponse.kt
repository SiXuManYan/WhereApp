package com.jcs.where.api.response.home

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * Created by Wangsw  2021/4/14 13:47.
 *
 */
class HomeNewsResponse {
    var category_id = 0
    var category_name = ""
    var news_list = ArrayList<NewsList>()
}

class NewsList {
    var id = 0

    /** 新闻类型（1：图文，2：视频） */
    var content_type = 0
    var title = ""

    /** 封面图（图文与视频公用字段） */
    var cover_images = ArrayList<String>()
    var video_time = ""
    var video_link = ""

    /** 发布者 */
    var publisher: Publisher? = null
    var comment_num = 0
    var read_num = 0

    /** 关注状态（1：未关注，2：已关注） */
    var follow_status = 0

    /** 收藏状态（1：未收藏，2：已收藏） */
    var collect_status = 0
    var created_at = ""
}

class Publisher {
    var id = 0
    var nickname = ""
    var avatar = ""
}


class TabEntity(var title: String, var selectedIcon: Int, var unSelectedIcon: Int) : CustomTabEntity{

    override fun getTabTitle() = title

    override fun getTabSelectedIcon() = selectedIcon

    override fun getTabUnselectedIcon() = unSelectedIcon

}