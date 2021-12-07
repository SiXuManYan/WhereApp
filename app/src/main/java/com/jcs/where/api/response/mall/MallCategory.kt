package com.jcs.where.api.response.mall

/**
 * Created by Wangsw  2021/12/7 14:35.
 * 新版商城分类
 */
class MallCategory {
    var id = 0
    var name = ""
    var icon = ""
    var second_level: ArrayList<MallCategory> = ArrayList()
    var nativeIsSelected = false
}

class MallBannerCategory {
    var childItem = ArrayList<MallCategory>()
}